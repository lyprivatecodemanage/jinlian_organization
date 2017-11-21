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
import com.xiangshangban.organization.bean.CheckPerson;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.service.CheckPersonService;

@RestController
@RequestMapping("/CheckPersonController")
public class CheckPersonController {
	@Autowired
	CheckPersonService checkPersonService;
	/**
	 * 加入公司企业管理员审核
	 * @param userid
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
		if(StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(status)){
			if(!"1".equals(status)||!"2".equals(status)){
				returnData.setMessage("参数格式错误");
				returnData.setReturnCode("3007");
				return returnData;
			}
			returnData = checkPersonService.updateApplyStatus(companyId, userId, status);
		}else{
			returnData.setMessage("必传参数为空");
			returnData.setReturnCode("3006");
		}
		return returnData;
	}
	
	/**
	 * 加入公司企业管理员审核
	 * @param userid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getCheckList", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData getCheckList(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){			
		ReturnData returnData = new ReturnData();
		//获取请求头信息			
		String companyId = request.getHeader("companyId");
		Map<String,String> params = new HashMap<String, String>();
		JSONObject obj = JSON.parseObject(jsonString);	
		String pageNum = obj.getString("pageNum");//页码
		String pageRecordNum = obj.getString("pageRecordNum");//每页记录数	
		if(StringUtils.isNotEmpty(pageNum) && StringUtils.isNotEmpty(pageRecordNum)){
			String pageNumPattern = "\\d{1,}";
			boolean pageNumFlag = Pattern.matches(pageNumPattern, pageNum);
			boolean pageRecordNumFlag = Pattern.matches(pageNumPattern, pageRecordNum);
			if(!pageNumFlag||!pageRecordNumFlag){
				returnData.setMessage("参数格式不正确");
				returnData.setReturnCode("3007");			
				return returnData;
			}
		}else{//默认设置
			pageNum = "1";
			pageRecordNum="10";
		}
		
		String strNum = (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageRecordNum)+"";
		params.put("pageRecordNum", pageRecordNum);
		params.put("fromPageNum", strNum);
		params.put("companyId", companyId);
		List<CheckPerson> checkList =checkPersonService.getcheckListByPage(params);
		int totalPages = checkPersonService.getcheckListByPageAllLength(params);//数据总条数
		//总页数
		int pageCountnum =totalPages%Integer.parseInt(pageRecordNum)==0?
				(totalPages/Integer.parseInt(pageRecordNum)):(totalPages/Integer.parseInt(pageRecordNum)+1);	
		returnData.setTotalPages(totalPages);
		returnData.setPagecountNum(pageCountnum);
		returnData.setData(checkList);
		returnData.setMessage("数据请求成功");
		returnData.setReturnCode("3000");
		return returnData;
	}
}
