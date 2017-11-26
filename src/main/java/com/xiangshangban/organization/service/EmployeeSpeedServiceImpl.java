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

import com.xiangshangban.organization.bean.Department;
import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.ImportReturnData;
import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.exception.CustomException;
import com.xiangshangban.organization.util.RegexUtil;
import com.xiangshangban.organization.util.TimeUtil;
@Service("employeeSpeedService")
public class EmployeeSpeedServiceImpl implements EmployeeSpeedImportService {
	private static final Logger logger = Logger.getLogger(EmployeeSpeedServiceImpl.class);
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private PostService postService;
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
				if("女".equals(newEmp.getEmployeeSex())){
					newEmp.setEmployeeSex("1");
				}else{
					newEmp.setEmployeeSex("0");
				}
				if(StringUtils.isNotEmpty(newEmp.getMarriageStatus())){
					if("离异".equals(newEmp.getMarriageStatus())){
						newEmp.setMarriageStatus("2");
					}else if("已婚".equals(newEmp.getMarriageStatus())){
						newEmp.setMarriageStatus("1");
					}else{
						newEmp.setMarriageStatus("0");
					}
				}
				if("离职".equals(newEmp.getEmployeeStatus())){
					newEmp.setEmployeeStatus("1");
				}else if("删除".equals(newEmp.getEmployeeStatus())){
					newEmp.setEmployeeStatus("2");
				}else{
					newEmp.setEmployeeStatus("0");
				}
				String entryTime = "";
				String probationaryExpired = "";
				try {
					 entryTime = TimeUtil.timeFormatTransfer(newEmp.getEntryTime());
					 probationaryExpired = TimeUtil.timeFormatTransfer(newEmp.getProbationaryExpired());
					
				}catch(Exception e){
					String importMessage = "第" + i + "行,"+((CustomException)e).getExceptionMessage();
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					continue;
				}
				if(TimeUtil.compareTime(entryTime, probationaryExpired)){
					String importMessage = "第" + i + "行,转正时间必须大于入职时间";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					continue;
				}
				newEmp.setCompanyId(companyId);
				String employeeNo = newEmp.getEmployeeNo();
				if (StringUtils.isNotEmpty(employeeNo)) {
					Employee employeeNotemp = employeeService.findByemployeeNo(employeeNo, companyId);
					if (employeeNotemp != null) {
						String importMessage = "第" + i + "行,工号已存在!";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						continue;
					}
				}
				String loginName = newEmp.getLoginName();
				boolean loginNameMatch = RegexUtil.matchPhone(loginName);
				if (!loginNameMatch) {
					String importMessage = "第" + i + "行,登录名格式必须为11位手机号!";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					continue;
				}
				/*String employeeName = newEmp.getEmployeeName();
				if (StringUtils.isEmpty(employeeName)) {
					String importMessage = "第" + i + "行,姓名必需填写!";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
				}*/

				/*if ((StringUtils.isNotEmpty(newEmp.getEntryTime()) && !RegexUtil.matchDate(newEmp.getEntryTime()))
						|| (StringUtils.isNotEmpty(newEmp.getProbationaryExpired())
								&& !RegexUtil.matchDate(newEmp.getProbationaryExpired()))) {
					String importMessage = "第" + i + "行,日期格式错误（yyyy-MM-dd）!";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
				}*/
				
				//查询添加信息中的部门是否存在
				boolean departmentFlag = true;
				 List<Department> departmentList = departmentService.findByAllDepartment(companyId); 
				 for(Department department : departmentList){
					 if(newEmp.getDepartmentName().equals(department.getDepartmentName())){
						 departmentFlag = false;
					 }
				 }
				 if(departmentFlag){
					 	String importMessage = "第" + i + "行,填写的部门不存在";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						continue;
				 }
				 
				//查询添加信息中的岗位是否存在于对应的部门之中
				
				//查询直接汇报人是否存在
				
				ReturnData serviceReturnData = employeeService.insertEmployee(newEmp);
				ImportReturnData importReturnData = new ImportReturnData();
				importReturnData.setImportMessage(serviceReturnData.getMessage());
				importReturnDataList.add(importReturnData);
			}
		}
		boolean flag = false;
		for(ImportReturnData importReturnDataObj:importReturnDataList){
			if(StringUtils.isNotEmpty(importReturnDataObj.getImportMessage())){
				flag=true;
			}
		}
		if(flag){
			returnData.setData(importReturnDataList);
			returnData.setMessage("部分导入失败,请检查表格数据是否填写正确");
			returnData.setReturnCode("4117");
			return returnData;
		}
		returnData.setMessage("成功");
		returnData.setReturnCode("3000");
		return returnData;
	}
}
