package com.xiangshangban.organization.service;

import com.xiangshangban.organization.bean.Transferjob;

public interface TransferjobService {
	String deleteByTransferjob(String transferJobId);
	   
	String insertTransferjob(Transferjob transferjob);

    Transferjob selectByTransferjob(String transferJobId,String companyId);

    String updateByTransferjob(Transferjob transferjob);
    
    Transferjob selectByTransferjobpost (String employeeId,String companyId);
    
    String updateByTrandepartmentId(String departmentId);
    
    String  updateBytransferendtime(String employeeId,String transferEndTime);
}
