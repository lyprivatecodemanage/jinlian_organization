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
import com.xiangshangban.organization.bean.Company;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.service.CompanyService;
import com.xiangshangban.organization.service.OSSFileService;

@RestController
@RequestMapping("/CompanyController")
public class CompanyController {
	@Autowired
	CompanyService companyService;
	@Autowired
	OSSFileService oSSFileService;
	
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
			returnData.setMessage("必传参数为空");
			returnData.setReturnCode("3007");	
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
		if(StringUtils.isNotEmpty(companyId)){
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
	 * 查询所有公司信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/fingdByAllCompany", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData fingdByAllCompany(HttpServletRequest request,HttpServletResponse response){
		ReturnData returnData = new ReturnData();		
		List<Company> list =companyService.fingdByAllCompany();	
		returnData.setData(list);
		returnData.setMessage("数据请求成功");
		returnData.setReturnCode("3000");
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
		if(!companyid.equals("")){
			Company company =companyService.selectByCompany(companyid);	
			if(StringUtils.isNotEmpty(company.getCompanyLogo())){
				String logoPath = oSSFileService.getPathByKey(company.getCompanyNo(),
						"companyLogo", company.getCompanyLogo());
				company.setCompanyLogoPath(logoPath);
			}
			returnData.setData(company);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");			
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");	
		}
		return returnData;
	}
	/**
	 * 保存公司logo
	 * @param jsonString
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveCompanyLogo",produces="application/json;charset=utf-8",method=RequestMethod.POST)
	public Map<String,Object> saveCompanyLogo(@RequestBody String jsonString,HttpServletRequest request){
		Map<String,Object> result = new HashMap<String,Object>();
		String companyId = request.getHeader("companyId");
		String userId = request.getHeader("accessUserId");
		JSONObject jobj = JSON.parseObject(jsonString);
		String companyLogo = jobj.getString("key");
		Company company = companyService.selectByCompany(companyId);
		if(company==null){
			result.put("message", "公司id不存在");
			result.put("returnCode", "4121");
			return result;
		}
		int i = companyService.updateCompanyLogoByCompanyId(companyLogo, companyId);
		if(i>0){
			result.put("message", "数据请求成功");
			result.put("returnCode", "3000");
			return result;
		}else{
			result.put("message", "服务器错误");
			result.put("returnCode", "3001");
			return result;
		}
	}
	/**
	 * 首页获取公司基础信息
	 * @param jsonString
	 * @param request
	 * @return
	 */
	public Map<String,Object> companyDetails(@RequestBody String jsonString,HttpServletRequest request){
		Map<String,Object> result = new HashMap<String,Object>();
		String companyId = request.getHeader("companyId");
		String userId = request.getHeader("accessUserId");
		Company company = companyService.selectByCompany(companyId);
		if(company==null){
			result.put("message", "公司id不存在");
			result.put("returnCode", "4121");
			return result;
		}
		
		
		return result;
	}
	/**
	 * 查询一个人加入了哪些公司
	 * @param userId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/selectByUserCompany", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData selectByUserCompany(HttpServletRequest request,HttpServletResponse response){
		ReturnData returnData = new ReturnData();	
		//获取请求头信息
		//String Account = request.getHeader("Account");
		String Account="admin";		
		if(!Account.equals("")){	
			List<Company> list =companyService.selectByUserCompany(Account);
			returnData.setData(list);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");			
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");	
		}
		return returnData;
	}
	
}
