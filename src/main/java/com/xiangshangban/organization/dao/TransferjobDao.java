package com.xiangshangban.organization.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.Transferjob;

@Mapper
public interface TransferjobDao {
   
    int deleteByTransferjob(String transferJobId);
   
    int insertTransferjob(Transferjob transferjob);

    Transferjob selectByTransferjob(@Param("transferJobId") String transferJobId,@Param("companyId") String companyId);

    int updateByTransferjob(Transferjob transferjob);
    
    int updateByTrandepartmentId(String departmentId);
    
    int  updateBytransferendtime(@Param("employeeId") String employeeId,@Param("transferEndTime") String transferEndTime );
    
    Transferjob selectByTransferjobpost (@Param("employeeId") String employeeId,@Param("companyId")String companyId);
    //根据员工ID，在职时间查询员工信息
    Transferjob findByempinfo(Map<String,String> map);
    //根据员工ID,当结束时间为null查询员工信息
    Transferjob findByempNullinfo(Map<String,String> map);
}