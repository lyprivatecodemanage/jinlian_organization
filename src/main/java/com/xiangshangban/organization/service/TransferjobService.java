package com.xiangshangban.organization.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

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
    /**
     * @author 李业
     * 功能:删除员工信息是添加员工离岗时间
     * @param comapanyId
     * @param employeeId
     * @param departmentId
     * @return
     */
    int updateTransferEndTimeWhereDeleteEmployee(String companyId,String employeeId,String departmentId,String postId);
}
