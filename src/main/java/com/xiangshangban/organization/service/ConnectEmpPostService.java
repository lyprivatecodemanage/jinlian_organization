package com.xiangshangban.organization.service;

import java.util.List;

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
}
