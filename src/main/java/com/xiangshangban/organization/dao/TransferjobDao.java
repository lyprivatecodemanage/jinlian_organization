package com.xiangshangban.organization.dao;

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
}