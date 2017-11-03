package com.xiangshangban.organization.service;

import java.util.List;
import java.util.Map;


import com.xiangshangban.organization.bean.Employee;

public interface EmployeeService {
	String deleteByEmployee(String employeeId);
	//用户注册调用方法
	String insertEmployeeuser(Employee employee);  
	//用户未注册调用方法
	String insertEmployee(Employee employee);
   //查询单条员信息
    Employee selectByEmployee(String employeeId,String companyId);  
    String updateByEmployee(Employee employee);
    //查询在职员工信息
    List<Employee> findByAllEmployee(String companyId);
    //查询离职员工信息
    List<Employee> findByLiZhiemployee(String companyId);   
    List<Employee> findByMoreEmployee(Map<String,String> map);
    //批量删除
    String batchUpdateTest(String employeeId);
    //离职
    String batchUpdateStatus(String employeeId);
    //调职
    String batchUpdateTransferJobStaus(String employeeId);
    String updateByEmployeedept(Employee employee);
    Employee findByemployeeNo(String employeeNo);
    Employee findByemploginName(String loginName);
    
    List<Employee> findByposcounttemp(String postId,String companyId);
	String updateByEmployeeapprove(Employee employee);
	List<Employee>findByruzhiempinfo();
    
}
