package com.xiangshangban.organization.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.arjuna.ats.internal.jdbc.drivers.modifiers.list;
import com.xiangshangban.organization.bean.Department;
import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.ImportReturnData;
import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.exception.CustomException;
import com.xiangshangban.organization.thread.ImportThread;
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
		logger.info(key);
		// Map<String, String> result = new HashMap<String, String>();
		// 判断是否为excel类型文件
		if (!key.endsWith(".xls") && !key.endsWith(".xlsx")) {
			returnData.setMessage("文件不是excel类型");
			returnData.setReturnCode("4116");
			return returnData;
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
			List<ImportReturnData> importReturnDataList = new ArrayList<ImportReturnData>();
			List<Employee> importEmployeeList = new ArrayList<Employee>();
			List<Employee> list = new ArrayList<Employee>();
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
					Cell cell = row.getCell(k);
					String value = "";
					try{
						if(k==10 || k == 11){
							try{
									value = POIUtil.getExcelCellValue(cell);
							}catch(Exception e){
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								value = sdf.format(sdf.parse(value));
							}
						}else if(k==5||k==8||k==17||k==18){
									value = POIUtil.getExcelCellValue(cell);
						}else{
								value = POIUtil.getExcelCellValue(cell);
						}
					}catch(Exception e){
						String importMessage = "第" + i + "行,第" +( k+1) + "列,请检查数据格式!";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						lineFlag = true;
						break;
					}
					if (k == 1   || k == 5 || k == 6 /*|| k == 2 || k == 3|| k == 9 || k == 10 || k == 11 || k == 12*/) {
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
						paramList.add(value);
				}
				if(lineFlag){
					continue;
				}
				Employee newEmp = new Employee(paramList.get(0)/*employeeNo*/,paramList.get(1)/*employeeName*/, paramList.get(2)/*employeeSex*/, 
						paramList.get(3)/*workAddress*/,paramList.get(4)/*marriageStatus*/, paramList.get(5)/*loginName*/,
						paramList.get(6)/*departmentName*/,paramList.get(7)/*directPersonName*/, paramList.get(8)/*directPersonLoginName*/,
						paramList.get(9)/*employeeStatus*/,paramList.get(10)/*entryTime*/, paramList.get(11)/*probationaryExpired*/,
						paramList.get(12)/*postName*/, postList/*postList*/,paramList.get(17)/*employeePhone*/, 
						paramList.get(18)/*employeeTwophone*/,paramList.get(19)/*seniority*/);
				newEmp.setOperateUserId(operateUserId);
				importEmployeeList.add(newEmp);
			}
			//找出错误数据
			//工号判断
			//1.传入的数据中工号不能有重复
			//2.传入的数据中工号不能与公司已有的工号重复
			
			//登录名判断
			//1.传入的数据中登录名不能有重复
			//2.传入的数据中登录名不能与公司已有的登录名重复

			//汇报人登录名判断
			//1.传入的数据中汇报人登录名在公司系统数据中是否存在
			//2.如果不存在,判断传入数据中是否存在
			//3.如果存在,判断填写的汇报人姓名是否和登录名对应
			
			//部门,岗位判断
			//1.判断传入的数据中部门是否在公司系统中存在
			//2.判断部门中是否包含填写的岗位(存在全公司特例)
			//3.判断传入的数据中岗位之间是否同名,不允许相同
			//首先对外部数据整体检查
			Iterator<Employee> emp = importEmployeeList.iterator();
			boolean externalFlag = false;
			for(int j=0;j<importEmployeeList.size();j++){
				for(int k=j+1;k<importEmployeeList.size();k++){
					if(StringUtils.isNotEmpty(importEmployeeList.get(j).getEmployeeNo()) && StringUtils.isNotEmpty(importEmployeeList.get(k).getEmployeeNo()) && importEmployeeList.get(j).getEmployeeNo().equals(importEmployeeList.get(k).getEmployeeNo())){
						String importMessage = "第" + (j+2) + "行与第"+(k+2)+"行工号重复";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						externalFlag =true;
						continue;
					}
				}
				if ("女".equals(importEmployeeList.get(j).getEmployeeSex())) {
					importEmployeeList.get(j).setEmployeeSex("1");
				} else if("男".equals(importEmployeeList.get(j).getEmployeeSex())){
					importEmployeeList.get(j).setEmployeeSex("0");
				}else if(StringUtils.isNotEmpty(importEmployeeList.get(j).getEmployeeSex())){
					String importMessage = "第" + (j+2) + "行,请正确填写性别";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					externalFlag =true;
					continue;
				}
				if (StringUtils.isNotEmpty(importEmployeeList.get(j).getMarriageStatus())) {
					if ("离异".equals(importEmployeeList.get(j).getMarriageStatus())) {
						importEmployeeList.get(j).setMarriageStatus("2");
					} else if ("已婚".equals(importEmployeeList.get(j).getMarriageStatus())) {
						importEmployeeList.get(j).setMarriageStatus("1");
					} else if("未婚".equals(importEmployeeList.get(j).getMarriageStatus())){
						importEmployeeList.get(j).setMarriageStatus("0");
					}else{
						String importMessage = "第" + (j+2) + "行,请正确填写婚姻状况";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						externalFlag =true;
						continue;
					}
				}
				if(StringUtils.isNotEmpty(importEmployeeList.get(j).getEmployeeStatus())){
					if ("离职".equals(importEmployeeList.get(j).getEmployeeStatus())) {
						importEmployeeList.get(j).setEmployeeStatus("1");
					} else if ("删除".equals(importEmployeeList.get(j).getEmployeeStatus())) {
						importEmployeeList.get(j).setEmployeeStatus("2");
					} else if("在职".equals(importEmployeeList.get(j).getEmployeeStatus())){
						importEmployeeList.get(j).setEmployeeStatus("0");
					}else{
						String importMessage = "第" + (j+2) + "行,请正确填写在职状态";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						externalFlag =true;
						continue;
					}
				}
				String entryTime = "";
				String probationaryExpired = "";
				try {
					if(StringUtils.isNotEmpty(importEmployeeList.get(j).getEntryTime())){
						entryTime = TimeUtil.timeFormatTransfer(importEmployeeList.get(j).getEntryTime());
					}
					if(StringUtils.isNotEmpty(importEmployeeList.get(j).getProbationaryExpired())){
						probationaryExpired = TimeUtil.timeFormatTransfer(importEmployeeList.get(j).getProbationaryExpired());
					}
				} catch (Exception e) {
					String importMessage = "第" + (j+2) + "行," + ((CustomException) e).getExceptionMessage();
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					externalFlag =true;
					continue;
				}
				if(StringUtils.isNotEmpty(importEmployeeList.get(j).getEntryTime()) && StringUtils.isNotEmpty(importEmployeeList.get(j).getProbationaryExpired())){
					if (TimeUtil.compareTime(entryTime, probationaryExpired)) {
						String importMessage = "第" + (j+2) + "行,转正时间必须大于入职时间";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						externalFlag =true;
						continue;
					}
				}
				boolean loginNameMatch = RegexUtil.matchPhone(importEmployeeList.get(j).getLoginName());
				if (!loginNameMatch) {
					String importMessage = "第" + (j+2) + "行,登录名格式必须为11位手机号!";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					externalFlag =true;
					continue;
				}
				for(int k=j+1;k<importEmployeeList.size();k++){
					if(StringUtils.isNotEmpty(importEmployeeList.get(j).getLoginName()) && StringUtils.isNotEmpty(importEmployeeList.get(k).getLoginName()) && importEmployeeList.get(j).getLoginName().equals(importEmployeeList.get(k).getLoginName())){
						String importMessage = "第" + (j+2) + "行与第"+(k+2)+"行登录名重复";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						externalFlag =true;
						continue;
					}
				}
				String masterDepartmentName = importEmployeeList.get(j).getDepartmentName();
				String masterPostName = importEmployeeList.get(j).getPostName();
				if(importEmployeeList.get(j).getPostList()!=null&&importEmployeeList.get(j).getPostList().size()>=0){
					if(importEmployeeList.get(j).getPostList().size()==1){
						String v1DepartmentName = importEmployeeList.get(j).getPostList().get(0).getDepartmentName();
						String v1PostName =  importEmployeeList.get(j).getPostList().get(0).getPostName();
						if(StringUtils.isNotEmpty(v1PostName) && StringUtils.isEmpty(v1DepartmentName)){
							importEmployeeList.get(j).getPostList().get(0).setDepartmentName("全公司");
						}
						if(masterDepartmentName.equals(v1DepartmentName)&&StringUtils.isNotEmpty(masterPostName)&&StringUtils.isNotEmpty(v1PostName) && masterPostName.equals(v1PostName)){
							String importMessage = "第" + (j+2) + "行,主岗位与副岗位相同";
							ImportReturnData importReturnData = new ImportReturnData();
							importReturnData.setImportMessage(importMessage);
							importReturnDataList.add(importReturnData);
							externalFlag =true;
							continue;
						}
					}
					if(importEmployeeList.get(j).getPostList().size()==2){
						String v1DepartmentName = importEmployeeList.get(j).getPostList().get(0).getDepartmentName();
						String v1PostName =  importEmployeeList.get(j).getPostList().get(0).getPostName();
						if(StringUtils.isNotEmpty(v1PostName) && StringUtils.isEmpty(v1DepartmentName)){
							importEmployeeList.get(j).getPostList().get(0).setDepartmentName("全公司");
						}
						String v2DepartmentName = importEmployeeList.get(j).getPostList().get(1).getDepartmentName();
						String v2PostName =  importEmployeeList.get(j).getPostList().get(1).getPostName();
						if(StringUtils.isNotEmpty(v2PostName) && StringUtils.isEmpty(v1DepartmentName)){
							importEmployeeList.get(j).getPostList().get(0).setDepartmentName("全公司");
						}					
						if((masterDepartmentName.equals(v1DepartmentName)&&StringUtils.isNotEmpty(masterPostName)&&StringUtils.isNotEmpty(v1PostName) && masterPostName.equals(v1PostName))
								||(masterDepartmentName.equals(v2DepartmentName)&&StringUtils.isNotEmpty(masterPostName)&&StringUtils.isNotEmpty(v2PostName) && masterPostName.equals(v2PostName))){
							String importMessage = "第" + (j+2) + "行,主岗位与副岗位相同";
							ImportReturnData importReturnData = new ImportReturnData();
							importReturnData.setImportMessage(importMessage);
							importReturnDataList.add(importReturnData);
							externalFlag =true;
							continue;
						}
						if(v1DepartmentName.equals(v2DepartmentName)&&StringUtils.isNotEmpty(v2PostName)&&StringUtils.isNotEmpty(v1PostName) && v2PostName.equals(v1PostName)){
							String importMessage = "第" + (j+2) + "行,副岗位相同";
							ImportReturnData importReturnData = new ImportReturnData();
							importReturnData.setImportMessage(importMessage);
							importReturnDataList.add(importReturnData);
							externalFlag =true;
							continue;
						}
						
					}
				}
				if(StringUtils.isNotEmpty(importEmployeeList.get(j).getDirectPersonLoginName())&&StringUtils.isNotEmpty(importEmployeeList.get(j).getDirectPersonName())){
					boolean directPersonLoginNameMatch = RegexUtil.matchPhone(importEmployeeList.get(j).getDirectPersonLoginName());
					if(!directPersonLoginNameMatch){
						String importMessage = "第" + (j+2) + "行,汇报人登录名格式必须为11位手机号!";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						externalFlag =true;
						continue;
					}
				}else if(StringUtils.isEmpty(importEmployeeList.get(j).getDirectPersonLoginName())&&StringUtils.isEmpty(importEmployeeList.get(j).getDirectPersonName())){
				}else{
					String importMessage = "第" + (j+2) + "行,必须同时填写汇报人登录名和汇报人姓名!";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					externalFlag =true;
					continue;
				}
			}
			if(externalFlag){
				returnData.setData(importReturnDataList);
				returnData.setMessage("拒绝导入,表格存在错误数据,请仔细检查表格数据是否完全正确");
				returnData.setReturnCode("4117");
				return returnData;
			}
			//内部数据检查
		
			int i =1;
			Iterator<Employee> iterator = importEmployeeList.iterator();
			while(iterator.hasNext()){
				i=i+1;
				Employee newEmp = iterator.next();
				boolean lineFlag = false;
				newEmp.setCompanyId(companyId);
				//String employeeNo = newEmp.getEmployeeNo();
				//String loginName = newEmp.getLoginName();
				//查询公司所有的员工
				List<Employee> employeeList = employeeService.selectAllEmployeeByCompanyId(companyId);
				if (/*StringUtils.isNotEmpty(newEmp.getEmployeeNo())&& */employeeList!=null &&employeeList.size()>0) {
					//Employee employeeNotemp = employeeService.findByemployeeNo(employeeNo, companyId);
					for(Employee employee :employeeList){
						
						if (StringUtils.isNotEmpty(employee.getEmployeeNo())&&newEmp.getEmployeeNo().equals(employee.getEmployeeNo())) {
							String importMessage = "第" + i + "行,工号已存在!";
							ImportReturnData importReturnData = new ImportReturnData();
							importReturnData.setImportMessage(importMessage);
							importReturnDataList.add(importReturnData);
							//iterator.remove();
							lineFlag=true;
							break;
						}
						if(employee.getLoginName().equals(newEmp.getLoginName())){
							String importMessage = "第" + i + "行,登录名已存在!";
							ImportReturnData importReturnData = new ImportReturnData();
							importReturnData.setImportMessage(importMessage);
							importReturnDataList.add(importReturnData);
							//iterator.remove();
							lineFlag=true;
							break;
						}
					}
						//String directPersonLoginName = newEmp.getDirectPersonLoginName();//汇报人登录名
						//String directPersonName = newEmp.getDirectPersonName();//汇报人姓名
						/*if(StringUtils.isEmpty(newEmp.getDirectPersonLoginName()) && StringUtils.isEmpty(newEmp.getDirectPersonName())){
							flag=false;
						}*/
						if(StringUtils.isNotEmpty(newEmp.getDirectPersonLoginName()) && StringUtils.isNotEmpty(newEmp.getDirectPersonName())){
							boolean flag = true;
							for(Employee employee :employeeList){
								if(newEmp.getDirectPersonLoginName().equals(employee.getLoginName())){
									flag = false;
								}
							}
							if(flag){
								String importMessage = "第" + i + "行,直接汇报人的登录名不存在!";
								ImportReturnData importReturnData = new ImportReturnData();
								importReturnData.setImportMessage(importMessage);
								importReturnDataList.add(importReturnData);
								//iterator.remove();
								lineFlag=true;
								break;
							}
							flag=true;
							for(Employee employee :employeeList){
								if(newEmp.getDirectPersonLoginName().equals(employee.getLoginName())&&newEmp.getDirectPersonName().equals(employee.getEmployeeName())){
									newEmp.setDirectPersonId(employee.getEmployeeId());//设置登录人id
									flag = false;
								}
							}
							if(flag){
								String importMessage = "第" + i + "行,直接汇报人姓名和登录名不匹配!";
								ImportReturnData importReturnData = new ImportReturnData();
								importReturnData.setImportMessage(importMessage);
								importReturnDataList.add(importReturnData);
								//iterator.remove();
								lineFlag=true;
								break;
							}
						}
					}
					
					if(lineFlag){
						continue;
					}
				
					//查询公司所有的部门以及部门下的岗位
					List<Department> departmentList = departmentService.selectDepartmentAndPostByCompanyId(companyId);
					if (departmentList.size() < 1) {
						String importMessage = "第" + i + "行,公司还没有创建部门,请先添加部门信息";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						//iterator.remove();
						lineFlag=true;
						continue;
					}
					boolean masterDepartment = true;//主部门是否存在的标志
					boolean departmentFlag = true;//主部门是否存在填写的主岗位的标志
					for(Department department :departmentList){
						//主岗位
						if(newEmp.getDepartmentName().equals(department.getDepartmentName())){
							if(StringUtils.isNotEmpty(newEmp.getPostName())){
								if(department.getDepartmentName().equals(newEmp.getDepartmentName())){
									for(Post post:department.getPostList()){
										if(post.getPostName().equals(newEmp.getPostName())){
											newEmp.setPostId(post.getPostId());
											departmentFlag =false;
										}
									}
								}
							}
							newEmp.setDepartmentId(department.getDepartmentId());
							masterDepartment=false;
						}
					}
				if(masterDepartment){
					String importMessage = "第" + i + "行,填写的主部门不存在";
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(importMessage);
					importReturnDataList.add(importReturnData);
					//iterator.remove();
					lineFlag=true;
					continue;
				}
				if(StringUtils.isNotEmpty(newEmp.getPostName())){
					if(departmentFlag){
						String importMessage = "第" + i + "行,填写的主部门中没有填写的主岗位";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						//iterator.remove();
						lineFlag=true;
						continue;
					}
				}
					boolean vDepartment = false;//副部门是否存在的标志
					boolean vdepartmentFlag = false;//副部门是否存在填写的副岗位的标志
					//副岗位
					for(Post post:newEmp.getPostList()){
						if(StringUtils.isNotEmpty(post.getDepartmentName())){
							vDepartment=true;
							vdepartmentFlag=true;
							for(Department department :departmentList){
								if(post.getDepartmentName().equals(department.getDepartmentName())){
									post.setDepartmentId(department.getDepartmentId());
									vDepartment=false;
									for(Post vpost: department.getPostList()){
										if(vpost.getPostName().equals(post.getPostName())){
											post.setDepartmentId(department.getDepartmentId());
											post.setDepartmentName(department.getDepartmentName());
											post.setPostId(vpost.getPostId());
											post.setPostName(vpost.getPostName());
											post.setPostGrades("0");
											vdepartmentFlag=false;
										}
									}
								}
							}
						}
					}
					if(vDepartment){
						String importMessage = "第" + i + "行,填写的副部门不存在";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						//iterator.remove();
						lineFlag=true;
						continue;
					}
					if(vdepartmentFlag){
						String importMessage = "第" + i + "行,请填写正确的副部门、副岗位";
						ImportReturnData importReturnData = new ImportReturnData();
						importReturnData.setImportMessage(importMessage);
						importReturnDataList.add(importReturnData);
						//iterator.remove();
						lineFlag=true;
						continue;
					}
			
				Post masterPost = new Post();
				masterPost.setPostId(newEmp.getPostId());
				masterPost.setPostName(newEmp.getPostName());
				masterPost.setDepartmentName(newEmp.getDepartmentName());
				masterPost.setDepartmentId(newEmp.getDepartmentId());
				masterPost.setPostGrades("1");
				newEmp.getPostList().add(masterPost);
				newEmp.setCompanyId(companyId);
				newEmp.setOperateUserId(operateUserId);
				//将筛选结果存于新的集合中
				list.add(newEmp);
				
			}
			if(list!=null&&list.size()>0){
			int length = list.size();
			List<Employee> list1 = new ArrayList<Employee>();
			List<Employee> list2 = new ArrayList<Employee>();
			for(int n=0;n<length/2;n++){
				list1.add(importEmployeeList.get(n));
			}
			for(int m=length/2;m<length;m++){
				list2.add(importEmployeeList.get(m));
			}
			/*ReturnData serviceReturnData = employeeService.insertEmployee(newEmp);
				if(!"3000".equals(serviceReturnData.getReturnCode())){
					ImportReturnData importReturnData = new ImportReturnData();
					importReturnData.setImportMessage(serviceReturnData.getMessage());
					importReturnDataList.add(importReturnData);
				}*/
			ImportThread importThread1 = new ImportThread(employeeService,importReturnDataList,list1);
			ImportThread importThread2 = new ImportThread(employeeService,importReturnDataList,list2);
			Thread thead1 = new Thread(importThread1);
			Thread thead2 = new Thread(importThread2);
			thead1.run();
			thead2.run();
			if(i==totalRowNum){
				try {
					thead1.join();
					thead2.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/*for(Employee employee:list){
				employeeService.activeEmp(employee.getCompanyId(), employee.getEmployeeId());
				employeeService.resetEmployeeStatus(employee.getCompanyId(), employee.getEmployeeId());
			}*/
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
