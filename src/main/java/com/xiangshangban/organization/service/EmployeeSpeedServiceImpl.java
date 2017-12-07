package com.xiangshangban.organization.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.aliyun.oss.OSSClient;
import com.xiangshangban.organization.bean.Department;
import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.ImportReturnData;
import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.exception.CustomException;
import com.xiangshangban.organization.util.ImportUtil;
import com.xiangshangban.organization.util.POIUtil;
import com.xiangshangban.organization.util.PropertiesUtils;
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
	public ReturnData speedImport(String operateUserId, String companyId, String key) {
		ReturnData returnData = new ReturnData();
		List<ImportReturnData> importReturnDataList = new ArrayList<ImportReturnData>();
		// Map<String, String> result = new HashMap<String, String>();
		// 判断是否为excel类型文件
		if (!key.endsWith(".xls") && !key.endsWith(".xlsx")) {
			System.out.println("文件不是excel类型");
		}
		InputStream in = null;
		Workbook wookbook = null;
		try {
			String accessId = PropertiesUtils.ossProperty("accessKey");
			String accessKey = PropertiesUtils.ossProperty("securityKey");
			String OSS_ENDPOINT = PropertiesUtils.ossProperty("OSS_ENDPOINT");
			String OSS_BUCKET = PropertiesUtils.ossProperty("OSS_BUCKET");
			OSSClient client = new OSSClient(OSS_ENDPOINT,accessId,accessKey);
			// 获取一个绝对地址的流
			in = client.getObject(OSS_BUCKET, key).getObjectContent();
			// 2003版本的excel，用.xls结尾
			if(key.endsWith(".xls")){
				wookbook = new HSSFWorkbook(in);// 得到工作簿
			}else{
				wookbook = new XSSFWorkbook(in);// 得到工作簿
			}
		} catch (Exception ex) {
			logger.info(ex);
			returnData.setMessage("文件读取错误");
			returnData.setReturnCode("3001");
			return returnData;
		}
		//int lastSheetNum = wookbook.getNumberOfSheets();
		//for (int i = 0; i < lastSheetNum; i++) {
			// 得到一个工作表
			Sheet sheet = wookbook.getSheetAt(0);
			// 获得表头
			Row rowHead = sheet.getRow(0);
			// 判断表头是否正确
			if (rowHead.getPhysicalNumberOfCells() != 20) {
				returnData.setMessage("上传的Excel模板格式不正确");
				returnData.setReturnCode("4116");
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return returnData;
			}
			// 获得数据的总行数
			int totalRowNum = sheet.getLastRowNum();
			// 遍历整个工作表,获取所有数据
			for (int i = 1; i <= totalRowNum; i++) {
				
				boolean lineFlag = false;
				// 获得第i行对象
				Row row = sheet.getRow(i);
				boolean isBlankRow = ImportUtil.isBlankRow(row);
				if(isBlankRow){
					break;
				}
				//int lastRowNum = row.getLastCellNum();
				List<String> paramList = new ArrayList<String>();
				List<Post> postList = new ArrayList<Post>();
				// 循环每一列
				for (int k = 0; k < 20; k++) {
					System.out.println("======>"+k);
					Cell cell = row.getCell(k);
				
					String value = "";
					try{
						if(k==10 || k == 11){
							try{
								//try{
									value = POIUtil.getExcelCellValue(cell);
								/*}catch(NullPointerException e){
									value = "";
								}*/
							}catch(Exception e){
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								value = sdf.format(sdf.parse(value));
								/*Date date = cell.getDateCellValue();
								value = sdf.format(date);*/
							}
							//TimeUtil.timeFormatTransfer();
						}else if(k==5||k==8||k==17||k==18){
							/*try{
								try{*/
									//value = cell.getStringCellValue();
									value = POIUtil.getExcelCellValue(cell);
								/*}catch(IllegalStateException e){
									DecimalFormat format = new DecimalFormat("#");  
									Number valueD = cell.getNumericCellValue();  
									value = format.format(valueD);  
								}
							}catch(NullPointerException e){
								value = "";
							}*/
						}else{
							//try{
								value = POIUtil.getExcelCellValue(cell);
								//value = cell.getStringCellValue();
							/*}catch(NullPointerException e){
								value = "";
							}*/
						}
					}catch(Exception e){
						String importMessage = "第" + i + "行,第" +( k+1) + "列,请检查数据格式!";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						lineFlag = true;
						break;
					}
					if (k == 1 || k == 2 || k == 3 || k == 5 || k == 6 || k == 9 || k == 10 || k == 11 || k == 12) {
						if (StringUtils.isEmpty(value)) {
							String importMessage = "第" + i + "行,第" +( k+1) + "列,必须填写!";
							ImportReturnData importReturnData = new ImportReturnData();
							importReturnData.setImportMessage(importMessage);
							importReturnDataList.add(importReturnData);
							lineFlag = true;
							break;
						}
					}
					if(k==13 || k ==15){
						Post post = new Post();
						postList.add(post);
					}
					switch(k){
					case 13:
						postList.get(0).setDepartmentName(value);
						break;
					case 14:
						postList.get(0).setPostName(value);
						break;
					case 15:
						postList.get(1).setDepartmentName(value);
						break;
					case 16:
						postList.get(1).setPostName(value);
						break;
					}
						System.out.println(value);
						paramList.add(value);
				}
				if(lineFlag){
					continue;
				}
				Employee newEmp = new Employee(paramList.get(0), paramList.get(1), paramList.get(2), paramList.get(3),
						paramList.get(4), paramList.get(5), paramList.get(6), paramList.get(7), paramList.get(8),
						paramList.get(9), paramList.get(10), paramList.get(11), paramList.get(12), postList,
						paramList.get(17), paramList.get(18),paramList.get(19));
				newEmp.setOperateUserId(operateUserId);
				if ("女".equals(newEmp.getEmployeeSex())) {
					newEmp.setEmployeeSex("1");
				} else if("男".equals(newEmp.getEmployeeSex())){
					newEmp.setEmployeeSex("0");
				}else{
					String importMessage = "第" + i + "行,请正确填写性别";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					continue;
				}
				if (StringUtils.isNotEmpty(newEmp.getMarriageStatus())) {
					if ("离异".equals(newEmp.getMarriageStatus())) {
						newEmp.setMarriageStatus("2");
					} else if ("已婚".equals(newEmp.getMarriageStatus())) {
						newEmp.setMarriageStatus("1");
					} else if("未婚".equals(newEmp.getMarriageStatus())){
						newEmp.setMarriageStatus("0");
					}else{
						String importMessage = "第" + i + "行,请正确填写婚姻状况";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						continue;
					}
				}
				if ("离职".equals(newEmp.getEmployeeStatus())) {
					newEmp.setEmployeeStatus("1");
				} else if ("删除".equals(newEmp.getEmployeeStatus())) {
					newEmp.setEmployeeStatus("2");
				} else if("在职".equals(newEmp.getEmployeeStatus())){
					newEmp.setEmployeeStatus("0");
				}else{
					String importMessage = "第" + i + "行,请正确填写在职状态";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					continue;
				}
				String entryTime = "";
				String probationaryExpired = "";
				try {
					entryTime = TimeUtil.timeFormatTransfer(newEmp.getEntryTime());
					probationaryExpired = TimeUtil.timeFormatTransfer(newEmp.getProbationaryExpired());

				} catch (Exception e) {
					String importMessage = "第" + i + "行," + ((CustomException) e).getExceptionMessage();
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					continue;
				}
				if (TimeUtil.compareTime(entryTime, probationaryExpired)) {
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
				Employee empLoginName = employeeService.selectEmployeeByLoginNameAndCompanyId(loginName, companyId);
				if(empLoginName!=null){
					String importMessage = "第" + i + "行,登录名已存在!";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					continue;
				}
				if (!loginNameMatch) {
					String importMessage = "第" + i + "行,登录名格式必须为11位手机号!";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					continue;
				}
				String directPersonLoginName = newEmp.getDirectPersonLoginName();//汇报人登录名
				boolean directPersonLoginNameMatch = true;
				if(StringUtils.isNotEmpty(directPersonLoginName) && "0".equals(directPersonLoginName)){
					directPersonLoginNameMatch = RegexUtil.matchPhone(directPersonLoginName);
				}
				if(!directPersonLoginNameMatch){
					String importMessage = "第" + i + "行,汇报人登录名格式必须为11位手机号!";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					continue;
				}
				/*String onePhone = newEmp.getEmployeePhone();
				boolean onePhoneMatch = true;
				if(StringUtils.isNotEmpty(onePhone) && "0".equals(onePhone)){
					onePhoneMatch = RegexUtil.matchPhone(onePhone);
				}
				if(!onePhoneMatch){
					String importMessage = "第" + i + "行,联系方式1格式必须为11位手机号!";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					continue;
				}
				String towPhone = newEmp.getEmployeeTwophone();
				boolean towPhoneMatch = true;
				if(StringUtils.isNotEmpty(towPhone) && "0".equals(towPhone)){
					towPhoneMatch = RegexUtil.matchPhone(towPhone);
				}
				if(!towPhoneMatch){
					String importMessage = "第" + i + "行,联系方式2格式必须为11位手机号!";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					continue;
				}*/
				// 查询直接汇报人是否存在
				String directPersonName = newEmp.getDirectPersonName();//汇报人姓名
				//汇报人姓名和登录名是否填写
				if(StringUtils.isNotEmpty(directPersonName) && StringUtils.isNotEmpty(directPersonLoginName)){
					//已填写,下一步
					Employee directPerson = employeeService.selectEmployeeByLoginNameAndCompanyId(directPersonLoginName, companyId);
					//汇报人是否存在
					if(directPerson==null){
						//不存在
						String importMessage = "第" + i + "行,直接汇报人的登录名不存在!";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						continue;
					}else{
						//存在,判断填写的登录人姓名和登录名是否正确
						if(directPerson.getEmployeeName().equals(directPersonName)){
							//正确
							newEmp.setDirectPersonId(directPerson.getEmployeeId());//设置登录人id
						}else{
							//不正确
							String importMessage = "第" + i + "行,直接汇报人姓名和登录名不匹配!";
							ImportReturnData importReturnData = new ImportReturnData();
							importReturnData.setImportMessage(importMessage);
							importReturnDataList.add(importReturnData);
							continue;
						}
						
					}
				}
				// 查询添加信息中的部门是否存在
				boolean departmentFlag = true;
				List<Department> departmentList = departmentService.findByAllDepartment(companyId);
				if (departmentList.size() < 1) {
					String importMessage = "第" + i + "行,公司还没有创建部门,请先添加部门信息";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					continue;
				}
				for (Department department : departmentList) {
					if (newEmp.getDepartmentName().equals(department.getDepartmentName())) {
						newEmp.setDepartmentId(department.getDepartmentId());
						departmentFlag = false;
					}
				}
				if (departmentFlag) {
					String importMessage = "第" + i + "行,填写的部门不存在";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					continue;
				}
				// 查询添加信息中的岗位是否存在于对应的部门之中
				boolean masterPostFlag = true;
				/*boolean vicePostOneFlag = true;
				boolean vicePostTowFlag = true;*/
				List<Post> postListFromDepartment = postService.findBydepartmentPost(companyId,
						newEmp.getDepartmentId());
				if (postListFromDepartment.size() < 1) {
					String importMessage = "第" + i + "行," + newEmp.getDepartmentName() + "部门还没有设置岗位,请先添加岗位";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					continue;
				}
				// 主岗位判断
				for (Post post : postListFromDepartment) {
					if (newEmp.getPostName().equals(post.getPostName())) {
						newEmp.setPostId(post.getPostId());//设置主岗位id
						masterPostFlag = false;
					}
				}
				if (masterPostFlag) {
					String importMessage = "第" + i + "行,主岗位不存在";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					continue;
				}
				
				List<Post> vicePostList = newEmp.getPostList();
				//副岗位部门判断
				for(Post post : vicePostList){
					if(StringUtils.isNotEmpty(post.getDepartmentName())){
						for (Department department : departmentList) {
							if (post.getDepartmentName().equals(department.getDepartmentName())) {
								post.setDepartmentId(department.getDepartmentId());
								departmentFlag = false;
							}
						}
						if (departmentFlag) {
							String importMessage = "第" + i + "行,填写的副部门不存在";
							ImportReturnData importReturnData = new ImportReturnData();
							importReturnData.setImportMessage(importMessage);
							importReturnDataList.add(importReturnData);
							continue;
						}else{
							if(StringUtils.isNotEmpty(post.getPostName())){
								Department department = departmentService.selectDepatmentByDepartmentNameAndCompanyId(companyId, post.getDepartmentName());
								List<Post> postList1 = postService.selectPostByDepartmentIdAndCompanyId(companyId, department.getDepartmentId());
								if(postList!=null &&postList.size()>0){
									boolean post1Flag = true;
									for(Post post1 : postList1){
										if(post1!=null && StringUtils.isNotEmpty(post1.getPostName()) && post.getPostName().equals(post1.getPostName())){
											if(department.getDepartmentName().equals(newEmp.getDepartmentName()) && post.getPostName().equals(newEmp.getPostName())){
												String importMessage = "第" + i + "行,主岗位和副岗位不可以相同";
												ImportReturnData importReturnData = new ImportReturnData();
												importReturnData.setImportMessage(importMessage);
												importReturnDataList.add(importReturnData);
												continue;
											}else{
												post.setDepartmentId(department.getDepartmentId());
												post.setDepartmentName(department.getDepartmentName());
												post.setPostId(post1.getPostId());
												post.setPostName(post1.getPostName());
												post.setPostGrades("0");
											}
											post1Flag = false;
										}
									}
									if(post1Flag){
										String importMessage = "第" + i + "行,填写的副部门不存在填写的副岗位";
										ImportReturnData importReturnData = new ImportReturnData();
										importReturnData.setImportMessage(importMessage);
										importReturnDataList.add(importReturnData);
										continue;
									}
								}else{
									String importMessage = "第" + i + "行,填写的副部门没有岗位";
									ImportReturnData importReturnData = new ImportReturnData();
									importReturnData.setImportMessage(importMessage);
									importReturnDataList.add(importReturnData);
									continue;
								}
							}else{
								String importMessage = "第" + i + "行,填写的副部门下对应的副岗位名称";
								ImportReturnData importReturnData = new ImportReturnData();
								importReturnData.setImportMessage(importMessage);
								importReturnDataList.add(importReturnData);
								continue;
							}
						}
					}
				}
				
				// 副岗位判断
				/*for (Post post : postListFromDepartment) {
					if (StringUtils.isNotEmpty(vicePostList.get(0).getPostName())) {
						if (vicePostList.get(0).getPostName().equals(post.getPostName())) {
							vicePostList.get(0).setPostId(post.getPostId());//设置副岗位1的id
							vicePostList.get(0).setDepartmentId(newEmp.getDepartmentId());
							vicePostList.get(0).setPostGrades("0");
							vicePostOneFlag = false;
						}
					}
					if (StringUtils.isNotEmpty(vicePostList.get(1).getPostName())) {
						if (vicePostList.get(1).getPostName().equals(post.getPostName())) {
							vicePostList.get(1).setPostId(post.getPostId());//设置副岗位2的id
							vicePostList.get(1).setDepartmentId(newEmp.getDepartmentId());
							vicePostList.get(1).setPostGrades("0");
							vicePostTowFlag = false;
						}
					}
				}
				if (StringUtils.isNotEmpty(vicePostList.get(0).getPostName())) {
					if (vicePostOneFlag) {
						String importMessage = "第" + i + "行,副岗位1不存在";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						continue;
					} else {
						if (vicePostList.get(0).getPostName().equals(newEmp.getPostName())) {
							String importMessage = "第" + i + "行,主岗位与副岗位1不可相同";
							ImportReturnData importReturnData = new ImportReturnData();
							importReturnData.setImportMessage(importMessage);
							importReturnDataList.add(importReturnData);
							continue;
						}
					}
				}
				if (StringUtils.isNotEmpty(vicePostList.get(1).getPostName())) {
					if (vicePostTowFlag) {
						String importMessage = "第" + i + "行,副岗位2不存在";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						continue;
					} else {
						if (vicePostList.get(1).getPostName().equals(newEmp.getPostName())) {
							String importMessage = "第" + i + "行,主岗位与副岗位2不可相同";
							ImportReturnData importReturnData = new ImportReturnData();
							importReturnData.setImportMessage(importMessage);
							importReturnDataList.add(importReturnData);
							continue;
						}

					}
				}
				if (StringUtils.isNotEmpty(vicePostList.get(0).getPostName()) && StringUtils.isNotEmpty(vicePostList.get(1).getPostName())) {
					if (!vicePostOneFlag && !vicePostTowFlag) {
						if (vicePostList.get(1).getPostName().equals(vicePostList.get(0).getPostName())) {
							String importMessage = "第" + i + "行,副岗位1与副岗位2不可相同";
							ImportReturnData importReturnData = new ImportReturnData();
							importReturnData.setImportMessage(importMessage);
							importReturnDataList.add(importReturnData);
							continue;
						}
					}
				}*/
				Post masterPost = new Post();
				masterPost.setPostId(newEmp.getPostId());
				masterPost.setPostName(newEmp.getPostName());
				masterPost.setDepartmentName(newEmp.getDepartmentName());
				masterPost.setDepartmentId(newEmp.getDepartmentId());
				masterPost.setPostGrades("1");
				newEmp.getPostList().add(masterPost);
				newEmp.setCompanyId(companyId);
				newEmp.setOperateUserId(operateUserId);
				ReturnData serviceReturnData = employeeService.insertEmployee(newEmp);
				if(!"3000".equals(serviceReturnData.getReturnCode())){
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(serviceReturnData.getMessage());
					importReturnDataList.add(importReturnData);
				}
			}
		
		boolean flag = false;
		for (ImportReturnData importReturnDataObj : importReturnDataList) {
			if (StringUtils.isNotEmpty(importReturnDataObj.getImportMessage())) {
				flag = true;
			}
		}
		if (flag) {
			returnData.setData(importReturnDataList);
			returnData.setMessage("部分导入失败,请检查表格数据是否填写正确");
			returnData.setReturnCode("4117");
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return returnData;
		}
		returnData.setMessage("成功");
		returnData.setReturnCode("3000");
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnData;
	}
}
