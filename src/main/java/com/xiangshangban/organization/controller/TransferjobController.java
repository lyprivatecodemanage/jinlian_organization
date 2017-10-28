package com.xiangshangban.organization.controller;



import java.util.Calendar;
import java.util.HashMap;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiangshangban.organization.bean.ConnectEmpPost;
import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.Transferjob;
import com.xiangshangban.organization.service.ConnectEmpPostService;
import com.xiangshangban.organization.service.EmployeeService;
import com.xiangshangban.organization.service.TransferjobService;
import com.xiangshangban.organization.util.TimeUtil;

@RestController
@RequestMapping("/TransferjobController")
public class TransferjobController {
	@Autowired
	TransferjobService transferjobService;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	ConnectEmpPostService connectEmpPostService;
	
	/**
	 * 添加调动记录
	 * @param transferjob
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/insertTransferjob", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public Map<String, Object> insertTransferjob(@RequestBody String transferjob,HttpServletRequest request,HttpServletResponse response){		
		JSONObject jsonObject = JSONObject.parseObject(transferjob);
		Map<String, Object> result = new HashMap<String, Object>();
		String employeeId = jsonObject.getString("employeeId");
		String transferBeginTime = jsonObject.getString("transferBeginTime");
		boolean transferBeginTime1 = Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", transferBeginTime);
		String departmentId = jsonObject.getString("departmentId");
		String transferJobCause = jsonObject.getString("transferJobCause");			
		String userId ="6B566C197A7D4337A5DA0B4D6F9FC1A3";				
		Transferjob transferjobs = new Transferjob();
		transferjobs.setDepartmentId(departmentId);
		transferjobs.setEmployeeId(employeeId);	
		transferjobs.setUserId(userId);
		transferjobs.setTransferJobCause(transferJobCause);
		transferjobs.setTransferBeginTime(transferBeginTime);				
		String companyId="977ACD3022C24B99AC9586CC50A8F786";
		transferjobs.setCompanyId(companyId);
		
		String transferEndtime = TimeUtil.getLongAfterDate(transferBeginTime, -1, Calendar.DATE);
		if (!transferBeginTime1) {			
			result.put("message", "参数格式不正确");
			return result;
		}
		if(!employeeId.equals("") || !departmentId.equals("") || userId.equals("") || !transferBeginTime.equals("")){
			transferjobService.updateBytransferendtime(employeeId, transferEndtime);
			transferjobService.insertTransferjob(transferjobs);
			//添加调动部门对应的岗位
			String postIdList = jsonObject.getString("postList");
			JSONArray postIdList1 = JSON.parseArray(postIdList);
			for(int i =0;i<postIdList1.size();i++){
				JSONObject jobj = jsonObject.parseObject(postIdList1.getString(i));
						String str = jobj.getString("postId");
						ConnectEmpPost empPost = new ConnectEmpPost();
						empPost.setEmployeeId(employeeId);
						empPost.setDepartmentId(departmentId);
						empPost.setPostId(str);			        	
				 connectEmpPostService.saveConnect(empPost);			 
			}
			Employee employee = new Employee();		
			employee.setDepartmentId(departmentId);
			employee.setEmployeeId(employeeId);
			employeeService.updateByEmployeedept(employee);			
			result.put("message", "调动成功");
			return result;
		}else{
			result.put("message", "调动失败");
			return result;
		}
						
	}
	
	/**
	 * 修改调动记录
	 * @param transferjob
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/updateByTransferjob", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public String updateByTransferjob(@RequestBody String transferjob,HttpServletRequest request,HttpServletResponse response){								
		JSONObject jsonObject = JSONObject.parseObject(transferjob);		
		String employeeId = jsonObject.getString("employeeId");
		String companyId ="977ACD3022C24B99AC9586CC50A8F786";
		Employee employeetemp = employeeService.selectByEmployee(employeeId, companyId);
		String transferBeginTime = jsonObject.getString("transferBeginTime");
		String departmentId = jsonObject.getString("departmentId");				
		String departmentid = employeetemp.getDepartmentId();	
		if(departmentId !=departmentid){
			ConnectEmpPost EmpPost = new ConnectEmpPost();			
			EmpPost.setEmployeeId(employeeId);
			EmpPost.setDepartmentId(departmentid);
			connectEmpPostService.updateConnectpostStaus(EmpPost);
		}				
		String transferJobCause = jsonObject.getString("transferJobCause");
		String transferEndTime = jsonObject.getString("transferEndTime");
		Transferjob transferjobs = new Transferjob();
		transferjobs.setDepartmentId(departmentId);
		transferjobs.setEmployeeId(employeeId);		
		transferjobs.setTransferJobCause(transferJobCause);
		transferjobs.setTransferBeginTime(transferBeginTime);
		transferjobs.setTransferEndTime(transferEndTime);						
		transferjobService.updateByTransferjob(transferjobs);				 				
		String postIdList = jsonObject.getString("postList");
		JSONArray postIdList1 = JSON.parseArray(postIdList);
		for(int i =0;i<postIdList1.size();i++){
			JSONObject jobj = jsonObject.parseObject(postIdList1.getString(i));
					String str = jobj.getString("postId");
					ConnectEmpPost empPost = new ConnectEmpPost();
					empPost.setEmployeeId(employeeId);
					empPost.setDepartmentId(departmentId);
					empPost.setPostId(str);			        	
			 connectEmpPostService.saveConnect(empPost);			 
		}				
		Employee employee = new Employee();		
		employee.setDepartmentId(departmentId);
		employee.setEmployeeId(employeeId);
		String i=employeeService.updateByEmployeedept(employee);		
		return "{\"message\":\""+i+"\"}";					
	}
	
	/**
	 * 删除调动记录
	 * @param transferJobId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/deleteByTransferjob", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public String deleteByDuty(@RequestBody String transferJobId,HttpServletRequest request,HttpServletResponse response){
		System.out.println(transferJobId);
		String i=transferjobService.deleteByTransferjob(transferJobId);		
		return "{\"message\":\""+i+"\"}";
	}
	
	
}
