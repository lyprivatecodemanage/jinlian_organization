package com.xiangshangban.organization.service;

import java.util.List;
import java.util.Map;


import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.ReturnData;

public interface EmployeeService {
	String deleteByEmployee(String employeeId);
	/**
	 * 添加员工。用户未注册时，加入注册操作；用户已注册，则做绑定操作。
	 * @param employee
	 * @return
	 */
	int insertEmployee(Employee employee);
   //查询单条员信息
    Employee selectByEmployee(String employeeId,String companyId);  
    String updateByEmployee(Employee employee);
    //查询在职员工信息
    List<Employee> findByAllEmployee(Map<String,String> map);
    //查询离职员工信息
    List<Employee> findByLiZhiemployee(String companyId);   
    List<Employee> findByMoreEmployee(Map<String,String> map);
    //批量删除
    String batchUpdateTest(String employeeId);
    //离职
    String batchUpdateStatus(String employeeId);
    //调职
    String batchUpdateTransferJobStaus(String employeeId);
    //分页查询员工信息
    List<Employee> selectByAllFnyeEmployee(Map<String,String> map);
    
    String updateByEmployeedept(Employee employee);
    Employee findByemploginName(String loginName);
    
    List<Employee> findByposcounttemp(String postId,String companyId);
	String updateByEmployeeapprove(Employee employee);
	List<Employee>findByruzhiempinfo(String companyId);
	List<Employee> selectByAllEmployee(String companyId);
	//根据人员姓名，所属部门，主岗位动态查询所有在职人员以及所属部门和主岗位 
    List<Employee> findBydynamicempadmin(Map<String,String> map);
    //查询所有在职人员以及所属部门和主岗位
    List<Employee> findByempadmin(Map<String,String> map);
    List<Employee> findByempadmins(Map<String,String> map);
    /**
     * 根据工号查询人员信息
     * @param employeeNo
     * @param companyId
     * @return
     */
	Employee findByemployeeNo(String employeeNo, String companyId);
	/**
	 * 查询部门下在职人员
	 * @param companyId 公司ID
	 * @param departmentId 部门ID
	 * @return
	 */
	List<Employee> findEmployeeByDepartmentId(String companyId, String departmentId);
}
