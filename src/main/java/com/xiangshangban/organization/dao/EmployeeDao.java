package com.xiangshangban.organization.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.Employee;

@Mapper
public interface EmployeeDao {
	
	int deleteByEmployee(String employeeId);	   
	int insertEmployee(Employee employee);
	int insertEmployeeuser(Employee employee);
	//查询单条员信息
    Employee selectByEmployee(@Param("employeeId") String employeeId,@Param("companyId") String companyId);               
   
    int updateByEmployee(Employee employee);
    int updateByEmployeeapprove(Employee employee);
    List<Employee> findByAllEmployee(String companyId);
    
    List<Employee> findByLiZhiemployee(String companyId);
    
    List<Employee> findBydeptemployee(@Param("departmentId") String departmentId,@Param("companyId") String companyId);
    
    List<Employee> findByMoreEmployee(Map<String,String> map);
    
    int batchUpdateTest(String employeeId);
    
    int batchUpdateStatus(String employeeId);
    Employee findByemploginName(String loginName);
    Employee findByemployeeNo(String employeeNo);
    int updateByEmployeedept(Employee employee);
    int batchUpdateTransferJobStaus(String employeeId);
    
    List<Employee> findByposcounttemp(@Param("postId") String postId,@Param("companyId") String companyId);
}
