package com.xiangshangban.organization.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiangshangban.organization.bean.Duty;


@Mapper
public interface DutyDao {
   
    int deleteByDuty(String dutyId);
   
    int insertDuty(Duty duty);

    Duty selectByDuty(String dutyId);
  
    int updateByDuty(Duty duty);
    
    List<Duty> findByAllDuty(String postId);
}