package com.xiangshangban.organization.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xiangshangban.organization.bean.CheckPerson;
@Mapper
public interface CheckPersonDao {
    /**
     * 修改状态
     * @param companyId
     * @param userId
     * @param status 0：待审核 ， 1：审核通过， 2：审核不通过
     * @return
     */
    int updateByPrimaryKeySelective(@Param("companyId") String companyId, 
    		@Param("userid")String userid, @Param("status")String status);
    /**
     * 查询
     * @param companyId
     * @param userId
     * @return
     */
    CheckPerson selectByPrimaryKey(@Param("companyId") String companyId, 
    		@Param("userid")String userid);
}