package com.xiangshangban.organization.service;

import java.util.List;

import com.xiangshangban.organization.bean.Duty;

public interface DutyService {
	
	String deleteByDuty(String dutyId);
	   
	String insertDuty(Duty duty);

    Duty selectByDuty(String dutyId);
  
    String updateByDuty(Duty duty);
    
    List<Duty> findByAllDuty(String postId);
}
