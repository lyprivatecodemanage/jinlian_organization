package com.xiangshangban.organization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiangshangban.organization.bean.User;
import com.xiangshangban.organization.dao.UserDao;
import com.xiangshangban.organization.util.FormatUtil;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
	
	@Override
	public String saveUser(User user) {
		String i="0";
		try {			
			userDao.saveUser(user);
			i="1";
		} catch (Exception e) {
			// TODO: handle exception
		}
		// TODO Auto-generated method stub
		return i;
	}

	@Override
	public User getOneUser(String userName) {
		// TODO Auto-generated method stub
		return userDao.getOneUser(userName);
	}

}
