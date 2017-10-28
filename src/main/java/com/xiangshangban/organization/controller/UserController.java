package com.xiangshangban.organization.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.xiangshangban.organization.bean.User;
import com.xiangshangban.organization.service.UserService;

@RestController
@RequestMapping("/UserController")
public class UserController {
	@Autowired
	UserService userService;
	@RequestMapping(value="/saveUser", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public String saveUser(@RequestBody String user,HttpServletRequest request,HttpServletResponse response){
		System.out.println(user);
		User usertemp=JSON.parseObject(user,User.class);		
		String i=userService.saveUser(usertemp);		
		return "{\"message\":\""+i+"\"}";
	}
}
