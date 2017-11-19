package com.xiangshangban.organization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangshangban.organization.dao.EmployeeDao;

@Service
public class CheckPersonServiceImpl implements CheckPersonService {
	@Autowired
	EmployeeDao employeeDao;
	@Override
	public Integer updateApplyStatus(String companyId, String userId, String status) {
		if("1".equals(status)){//同意
			//将用户信息加入到员工表
			
		}else{//驳回
			
		}
		return 1;
	}
   
}
