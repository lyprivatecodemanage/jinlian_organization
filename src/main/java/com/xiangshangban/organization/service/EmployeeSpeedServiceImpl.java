package com.xiangshangban.organization.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.ImportReturnData;
import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.util.RegexUtil;
@Service("employeeSpeedService")
public class EmployeeSpeedServiceImpl implements EmployeeSpeedImportService {
	private static final Logger logger = Logger.getLogger(EmployeeSpeedServiceImpl.class);
	
	@Autowired
	private EmployeeService employeeService;
	@Override
	public ReturnData speedImport(String operateUserId,String companyId,String filePath) {
		ReturnData returnData = new ReturnData();
		List<ImportReturnData> importReturnDataList = new ArrayList<ImportReturnData>();
		//Map<String, String> result = new HashMap<String, String>();
		// 判断是否为excel类型文件
		if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
			System.out.println("文件不是excel类型");
		}
		FileInputStream fis = null;
		Workbook wookbook = null;
		try {
			// 获取一个绝对地址的流
			fis = new FileInputStream(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
			returnData.setMessage("服务系错误");
			returnData.setReturnCode("3001");
			return returnData;
		}
		try {
			// 2003版本的excel，用.xls结尾
			wookbook = new HSSFWorkbook(fis);// 得到工作簿
		} catch (Exception ex) {
			// ex.printStackTrace();
			try {
				// 2007版本的excel，用.xlsx结尾
				wookbook = new XSSFWorkbook(fis);// 得到工作簿
			} catch (IOException e) {
				e.printStackTrace();
				logger.info(e);
				returnData.setMessage("服务系错误");
				returnData.setReturnCode("3001");
				return returnData;
			}
		}
		int lastSheetNum = wookbook.getNumberOfSheets();
		for (int i = 0; i < lastSheetNum; i++) {
			// 得到一个工作表
			Sheet sheet = wookbook.getSheetAt(i);
			// 获得表头
			Row rowHead = sheet.getRow(0);
			// 判断表头是否正确
			if (rowHead.getPhysicalNumberOfCells() != 17) {
				returnData.setMessage("上传的Excel模板格式不正确");
				returnData.setReturnCode("4116");
				return returnData;
			}
			// 获得数据的总行数
			int totalRowNum = sheet.getLastRowNum();
			// 遍历整个工作表,获取所有数据
			for (int j = 1; j <= totalRowNum; j++) {
				// 获得第i行对象
				Row row = sheet.getRow(j);
				int lastRowNum = row.getLastCellNum();
				List<String> paramList = new ArrayList<String>();
				List<Post> postList = new ArrayList<Post>();
				// 循环每一列
				for (int k = 0; k < lastRowNum; k++) {
					Cell cell = row.getCell(k);
					String value = cell.getStringCellValue();
					if (k == 2 || k == 3 || k == 4 || k == 6 || k == 7 || k == 9 || k == 10 || k == 11 || k == 12) {
						if (StringUtils.isEmpty(value)) {
							String importMessage = "第" + i + "行,第" + k + "列,必须填写!";
							ImportReturnData importReturnData = new ImportReturnData();
							importReturnData.setImportMessage(importMessage);
							importReturnDataList.add(importReturnData);
							break;
						}
					}
					if (StringUtils.isNotEmpty(value)) {
						if (k == 13 || k == 14) {
							Post post = new Post();
							post.setPostId(value);
							postList.add(post);
						} else {
							paramList.add(value);
						}
					} else {
						paramList.add(null);
					}
				}
				Employee newEmp = new Employee(paramList.get(0), paramList.get(1), paramList.get(2), paramList.get(3),
						paramList.get(4), paramList.get(5), paramList.get(6), paramList.get(7), paramList.get(8),
						paramList.get(9), paramList.get(10), paramList.get(11), postList, paramList.get(14),
						paramList.get(15), paramList.get(16));
				newEmp.setOperateUserId(operateUserId);
				newEmp.setCompanyId(companyId);
				String employeeNo = newEmp.getEmployeeNo();
				if (StringUtils.isNotEmpty(employeeNo)) {
					Employee employeeNotemp = employeeService.findByemployeeNo(employeeNo, companyId);
					if (employeeNotemp != null) {
						String importMessage = "第" + i + "行,工号已存在!";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
					}
				}

				String loginName = newEmp.getLoginName();
				boolean loginNameMatch = RegexUtil.matchPhone(loginName);
				if (!loginNameMatch) {
					String importMessage = "第" + i + "行,登录名格式必须为11位手机号!";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
				}
				String employeeName = newEmp.getEmployeeName();
				if (StringUtils.isEmpty(employeeName)) {
					String importMessage = "第" + i + "行,姓名必需填写!";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
				}

				if ((StringUtils.isNotEmpty(newEmp.getEntryTime()) && !RegexUtil.matchDate(newEmp.getEntryTime()))
						|| (StringUtils.isNotEmpty(newEmp.getProbationaryExpired())
								&& !RegexUtil.matchDate(newEmp.getProbationaryExpired()))) {
					String importMessage = "第" + i + "行,日期格式错误（yyyy-MM-dd）!";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
				}
				int result = employeeService.insertEmployee(newEmp);
				if(result == 0){
					String importMessage = "第" + i + "行,日期格式错误（yyyy-MM-dd）!";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
				}
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");
				return returnData;
				//按人员的loginName登录名(phone)以及姓名(username|employeeName)查询添加的人员是否已存在
				
					//已存在(提示用户已存在)
						
						
				
				//不存在
				
				
				
				
			}
		}
		returnData.setMessage("部分导入失败,请检查表格数据是否填写正确");
		returnData.setReturnCode("4117");
		return null;
	}
}
