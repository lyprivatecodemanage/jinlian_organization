package com.xiangshangban.organization.service;


import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.dao.ConnectEmpPostDao;
import com.xiangshangban.organization.dao.EmployeeDao;
import com.xiangshangban.organization.util.FormatUtil;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	EmployeeDao employeeDao;
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
	public String insertEmployee(Employee employee) {			
		String i ="0";
		try {
			String employeeId = FormatUtil.createUuid();			
			employee.setEmployeeId(employeeId);						
			employeeDao.insertEmployee(employee);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
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
	public List<Employee> findByAllEmployee(String companyId) {
		// TODO Auto-generated method stub
		return employeeDao.findByAllEmployee(companyId);
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
	public Employee findByemployeeNo(String employeeNo) {
		// TODO Auto-generated method stub
		return employeeDao.findByemployeeNo(employeeNo);
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
	public String insertEmployeeuser(Employee employee) {
		String i ="0";
		try {								
			employeeDao.insertEmployeeuser(employee);
			i="1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
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
	public List<Employee> findByruzhiempinfo() {
		// TODO Auto-generated method stub
		return employeeDao.findByruzhiempinfo();
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

	
}
