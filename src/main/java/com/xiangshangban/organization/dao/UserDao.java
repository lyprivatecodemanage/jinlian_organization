package com.xiangshangban.organization.dao;

import org.apache.ibatis.annotations.Mapper;

import com.xiangshangban.organization.bean.User;


@Mapper
public interface UserDao {
	int saveUser(User user);
	// 根据username获得一个User类 
    User getOneUser(String userName);
}
