package com.xiangshangban.organization.controller;


import java.util.ArrayList;
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
import com.xiangshangban.organization.bean.Department;
import com.xiangshangban.organization.bean.DepartmentTree;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.service.DepartmentService;

@RestController
@RequestMapping("/DepartmentController")
public class DepartmentController {

	@Autowired
	DepartmentService departmentService;
	
	
	
	
	/**
	 * 根据当前组织机构、部门名称、部门负责人查询部门信息
	 * @param jsonString
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findByMoreDepartment",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public ReturnData findByMoreDepartment(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){		
			Map<String,String> params = new HashMap<String, String>();
			ReturnData returnData = new ReturnData();
			JSONObject obj = JSON.parseObject(jsonString);			
			params.put("companyName", obj.getString("companyName"));
			params.put("departmentName", obj.getString("departmentName"));
			params.put("employeeName", obj.getString("employeeName"));								
			List<Department> employeelist =departmentService.findByMoreDepartment(params);
			returnData.setData(employeelist);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");		
			return returnData;
	}
	
	
	
	/**
	 * 查询部门关系树
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findDepartmentTree",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public ReturnData findDepartmentTree(HttpServletRequest request,HttpServletResponse response){	
		//String companyId="977ACD3022C24B99AC9586CC50A8F786";
		//获取请求头信息
	    String companyId = request.getHeader("companyId");
		ReturnData returnData = new ReturnData();
		if(!companyId.equals("")){
			List<DepartmentTree> treeNode =departmentService.getDepartmentTreeAll(companyId);
			returnData.setData(treeNode);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");	
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
		}			
		return returnData;
	}
	/**
	 * 查询所有部门下的岗位关人员系树
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getDepartmentempTree",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public ReturnData getDepartmentempTree(HttpServletRequest request,HttpServletResponse response){
		ReturnData returnData = new ReturnData();
		//String companyId="977ACD3022C24B99AC9586CC50A8F786";
		//获取请求头信息
	    String companyId = request.getHeader("companyId");
	    if(!companyId.equals("")){
	    	List<DepartmentTree> treeNode =departmentService.getDepartmentempTreeAll(companyId);
			returnData.setData(treeNode);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");	
	    }else{
	    	returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
	    }			
		return returnData;
	}
	
	
		/**
		 * 查询所有部门信息
		 * @param request
		 * @param response
		 * @return
		 */
	@RequestMapping(value = "/findByAllDepartment",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public ReturnData findByAllDepartment(HttpServletRequest request,HttpServletResponse response){
		ReturnData returnData = new ReturnData();
		//String companyId="977ACD3022C24B99AC9586CC50A8F786";
		String companyId = request.getHeader("companyId");
		if(!companyId.equals("")){
			List<Department> treeNode =departmentService.findByAllDepartment(companyId);
			returnData.setData(treeNode);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
		}				
		return returnData;
	}
	
	/**
	 * 分页查询部门信息
	 * @param pageNum
	 * @param pageRecordNum
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findByAllFenyeDepartment",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public ReturnData findByAllFenyeDepartment(String pageNum, String pageRecordNum,HttpServletRequest request,HttpServletResponse response){
		ReturnData returnData = new ReturnData();
		Map<String, String> params = new HashMap<String, String>();
		//String companyId="977ACD3022C24B99AC9586CC50A8F786";
		String companyId = request.getHeader("companyId");
		String pageNumPattern = "\\d{1,}";
		boolean pageNumFlag = Pattern.matches(pageNumPattern, pageNum);
		boolean pageRecordNumFlag = Pattern.matches(pageNumPattern, pageRecordNum);
		if(!pageNumFlag||!pageRecordNumFlag){
			returnData.setMessage("参数格式不正确");
			returnData.setReturnCode("3007");			
			return returnData;
		}
		List<Department>  treeNode = new ArrayList<>();
		if(!companyId.equals("")){			
			if (pageNum != null && pageNum != "" && pageRecordNum != null && pageRecordNum != "") {
			int number = (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageRecordNum);
				String strNum = String.valueOf(number);
				params.put("pageRecordNum", pageRecordNum);
				params.put("fromPageNum", strNum);
				params.put("companyId", companyId);
				treeNode =departmentService.findByAllFenyeDepartment(params);
				returnData.setData(treeNode);
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");		
		        return returnData;
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");		
			return returnData;	
		}
	}
		returnData.setMessage("数据请求失败");
		returnData.setReturnCode("3001");		
		return returnData;	
			
	}
	
	
	
	/**
	 * 添加部门
	 * @param department
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/insertDepartment", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData insertDepartment(@RequestBody String department,HttpServletRequest request,HttpServletResponse response){		
		ReturnData returnData = new ReturnData();
		Department departmenttemp=JSON.parseObject(department,Department.class);
		//String companyId="977ACD3022C24B99AC9586CC50A8F786";
		//获取请求头信息
		String companyId = request.getHeader("companyId");
		departmenttemp.setCompanyId(companyId);		
		String DepartmentNumbe = departmenttemp.getDepartmentNumbe();
		String DepartmentName = departmenttemp.getDepartmentName();
		//String EmployeeId = departmenttemp.getEmployeeId();
		String DepartmentParentId = departmenttemp.getDepartmentParentId();
		if(DepartmentParentId.equals("")){
			departmenttemp.setDepartmentParentId("0");
		}		
		if(!companyId.equals("") || !DepartmentNumbe.equals("") || !DepartmentName.equals("")){			
			departmentService.insertDepartment(departmenttemp);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");			
			return returnData;						
		}else{			
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
			return returnData;
		}		
	}
	
	/**
	 * 修改部门
	 * @param department
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/updateByDepartment", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData updateByCompany(@RequestBody String department,HttpServletRequest request,HttpServletResponse response){
		ReturnData returnData = new ReturnData();
		Department departmenttemp=JSON.parseObject(department,Department.class);
		String departmentId = departmenttemp.getDepartmentId();
		if(!departmentId.equals("")){
			departmentService.updateByDepartment(departmenttemp);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");		
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
		}
		return returnData;	
	}
	
	/**
	 * 删除部门
	 * @param departmentId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/deleteByDepartment", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData deleteByDepartment(@RequestBody String departmentId,HttpServletRequest request,HttpServletResponse response){				
		ReturnData returnData = new ReturnData();
		JSONObject obj = JSON.parseObject(departmentId);
		departmentId=obj.getString("departmentId");
		if(!departmentId.equals("")){
			departmentService.deleteByDepartment(departmentId);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");			
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");	
		}	
		return returnData;
	}
	
	
}
