package com.xiangshangban.organization.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.ConnectEmpPost;

public interface ConnectEmpPostService {
	String saveConnect(ConnectEmpPost connect);	
	String deleteConnect(String employeeId,String postId);	
	String updateConnect(ConnectEmpPost connect);
	//String updateConnectpostStaus(ConnectEmpPost connect);
	ConnectEmpPost findByConnectpostemp(String employeeId,String postId);
	String updateConnectDelehipostStaus(String employeeId);
	String updatetpostGradespostStaus(ConnectEmpPost connect);
	ConnectEmpPost findByConnect(String employeeId,String departmentId,String postGrades);
	/**
	 * 查询岗位下的人员
	 * @param companyId
	 * @param postId
	 * @return
	 */
	List<ConnectEmpPost> findEmpByPostId(String companyId, String postId);
	/**
	 * @author 李业:编辑个人信息(副岗位)
	 * 删除副岗位信息
	 * @param employeeId
	 * @param departmentId
	 * @return
	 */
	int deleteEmployeeWithPost(String employeeId,String departmentId);
	/**
	 * @author 李业:编辑个人信息(副岗位)
	 * 添加副岗位信息
	 * @param list
	 * @return
	 */
	int insertEmployeeWithPost(List<ConnectEmpPost> list);
	/**
	 * @author 李业:编辑个人信息(主岗位)
	 * 更新主岗位信息
	 * @param employeeId
	 * @param departmentId
	 * @param postId
	 * @return
	 */
	int updateEmployeeWithPost(String employeeId,String departmentId,String postId);
	/**
	 * @author 李业:编辑个人信息(主岗位)
	 * 查询主岗位信息
	 * @param employeeId
	 * @param companyId
	 * @return
	 */

	ConnectEmpPost selectEmployeePostInformation(String employeeId, String companyId);

	/**
	 * 删除主岗位记录
	 * @param employeeId
	 * @param departmentId
	 * @return
	 */
	int deleteEmployeeFromPost(String employeeId,String departmentId);
}
