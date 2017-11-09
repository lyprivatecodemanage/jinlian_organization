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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.xiangshangban.organization.bean.ConnectEmpPost;
import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.bean.Transferjob;
import com.xiangshangban.organization.bean.User;
import com.xiangshangban.organization.service.ConnectEmpPostService;
import com.xiangshangban.organization.service.EmployeeService;
import com.xiangshangban.organization.service.PostService;
import com.xiangshangban.organization.service.TransferjobService;
import com.xiangshangban.organization.service.UserService;
import com.xiangshangban.organization.util.HttpRequestFactory;

@RestController
@RequestMapping("/EmployeeController")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	@Autowired
	ConnectEmpPostService connectEmpPostService;
	@Autowired
	PostService postService;
	@Autowired
	UserService userService;
	@Autowired
	TransferjobService  transferjobService;
	
	
	/**
	 * 点击激活，管理员把员工登录名注册到用户注册表里
	 * @param employeeId
	 * @param request
	 * @param response
	 * @return
	 * 
	 */
	@RequestMapping(value="/saveUserEmp", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData saveUserEmp(@RequestBody String employeeId,HttpServletRequest request,HttpServletResponse response){
		JSONObject obj = JSON.parseObject(employeeId);
		ReturnData returnData = new ReturnData();
		employeeId=obj.getString("employeeId");			
		//String companyId="977ACD3022C24B99AC9586CC50A8F786";
		//获取请求头信息
		String companyId = request.getHeader("companyId");			
		Employee employee = employeeService.selectByEmployee(employeeId, companyId);
		String LoginName = employee.getLoginName();		
		User useremp = userService.getOneUser(LoginName);		
		User user = new User();
		user.setUserId(employeeId);
		user.setUserName(LoginName);				
		if(!employeeId.equals("") || useremp ==null){
			userService.saveUser(user);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");							
		}else{
			returnData.setMessage("业务错误");
			returnData.setReturnCode("4000");				
		}	
		return returnData;	
	}
	
	/**
	 * 用户申请加入公司(如果用户已经注册，调该方法)
	 * @param employee
	 * @param request
	 * @param response
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value="/insertEmployeeuser", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData insertEmployeeuser(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response) throws JsonProcessingException{								
		ReturnData returnData = new ReturnData();
		Employee employeenew = new Employee();
		JSONObject obj = JSON.parseObject(jsonString);
		String userName = obj.getString("userName");		
		String Phone = obj.getString("Phone");
		String Account = obj.getString("Account");
		String companyId = obj.getString("companyId");						
		String userId = obj.getString("userId");
		 Employee loginname=employeeService.findByemploginName(Account);		 
		if(loginname != null){			
			returnData.setMessage("业务错误");
			returnData.setReturnCode("4000");  
			return returnData;			
		}else{						
			employeenew.setEmployeeName(userName);
			employeenew.setEmployeePhone(Phone);
			employeenew.setEmployeeId(userId);
			employeenew.setLoginName(Account);
			employeenew.setCompanyId(companyId);						
			if(!userName.equals("") || !Phone.equals("") || !userId.equals("") || !Account.equals("") || !companyId.equals("")){
				employeeService.insertEmployeeuser(employeenew);
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");				
			}else{
				returnData.setMessage("数据请求失败");
				returnData.setReturnCode("3001");
			}				
				return returnData;										    			
		}					
	}		
	/**
	 * 完善申请入职员工的入职信息
	 * @param jsonString
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/ImproveEmployeeOrientation", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData ImproveEmployeeOrientation(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){		 		
		ReturnData returnData = new ReturnData();
		Employee employeenew = new Employee();
		JSONObject obj = JSON.parseObject(jsonString);	
		String employeeNo = obj.getString("employeeNo");		
		Employee employeeNotemp =employeeService.findByemployeeNo(employeeNo);	
		if(employeeNotemp != null ){			
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
			return returnData;			
		}
		String employeeId = obj.getString("employeeId");
		String employeeName = obj.getString("employeeName");			
		String employeeSex = obj.getString("employeeSex");
		String loginName = obj.getString("loginName");
		String employeePhone = obj.getString("employeePhone");
		boolean employeephone = Pattern.matches("^[1][3,4,5,7,8][0-9]{9}$", employeePhone);
		String employeeTwophone = obj.getString("employeeTwophone");
		boolean employeetwophone = Pattern.matches("^[1][3,4,5,7,8][0-9]{9}$", employeeTwophone);
		String directPersonId = obj.getString("directPersonId");
		String entryTime = obj.getString("entryTime");
		boolean entrytime = Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", entryTime);
		String probationaryExpired = obj.getString("probationaryExpired");
		boolean probationaryexpired = Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", probationaryExpired);
		String departmentId = obj.getString("departmentId");
		employeenew.setEmployeeId(employeeId);
		employeenew.setEmployeeName(employeeName);
		employeenew.setEmployeeSex(employeeSex);		
		employeenew.setLoginName(loginName);//登录名
		employeenew.setEmployeePhone(employeePhone);
		employeenew.setEmployeeTwophone(employeeTwophone);
		employeenew.setDirectPersonId(directPersonId);			
		employeenew.setEntryTime(entryTime);
		employeenew.setEmployeeNo(employeeNo);
		employeenew.setProbationaryExpired(probationaryExpired);
		employeenew.setDepartmentId(departmentId);
		employeenew.setEmployeeStatus("0");
		if (!employeephone || !employeetwophone || !entrytime || !probationaryexpired) {				
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
			return returnData;
		}
			employeeService.updateByEmployee(employeenew);   
			//添加员工信息的同时把员工信息添加到调动表里
		    Transferjob transferjob = new Transferjob();		    
		    transferjob.setEmployeeId(employeeId);
		    transferjob.setDepartmentId(departmentId);
		    transferjob.setTransferBeginTime(entryTime);
		    transferjob.setTransferJobCause("新员工入职该岗位");
		    //获取请求头信息
			String companyId = request.getHeader("companyId");
			String userId = request.getHeader("userId");
		    transferjob.setUserId(userId);//操作人ID	
		    transferjob.setCompanyId(companyId);
		    transferjobService.insertTransferjob(transferjob);
		    //把员工关联的岗位添加到connect_emp_post_中间表里面					    
			String postIdList = obj.getString("postList");
			JSONArray postIdList1 = JSON.parseArray(postIdList);			
			for(int i =0;i<postIdList1.size();i++){
				JSONObject jobj = obj.parseObject(postIdList1.getString(i)) ;
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
			if(!employeeId.equals("")){
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");		
			}else{
				returnData.setMessage("数据请求失败");
				returnData.setReturnCode("3001");						
			}						
			return returnData;
	}
	
	
	
	
	/**
	 * 根据姓名、登录名、联系方式、性别、所属部门、岗位、入职时间、在职状态查询员工信息
	 * @param jsonString
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findByMoreEmployee",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public ReturnData findByMoreEmployee(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){						   		 	    		   
		    ReturnData returnData = new ReturnData();
			Map<String, Object> postnamelist=new HashMap<String, Object>();						
			Map<String,String> params = new HashMap<String, String>();
			JSONObject obj = JSON.parseObject(jsonString);
			//String companyId="977ACD3022C24B99AC9586CC50A8F786";
			//获取请求头信息
			String companyId = request.getHeader("companyId");			
			params.put("companyId",companyId);
			params.put("employeeName", obj.getString("employeeName"));
			params.put("loginName", obj.getString("loginName"));			
			params.put("employeePhone", obj.getString("employeePhone"));
			params.put("employeeTwophone", obj.getString("employeeTwophone"));
			params.put("employeeSex", obj.getString("employeeSex"));
			params.put("departmentName", obj.getString("departmentName"));
			params.put("postName", obj.getString("postName"));
			params.put("entryTime", obj.getString("entryTime"));
			params.put("employeeStatus", obj.getString("employeeStatus"));
			List<Employee> employeelistemp =employeeService.findByMoreEmployee(params);	
			int s=0;	
			for (int i = 0; i < employeelistemp.size(); i++) {
				s=s+1;
                String temps =String.valueOf(s);
				Employee employeelist = employeelistemp.get(i);
				String Employeeid = employeelist.getEmployeeId();
				String departmentId = employeelist.getDepartmentId();
	            List<Post> PostNamelist = postService.selectByPostName(Employeeid,departmentId);	            
	            employeelist.setPostList(PostNamelist);	                 
	            postnamelist.put("employeelist"+temps, employeelist);				
			}	
			returnData.setData(postnamelist);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
			return returnData;
	}
	
	/**
	 * 查询一个岗位下的所有员工
	 */
	@RequestMapping(value = "/findByposcounttemp",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public ReturnData findByposcounttemp(@RequestBody String postId, HttpServletRequest request,HttpServletResponse response){				
		 ReturnData returnData = new ReturnData();
		//String companyId="977ACD3022C24B99AC9586CC50A8F786";
		//获取请求头信息
		String companyId = request.getHeader("companyId");	
		if(!postId.equals("")){
		List<Employee> emplist = employeeService.findByposcounttemp(postId, companyId);
		if(emplist.size()!=0){
			returnData.setData(emplist);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");	
			return returnData;
		}
	}else{
		returnData.setMessage("数据请求失败");
		returnData.setReturnCode("3001");
	}
		return returnData;
}
	
	
	/**
	 * 所有在职员工的信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findByAllEmployee",produces = "application/json;charset=UTF-8", method=RequestMethod.GET)	
	public ReturnData findByAllEmployee(HttpServletRequest request,HttpServletResponse response){		
		ReturnData returnData = new ReturnData();
		Map<String, Object> postnamelist=new HashMap<String, Object>();
		//String companyId="977ACD3022C24B99AC9586CC50A8F786";	 
		//获取请求头信息
		String companyId = request.getHeader("companyId");
		List<Employee> employeelistemp =employeeService.findByAllEmployee(companyId);
		int s=0;	
		for (int i = 0; i < employeelistemp.size(); i++) {
			 s=s+1;
           String temps =String.valueOf(s);
		   Employee employeelist = employeelistemp.get(i);
		   String Employeeid = employeelist.getEmployeeId();
		   String departmentId = employeelist.getDepartmentId();
           List<Post> PostNamelist = postService.selectByPostName(Employeeid,departmentId);	            
           employeelist.setPostList(PostNamelist);	                 
           postnamelist.put("employeelist"+temps, employeelist);			
		}	
		if(postnamelist.size()!=0){
			returnData.setData(postnamelist);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");				
		}					
		return returnData;
			
	}
	
	/**
	 * 所有离职员工的信息
	 * @param request
	 * @param response
	 * @return
	 */
	
	@RequestMapping(value = "/findByLiZhiemployee",produces = "application/json;charset=UTF-8", method=RequestMethod.GET)	
	public ReturnData findByLiZhiemployee(HttpServletRequest request,HttpServletResponse response){		
		int s=0;	
		ReturnData returnData = new ReturnData();
		Map<String, Object> postnamelist=new HashMap<String, Object>();
		//String companyId="977ACD3022C24B99AC9586CC50A8F786";
		//获取请求头信息
		String companyId = request.getHeader("companyId");
		List<Employee> LiZhiemployeelist =employeeService.findByLiZhiemployee(companyId);		
		for (int i = 0; i < LiZhiemployeelist.size(); i++) {
			 s=s+1;
           String temps =String.valueOf(s);
		   Employee employeelist = LiZhiemployeelist.get(i);
		   String Employeeid = employeelist.getEmployeeId();
		   String departmentId = employeelist.getDepartmentId();
           List<Post> PostNamelist = postService.selectByPostName(Employeeid,departmentId);	            
          employeelist.setPostList(PostNamelist);	                 
          postnamelist.put("employeelist"+temps, employeelist);
		}
		if(postnamelist.size()!=0){			
			returnData.setData(postnamelist);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");				
		}					
		return returnData;
		 
	}
	
	
	/**
	 *  
	 * 添加员工信息(如果用户没有注册，调该方法)
	 * @param employee
	 * @param request
	 * @param response
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value="/insertEmployee", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData insertEmployee(@RequestBody String employee,HttpServletRequest request,HttpServletResponse response) throws JsonProcessingException{								
		ReturnData returnData = new ReturnData();
		Employee employeenew = new Employee();
		JSONObject jsonObject = JSONObject.parseObject(employee);
		String employeeNo = jsonObject.getString("employeeNo");		
		Employee employeeNotemp =employeeService.findByemployeeNo(employeeNo);	
		if(employeeNotemp != null ){			
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
			return returnData;			
		}
		 String loginName = jsonObject.getString("loginName");		 
		 Employee loginname=employeeService.findByemploginName(loginName);		 
		if(loginname != null ){			
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
			return returnData;			
		}else{
			String employeeName = jsonObject.getString("employeeName");			
			String employeeSex = jsonObject.getString("employeeSex");			
			String employeePhone = jsonObject.getString("employeePhone");
			boolean employeephone = Pattern.matches("^[1][3,4,5,7,8][0-9]{9}$", employeePhone);
			String employeeTwophone = jsonObject.getString("employeeTwophone");
			boolean employeetwophone = Pattern.matches("^[1][3,4,5,7,8][0-9]{9}$", employeeTwophone);
			String directPersonId = jsonObject.getString("directPersonId");
			String entryTime = jsonObject.getString("entryTime");
			boolean entrytime = Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", entryTime);
			String probationaryExpired = jsonObject.getString("probationaryExpired");
			boolean probationaryexpired = Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", probationaryExpired);
			String departmentId = jsonObject.getString("departmentId");
			//String companyId ="977ACD3022C24B99AC9586CC50A8F786";
			//获取请求头信息
			String companyId = request.getHeader("companyId");
			employeenew.setCompanyId(companyId);
			employeenew.setEmployeeName(employeeName);
			employeenew.setEmployeeSex(employeeSex);
			employeenew.setLoginName(loginName);
			employeenew.setEmployeePhone(employeePhone);
			employeenew.setEmployeeTwophone(employeeTwophone);
			employeenew.setDirectPersonId(directPersonId);			
			employeenew.setEntryTime(entryTime);
			employeenew.setEmployeeNo(employeeNo);
			employeenew.setProbationaryExpired(probationaryExpired);
			employeenew.setDepartmentId(departmentId);
			employeenew.setEmployeeStatus("0");
			if (!employeephone || !employeetwophone || !entrytime || !probationaryexpired) {				
				returnData.setMessage("数据请求失败");
				returnData.setReturnCode("3001");
				return returnData;
			}else{
				employeeService.insertEmployee(employeenew);				
				//添加员工信息的同时把员工形象添加到调动表里
				Employee employeeNotemps =employeeService.findByemployeeNo(employeeNo);
				String EmployeeId = employeeNotemps.getEmployeeId();
			    Transferjob transferjob = new Transferjob();		    
			    transferjob.setEmployeeId(EmployeeId);
			    transferjob.setDepartmentId(departmentId);
			    transferjob.setTransferBeginTime(entryTime);
			    transferjob.setTransferJobCause("新员工入职该岗位");
			    //获取请求头信息			
				String userId = request.getHeader("userId");
			    transferjob.setUserId(userId);//操作人ID	
			    transferjob.setCompanyId(companyId);
			    transferjobService.insertTransferjob(transferjob);
			    //把员工关联的岗位添加到connect_emp_post_中间表里面
			    String empId=employeenew.getEmployeeId();						
				String postIdList = jsonObject.getString("postList");
				JSONArray postIdList1 = JSON.parseArray(postIdList);
				for(int i =0;i<postIdList1.size();i++){
					JSONObject jobj = jsonObject.parseObject(postIdList1.getString(i)) ;
							String postId = jobj.getString("postId");
							String postGrades = jobj.getString("postGrades");
							ConnectEmpPost empPost = new ConnectEmpPost();
							empPost.setEmployeeId(empId);
							empPost.setDepartmentId(departmentId);
							empPost.setPostGrades(postGrades);
							empPost.setPostId(postId);				          
					 connectEmpPostService.saveConnect(empPost);				 				
				}									
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");
				return returnData;
			}								    			
		}					
	}
	/**
	 * 查询申请入职的人员信息 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findByruzhiempinfo",produces = "application/json;charset=UTF-8", method=RequestMethod.GET)	
	public ReturnData findByruzhiempinfo(HttpServletRequest request,HttpServletResponse response){		
		ReturnData returnData = new ReturnData();
		List<Employee> employeelist = employeeService.findByruzhiempinfo();
		if(employeelist !=null){
			returnData.setData(employeelist);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");			 
		}
		return returnData;		  
	}
	
	/**
	 * 编辑员工信息
	 * @param employee
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/updateByEmployee", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData updateByEmployee(@RequestBody String employee,HttpServletRequest request,HttpServletResponse response){		 		
		JSONObject jsonObject = JSONObject.parseObject(employee);	
		ReturnData returnData = new ReturnData();		
		String employeeId = jsonObject.getString("employeeId");
			if(!employeeId.equals("")){
				Employee employeenew = new Employee();									
					//String companyId ="977ACD3022C24B99AC9586CC50A8F786";
					//获取请求头信息			
					String companyId = request.getHeader("companyId");
					Transferjob transferjob = transferjobService.selectByTransferjobpost(employeeId, companyId);
					//上一次所在部门ID
					String departmentid = transferjob.getDepartmentId();
					//修改后传的部门id
					String departmentId = jsonObject.getString("departmentId");	
					if(departmentId !=departmentid){
						ConnectEmpPost EmpPost = new ConnectEmpPost();			
						EmpPost.setEmployeeId(employeeId);
						//原来所在部门ID
						EmpPost.setDepartmentId(departmentid);
						//把原来所在部门所对应的岗位的状态改为1
						connectEmpPostService.updateConnectpostStaus(EmpPost);
						//员工所在部门修改后，也要修改调动表现在所在部门的员工部门
						transferjobService.updateByTrandepartmentId(departmentId);
					}
					String employeeName = jsonObject.getString("employeeName");
					String employeeSex = jsonObject.getString("employeeSex");
					String loginName = jsonObject.getString("loginName");
					String employeePhone = jsonObject.getString("employeePhone");
					String employeeTwophone = jsonObject.getString("employeeTwophone");
					String directPersonId = jsonObject.getString("directPersonId");
					String entryTime = jsonObject.getString("entryTime");
					String probationaryExpired = jsonObject.getString("probationaryExpired");
					employeenew.setEmployeeName(employeeName);
					employeenew.setEmployeeSex(employeeSex);
					employeenew.setLoginName(loginName);
					employeenew.setEmployeePhone(employeePhone);
					employeenew.setEmployeeTwophone(employeeTwophone);
					employeenew.setDirectPersonId(directPersonId);			
					employeenew.setEntryTime(entryTime);			
					employeenew.setProbationaryExpired(probationaryExpired);
					employeenew.setDepartmentId(departmentId);
					
					employeeService.updateByEmployee(employeenew);   				
				    //把员工关联的岗位添加到connect_emp_post_中间表里面					    
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
	 * 查询单条员工信息
	 * @param employeeId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/selectByEmployee",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public ReturnData selectByEmployee(@RequestBody String employeeId,HttpServletRequest request,HttpServletResponse response){									
		JSONObject obj = JSON.parseObject(employeeId);		
		String employeeid = obj.getString("employeeId");	
		ReturnData returnData = new ReturnData();
		String companyId = "977ACD3022C24B99AC9586CC50A8F786";
		//获取请求头信息			
		//String companyId = request.getHeader("companyId");
         if(!employeeId.equals("")){ 
        	 Employee emp =employeeService.selectByEmployee(employeeid,companyId);	
        	 String departmentId = emp.getDepartmentId();
             List<Post> PostNamelist = postService.selectByPostName(employeeid,departmentId);
             emp.setPostList(PostNamelist);
             returnData.setData(emp);
             returnData.setMessage("数据请求成功");
			 returnData.setReturnCode("3000");	
         }else{      	 
	        	returnData.setMessage("数据请求失败");
				returnData.setReturnCode("3001");				
         }	
         return returnData;
		}
	
	/**
	 * 删除员工信息
	 * @param employeeId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/deleteByEmployee", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData deleteByEmployee(@RequestBody String employeeId,HttpServletRequest request,HttpServletResponse response){		
		ReturnData returnData = new ReturnData();
		Map<String, Object> cmdmap=new HashMap<String, Object>();		
		List<String> cmdlist=new ArrayList<String>();
		cmdmap.put("action", "UPDATE_USER_INFO");
		cmdlist.add(employeeId);
		cmdmap.put("employeeIdCollection", cmdlist);
		HttpRequestFactory.sendRequet("http://192.168.0.119:8080/employee/commandGenerate", cmdmap);
		if(!employeeId.equals("")){
			employeeService.batchUpdateTest(employeeId);
			connectEmpPostService.updateConnectDelehipostStaus(employeeId);
			 returnData.setMessage("数据请求成功");
			 returnData.setReturnCode("3000");
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
		}	
		return returnData;
	}
	
	/**
	 * 删除一个人员对应下的岗位
	 * @param employeeId
	 * @param postId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/deleteConnect", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData deleteConnect(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){			
		JSONObject obj = JSON.parseObject(jsonString);
		ReturnData returnData = new ReturnData();
		String employeeId = obj.getString("employeeId");		
		String postId = obj.getString("postId");				
		Map<String, Object> cmdmap=new HashMap<String, Object>();		
		List<String> cmdlist=new ArrayList<String>();
		cmdmap.put("action", "UPDATE_USER_INFO");
		cmdlist.add(employeeId);
		cmdmap.put("employeeIdCollection", cmdlist);
		HttpRequestFactory.sendRequet("http://192.168.0.119:8080/employee/commandGenerate", cmdmap);
		if(!employeeId.equals("")){
			connectEmpPostService.deleteConnect(employeeId, postId);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
		}	
		return returnData;
	}
		
	/**
	 * 逻辑删除，把员工在职状态更新为（2：删除）
	 * @param jsonString
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/batchUpdateTest", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData batchUpdateTest(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){		
		JSONObject jobj = JSON.parseObject(jsonString);
		ReturnData returnData = new ReturnData();
		JSONArray array = jobj.getJSONArray("listEmployee");		
		for (int i = 0; i < array.size(); i++) {
			String empId = JSON.parseObject(array.get(i).toString()).getString("EmployeeId");	
			if(!empId.equals("")){	
				//把员工在职状态改为2
				employeeService.batchUpdateTest(empId);	
				//删除后，把connect_emp_post_表对应的岗位ID状态改为1
				connectEmpPostService.updateConnectDelehipostStaus(empId);
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");
			}else{
				returnData.setMessage("数据请求失败");
				returnData.setReturnCode("3001");					
			}	
			Map<String, Object> cmdmap=new HashMap<String, Object>();
			List<String> cmdlist=new ArrayList<String>();
			cmdmap.put("action", "UPDATE_USER_INFO");
			cmdlist.add(empId);
			cmdmap.put("employeeIdCollection", cmdlist);
			HttpRequestFactory.sendRequet("http://192.168.0.119:8080/employee/commandGenerate", cmdmap);					
		}
		if(array.size() == 0){
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");			
		}
		return returnData;				
	}
	/**
	 * 把员工在职状态更新为（1：离职）
	 * @param employeeid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/batchUpdateStatus", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData batchUpdateStatus(@RequestBody String employeeId,HttpServletRequest request,HttpServletResponse response){															
			ReturnData returnData = new ReturnData();
			JSONObject obj = JSON.parseObject(employeeId);
			Map<String, Object> cmdmap=new HashMap<String, Object>();			
			try {
				List<String> cmdlist=new ArrayList<String>();
				cmdmap.put("action", "UPDATE_USER_INFO");
				employeeId=obj.getString("employeeId");
				System.err.println(employeeId);
				cmdlist.add(employeeId);
				cmdmap.put("employeeIdCollection", cmdlist);
				HttpRequestFactory.sendRequet("http://192.168.0.119:8080/employee/commandGenerate", cmdmap);
			} catch (Exception e) {
				System.err.println("设备模块不在线");
			}
			if(!employeeId.equals("")){			
				employeeService.batchUpdateStatus(employeeId);	
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");			
			}else{
				returnData.setMessage("数据请求失败");
				returnData.setReturnCode("3001");			
			}
			return returnData;
	}
	
		
}
