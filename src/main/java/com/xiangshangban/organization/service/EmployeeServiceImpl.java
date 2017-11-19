package com.xiangshangban.organization.service;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiangshangban.organization.bean.ConnectEmpPost;
import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.bean.Transferjob;
import com.xiangshangban.organization.bean.UserCompanyDefault;
import com.xiangshangban.organization.bean.Uusers;
import com.xiangshangban.organization.dao.ConnectEmpPostDao;
import com.xiangshangban.organization.dao.EmployeeDao;
import com.xiangshangban.organization.dao.TransferjobDao;
import com.xiangshangban.organization.dao.UserCompanyDefaultDao;
import com.xiangshangban.organization.dao.UusersDao;
import com.xiangshangban.organization.util.FormatUtil;

@Service
public class EmployeeServiceImpl implements EmployeeService {
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
	
	
	@Override
	public String deleteByEmployee(String connectEmpPostId) {
		String i ="0";
		try {
			employeeDao.deleteByEmployee(connectEmpPostId);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
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
			userCompanyDefaultDao.insert(userCompany);//添加用户公司的绑定关系
		}else{//添加绑定关系
			UserCompanyDefault userCompany = userCompanyDefaultDao.selectByUserIdAndCompanyId(user.getUserid(), employee.getCompanyId());
			if(userCompany==null || StringUtils.isEmpty(userCompany.getCompanyId())){//不存在绑定关系
				userCompany.setCompanyId(employee.getCompanyId());
				userCompany.setCurrentOption("2");
				userCompany.setUserId(user.getUserid());
				userCompanyDefaultDao.insert(userCompany);//添加用户公司的绑定关系
			}else if(userCompany!=null && userCompany.getCompanyId().equals(employee.getCompanyId())){//已存在绑定关系，则直接返回
				return 0;
			}
			employee.setEmployeeId(user.getUserid());						
			employeeDao.insertEmployee(employee);//插入人员表
		}
	    Transferjob transferjob = new Transferjob();
	    transferjob.setTransferJobId(FormatUtil.createUuid());
	    transferjob.setEmployeeId(employee.getEmployeeId());
	    transferjob.setDepartmentId(employee.getDepartmentId());
	    transferjob.setTransferBeginTime(employee.getEntryTime());
	    transferjob.setTransferJobCause("入职");		
	    transferjob.setUserId(employee.getOperateUserId());//操作人ID	
	    transferjob.setCompanyId(employee.getCompanyId());
	    transferjobDao.insertTransferjob(transferjob);
	    //把员工关联的岗位添加到connect_emp_post_中间表里面
		for(Post post:employee.getPostList()){
			String postId = post.getPostId();
			ConnectEmpPost empPost = new ConnectEmpPost();
			empPost.setEmployeeId(employee.getEmployeeId());
			empPost.setDepartmentId(employee.getDepartmentId());
			empPost.setPostGrades(post.getPostGrades());
			empPost.setPostId(postId);				          
			connectEmpPostDao.saveConnect(empPost);				 				
		}	
		return 1;
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
		// TODO Auto-generated method stub
		return employeeDao.findByAllEmployee(map);
	}

	@Override
	public List<Employee> findByMoreEmployee(Map<String, String> map) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
	public List<Employee> selectByAllFnyeEmployee(Map<String, String> map) {
		// TODO Auto-generated method stub
		return employeeDao.selectByAllFnyeEmployee(map);
	}

	@Override
	public List<Employee> findBydynamicempadmin(Map<String, String> map) {
		// TODO Auto-generated method stub
		return employeeDao.findBydynamicempadmin(map);
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

	
}
