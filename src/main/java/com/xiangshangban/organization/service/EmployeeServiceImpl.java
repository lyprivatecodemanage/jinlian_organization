package com.xiangshangban.organization.service;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangshangban.organization.bean.CheckPerson;
import com.xiangshangban.organization.bean.Company;
import com.xiangshangban.organization.bean.ConnectEmpPost;
import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.bean.Transferjob;
import com.xiangshangban.organization.bean.Uroles;
import com.xiangshangban.organization.bean.UserCompanyDefault;
import com.xiangshangban.organization.bean.Uusers;
import com.xiangshangban.organization.bean.UusersRoles;
import com.xiangshangban.organization.dao.CheckPersonDao;
import com.xiangshangban.organization.dao.CompanyDao;
import com.xiangshangban.organization.dao.ConnectEmpPostDao;
import com.xiangshangban.organization.dao.EmployeeDao;
import com.xiangshangban.organization.dao.TransferjobDao;
import com.xiangshangban.organization.dao.UserCompanyDefaultDao;
import com.xiangshangban.organization.dao.UusersDao;
import com.xiangshangban.organization.util.FormatUtil;
import com.xiangshangban.organization.util.HttpClientUtil;
import com.xiangshangban.organization.util.PropertiesUtils;
import com.xiangshangban.organization.util.TimeUtil;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	private static final Logger logger = Logger.getLogger(EmployeeServiceImpl.class);
	@Autowired
	EmployeeDao employeeDao;
	@Autowired
	UserCompanyDefaultDao userCompanyDefaultDao;
	@Autowired
	UusersDao usersDao;
	@Autowired
	TransferjobDao transferjobDao;
	@Autowired
	ConnectEmpPostDao connectEmpPostDao;
	@Autowired
	CheckPersonDao checkPersonDao;
	@Autowired
	CompanyDao companyDao;
	
	
	@Override
	public int deleteByEmployee(String companyId,String employeeId) {
		int i = 0;
		Employee employee = employeeDao.selectByEmployee(employeeId, companyId);
		i=i+userCompanyDefaultDao.deleteUserFromCompany(companyId,employeeId);//更改is_active='2', current_option='2'
		i=i+employeeDao.deleteByEmployee(employeeId,companyId);
		i+=connectEmpPostDao.deleteByEmployeeIdAndCompanyId(employeeId, companyId);
		//更改默认公司设置
		//查询已激活并且为默认的公司
		UserCompanyDefault companyDefalt = userCompanyDefaultDao.getActiveDefault(employeeId);
		if(companyDefalt==null || StringUtils.isEmpty(companyDefalt.getCompanyId())){
			//抽取排序中已激活但非默认的第一个公司作为默认公司
			UserCompanyDefault newDefalt = userCompanyDefaultDao.getActiveNoDefaultFirst(employeeId);
			if(companyDefalt!=null && StringUtils.isNotEmpty(companyDefalt.getCompanyId())){
				//设置默认公司
				userCompanyDefaultDao.updateCurrentCompany(newDefalt.getCompanyId(), employeeId);
			}
		}
		if(i>1){
			employee.setCompanyId(companyId);
			employee.setEmployeeStatus("1");
			this.deleteDeviceEmp(employee);
		}
		
		return i;
	}

	@Override
	public ReturnData insertEmployee(Employee employee) {
		ReturnData returnData = new ReturnData();
		Uusers user = usersDao.selectByPhone(employee.getLoginName());
		Employee emp = employeeDao.selectEmployeeByLoginNameAndCompanyId(employee.getLoginName(), employee.getCompanyId());
		if(emp!=null){
			returnData.setMessage("登录名已被占用");
			returnData.setReturnCode("4115");
			return returnData;
		}
		employee.setEmployeeStatus("0");
		if(user==null || StringUtils.isEmpty(user.getUserid())){//未注册，写入注册表	
			user = new Uusers();
			String employeeId = FormatUtil.createUuid();			
			employee.setEmployeeId(employeeId);	
			
			employeeDao.insertEmployee(employee);
			
			user.setUserid(employeeId);
			user.setUsername(employee.getEmployeeName());
			user.setPhone(employee.getLoginName());
			user.setStatus("1");
			usersDao.insertSelective(user);//加入注册表
			
			UserCompanyDefault userCompany = new UserCompanyDefault();
			userCompany.setCompanyId(employee.getCompanyId());
			//查询已激活并且为默认的公司
			UserCompanyDefault companyDefalt = userCompanyDefaultDao.getActiveDefault(user.getUserid());
			if(companyDefalt==null || StringUtils.isEmpty(companyDefalt.getCompanyId())){
				//抽取排序中已激活但非默认的第一个公司作为默认公司
				userCompany.setCurrentOption("1");
			}else{
				userCompany.setCurrentOption("2");
			}
			userCompany.setUserId(user.getUserid());
			userCompany.setInfoStatus("1");
			userCompanyDefaultDao.insertSelective(userCompany);//添加用户公司的绑定关系
			
			CheckPerson checkPerson = new CheckPerson();
			checkPerson.setUserid(employeeId);
			checkPerson.setCompanyid(employee.getCompanyId());
			checkPerson.setStatus("1");
			checkPerson.setApplyTime(TimeUtil.getCurrentTime());
			checkPersonDao.insertSelective(checkPerson );
		}else{
			employee.setEmployeeId(user.getUserid());
			if(!user.getUsername().equals(employee.getEmployeeName())){//姓名不匹配，添加失败
				returnData.setMessage("登录名已被【"+user.getUsername()+"】使用");
				returnData.setReturnCode("4115");
				return returnData;
			}
			if("0".equals(user.getStatus())){//将不可用状态改为可用
				usersDao.updateStatus(user.getUserid(), "1");
			}
			//添加绑定关系
			UserCompanyDefault userCompany = userCompanyDefaultDao.selectByUserIdAndCompanyId(user.getUserid(), employee.getCompanyId());
			if(userCompany==null || StringUtils.isEmpty(userCompany.getCompanyId())){//不存在绑定关系
				userCompany = new UserCompanyDefault();
				//查询已激活并且为默认的公司
				UserCompanyDefault companyDefalt = userCompanyDefaultDao.getActiveDefault(user.getUserid());
				if(companyDefalt==null || StringUtils.isEmpty(companyDefalt.getCompanyId())){
					//抽取排序中已激活但非默认的第一个公司作为默认公司
					userCompany.setCurrentOption("1");
				}else{
					userCompany.setCurrentOption("2");
				}
				userCompany.setCompanyId(employee.getCompanyId());
				
				userCompany.setUserId(user.getUserid());
				userCompany.setInfoStatus("1");
				userCompanyDefaultDao.insertSelective(userCompany);//添加用户公司的绑定关系
				employeeDao.insertEmployee(employee);//插入人员表
			}else if(userCompany!=null && userCompany.getCompanyId().equals(employee.getCompanyId())){//已存在绑定关系，则直接返回
				if("2".equals(userCompany.getIsActive())){
					userCompany.setIsActive("0");
					userCompany.setInfoStatus("1");
					//查询已激活并且为默认的公司
					UserCompanyDefault companyDefalt = userCompanyDefaultDao.getActiveDefault(user.getUserid());
					if(companyDefalt==null || StringUtils.isEmpty(companyDefalt.getCompanyId())){
						//抽取排序中已激活但非默认的第一个公司作为默认公司
						userCompany.setCurrentOption("1");
					}else{
						userCompany.setCurrentOption("2");
					}
					userCompanyDefaultDao.updateSelective(userCompany);
				}
				this.updateTransfer(employee);//岗位信息设置
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");
				return returnData;
			}
		}
		List<UusersRoles> userRoleList = usersDao.selectRoleByUserIdAndCompanyId(employee.getCompanyId(), user.getUserid());
		if(userRoleList!=null && userRoleList.size()>0){
			usersDao.updateUserRoleByCompanyId(employee.getCompanyId(), user.getUserid(), Uroles.user_role,Uroles.user_role);
		}else{
			usersDao.insertUserRoleByCompanyId(employee.getCompanyId(), user.getUserid(), Uroles.user_role);
		}
	    this.updateTransfer(employee);//岗位信息设置
	    updateDeviceEmp(employee);
	    returnData.setEmployeeId(employee.getEmployeeId());
	   /* returnData.setMessage("数据请求成功");
		returnData.setReturnCode("3000");*/
		return returnData;
	}
	/**
	 * 告知设备模块更新人员信息
	 * @param employee
	 */
	public void updateDeviceEmp(Employee employee) {
		employee = employeeDao.selectByEmployee(employee.getEmployeeId(), employee.getCompanyId());
		Company company = companyDao.selectByCompany(employee.getCompanyId());
	    employee.setCompanyNo(company.getCompanyNo());
		List<Employee> cmdlist=new ArrayList<Employee>();
		cmdlist.add(employee);
		try {
			String result = HttpClientUtil.sendRequet(PropertiesUtils.pathUrl("commandGenerate"), cmdlist);
			logger.info("设备访问成功"+result);
		} catch (IOException e) {
			logger.info("将人员信息更新到设备模块时，获取路径出错");
			e.printStackTrace();
		}
	}
	/**
	 * 告知设备模块更新人员信息
	 * @param employee
	 */
	public void deleteDeviceEmp(Employee employee) {
		Company company = companyDao.selectByCompany(employee.getCompanyId());
	    employee.setCompanyNo(company.getCompanyNo());
		List<Employee> cmdlist=new ArrayList<Employee>();
		cmdlist.add(employee);
		try {
			String result = HttpClientUtil.sendRequet(PropertiesUtils.pathUrl("deleteEmployeeInformationEmp"), cmdlist);
			logger.info("设备访问成功"+result);
		} catch (IOException e) {
			logger.info("将人员信息更新到设备模块时，获取路径出错");
			e.printStackTrace();
		}
	}
	public void updateTransfer(Employee employee) {
		//把员工关联的岗位添加到connect_emp_post_中间表里面
		
		//ConnectEmpPost masterPost = connectEmpPostDao.selectEmployeePostInformation(employee.getEmployeeId(), employee.getCompanyId());
		for(Post post:employee.getPostList()){
			System.out.println("============>"+post.getDepartmentId());
			System.out.println("============>"+post.getPostId());
			//主岗位
			//if("1".equals(post.getPostGrades())){
				/*if(masterPost!=null){
					connectEmpPostDao.updateEmployeeWithPost(employee.getEmployeeId(), post.getDepartmentId(), post.getPostId(),employee.getCompanyId());
				}else{*/
			if(StringUtils.isNotEmpty(post.getPostId()) && StringUtils.isNotEmpty(post.getDepartmentId())){
					ConnectEmpPost connect = new ConnectEmpPost();
					connect.setCompanyId(employee.getCompanyId());
					connect.setDepartmentId(post.getDepartmentId());
					connect.setEmployeeId(employee.getEmployeeId());
					connect.setIsDelete("0");
					connect.setPostId(post.getPostId());
					connect.setPostGrades(post.getPostGrades());
					connectEmpPostDao.saveConnect(connect);
					if("1".equals(post.getPostGrades())){
						Transferjob transferjob = new Transferjob();
						transferjob.setTransferJobId(FormatUtil.createUuid());
						transferjob.setEmployeeId(employee.getEmployeeId());
						transferjob.setDepartmentId(post.getDepartmentId());
						transferjob.setTransferBeginTime(employee.getEntryTime());
						transferjob.setTransferJobCause("入职");		
						transferjob.setUserId(employee.getOperateUserId());//操作人ID	
						transferjob.setCompanyId(employee.getCompanyId());
						transferjob.setPostId(post.getPostId());
						transferjobDao.insertTransferjob(transferjob);
					}
			}
			
		}
	}
	//查询单条员信息
	@Override
	public Employee selectByEmployee(String employeeId,String companyId) {		
		return employeeDao.selectByEmployee(employeeId, companyId);
	}

	@Override
	public String updateByEmployee(Employee employee) {
		String i ="0";
		try {			
			employeeDao.updateByEmployee(employee);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public List<Employee> findByAllEmployee(Map<String,String> map) {
		return employeeDao.findByAllEmployee(map);
	}
	@Override
	public List<Employee> findByAllEmployee(String companyId) {
		return employeeDao.findAllEmployeeByCompanyId(companyId);
	}

	@Override
	public List<Employee> findByMoreEmployee(Map<String, String> map) {
		return employeeDao.findByMoreEmployee(map);
	}

	//批量删除
	public String batchUpdateTest(String employeeId) {
		String i ="0";
		try {
			employeeDao.batchUpdateTest(employeeId);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
		
	}

	@Override
	public Employee findByemployeeNo(String employeeNo, String companyId) {
		return employeeDao.findByemployeeNo(employeeNo, companyId);
	}
	//离职
	@Override
	public String batchUpdateStatus(String employeeId) {
		String i ="0";
		try {
			employeeDao.batchUpdateStatus(employeeId);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
		
	}
	//查询离职
	@Override
	public List<Employee> findByLiZhiemployee(String companyId) {
		
		return employeeDao.findByLiZhiemployee(companyId);
	}
	//调职
	@Override
	public String batchUpdateTransferJobStaus(String employeeId) {
		String i ="0";
		try {
			employeeDao.batchUpdateTransferJobStaus(employeeId);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;		
	}

	@Override
	public String updateByEmployeedept(Employee employee) {
		String i ="0";
		try {
			employeeDao.updateByEmployeedept(employee);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;	
	}

	@Override
	public Employee findByemploginName(String loginName) {
		
		return employeeDao.findByemploginName(loginName);
	}


	@Override
	public List<Employee> findByposcounttemp(String postId,String companyId) {
		// TODO Auto-generated method stub
		return employeeDao.findByposcounttemp(postId, companyId);
	}


	@Override
	public String updateByEmployeeapprove(Employee employee) {
		String i ="0";
		try {								
			employeeDao.updateByEmployeeapprove(employee);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	//查询申请入职的人员信息 
	@Override
	public List<Employee> findByruzhiempinfo(String companyId) {
		// TODO Auto-generated method stub
		return employeeDao.findByruzhiempinfo(companyId);
	}

	@Override
	public List<Employee> selectByAllEmployee(String companyId) {
		// TODO Auto-generated method stub
		return employeeDao.selectByAllEmployee(companyId);
	}

	@Override
	public List<Employee> selectByAllFnyeEmployee(String companyId,String numPage,String numRecordCount, String employeeName, String employeeSex, String departmentName,String postName,String employeeStatus,String departmentId) {
		// TODO Auto-generated method stub
		return employeeDao.selectByAllFnyeEmployee(companyId, numPage, numRecordCount,  employeeName,  employeeSex,  departmentName, postName, employeeStatus,departmentId);
	}



	@Override
	public List<Employee> findByempadmin(Map<String,String> map) {
		// TODO Auto-generated method stub
		return employeeDao.findByempadmin(map);
	}

	@Override
	public List<Employee> findByempadmins(Map<String, String> map) {
		// TODO Auto-generated method stub
		return employeeDao.findByempadmins(map);
	}

	@Override
	public List<Employee> findEmployeeByDepartmentId(String companyId, String departmentId) {
		return employeeDao.findEmployeeByDepartmentId(companyId, departmentId);
	}

	@Override
	public int selectCountEmployeeFromCompany(String companyId, String numPage, String numRecordCount,
			String employeeName, String employeeSex, String departmentName, String postName, String employeeStatus,String  departmentId) {
		
		return employeeDao.selectCountEmployeeFromCompany(companyId, numPage, numRecordCount, employeeName, employeeSex, departmentName, postName, employeeStatus,departmentId);
	}

	@Override
	public int deleteUserFromCompany(String companyId, String employeeId) {
		
		return userCompanyDefaultDao.deleteUserFromCompany(companyId, employeeId);
	}

	@Override
	public Employee selectByEmployeeFromApp(String companyId, String userId) {
	
		return employeeDao.selectByEmployeeFromApp(companyId, userId);
	}

	@Override
	public int updateEmployeeInformation(Employee emp) {
		
		return employeeDao.updateEmployeeInformation(emp);
	}
	@Override
	public int updateEmployeeInfoStatus(Employee employee) {
		int result = employeeDao.updateEmployeeInfoStatus(employee.getCompanyId(), employee.getEmployeeId());
		/*Employee employee = employeeDao.selectByEmployee(userId, companyId);*/
		this.updateDeviceEmp(employee);
		return result;
	}

	@Override
	public int activeEmp(String companyId, String employeeId) {
		Employee employee = employeeDao.selectEmployeeByCompanyIdAndEmployeeId(employeeId, companyId);
		Uusers user = usersDao.selectByPhone(employee.getLoginName());
		employee.setEmployeeStatus("0");
		if(user==null || StringUtils.isEmpty(user.getUserid())){//未注册，写入注册表	
			user = new Uusers();
			user.setUserid(employeeId);
			user.setUsername(employee.getEmployeeName());
			user.setPhone(employee.getLoginName());
			user.setStatus("1");
			usersDao.insertSelective(user);//加入注册表
		}
		return userCompanyDefaultDao.updateActive(companyId, employeeId);
	}

	@Override
	public int isAdmin(String companyId, String employeeId) {
		return employeeDao.isAdmin(companyId, employeeId);
	}

	@Override
	public int resetEmployeeStatus(String companyId, String employeeId) {
		return employeeDao.resetEmployeeStatus(companyId, employeeId);
	}

	@Override
	public Employee selectEmployeeByLoginNameAndCompanyId(String loginName, String companyId) {
		
		return employeeDao.selectEmployeeByLoginNameAndCompanyId(loginName, companyId);
	}

	@Override
	public int updateEmployeeImgUrl(String employeeId, String companyId, String ossFileString) {
		
		return employeeDao.updateEmployeeImgUrl(employeeId, companyId, ossFileString);
	}

	@Override
	public void export(String excelName, OutputStream out, String companyId) {
		List<Employee> empList = employeeDao.findExport(companyId);
		String[] headers = new String[]{"工号","姓名*","性别（男/女）","所在地（省份）*","婚姻状况（已婚/未婚/离异）","登录名（手机号）*","所属部门*",
				"汇报人","汇报人登录名（手机号）", "在职状态（在职/离职）","入职时间*","转正时间*","主岗位*","副部门1","副岗位1","副部门2","副岗位2","联系方式1","联系方式2","工龄"};  
		 // 第一步，创建一个webbook，对应一个Excel文件  
		HSSFWorkbook workbook = new HSSFWorkbook();  
        //生成一个表格  
        HSSFSheet sheet = workbook.createSheet(excelName);  
        //设置表格默认列宽度为15个字符  
        sheet.setDefaultColumnWidth(20);  
        //生成一个样式，用来设置标题样式  
        HSSFCellStyle style = workbook.createCellStyle();  
        //设置这些样式  
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        //生成一个字体  
        HSSFFont font = workbook.createFont();  
        font.setColor(HSSFColor.VIOLET.index);  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        //把字体应用到当前的样式  
        style.setFont(font);  
        // 生成并设置另一个样式,用于设置内容样式  
        HSSFCellStyle style2 = workbook.createCellStyle();  
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);  
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 生成另一个字体  
        HSSFFont font2 = workbook.createFont();  
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
        // 把字体应用到当前的样式  
        style2.setFont(font2);  
        //产生表格标题行  
        HSSFRow row = sheet.createRow(0);  
        for(int i = 0; i<headers.length;i++){  
            HSSFCell cell = row.createCell(i);  
            cell.setCellStyle(style);  
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);  
            cell.setCellValue(text);  
        }  
        for (int i=0;i<empList.size();i++) {  
           Employee emp = empList.get(i);  
            row = sheet.createRow(i+1);  
            int j = 0;  
            row.createCell(j++).setCellValue(emp.getEmployeeNo());//工号
            row.createCell(j++).setCellValue(emp.getEmployeeName());//姓名
            if(emp.getEmployeeSex().equals("0")){
            	row.createCell(j++).setCellValue("男");
            }else{
            	row.createCell(j++).setCellValue("女");
            }
            row.createCell(j++).setCellValue(emp.getWorkAddress());//工作地
            //0：未婚，1：已婚 ，2：离异
            if("2".equals(emp.getMarriageStatus())){
            	row.createCell(j++).setCellValue("离异");
            }else if("1".equals(emp.getMarriageStatus())){
            	row.createCell(j++).setCellValue("已婚");
            }else{
            	row.createCell(j++).setCellValue("未婚");
            }
            row.createCell(j++).setCellValue(emp.getLoginName());//登录名
            
            String mainPost = "";
            String mainDept = "";
            for(Post post:emp.getPostList()){
            	if("1".equals(post.getPostGrades())){
            		mainDept=post.getDepartmentName();//主岗位对应的部门
            		mainPost = post.getPostName();//主岗位
            	}
            }
            row.createCell(j++).setCellValue(mainDept);//所在部门
            row.createCell(j++).setCellValue(emp.getDirectPersonName());//汇报人
            row.createCell(j++).setCellValue(emp.getDirectPersonLoginName());//汇报人登录名
            if("0".equals(emp.getEmployeeStatus())){//在职状态
            	row.createCell(j++).setCellValue("在职");
            }else{
            	row.createCell(j++).setCellValue("离职");
            }
            row.createCell(j++).setCellValue(emp.getEntryTime());//入职时间
            row.createCell(j++).setCellValue(emp.getProbationaryExpired());//转正时间
            row.createCell(j++).setCellValue(mainPost);//主岗位
            for(Post post:emp.getPostList()){
            	if("0".equals(post.getPostGrades())){
            		row.createCell(j++).setCellValue(post.getDepartmentName());//副部门
            		row.createCell(j++).setCellValue(post.getPostName());//副岗位
            	}
            }
            if(emp.getPostList().size()==1){
            	j=j+4;
            }else if(emp.getPostList().size()==2){
            	j=j+2;
            }
            row.createCell(j++).setCellValue(emp.getEmployeePhone());//联系方式1
            row.createCell(j++).setCellValue(emp.getEmployeeTwophone());//联系方式2
            row.createCell(j++).setCellValue(emp.getSeniority());//工龄
        }  
        try {  
            workbook.write(out);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
		
	}

	@Override
	public int selectEmployeeCountByCompanyId(String companyId) {
		 
		return employeeDao.selectEmployeeCountByCompanyId(companyId);
	}

	
}
