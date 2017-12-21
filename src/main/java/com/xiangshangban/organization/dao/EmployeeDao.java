package com.xiangshangban.organization.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.UusersRoles;

@Mapper
public interface EmployeeDao {

	Employee selectEmployeeByCompanyIdAndEmployeeId(@Param("employeeId")String employeeId,@Param("companyId")String companyId);
	
	int deleteByEmployee(@Param("employeeId") String employeeId, @Param("companyId") String companyId);

	int insertEmployee(Employee employee);
	/**
	 * @author 李业
	 * 通过公司id查询公司所有的在职员工
	 * @param companyId
	 * @return
	 */
	List<Employee> selectAllEmployeeByCompanyId(String companyId);
	
	// 查询单条员信息
	Employee selectByEmployee(@Param("employeeId") String employeeId, @Param("companyId") String companyId);

	int updateByEmployee(Employee employee);

	int updateByEmployeeapprove(Employee employee);

	List<Employee> findByAllEmployee(Map<String, String> map);
	
	List<Employee> findAllEmployeeByCompanyId(String companyId);

	List<Employee> findByLiZhiemployee(String companyId);

	List<Employee> findBydeptemployee(@Param("departmentId") String departmentId, @Param("companyId") String companyId);

	List<Employee> findByMoreEmployee(Map<String, String> map);

	// 分页查询员工信息
	List<Employee> selectByAllFnyeEmployee(@Param("companyId")String companyId,@Param("numPage") String numPage,
			@Param("numRecordCount") String numRecordCount, @Param("employeeName") String employeeName,
			@Param("employeeSex") String employeeSex, @Param("departmentName") String departmentName,
			@Param("postName") String postName, @Param("employeeStatus") String employeeStatus,@Param("departmentId")String departmentId);
	
	int selectCountEmployeeFromCompany(@Param("companyId")String companyId,/*@Param("numPage") String numPage,
			@Param("numRecordCount") String numRecordCount,*/ @Param("employeeName") String employeeName,
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

	int resetEmployeeStatus(@Param("companyId")String companyId, @Param("employeeId")String employeeId);
	
	Employee selectEmployeeByLoginNameAndCompanyId(@Param("loginName")String loginName,@Param("companyId")String companyId);
	
	/**
	 * 更新手机端用户上传的头像key
	 * @param employeeId
	 * @param companyId
	 * @return
	 */
	int updateEmployeeImgUrl(@Param("employeeId")String employeeId,@Param("companyId")String companyId,@Param("employeeImgUrl")String employeeImgUrl);
	/**
	 * 查询导出人员
	 * @param companyId 
	 * @return
	 */
	List<Employee> findExport(@Param("companyId")String companyId);

	int selectEmployeeCountByCompanyId(@Param("companyId")String companyId);
	
	int updateLoginNameByEmployeeId(@Param("loginName")String loginName,@Param("employeeId")String employeeId);
	
	List<UusersRoles> selectRoleIdByEmployeeId(@Param("userId")String userId);
	
	Employee selectDirectPersonLoginName(@Param("employeeId")String employeeId,@Param("companyId")String companyId);
	/**
	 * @author 李业   查询公司首页管理员展示信息
	 * @param employeeId
	 * @param companyId
	 * @return
	 */
	Employee selectAdminEmployeeDetails(@Param("employeeId")String employeeId,@Param("companyId")String companyId);
	/**
	 * @author 李业   修改管理员头像
	 * @param companyId
	 * @param employeeId
	 * @param employeeImgUrl
	 * @return
	 */
	int updateAdminEmployeeImgUrl(@Param("companyId")String companyId,@Param("employeeId")String employeeId,@Param("employeeImgUrl")String employeeImgUrl);
}
