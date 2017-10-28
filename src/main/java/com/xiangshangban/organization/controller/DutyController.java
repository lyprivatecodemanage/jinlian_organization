package com.xiangshangban.organization.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.xiangshangban.organization.bean.Duty;
import com.xiangshangban.organization.service.DutyService;

@RestController
@RequestMapping("/DutyController")
public class DutyController {
	@Autowired
	DutyService dutyService;
	
	
	/**
	 * 添加职责
	 * @param duty
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/insertDuty", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public String insertDuty(@RequestBody String duty,HttpServletRequest request,HttpServletResponse response){
		System.out.println(duty);
		Duty dutytemp=JSON.parseObject(duty,Duty.class);
		String i=dutyService.insertDuty(dutytemp);		
		return "{\"message\":\""+i+"\"}";
	}
	
	/**
	 * 修改职责
	 * @param duty
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/updateByDuty", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public String updateByDuty(@RequestBody String duty,HttpServletRequest request,HttpServletResponse response){
		System.out.println(duty);
		Duty dutytemp=JSON.parseObject(duty,Duty.class);
		String i=dutyService.updateByDuty(dutytemp);	
		return "{\"message\":\""+i+"\"}";
	}
	
	/**
	 * 删除职责
	 * @param dutyId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/deleteByDuty", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public String deleteByDuty(@RequestBody String dutyId,HttpServletRequest request,HttpServletResponse response){
		System.out.println(dutyId);
		String i=dutyService.deleteByDuty(dutyId);		
		return "{\"message\":\""+i+"\"}";
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/findByAllDuty", produces = "application/json;charset=UTF-8", method=RequestMethod.GET)
	public String findByAllDuty(@RequestBody String postId,HttpServletRequest request,HttpServletResponse response){
		List<Duty> list=dutyService.findByAllDuty(postId);	
		return JSON.toJSONString(list);
	}

}
