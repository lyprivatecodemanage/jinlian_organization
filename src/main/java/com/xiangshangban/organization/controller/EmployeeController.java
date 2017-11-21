package com.xiangshangban.organization.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger;
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
import com.xiangshangban.organization.service.ConnectEmpPostService;
import com.xiangshangban.organization.service.EmployeeService;
import com.xiangshangban.organization.service.PostService;
import com.xiangshangban.organization.service.TransferjobService;
import com.xiangshangban.organization.util.HttpRequestFactory;
import com.xiangshangban.organization.util.PropertiesUtils;
import com.xiangshangban.organization.util.RegexUtil;

@RestController
@RequestMapping("/EmployeeController")
public class EmployeeController {
	Logger logger = Logger.getLogger(EmployeeController.class);
	@Autowired
	EmployeeService employeeService;
	@Autowired
	ConnectEmpPostService connectEmpPostService;
	@Autowired
	PostService postService;
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
	/*@RequestMapping(value="/saveUserEmp", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
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
			//用户操作
			//加入注册表
			userService.saveUser(user);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");							
		}else{
			returnData.setMessage("参数格式错误");
			returnData.setReturnCode("3007");				
		}	
		return returnData;	
	}*/
	
	
	/**
	 *  
	 * 添加员工信息
	 * @param employee
	 * @param request
	 * @param response
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value="/insertEmployee", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData insertEmployee(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){								
		ReturnData returnData = new ReturnData();
		//获取请求头信息
		String companyId = request.getHeader("companyId");			
		String operateUserId = request.getHeader("accessUserId");
		
		Employee employeenew = JSON.parseObject(jsonString, Employee.class);
		employeenew.setOperateUserId(operateUserId);
		employeenew.setCompanyId(companyId);
		String employeeNo = employeenew.getEmployeeNo();
		if(StringUtils.isNotEmpty(employeeNo)){
			Employee employeeNotemp =employeeService.findByemployeeNo(employeeNo, companyId);
			if(employeeNotemp != null ){			
				returnData.setMessage("工号已存在");
				returnData.setReturnCode("4101");
				return returnData;			
			}
		}
		
		String loginName = employeenew.getLoginName();
		boolean loginNameMatch = RegexUtil.matchPhone(loginName);
		if(!loginNameMatch){
			returnData.setMessage("登录名格式必须为11位手机号");
			returnData.setReturnCode("4102");
			return returnData;
		}
		String employeeName = employeenew.getEmployeeName();
		if(StringUtils.isEmpty(employeeName)){
			returnData.setMessage("必传参数为空");
			returnData.setReturnCode("3006");
			return returnData;
		}
		
		if ((StringUtils.isNotEmpty(employeenew.getEntryTime()) &&!RegexUtil.matchDate(employeenew.getEntryTime())) 
				|| (StringUtils.isNotEmpty(employeenew.getProbationaryExpired())
						&& !RegexUtil.matchDate(employeenew.getProbationaryExpired()))) {				
			returnData.setMessage("日期格式错误（yyyy-MM-dd）");
			returnData.setReturnCode("3009");
			return returnData;
		}
		int result = employeeService.insertEmployee(employeenew);					
		returnData.setMessage("数据请求成功");
		returnData.setReturnCode("3000");
		return returnData;
	}
	/**
	 * 编辑员工信息
	 * @param jsonString
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/updateByEmployee", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public ReturnData updateByEmployee(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){		 		
		JSONObject jsonObject = JSONObject.parseObject(jsonString);	
		ReturnData returnData = new ReturnData();		
		String employeeId = jsonObject.getString("employeeId");
		if(StringUtils.isNotEmpty(employeeId)){
			Employee employeenew = JSON.parseObject(jsonString, Employee.class);
			//获取请求头信息
			String companyId = request.getHeader("companyId");
			String operateUserId = request.getHeader("accessUserId");
			employeenew.setOperateUserId(operateUserId);
			
			employeenew.setCompanyId(companyId);
			employeenew.setEmployeeId(employeeId);
			employeeService.updateByEmployee(employeenew);
			
			Transferjob transferjob = transferjobService.selectByTransferjobpost(employeeId, companyId);
			//上一次所在部门ID
			String departmentid = transferjob.getDepartmentId();
			//修改后传的部门id
			
			if(employeenew.getDepartmentId().equals(departmentid)){
				ConnectEmpPost EmpPost = new ConnectEmpPost();			
				EmpPost.setEmployeeId(employeeId);
				//原来所在部门ID
				EmpPost.setDepartmentId(departmentid);
				//员工所在部门修改后，也要修改调动表现在所在部门的员工部门
				transferjobService.updateByTrandepartmentId(employeenew.getDepartmentId());
			}
		    //把员工关联的岗位添加到connect_emp_post_中间表里面					    
			String postIdList = jsonObject.getString("postList");
			JSONArray postIdList1 = JSON.parseArray(postIdList);			
			for(int i =0;i<postIdList1.size();i++){
				JSONObject jobj = jsonObject.parseObject(postIdList1.getString(i)) ;
				String postId = jobj.getString("postId");
				String postGrades = jobj.getString("postGrades");
				ConnectEmpPost empPost = new ConnectEmpPost();
				ConnectEmpPost connect = new ConnectEmpPost();
				ConnectEmpPost connectemppos = connectEmpPostService.findByConnect(employeeId,employeenew.getDepartmentId(), postGrades);
				if(connectemppos==null){
					empPost.setEmployeeId(employeeId);
					empPost.setDepartmentId(employeenew.getDepartmentId());
					empPost.setPostGrades(postGrades);
					empPost.setPostId(postId);				          
				    connectEmpPostService.saveConnect(empPost);
				}
				if(connectemppos!=null){
					String postid = connectemppos.getPostId();
					if(!postId.equals("postid")){
						connect.setDepartmentId(employeenew.getDepartmentId());
						connect.setEmployeeId(employeeId);
						connect.setPostId(postid);
						connectEmpPostService.updatetpostGradespostStaus(connect);
						empPost.setEmployeeId(employeeId);
						empPost.setDepartmentId(employeenew.getDepartmentId());
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
			returnData.setMessage("必传参数为空");
			returnData.setReturnCode("3006");
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
		//获取请求头信息
		String companyId = request.getHeader("companyId");			
		String operateUserId = request.getHeader("accessUserId");
		
		Employee employeenew = new Employee();
		JSONObject obj = JSON.parseObject(jsonString);	
		String employeeNo = obj.getString("employeeNo");		
		Employee employeeNotemp =employeeService.findByemployeeNo(employeeNo, companyId);	
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
		boolean employeephone = RegexUtil.matchPhone(employeePhone);
		String employeeTwophone = obj.getString("employeeTwophone");
		boolean employeetwophone = RegexUtil.matchPhone(employeeTwophone);
		String directPersonId = obj.getString("directPersonId");
		String entryTime = obj.getString("entryTime");
		boolean entrytime = RegexUtil.matchDate(entryTime);
		String probationaryExpired = obj.getString("probationaryExpired");
		boolean probationaryexpired = RegexUtil.matchDate(probationaryExpired);
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
		    transferjob.setUserId(operateUserId);//操作人ID	
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
	 * 分页查询员工信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/selectByAllFnyeEmployee",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public Map<String,Object> selectByAllFnyeEmployee(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){		
		Map<String,Object> result = new HashMap<String,Object>();
		try{
		String companyId = request.getHeader("companyId");//公司id
		String userId = request.getHeader("userId");//操作人id
		JSONObject obj = JSON.parseObject(jsonString);			
		String employeeName = obj.getString("employeeName");//员工姓名
		String employeeSex = obj.getString("employeeSex");//员工性别
		String departmentName = obj.getString("departmentName");//部门名称
		String postName = obj.getString("postName");//岗位名称
		String employeeStatus = obj.getString("employeeStatus");//员工状态，0,在职，1,离职，2,删除
		
		String pageNum = obj.getString("pageNum");//页码
		String pageRecordNum = obj.getString("pageRecordNum");//页记录行数		
		boolean pageNumFlag = Pattern.matches("\\d{1,}", pageNum);
		boolean pageRecordNumFlag = Pattern.matches("\\d{1,}", pageRecordNum);
		if(StringUtils.isEmpty("pageNum") || StringUtils.isEmpty(pageRecordNum)){
			result.put("message", "参数不完整");
			result.put("returnCode", "4018");
			return result;
		}
		if(!pageNumFlag||!pageRecordNumFlag){
			result.put("message", "参数格式不正确");
			result.put("returnCode", "3007");			
			return result;
		}
		pageNum = String.valueOf((Integer.valueOf(pageNum)-1)*Integer.valueOf(pageRecordNum));
		if(!StringUtils.isEmpty(employeeName)){
			employeeName = "%"+employeeName+"%";
		}
		if(!StringUtils.isEmpty(employeeSex)){
			boolean employeeSexFlag = Pattern.matches("0|1", employeeSex);
			if(!employeeSexFlag){
				result.put("message", "参数格式不正确");
				result.put("returnCode", "3007");			
				return result;
			}
		}
		if(!StringUtils.isEmpty(departmentName)){
			departmentName = "%"+departmentName+"%";
		}
		if(!StringUtils.isEmpty(postName)){
			postName = "%"+postName+"%";
		}
		if(!StringUtils.isEmpty(employeeStatus)){
			boolean employeeStatusFlag = Pattern.matches("0|1", employeeStatus);
			if(!employeeStatusFlag){
				result.put("message", "参数格式不正确");
				result.put("returnCode", "3007");			
				return result;
			}
		}
		List<Employee> employeeList = employeeService.selectByAllFnyeEmployee(companyId,pageNum, pageRecordNum, employeeName, employeeSex, departmentName, postName, employeeStatus);
		result.put("data", employeeList);
		result.put("message","成功");
		result.put("returnCode","3000");
		return result;
		}catch(Exception e){
			e.printStackTrace();
			result.put("message", "服务器错误");
			result.put("returnCode", "3001");
			return result;
		}
}
	@RequestMapping("")
	public Map<String,Object> search(@RequestBody String jsonString,HttpServletRequest request){
		Map<String,Object> result = new HashMap<String,Object>();
		try{
			String companyId = request.getHeader("companyId");//公司id
			String userId = request.getHeader("userId");//操作人id
			JSONObject jsonObj = JSON.parseObject(jsonString);
			String employeeId = jsonObj.getString("employeeId");
			employeeService.selectByEmployee(employeeId, companyId);
			result.put("message","成功");
			result.put("returnCode","3000");
			return result;
		}catch(Exception e){
			e.printStackTrace();
			result.put("message", "服务器错误");
			result.put("returnCode", "3001");
			return result;
		}
		
		
		
	}
	
	/**
	 * 根据姓名、登录名、联系方式、性别、所属部门、岗位、入职时间、在职状态查询员工信息
	 * @param jsonString
	 * @param request
	 * @param response
	 * @return
	 */
	/*@RequestMapping(value = "/findByMoreEmployee",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public ReturnData findByMoreEmployee(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){						   		 	    		   
		    ReturnData returnData = new ReturnData();
		    Map<String,String> params = new HashMap<String, String>();
		    Map<String,String> param = new HashMap<String, String>();
			Map<String, Object> postnamelist=new HashMap<String, Object>();						
			JSONObject obj = JSON.parseObject(jsonString);			
			//获取请求头信息
			String companyId = request.getHeader("companyId");			
			params.put("companyId",companyId);
			String employeeName = obj.getString("employeeName");			
			String loginName = obj.getString("loginName");
			String employeePhone = obj.getString("employeePhone");				
			String employeeTwophone = obj.getString("employeeTwophone");		
			String employeeSex = obj.getString("employeeSex");			
			String departmentName = obj.getString("departmentName");
			String postName = obj.getString("postName");
			String entryTime = obj.getString("entryTime");
			String employeeStatus = obj.getString("employeeStatus");
			params.put("employeeSex",employeeSex);
			params.put("employeeName", employeeName);
			params.put("employeePhone", employeePhone);
			params.put("loginName", loginName);	
			params.put("employeeTwophone", employeeTwophone);
			params.put("departmentName", departmentName);
			params.put("postName", postName);
			params.put("entryTime",entryTime);
			params.put("employeeStatus", employeeStatus);
			//分页
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
			if(employeeName.equals("")&& employeeSex.equals("") && departmentName.equals("") && postName.equals("")&& employeePhone.equals("")&&loginName.equals("")&&entryTime.equals("")&& employeeTwophone.equals("") &&employeeStatus.equals("")){
					if (pageNum != null && pageNum != "" && pageRecordNum != null && pageRecordNum != "") {
						int number = (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageRecordNum);
						String strNum = String.valueOf(number);
						param.put("pageRecordNum", pageRecordNum);
						param.put("fromPageNum", strNum);
						param.put("companyId", companyId);
						List<Employee> employeelistemp =employeeService.selectByAllFnyeEmployee(param);
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
					}
					List<Employee> emplist = employeeService.selectByAllEmployee(companyId);
					int totalPages = emplist.size();
					double  pageCountnum =(double)totalPages/Integer.parseInt(pageRecordNum);	
					int pagecountnum=(int) Math.ceil(pageCountnum);
					returnData.setTotalPages(totalPages);
					returnData.setPagecountNum(pagecountnum);
					returnData.setData(postnamelist);
					returnData.setMessage("数据请求成功");
					returnData.setReturnCode("3000");		
				}else{		
					if (pageNum != null && pageNum != "" && pageRecordNum != null && pageRecordNum != "") {
						int number = (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageRecordNum);
						String strNum = String.valueOf(number);
						params.put("pageRecordNum", pageRecordNum);
						params.put("fromPageNum", strNum);
						params.put("companyId", companyId);
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
						int totalPages = employeelistemp.size();
						double  pageCountnum =(double)totalPages/Integer.parseInt(pageRecordNum);	
						int pagecountnum=(int) Math.ceil(pageCountnum);
						returnData.setTotalPages(totalPages);
						returnData.setPagecountNum(pagecountnum);
						returnData.setData(postnamelist);
						returnData.setMessage("数据请求成功");
						returnData.setReturnCode("3000");		
						
					}																	
			}
			return returnData;
		}*/

	
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
			returnData.setData(emplist);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");				
	}else{
		returnData.setMessage("数据请求失败");
		returnData.setReturnCode("3001");
	}
		return returnData;
}
	/**
	 * 根据人员姓名，所属部门，主岗位动态查询所有在职人员以及所属部门和主岗位 
	 * @param jsonString
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findBydynamicempadmin",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public ReturnData findBydynamicempadmin(@RequestBody String jsonString, HttpServletRequest request,HttpServletResponse response){				
		 ReturnData returnData = new ReturnData();		 
		 Map<String,String> params = new HashMap<String, String>();
		 Map<String,String> temp = new HashMap<String, String>();
		 Map<String,String> temps = new HashMap<String, String>();
			JSONObject obj = JSON.parseObject(jsonString);					
			//获取请求头信息
			String companyId = request.getHeader("companyId");			
			params.put("companyId",companyId);
			String employeeName = obj.getString("employeeName");
			String departmentName = obj.getString("departmentName");
			String postName = obj.getString("postName");			
			String pageNum = obj.getString("pageNum");
			String pageNumPattern = "\\d{1,}";
			boolean pageNumFlag = Pattern.matches(pageNumPattern, pageNum);			
			String pageRecordNum = obj.getString("pageRecordNum");
			boolean pageRecordNumFlag = Pattern.matches(pageNumPattern, pageRecordNum);				
			if(!pageNumFlag||!pageRecordNumFlag){
				returnData.setMessage("参数格式不正确");
				returnData.setReturnCode("3007");			
				return returnData;
		}					
			if(employeeName.equals("") && departmentName.equals("") && postName.equals("")){
				temps.put("companyId",companyId); 
		    	List<Employee> Employeelist = employeeService.findByempadmins(temps);
		    	if (pageNum != null && pageNum != "" && pageRecordNum != null && pageRecordNum != "") {
					int number = (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageRecordNum);
						String strNum = String.valueOf(number);
						temp.put("pageRecordNum", pageRecordNum);
						temp.put("fromPageNum", strNum);	
						temp.put("companyId",companyId);
						List<Employee> Emplist= employeeService.findByempadmin(temp);
				    	int totalPages = Employeelist.size();//数据总条数
						double  pageCountnum =(double)totalPages/Integer.parseInt(pageRecordNum);	
						int pagecountnum=(int) Math.ceil(pageCountnum);//总页数
						returnData.setTotalPages(totalPages);
						returnData.setPagecountNum(pagecountnum);
					    returnData.setData(Emplist);
						returnData.setMessage("数据请求成功");
						returnData.setReturnCode("3000");	
		    	}			
	    }else{
	    	if (pageNum != null && pageNum != "" && pageRecordNum != null && pageRecordNum != "") {
		    	int number = (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageRecordNum);
				String strNum = String.valueOf(number);
				params.put("employeeName", employeeName);
				params.put("departmentName", departmentName);
				params.put("postName", postName);
				params.put("pageRecordNum", pageRecordNum);
				params.put("fromPageNum", strNum);
				List<Employee> emplist = employeeService.findBydynamicempadmin(params);
				int totalPages = emplist.size();//数据总条数
				double  pageCountnum =(double)totalPages/Integer.parseInt(pageRecordNum);	
				int pagecountnum=(int) Math.ceil(pageCountnum);//总页数
				returnData.setTotalPages(totalPages);
				returnData.setPagecountNum(pagecountnum);
			    returnData.setData(emplist);
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");
		}	
	  }
	        return returnData;				
}
	
	
	
	/**
	 * 所有在职员工的信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findByAllEmployee",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public ReturnData findByAllEmployee(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){		
		ReturnData returnData = new ReturnData();
		Map<String, String> params = new HashMap<String, String>();
		Map<String, Object> postnamelist=new HashMap<String, Object>();		
		//获取请求头信息
		String companyId = request.getHeader("companyId");
		JSONObject obj = JSON.parseObject(jsonString);			
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
		if(!companyId.equals("")){
			if (pageNum != null && pageNum != "" && pageRecordNum != null && pageRecordNum != "") {
				int number = (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageRecordNum);
				String strNum = String.valueOf(number);
				params.put("pageRecordNum", pageRecordNum);
				params.put("fromPageNum", strNum);
				params.put("companyId", companyId);			
				List<Employee> employeelistemp =employeeService.findByAllEmployee(params);
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
			}																							
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
		}					
		return returnData;			
	}
	
	/**
	 * 所有离职员工的信息
	 * @param request
	 * @param response
	 * @return
	 */
	
	@RequestMapping(value = "/findByLiZhiemployee",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public ReturnData findByLiZhiemployee(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){		
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
			returnData.setData(postnamelist);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");								
		    return returnData;
		 
	}
	
	
	
	/**
	 * 查询申请入职的人员信息 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findByruzhiempinfo",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public ReturnData findByruzhiempinfo(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){		
		ReturnData returnData = new ReturnData();
		//获取请求头信息			
		String companyId = request.getHeader("companyId");
		if(!companyId.equals("")){
			List<Employee> employeelist = employeeService.findByruzhiempinfo(companyId);			
				returnData.setData(employeelist);
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");			 
		}else{
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
		}		
		return returnData;		  
	}
	
	
	
	/**
	 * 查询单条员工信息
	 * @param employeeId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/selectByEmployee",produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public ReturnData selectByEmployee(@RequestBody String jsonString,HttpServletRequest request,HttpServletResponse response){									
		JSONObject obj = JSON.parseObject(jsonString);
		ReturnData returnData = new ReturnData();
		String employeeId = obj.getString("employeeId");	
		String companyId = obj.getString("companyId");	
		if(StringUtils.isEmpty(employeeId)){
			companyId = request.getHeader("companyId");
		}			
         if(StringUtils.isNotEmpty(employeeId) && StringUtils.isNotEmpty(employeeId)){ 
        	 Employee emp =employeeService.selectByEmployee(employeeId,companyId);	
        	 String departmentId = emp.getDepartmentId();
             List<Post> PostNamelist = postService.selectByPostName(employeeId,departmentId);
             emp.setPostList(PostNamelist);
             returnData.setData(emp);
             returnData.setMessage("数据请求成功");
			 returnData.setReturnCode("3000");	
         }else{      	 
	        	returnData.setMessage("必传参数为空");
				returnData.setReturnCode("3006");				
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
		try {
			HttpRequestFactory.sendRequet(PropertiesUtils.pathUrl("commandGenerate"), cmdmap);
		} catch (IOException e) {
			logger.info("将人员信息更新到设备模块时，获取路径出错");
			e.printStackTrace();
		}	
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
		try {
			HttpRequestFactory.sendRequet(PropertiesUtils.pathUrl("commandGenerate"), cmdmap);
		} catch (IOException e) {
			logger.info("将人员信息更新到设备模块时，获取路径出错");
			e.printStackTrace();
		}	
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
			try {
				HttpRequestFactory.sendRequet(PropertiesUtils.pathUrl("commandGenerate"), cmdmap);
			} catch (IOException e) {
				logger.info("将人员信息更新到设备模块时，获取路径出错");
				e.printStackTrace();
			}					
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
				try {
					HttpRequestFactory.sendRequet(PropertiesUtils.pathUrl("commandGenerate"), cmdmap);
				} catch (IOException e) {
					logger.info("将人员信息更新到设备模块时，获取路径出错");
					e.printStackTrace();
				}	
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
