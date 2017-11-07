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
import com.xiangshangban.organization.bean.ReturnData;
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
	 * 根据员工ID，在职时间查询员工信息 
	 * @param jsonString
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/findByempinfo", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData findByempinfo(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){	
		ReturnData returnData = new ReturnData();
		JSONObject obj = JSON.parseObject(jsonString);
		Map<String,String> params = new HashMap<String, String>();
		Map<String,String> nullemp = new HashMap<String, String>();
		String companyId="977ACD3022C24B99AC9586CC50A8F786";
		//获取请求头信息			
		//String companyId = request.getHeader("companyId");
		params.put("companyId",companyId);
		params.put("employeeId",obj.getString("employeeId"));
		nullemp.put("companyId", companyId);
		nullemp.put("employeeId", obj.getString("employeeId"));
		String positionTime = obj.getString("positionTime");
		params.put("positionTime",positionTime);
		boolean positiontime = Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", positionTime);
		if(obj.getString("employeeId").equals("")){
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
			return returnData;
		}
		if (!positiontime) {				
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
			return returnData;
		}			
			Transferjob transferjobemp=transferjobService.findByempinfo(params);
			if(transferjobemp!=null){
				returnData.setData(transferjobemp);
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");			
			}else{
				Transferjob transferjobnullemp=transferjobService.findByempNullinfo(nullemp);
				if(transferjobnullemp!=null){
				returnData.setData(transferjobnullemp);
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");
				}					
		}
			return returnData;		
	}
	
	/**
	 * 添加调动记录
	 * @param transferjob
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/insertTransferjob", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData insertTransferjob(@RequestBody String transferjob,HttpServletRequest request,HttpServletResponse response){		
		JSONObject jsonObject = JSONObject.parseObject(transferjob);
		ReturnData returnData = new ReturnData();
		String employeeId = jsonObject.getString("employeeId");
		String transferBeginTime = jsonObject.getString("transferBeginTime");
		boolean transferBeginTime1 = Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", transferBeginTime);
		//修改后传的部门id
		String departmentId = jsonObject.getString("departmentId");
		String transferJobCause = jsonObject.getString("transferJobCause");	
		String directPersonId = jsonObject.getString("directPersonId");
		String userId ="6B566C197A7D4337A5DA0B4D6F9FC1A3";
		String companyId="977ACD3022C24B99AC9586CC50A8F786";
		//获取请求头信息			
		//String companyId = request.getHeader("companyId");
		//String userId = request.getHeader("userId");
		Transferjob transferjobs = new Transferjob();
		transferjobs.setDepartmentId(departmentId); 
		transferjobs.setEmployeeId(employeeId);	
		transferjobs.setUserId(userId);
		transferjobs.setDirectPersonId(directPersonId);
		transferjobs.setTransferJobCause(transferJobCause);
		transferjobs.setTransferBeginTime(transferBeginTime);						
		transferjobs.setCompanyId(companyId);
		String transferEndtime = TimeUtil.getLongAfterDate(transferBeginTime, -1, Calendar.DATE);
		if (!transferBeginTime1) {						
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
			return returnData;
		}
		Transferjob transferjobemp = transferjobService.selectByTransferjobpost(employeeId, companyId);
		//上一次所在部门ID
		String departmentid = transferjobemp.getDepartmentId();		
		if(departmentId !=departmentid){
			ConnectEmpPost EmpPost = new ConnectEmpPost();			
			EmpPost.setEmployeeId(employeeId);
			//原来所在部门ID
			EmpPost.setDepartmentId(departmentid);
			//把原来所在部门所对应的岗位的状态改为1
			connectEmpPostService.updateConnectpostStaus(EmpPost);
			//员工所在部门调动后，也要修改员工信息表现在所在部门的部门id
			Employee employee = new Employee();		
			employee.setDepartmentId(departmentId);
			employee.setEmployeeId(employeeId);
			employeeService.updateByEmployeedept(employee);
		}		
		if(!employeeId.equals("") || !departmentId.equals("") || userId.equals("") || !transferBeginTime.equals("")){
			transferjobService.updateBytransferendtime(employeeId, transferEndtime);
			transferjobService.insertTransferjob(transferjobs);
			//添加调动部门对应的岗位
			String postIdList = jsonObject.getString("postList");
			JSONArray postIdList1 = JSON.parseArray(postIdList);			
			for(int i =0;i<postIdList1.size();i++){
				JSONObject jobj = jsonObject.parseObject(postIdList1.getString(i)) ;
						String postId = jobj.getString("postId");
						String postGrades = jobj.getString("postGrades");
						ConnectEmpPost empPost = new ConnectEmpPost();
						ConnectEmpPost connect = new ConnectEmpPost();
						ConnectEmpPost connectemppos = connectEmpPostService.findByConnect(employeeId,departmentId, postGrades);
						if(connectemppos==null){
							empPost.setEmployeeId(employeeId);
							empPost.setDepartmentId(departmentId);
							empPost.setPostGrades(postGrades);
							empPost.setPostId(postId);				          
						    connectEmpPostService.saveConnect(empPost);
						}
						if(connectemppos!=null){
							String postid = connectemppos.getPostId();
							if(!postId.equals("postid")){
								connect.setDepartmentId(departmentId);
								connect.setEmployeeId(employeeId);
								connect.setPostId(postid);
								connectEmpPostService.updatetpostGradespostStaus(connect);
								empPost.setEmployeeId(employeeId);
								empPost.setDepartmentId(departmentId);
								empPost.setPostGrades(postGrades);
								empPost.setPostId(postId);				          
							    connectEmpPostService.saveConnect(empPost);
							}continue;	
						}						
																					 				
			}						
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
	 * 修改调动记录
	 * @param transferjob
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/updateByTransferjob", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData updateByTransferjob(@RequestBody String transferjob,HttpServletRequest request,HttpServletResponse response){								
		JSONObject jsonObject = JSONObject.parseObject(transferjob);
		ReturnData returnData = new ReturnData();
		String employeeId = jsonObject.getString("employeeId");
		String transferBeginTime = jsonObject.getString("transferBeginTime");
		boolean transferBeginTime1 = Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", transferBeginTime);
		//修改后传的部门id
		String departmentId = jsonObject.getString("departmentId");
		String transferJobId = jsonObject.getString("transferJobId");
		String transferJobCause = jsonObject.getString("transferJobCause");	
		String directPersonId = jsonObject.getString("directPersonId");
		String userId ="6B566C197A7D4337A5DA0B4D6F9FC1A3";
		String companyId="977ACD3022C24B99AC9586CC50A8F786";
		//获取请求头信息			
		//String companyId = request.getHeader("companyId");
		//String userId = request.getHeader("userId");
		Transferjob transferjobs = new Transferjob();
		transferjobs.setDepartmentId(departmentId);
		transferjobs.setEmployeeId(employeeId);	
		transferjobs.setUserId(userId);
		transferjobs.setDirectPersonId(directPersonId);
		transferjobs.setTransferJobCause(transferJobCause);
		transferjobs.setTransferBeginTime(transferBeginTime);
		transferjobs.setTransferJobId(transferJobId);		
		transferjobs.setCompanyId(companyId);
		String transferEndtime = TimeUtil.getLongAfterDate(transferBeginTime, -1, Calendar.DATE);
		if (!transferBeginTime1) {			
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
			return returnData;
		}
		Transferjob transferjobemp = transferjobService.selectByTransferjobpost(employeeId, companyId);
		//上一次所在部门ID
		String departmentid = transferjobemp.getDepartmentId();		
		if(departmentId !=departmentid){
			ConnectEmpPost EmpPost = new ConnectEmpPost();			
			EmpPost.setEmployeeId(employeeId);
			//原来所在部门ID
			EmpPost.setDepartmentId(departmentid);
			//把原来所在部门所对应的岗位的状态改为1
			connectEmpPostService.updateConnectpostStaus(EmpPost);
			//员工所在部门调动后，也要修改员工信息表现在所在部门的部门id
			Employee employee = new Employee();		
			employee.setDepartmentId(departmentId);
			employee.setEmployeeId(employeeId);
			employeeService.updateByEmployeedept(employee);
		}		
		if(!employeeId.equals("") || !departmentId.equals("") || userId.equals("") || !transferBeginTime.equals("") || !transferJobId.equals("")){
			transferjobService.updateBytransferendtime(employeeId, transferEndtime);
			transferjobService.insertTransferjob(transferjobs);
			//添加调动部门对应的岗位
			String postIdList = jsonObject.getString("postList");
			JSONArray postIdList1 = JSON.parseArray(postIdList);			
			for(int i =0;i<postIdList1.size();i++){
				JSONObject jobj = jsonObject.parseObject(postIdList1.getString(i)) ;
						String postId = jobj.getString("postId");
						String postGrades = jobj.getString("postGrades");
						ConnectEmpPost empPost = new ConnectEmpPost();
						ConnectEmpPost connect = new ConnectEmpPost();
						ConnectEmpPost connectemppos = connectEmpPostService.findByConnect(employeeId,departmentId, postGrades);
						if(connectemppos==null){
							empPost.setEmployeeId(employeeId);
							empPost.setDepartmentId(departmentId);
							empPost.setPostGrades(postGrades);
							empPost.setPostId(postId);				          
						    connectEmpPostService.saveConnect(empPost);
						}
						if(connectemppos!=null){
							String postid = connectemppos.getPostId();
							if(!postId.equals("postid")){
								connect.setDepartmentId(departmentId);
								connect.setEmployeeId(employeeId);
								connect.setPostId(postid);
								connectEmpPostService.updatetpostGradespostStaus(connect);
								empPost.setEmployeeId(employeeId);
								empPost.setDepartmentId(departmentId);
								empPost.setPostGrades(postGrades);
								empPost.setPostId(postId);				          
							    connectEmpPostService.saveConnect(empPost);
							}continue;	
						}						
																					 				
			}						
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
	 * 删除调动记录
	 * @param transferJobId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/deleteByTransferjob", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData deleteByDuty(@RequestBody String transferJobId,HttpServletRequest request,HttpServletResponse response){
		ReturnData returnData = new ReturnData();		
		if(transferJobId.equals("")){
			transferjobService.deleteByTransferjob(transferJobId);	
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");			
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");			
		}
		return returnData;
	}
	
	
}
