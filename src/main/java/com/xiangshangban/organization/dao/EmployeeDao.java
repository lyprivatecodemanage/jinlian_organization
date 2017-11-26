package com.xiangshangban.organization.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.Employee;

@Mapper
public interface EmployeeDao {

	int deleteByEmployee(@Param("employeeId") String employeeId, @Param("companyId") String companyId);

	int insertEmployee(Employee employee);

	// 查询单条员信息
	Employee selectByEmployee(@Param("employeeId") String employeeId, @Param("companyId") String companyId);

	int updateByEmployee(Employee employee);

	int updateByEmployeeapprove(Employee employee);

	List<Employee> findByAllEmployee(Map<String, String> map);

	List<Employee> findByLiZhiemployee(String companyId);

	List<Employee> findBydeptemployee(@Param("departmentId") String departmentId, @Param("companyId") String companyId);

	List<Employee> findByMoreEmployee(Map<String, String> map);

	// 分页查询员工信息
	List<Employee> selectByAllFnyeEmployee(@Param("companyId")String companyId,@Param("numPage") String numPage,
			@Param("numRecordCount") String numRecordCount, @Param("employeeName") String employeeName,
			@Param("employeeSex") String employeeSex, @Param("departmentName") String departmentName,
			@Param("postName") String postName, @Param("employeeStatus") String employeeStatus,@Param("departmentId")String departmentId);
	
	int selectCountEmployeeFromCompany(@Param("companyId")String companyId,@Param("numPage") String numPage,
			@Param("numRecordCount") String numRecordCount, @Param("employeeName") String employeeName,
			@Param("employeeSex") String employeeSex, @Param("departmentName") String departmentName,
			@Param("postName") String postName, @Param("employeeStatus") String employeeStatus,
			@Param("departmentId")String departmentId);

	int batchUpdateTest(String employeeId);

	int batchUpdateStatus(String employeeId);

	Employee findByemploginName(String loginName);

	Employee findByemployeeNo(@Param("employeeNo") String employeeNo, @Param("companyId") String companyId);

	int updateByEmployeedept(Employee employee);

	int batchUpdateTransferJobStaus(String employeeId);

	List<Employee> findByposcounttemp(@Param("postId") String postId, @Param("companyId") String companyId);

	List<Employee> findByruzhiempinfo(String companyId);

	List<Employee> selectByAllEmployee(String companyId);

	// 根据人员姓名，所属部门，主岗位动态查询所有在职人员以及所属部门和主岗位
	List<Employee> findBydynamicempadmin(Map<String, String> map);

	// 查询所有在职人员以及所属部门和主岗位
	List<Employee> findByempadmin(Map<String, String> map);

	List<Employee> findByempadmins(Map<String, String> map);

	/**
	 * 根据登录名查询是否用户Id
	 * 
	 * @param loginName
	 *            登录名
	 * @return
	 */
	String getUserIdByLoginName(String loginName);

	/**
	 * 添加用户信息
	 * 
	 * @param employee
	 * @return
	 */
	int insertUser(Employee employee);

	List<Employee> findEmployeeByDepartmentId(@Param("companyId") String companyId,
			@Param("departmentId") String departmentId);
	
	Employee selectByEmployeeFromApp(@Param("companyId")String companyId,@Param("userId")String userId);
	/**
	 * @author 李业/编辑个人信息
	 * @param params
	 * @return
	 */
	int updateEmployeeInformation(Employee emp);
	
	int updateEmployeeInfoStatus(@Param("companyId")String companyId,@Param("userId")String userId);

	int isAdmin(@Param("companyId")String companyId,@Param("userId")String userId);
}
