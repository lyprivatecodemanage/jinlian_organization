package com.xiangshangban.organization.service;


import java.util.List;
import java.util.Map;

import com.xiangshangban.organization.bean.ConnectEmpPost;

public interface ConnectEmpPostService {
	String saveConnect(ConnectEmpPost connect);	
	String deleteConnect(String employeeId,String postId);	
	String updateConnect(ConnectEmpPost connect);
	List<ConnectEmpPost> findByConnect(Map<String,String> map);
	String updateConnectpostStaus(ConnectEmpPost connect);
	List<ConnectEmpPost> findByConnectpostemp(String postId);
	String updateConnectLizhipostStaus(String employeeId);
	String updateConnectDelehipostStaus(String employeeId);
}
