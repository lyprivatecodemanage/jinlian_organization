package com.xiangshangban.organization.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.service.CheckPersonService;
import com.xiangshangban.organization.service.ConnectEmpPostService;
import com.xiangshangban.organization.service.PostService;

@RestController
@RequestMapping("/CheckPersonController")
public class CheckPersonController {
	@Autowired
	CheckPersonService checkPersonService;
	/**
	 * 加入公司企业管理员审核通过
	 * @param post
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/process", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData insertPost(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){			
		ReturnData returnData = new ReturnData();
		//获取请求头信息			
		String companyId = request.getHeader("companyId");
		JSONObject obj = JSON.parseObject(jsonString);
		String userId = obj.getString("userId");
		String status = obj.getString("status");
		if(StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(status) && ("0".equals(status)||"1".equals(status))){
			if(!("0".equals(status)||"1".equals(status))){
				returnData.setMessage("参数格式错误");
				returnData.setReturnCode("3007");
				return returnData;
			}
			checkPersonService.updateApplyStatus(companyId, userId, status);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
		}else{
			returnData.setMessage("必传参数为空");
			returnData.setReturnCode("3006");
		}
		return returnData;
	}
}
