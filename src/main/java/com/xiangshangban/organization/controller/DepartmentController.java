package com.xiangshangban.organization.controller;


import java.util.ArrayList;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiangshangban.organization.bean.Company;
import com.xiangshangban.organization.bean.Department;
import com.xiangshangban.organization.bean.DepartmentTree;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.service.CompanyService;
import com.xiangshangban.organization.service.DepartmentService;

@RestController
@RequestMapping("/DepartmentController")
public class DepartmentController {

	@Autowired
	DepartmentService departmentService;
	@Autowired
	CompanyService companyService;
	
	
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
			//获取请求头信息
		    String companyId = request.getHeader("companyId");
			JSONObject obj = JSON.parseObject(jsonString);
			String companyName = obj.getString("companyName");
			String departmentName = obj.getString("departmentName");
			String employeeName = obj.getString("employeeName");
			params.put("companyName", companyName);
			params.put("departmentName", departmentName);
			params.put("employeeName", employeeName);	
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
			List<Department> treeNode =departmentService.findByAllFenyeDepartment(params);
			int totalPages = departmentService.findDepartmentPageAllLength(params);//数据总条数
			//总页数
			int pageCountnum =totalPages%Integer.parseInt(pageRecordNum)==0?
					(totalPages/Integer.parseInt(pageRecordNum)):(totalPages/Integer.parseInt(pageRecordNum)+1);	
			returnData.setTotalPages(totalPages);
			returnData.setPagecountNum(pageCountnum);
			returnData.setData(treeNode);
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
		ReturnData returnData = new ReturnData();
		//获取请求头信息
	    String companyId = request.getHeader("companyId");
	    Company emp = companyService.selectByCompany(companyId);
	    String companyName = emp.getCompanyName();
		if(!companyId.equals("")){
			List<DepartmentTree> treeNode =departmentService.getDepartmentTreeAll(companyId);
			returnData.setCompanyName(companyName);
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
	 * 根据部门ID查询部门详细信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findDepartmentById",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public ReturnData findByDepartmentById(@RequestBody String jsonString, HttpServletRequest request,HttpServletResponse response){
		ReturnData returnData = new ReturnData();
		String companyId = request.getHeader("companyId");
		JSONObject obj = JSON.parseObject(jsonString);
		String deptId = obj.getString("departmentId");
		if(StringUtils.isNotEmpty(companyId) && StringUtils.isNotEmpty(deptId)){
			Department department =departmentService.findByDepartmentById(companyId, deptId);
			returnData.setData(department);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
		}else{
			returnData.setMessage("departmentId参数为空");
			returnData.setReturnCode("3006");
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
	public ReturnData findByAllFenyeDepartment(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){
		ReturnData returnData = new ReturnData();
		Map<String, String> params = new HashMap<String, String>();
		JSONObject obj = JSON.parseObject(jsonString);	
		String companyId = request.getHeader("companyId");
		String pageNum = obj.getString("pageNum");
		String pageRecordNum = obj.getString("pageRecordNum");					
		String pageNumPattern = "\\d{1,}";
		boolean pageNumFlag = Pattern.matches(pageNumPattern, pageNum);
		boolean pageRecordNumFlag = Pattern.matches(pageNumPattern, pageRecordNum);
		if(!pageNumFlag||!pageRecordNumFlag){
			returnData.setMessage("参数格式不正确");
			returnData.setReturnCode("3007");			
			return returnData;
		}
		List<Department> list =departmentService.findByAllDepartment(companyId);		
		List<Department>  treeNode = new ArrayList<>();
		if(!companyId.equals("")){			
			if (pageNum != null && pageNum != "" && pageRecordNum != null && pageRecordNum != "") {
			int number = (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageRecordNum);
				String strNum = String.valueOf(number);
				params.put("pageRecordNum", pageRecordNum);
				params.put("fromPageNum", strNum);
				params.put("companyId", companyId);				
				treeNode =departmentService.findByAllFenyeDepartment(params);
				int totalPages = list.size();//数据总条数
				double  pageCountnum =(double)totalPages/Integer.parseInt(pageRecordNum);	
				int pagecountnum=(int) Math.ceil(pageCountnum);//总页数
				returnData.setTotalPages(totalPages);
				returnData.setPagecountNum(pagecountnum);
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
		//获取请求头信息
		String companyId = request.getHeader("companyId");
		Department departmentNumbe = departmentService.findByDepartmentNumber(departmenttemp.getDepartmentNumbe());	
		if(departmentNumbe!=null){
			returnData.setMessage("部门编号已存在");
			returnData.setReturnCode("4019");			
			return returnData;	
		}
		departmenttemp.setCompanyId(companyId);
		String DepartmentName = departmenttemp.getDepartmentName();
		String departmentParentId = departmenttemp.getDepartmentParentId();		
		if(departmentParentId.equals("")){
			departmenttemp.setDepartmentParentId("0");
		}		
		if(StringUtils.isNotEmpty(companyId) && StringUtils.isNotEmpty(DepartmentName)
				&& StringUtils.isNotEmpty(departmentParentId)){			
			departmentService.insertDepartment(departmenttemp);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");			
			return returnData;						
		}else{			
			returnData.setMessage("必传参数为空");
			returnData.setReturnCode("3006");
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
			returnData.setMessage("必传参数为空");
			returnData.setReturnCode("3006");
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
		JSONArray listdepartment = obj.getJSONArray("listdepartment");
		for(int i=0;i<listdepartment.size();i++){
			String departmentid = listdepartment.get(i).toString();
			JSONObject objs = JSON.parseObject(departmentid);
			departmentId=objs.getString("departmentId");	
			if(!departmentId.equals("")){
				departmentService.deleteByDepartment(departmentId);
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");			
			}else{
				returnData.setMessage("必传参数为空");
				returnData.setReturnCode("3006");	
			}			
		}
		return returnData;		
		 
		
	}
	
	
}
