package com.xiangshangban.organization.service;

import java.util.Map;

import com.xiangshangban.organization.bean.Transferjob;

public interface TransferjobService {
	String deleteByTransferjob(String transferJobId);
	   
	String insertTransferjob(Transferjob transferjob);

    Transferjob selectByTransferjob(String transferJobId,String companyId);

    String updateByTransferjob(Transferjob transferjob);
    
    Transferjob selectByTransferjobpost (String employeeId,String companyId);
    
    String updateByTrandepartmentId(String departmentId);
    
    String  updateBytransferendtime(String employeeId,String transferEndTime);
    
    //根据员工ID，在职时间查询员工信息
	Transferjob findByempinfo(Map<String, String> map);
	//根据员工ID,当结束时间为null查询员工信息
    Transferjob findByempNullinfo(Map<String,String> map);
}
