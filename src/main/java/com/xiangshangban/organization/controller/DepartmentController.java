package com.xiangshangban.organization.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public String findByMoreDepartment(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){		
			Map<String,String> params = new HashMap<String, String>();
			JSONObject obj = JSON.parseObject(jsonString);
			params.put("companyName", obj.getString("companyName"));
			params.put("departmentName", obj.getString("departmentName"));
			params.put("employeeName", obj.getString("employeeName"));								
			List<Department> employeelist =departmentService.findByMoreDepartment(params);
		return JSON.toJSONString(employeelist);
	}
	
	
	
	/**
	 * 查询部门关系树
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findDepartmentTree",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public String findDepartmentTree(HttpServletRequest request,HttpServletResponse response){	
		String companyId="977ACD3022C24B99AC9586CC50A8F786";
		List<DepartmentTree> treeNode =departmentService.getDepartmentTreeAll(companyId);	
		return JSON.toJSONString(treeNode);
	}
	/**
	 * 查询所有部门下的岗位关人员系树
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getDepartmentempTree",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public String getDepartmentempTree(HttpServletRequest request,HttpServletResponse response){
		String companyId="977ACD3022C24B99AC9586CC50A8F786";
		List<DepartmentTree> treeNode =departmentService.getDepartmentempTreeAll(companyId);	
		return JSON.toJSONString(treeNode);
	}
	
	
		/**
		 * 查询所有部门信息
		 * @param request
		 * @param response
		 * @return
		 */
	@RequestMapping(value = "/findByAllDepartment",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public String findByAllDepartment(HttpServletRequest request,HttpServletResponse response){
		String companyId="977ACD3022C24B99AC9586CC50A8F786";
		List<Department> treeNode =departmentService.findByAllDepartment(companyId);	
		return JSON.toJSONString(treeNode);
	}
	
	
	
	/**
	 * 添加部门
	 * @param department
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/insertDepartment", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public Map<String, Object> insertDepartment(@RequestBody String department,HttpServletRequest request,HttpServletResponse response){		
		Map<String, Object> map=new HashMap<String, Object>();
		String companyId="977ACD3022C24B99AC9586CC50A8F786";
		Department departmenttemp=JSON.parseObject(department,Department.class);
		departmenttemp.setCompanyId(companyId);
		String DepartmentNumbe = departmenttemp.getDepartmentNumbe();
		String DepartmentName = departmenttemp.getDepartmentName();
		String EmployeeId = departmenttemp.getEmployeeId();
		String DepartmentParentId = departmenttemp.getDepartmentParentId();
		departmentService.insertDepartment(departmenttemp);
		if(companyId.equals("") || DepartmentNumbe.equals("") || DepartmentName.equals("") || EmployeeId.equals("") || DepartmentParentId.equals("")){
			map.put("message","添加失败");
			return map;			
		}else{			
			map.put("message", "添加成功");			
			return map;
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
	public Map<String, String> updateByCompany(@RequestBody String department,HttpServletRequest request,HttpServletResponse response){
		System.out.println(department);
		Map<String,String> param = new HashMap<String, String>();
		Department departmenttemp=JSON.parseObject(department,Department.class);
		String i=departmentService.updateByDepartment(departmenttemp);
		param.put("message", i);
	    return	param;			
	}
	
	/**
	 * 删除部门
	 * @param departmentId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/deleteByDepartment", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public Map<String, Object> deleteByDepartment(@RequestBody String departmentId,HttpServletRequest request,HttpServletResponse response){				
		Map<String, Object> map=new HashMap<String, Object>();
		JSONObject obj = JSON.parseObject(departmentId);
		departmentId=obj.getString("departmentId");
		if(!departmentId.equals("")){
			departmentService.deleteByDepartment(departmentId);
			map.put("message", "删除成功");			
		}else{
			map.put("message","删除失败");		
		}	
		return map;
	}
	
}
