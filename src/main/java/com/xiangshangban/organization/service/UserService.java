package com.xiangshangban.organization.service;

import com.xiangshangban.organization.bean.User;

public interface UserService {
	String saveUser(User user);
	
	User getOneUser(String userName);
}
