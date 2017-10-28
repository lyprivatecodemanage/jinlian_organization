package com.xiangshangban.organization.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiangshangban.organization.bean.Company;
import com.xiangshangban.organization.service.CompanyService;

@RestController
@RequestMapping("/CompanyController")
public class CompanyController {
	@Autowired
	CompanyService companyService;

	
	/**
	 * 添加公司信息
	 * @param company
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/insertCompany", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public Map<String, Object> insertCompany(@RequestBody String company,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		Company companytemp=JSON.parseObject(company,Company.class);
		String CompanyPhone = companytemp.getCompanyPhone();
		boolean Companyphone = Pattern.matches("^[1][3,4,5,7,8][0-9]{9}$", CompanyPhone);
		if (!Companyphone) {
			result.put("errorCode", "3007");
			result.put("message", "参数格式不正确");
			return result;
		}			
			companyService.insertCompany(companytemp);					
			result.put("errorCode", "1");
			result.put("message", "成功");
		return result;
						
	}
	
	/**
	 * 编辑公司信息
	 * @param company
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/updateByCompany", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public String updateByCompany(@RequestBody String company,HttpServletRequest request,HttpServletResponse response){
		System.out.println(company);
		Company companytemp=JSON.parseObject(company,Company.class);
		String i=companyService.updateByCompany(companytemp);	
		return "{\"message\":\""+i+"\"}";
	}
	
	/**
	 * 删除公司信息
	 * @param companyId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/deleteByCompany", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public Map<String, Object> deleteByCompany(@RequestBody String companyId,HttpServletRequest request,HttpServletResponse response){
		System.out.println(companyId);
		
		Map<String, Object> map=new HashMap<String, Object>();
		JSONObject obj = JSON.parseObject(companyId);
		companyId=obj.getString("employeeId");	
		if(companyId.equals("")){
			map.put("message","删除失败");	
		}else{
			companyService.deleteByCompany(companyId);	
			map.put("message", "删除成功");					
		}	
		return map;
	}
	/**
	 * 查询所有公司信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/fingdByAllCompany", produces = "application/json;charset=UTF-8", method=RequestMethod.GET)
	public String fingdByAllCompany(HttpServletRequest request,HttpServletResponse response){
		List<Company> list =companyService.fingdByAllCompany();		
		return JSON.toJSONString(list);
	}
	
	/**
	 * 查询一个公司详细信息
	 * @param companyId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/selectByCompany", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public String selectByCompany(@RequestBody String companyId,HttpServletRequest request,HttpServletResponse response){
		Company company =companyService.selectByCompany(companyId);		
		return JSON.toJSONString(company);
	}
	
}
