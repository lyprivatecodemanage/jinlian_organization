package com.xiangshangban.organization.controller;

import java.util.List;
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
import com.xiangshangban.organization.bean.ReturnData;
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
	public ReturnData insertCompany(@RequestBody String company,HttpServletRequest request,HttpServletResponse response){
		Company companytemp=JSON.parseObject(company,Company.class);
		ReturnData returnData = new ReturnData();
		//获取请求头信息操作的用户
		String userName = request.getHeader("userName");
		companytemp.setUserName(userName);
		String CompanyPhone = companytemp.getCompanyPhone();
		boolean Companyphone = Pattern.matches("^[1][3,4,5,7,8][0-9]{9}$", CompanyPhone);
		if (!Companyphone) {
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");	
			return returnData;
		}			
			companyService.insertCompany(companytemp);					
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
		return returnData;
						
	}
	
	/**
	 * 编辑公司信息
	 * @param company
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/updateByCompany", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData updateByCompany(@RequestBody String company,HttpServletRequest request,HttpServletResponse response){
		Company companytemp=JSON.parseObject(company,Company.class);
		ReturnData returnData = new ReturnData();
		String companyId = companytemp.getCompanyId();
		if(!companyId.equals("")){
			companyService.updateByCompany(companytemp);	
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");	
		}
		return returnData;
		
	}
	
	/**
	 * 删除公司信息
	 * @param companyId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/deleteByCompany", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData deleteByCompany(@RequestBody String companyId,HttpServletRequest request,HttpServletResponse response){		
		ReturnData returnData = new ReturnData();
		JSONObject obj = JSON.parseObject(companyId);		
		String companyid=obj.getString("companyId");			
		if(companyId !=null){
			companyService.deleteByCompany(companyid);	
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");	
		}
		return returnData;
	}
	/**
	 * 查询所有公司信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/fingdByAllCompany", produces = "application/json;charset=UTF-8", method=RequestMethod.GET)
	public ReturnData fingdByAllCompany(HttpServletRequest request,HttpServletResponse response){
		ReturnData returnData = new ReturnData();		
		List<Company> list =companyService.fingdByAllCompany();	
		if(list.size() != 0){		
			returnData.setData(list);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");			
		}
		return returnData;
	}
	
	/**
	 * 查询一个公司详细信息
	 * @param companyId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/selectByCompany", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData selectByCompany(@RequestBody String companyId,HttpServletRequest request,HttpServletResponse response){
		ReturnData returnData = new ReturnData();
		JSONObject obj = JSON.parseObject(companyId);		
		String companyid=obj.getString("companyId");
		Company company =companyService.selectByCompany(companyid);	
		if(company !=null){
			returnData.setData(company);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");			
		}
		return returnData;
	}
	
}
