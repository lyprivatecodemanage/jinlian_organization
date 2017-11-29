package com.xiangshangban.organization.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.ReturnData;

public interface EmployeeService {
	int deleteByEmployee(String employeeId,String companyId);
	/**
	 * 添加员工。用户未注册时，加入注册操作；用户已注册，则做绑定操作。
	 * @param employee
	 * @return
	 */
	ReturnData insertEmployee(Employee employee);
   //查询单条员信息
    Employee selectByEmployee(String employeeId,String companyId);  
    String updateByEmployee(Employee employee);
    //查询在职员工信息
    List<Employee> findByAllEmployee(Map<String,String> map);
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
    /**
     * @author 李业:分页条件查询员工信息
     * @param companyId
     * @param numPage
     * @param numRecordCount
     * @param employeeName
     * @param employeeSex
     * @param departmentName
     * @param postName
     * @param employeeStatus
     * @return
     */
    List<Employee> selectByAllFnyeEmployee(String companyId,String numPage,String numRecordCount, String employeeName, String employeeSex, String departmentName,String postName,String employeeStatus,String departmentId);
    /**
     * 分页条件查询总记录数
     * @param companyId
     * @param numPage
     * @param numRecordCount
     * @param employeeName
     * @param employeeSex
     * @param departmentName
     * @param postName
     * @param employeeStatus
     * @return
     */
    int selectCountEmployeeFromCompany(String companyId,String numPage,String numRecordCount, String employeeName, String employeeSex, String departmentName,String postName,String employeeStatus,String departmentId);
    
    String updateByEmployeedept(Employee employee);
    Employee findByemploginName(String loginName);
    
    List<Employee> findByposcounttemp(String postId,String companyId);
	String updateByEmployeeapprove(Employee employee);
	List<Employee>findByruzhiempinfo(String companyId);
	List<Employee> selectByAllEmployee(String companyId);
	
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
	/**
	 * 删除user_company表关系
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	int deleteUserFromCompany(String companyId,String employeeId);
	/**
	 *@author 李业/app查询个人信息
	 * @param companyId
	 * @param userId
	 * @return
	 */
	Employee selectByEmployeeFromApp(String companyId,String userId);
	
	/**
	 * @author 李业/编辑个人信息
	 * @param params
	 * @return
	 */
	int updateEmployeeInformation(Employee emp);
	
	int updateEmployeeInfoStatus(String companyId,String userId);
	/**
	 * 激活
	 * @param employeeId
	 * @return
	 */
	int activeEmp(String companyId, String employeeId);
	/**
	 * 查询员工是否为该公司的管理员
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	int isAdmin(String companyId, String employeeId);
	/**
	 * 恢复在职状态
	 * @param companyId
	 * @param employeeId
	 * @return
	 */
	int resetEmployeeStatus(String companyId, String employeeId);
	/**
	 * 根据登录名和公司id查询员工
	 * @param loginName
	 * @param companyId
	 * @return
	 */
	Employee selectEmployeeByLoginNameAndCompanyId(String loginName,String companyId);
	
	/**
	 * 保存员工头像key
	 * @param employeeId
	 * @param companyId
	 * @param key
	 * @return
	 */
	int updateEmployeeImgUrl(String employeeId, String companyId, String key);
}
