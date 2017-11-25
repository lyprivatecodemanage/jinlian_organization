package com.xiangshangban.organization.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangshangban.organization.bean.CheckPerson;
import com.xiangshangban.organization.bean.Company;
import com.xiangshangban.organization.bean.ConnectEmpPost;
import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.bean.Transferjob;
import com.xiangshangban.organization.bean.UserCompanyDefault;
import com.xiangshangban.organization.bean.Uusers;
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
		i=i+userCompanyDefaultDao.deleteUserFromCompany(companyId,employeeId);
		i=i+employeeDao.deleteByEmployee(employeeId,companyId);
		if(i>1){
			employee.setCompanyId(companyId);
			employee.setEmployeeStatus("1");
			this.updateDeviceEmp(employee);
		}
		
		return i;
	}

	@Override
	public int insertEmployee(Employee employee) {
		Uusers user = usersDao.selectByPhone(employee.getLoginName());
		if(user==null || StringUtils.isEmpty(user.getUserid())){//未注册，写入注册表	
			user = new Uusers();
			String employeeId = FormatUtil.createUuid();			
			employee.setEmployeeId(employeeId);	
			employee.setEmployeeStatus("0");
			employeeDao.insertEmployee(employee);
			
			user.setUserid(employeeId);
			user.setUsername(employee.getEmployeeName());
			user.setPhone(employee.getLoginName());
			user.setStatus("1");
			usersDao.insertSelective(user);//加入注册表
			
			UserCompanyDefault userCompany = new UserCompanyDefault();
			userCompany.setCompanyId(employee.getCompanyId());
			userCompany.setCurrentOption("2");
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
				return 0;
			}
			//添加绑定关系
			UserCompanyDefault userCompany = userCompanyDefaultDao.selectByUserIdAndCompanyId(user.getUserid(), employee.getCompanyId());
			if(userCompany==null || StringUtils.isEmpty(userCompany.getCompanyId())){//不存在绑定关系
				userCompany = new UserCompanyDefault();
				userCompany.setCompanyId(employee.getCompanyId());
				userCompany.setCurrentOption("2");
				userCompany.setUserId(user.getUserid());
				userCompany.setInfoStatus("1");
				userCompanyDefaultDao.insertSelective(userCompany);//添加用户公司的绑定关系
				employeeDao.insertEmployee(employee);//插入人员表
			}else if(userCompany!=null && userCompany.getCompanyId().equals(employee.getCompanyId())){//已存在绑定关系，则直接返回
				if("2".equals(userCompany.getIsActive())){
					userCompany.setIsActive("0");
					userCompany.setInfoStatus("1");
					userCompanyDefaultDao.updateSelective(userCompany);
				}
				this.updateTransfer(employee);//岗位信息设置
				return 1;
			}
		}
	    
	    this.updateTransfer(employee);//岗位信息设置
	    
	    updateDeviceEmp(employee);
		return 1;
	}
	/**
	 * 告知设备模块更新人员信息
	 * @param employee
	 */
	public void updateDeviceEmp(Employee employee) {
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

	public void updateTransfer(Employee employee) {
		//把员工关联的岗位添加到connect_emp_post_中间表里面
		for(Post post:employee.getPostList()){
			String postId = post.getPostId();
			if(StringUtils.isNotEmpty(postId)){
				ConnectEmpPost empPost = connectEmpPostDao.findByConnect(employee.getEmployeeId(), 
						employee.getDepartmentId(), post.getPostGrades());
				if(empPost==null || StringUtils.isEmpty(empPost.getPostId())){//不存在，则添加
					empPost =new ConnectEmpPost();
					empPost.setEmployeeId(employee.getEmployeeId());
					empPost.setDepartmentId(employee.getDepartmentId());
					empPost.setPostGrades(post.getPostGrades());
					empPost.setPostId(postId);
					empPost.setIsDelete("0");
					empPost.setEmployeeId(employee.getEmployeeId());
					empPost.setCompanyId(employee.getCompanyId());
					connectEmpPostDao.saveConnect(empPost);	
					if("1".equals(post.getPostGrades())){//主岗位添加调动记录
						Transferjob transferjob = new Transferjob();
					    transferjob.setTransferJobId(FormatUtil.createUuid());
					    transferjob.setEmployeeId(employee.getEmployeeId());
					    transferjob.setDepartmentId(employee.getDepartmentId());
					    transferjob.setTransferBeginTime(employee.getEntryTime());
					    transferjob.setTransferJobCause("入职");		
					    transferjob.setUserId(employee.getOperateUserId());//操作人ID	
					    transferjob.setCompanyId(employee.getCompanyId());
					    transferjob.setPostId(postId);
					    transferjobDao.insertTransferjob(transferjob);
					}
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
		// TODO Auto-generated method stub
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
	public int updateEmployeeInfoStatus(String companyId, String userId) {
		int result = employeeDao.updateEmployeeInfoStatus(companyId, userId);
		Employee employee = employeeDao.selectByEmployee(userId, companyId);
		this.updateDeviceEmp(employee);
		return result;
	}

	@Override
	public int activeEmp(String companyId, String employeeId) {
		return userCompanyDefaultDao.updateActive(companyId, employeeId);
	}


	
}
