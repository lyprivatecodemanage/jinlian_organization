package com.xiangshangban.organization.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.ConnectEmpPost;


@Mapper
public interface ConnectEmpPostDao {
	int saveConnect(ConnectEmpPost connect);	
	int deleteConnect(@Param("employeeId") String employeeId,@Param("postId") String postId);		
	int updateConnect(ConnectEmpPost connect);
	List<ConnectEmpPost> findByConnect(Map<String,String> map);
	int updateConnectpostStaus(ConnectEmpPost connect);
	int updateConnectLizhipostStaus(String employeeId);
	int updateConnectDelehipostStaus(String employeeId);
	
	List<ConnectEmpPost> findByConnectpostemp(String postId);
}
