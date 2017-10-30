package com.xiangshangban.organization.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.ConnectEmpPost;


@Mapper
public interface ConnectEmpPostDao {
	int saveConnect(ConnectEmpPost connect);	
	int deleteConnect(@Param("employeeId") String employeeId,@Param("postId") String postId);		
	int updateConnect(ConnectEmpPost connect);
	int updateConnectpostStaus(ConnectEmpPost connect);
	int updateConnectDelehipostStaus(String employeeId);	
	int updatetpostGradespostStaus(ConnectEmpPost connect);	
	ConnectEmpPost findByConnect(@Param("employeeId") String employeeId,@Param("departmentId") String departmentId,@Param("postGrades")String postGrades);	
	ConnectEmpPost findByConnectpostemp(@Param("employeeId") String employeeId,@Param("postId") String postId);
}
