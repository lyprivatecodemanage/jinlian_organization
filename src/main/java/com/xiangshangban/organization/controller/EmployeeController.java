package com.xiangshangban.organization.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.xiangshangban.organization.bean.Company;
import com.xiangshangban.organization.bean.ConnectEmpPost;
import com.xiangshangban.organization.bean.Department;
import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.bean.Transferjob;
import com.xiangshangban.organization.bean.Uusers;
import com.xiangshangban.organization.bean.UusersRoles;
import com.xiangshangban.organization.service.CompanyService;
import com.xiangshangban.organization.service.ConnectEmpPostService;
import com.xiangshangban.organization.service.DepartmentService;
import com.xiangshangban.organization.service.EmployeeService;
import com.xiangshangban.organization.service.EmployeeSpeedImportService;
import com.xiangshangban.organization.service.OSSFileService;
import com.xiangshangban.organization.service.PostService;
import com.xiangshangban.organization.service.TransferjobService;
import com.xiangshangban.organization.util.FormatUtil;
import com.xiangshangban.organization.util.HttpRequestFactory;
import com.xiangshangban.organization.util.PropertiesUtils;
import com.xiangshangban.organization.util.RegexUtil;

@RestController
@RequestMapping("/EmployeeController")
public class EmployeeController {
	Logger logger = Logger.getLogger(EmployeeController.class);
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private ConnectEmpPostService connectEmpPostService;
	@Autowired
	private PostService postService;
	@Autowired
	private TransferjobService transferjobService;
	@Autowired
	private OSSFileService oSSFileService;
	@Autowired
	private EmployeeSpeedImportService employeeSpeedImportService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private DepartmentService departmentService;
	
	public Map<String,Object> newInsertEmployee(@RequestBody String jsonString,HttpServletRequest request){
		Map<String,Object> result = new HashMap<String,Object>();
		try{
			String companyId = request.getHeader("header");
			
			return result;
		}catch(Exception e){
			logger.info(e);
			result.put("message", "服务器错误");
			result.put("returnCode", "3001");
			return result;
		}
	}
	
	
	
	
	/**
	 * 激活
	 * 
	 * @param jsonString
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value = "/activeEmployee", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public ReturnData activeEmp(@RequestBody String jsonString, HttpServletRequest request,
			HttpServletResponse response) {
		ReturnData returnData = new ReturnData();
		// 获取请求头信息
		String companyId = request.getHeader("companyId");
		// String operateUserId = request.getHeader("accessUserId");
		JSONObject obj = JSON.parseObject(jsonString);
		String employeeId = obj.getString("employeeId");
		//String loginName = obj.getString("loginName");
		if (StringUtils.isEmpty(employeeId)) {
			returnData.setMessage("必传参数为空");
			returnData.setReturnCode("3006");
			return returnData;
		}
		employeeService.activeEmp(companyId, employeeId);
		employeeService.resetEmployeeStatus(companyId, employeeId);
		returnData.setMessage("数据请求成功");
		returnData.setReturnCode("3000");
		return returnData;
	}

	/**
	 * 
	 * 添加员工信息
	 * 
	 * @param employee
	 * @param request
	 * @param response
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/insertEmployee", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public ReturnData insertEmployee(@RequestBody String jsonString, HttpServletRequest request,
			HttpServletResponse response) {
		ReturnData returnData = new ReturnData();
		// 获取请求头信息
		String companyId = request.getHeader("companyId");
		String operateUserId = request.getHeader("accessUserId");

		Employee employeenew = JSON.parseObject(jsonString, Employee.class);
		employeenew.setOperateUserId(operateUserId);
		employeenew.setCompanyId(companyId);
		String employeeNo = employeenew.getEmployeeNo();
		if (StringUtils.isNotEmpty(employeeNo)) {
			Employee employeeNotemp = employeeService.findByemployeeNo(employeeNo, companyId);
			if (employeeNotemp != null) {
				returnData.setMessage("工号已存在");
				returnData.setReturnCode("4101");
				return returnData;
			}
		}
		String loginName = employeenew.getLoginName();
		boolean loginNameMatch = RegexUtil.matchPhone(loginName);
		if (!loginNameMatch) {
			returnData.setMessage("登录名格式必须为11位手机号");
			returnData.setReturnCode("4102");
			return returnData;
		}
		String employeeName = employeenew.getEmployeeName();
		if (StringUtils.isEmpty(employeeName)) {
			returnData.setMessage("必传参数为空");
			returnData.setReturnCode("3006");
			return returnData;
		}

		if ((StringUtils.isNotEmpty(employeenew.getEntryTime()) && !RegexUtil.matchDate(employeenew.getEntryTime()))
				|| (StringUtils.isNotEmpty(employeenew.getProbationaryExpired())
						&& !RegexUtil.matchDate(employeenew.getProbationaryExpired()))) {
			returnData.setMessage("日期格式错误（yyyy-MM-dd）");
			returnData.setReturnCode("3009");
			return returnData;
		}
		//判断三个岗位是否重复
		List<Post> postList = employeenew.getPostList();
		List<String> postString = new ArrayList<String>();
		boolean masterPostFlag = true;
		if(postList!=null && postList.size()>0){
			for( Post post:postList){
				if("1".equals(post.getPostGrades())){
					employeenew.setDepartmentId(post.getDepartmentId());
					masterPostFlag=false;
				}
				if(StringUtils.isNotEmpty(post.getPostId())){
					if(postString.contains(post.getPostId())){
						returnData.setMessage("岗位不可相同");
						returnData.setReturnCode("4119");
						return returnData;
					}
				}
				postString.add(post.getPostId());
			}
			/*if(masterPostFlag){
				returnData.setMessage("必传参数为空");
				returnData.setReturnCode("3006");
				return returnData;
			}*/
		}else{
			returnData.setMessage("必传参数为空");
			returnData.setReturnCode("3006");
			return returnData;
		}
		returnData = employeeService.insertEmployee(employeenew);
		// String operateUserId = request.getHeader("accessUserId");
		if(StringUtils.isEmpty(returnData.getMessage()) && StringUtils.isEmpty(returnData.getReturnCode())){
		String employeeId = returnData.getEmployeeId();
		//String loginName = obj.getString("loginName");
		
		employeeService.activeEmp(companyId, employeeId);
		employeeService.resetEmployeeStatus(companyId, employeeId);
		}else{
			return returnData;
		}
		returnData.setMessage("数据请求成功");
		returnData.setReturnCode("3000");
		return returnData;
	}

	/**
	 * 完善申请入职员工的入职信息
	 * 
	 * @param jsonString
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ImproveEmployeeOrientation", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public ReturnData ImproveEmployeeOrientation(@RequestBody String jsonString, HttpServletRequest request,
			HttpServletResponse response) {
		ReturnData returnData = new ReturnData();
		// 获取请求头信息
		String companyId = request.getHeader("companyId");
		String operateUserId = request.getHeader("accessUserId");

		Employee employeenew = new Employee();
		JSONObject obj = JSON.parseObject(jsonString);
		String employeeNo = obj.getString("employeeNo");
		Employee employeeNotemp = employeeService.findByemployeeNo(employeeNo, companyId);
		if (employeeNotemp != null) {
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
		employeenew.setLoginName(loginName);// 登录名
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
		// 添加员工信息的同时把员工信息添加到调动表里
		Transferjob transferjob = new Transferjob();
		transferjob.setEmployeeId(employeeId);
		transferjob.setDepartmentId(departmentId);
		transferjob.setTransferBeginTime(entryTime);
		transferjob.setTransferJobCause("新员工入职该岗位");
		transferjob.setUserId(operateUserId);// 操作人ID
		transferjob.setCompanyId(companyId);
		transferjobService.insertTransferjob(transferjob);
		// 把员工关联的岗位添加到connect_emp_post_中间表里面
		String postIdList = obj.getString("postList");
		JSONArray postIdList1 = JSON.parseArray(postIdList);
		for (int i = 0; i < postIdList1.size(); i++) {
			JSONObject jobj = JSONObject.parseObject(postIdList1.getString(i));
			String postId = jobj.getString("postId");
			String postGrades = jobj.getString("postGrades");
			ConnectEmpPost empPost = new ConnectEmpPost();
			ConnectEmpPost connect = new ConnectEmpPost();
			ConnectEmpPost connectemppos = connectEmpPostService.findByConnect(employeeId, departmentId, postGrades,companyId);
			if (connectemppos == null) {
				empPost.setEmployeeId(employeeId);
				empPost.setDepartmentId(departmentId);
				empPost.setPostGrades(postGrades);
				empPost.setPostId(postId);
				connectEmpPostService.saveConnect(empPost);
			}
			if (connectemppos != null) {
				String postid = connectemppos.getPostId();
				if (!postId.equals("postid")) {
					connect.setDepartmentId(departmentId);
					connect.setEmployeeId(employeeId);
					connect.setPostId(postid);
					connectEmpPostService.updatetpostGradespostStaus(connect);
					empPost.setEmployeeId(employeeId);
					empPost.setDepartmentId(departmentId);
					empPost.setPostGrades(postGrades);
					empPost.setPostId(postId);
					connectEmpPostService.saveConnect(empPost);
				}
				continue;
			}
		}
		if (!employeeId.equals("")) {
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
		} else {
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
		}
		return returnData;
	}

	/**
	 * @author 李业/分页条件查询员工信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/selectByAllFnyeEmployee", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public Map<String, Object> selectByAllFnyeEmployee(@RequestBody String jsonString, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String companyId = request.getHeader("companyId");// 公司id
			String type = request.getHeader("type");
			//String userId = request.getHeader("accessUserId");// 操作人id
			JSONObject obj = JSON.parseObject(jsonString);
			String employeeName = obj.getString("employeeName");// 员工姓名
			String employeeSex = obj.getString("employeeSex");// 员工性别
			String departmentName = obj.getString("departmentName");// 部门名称
			String postName = obj.getString("postName");// 岗位名称
			String employeeStatus = obj.getString("employeeStatus");// 员工状态，0,在职，1,离职，2,删除
			String departmentId = obj.getString("departmentId");// 部门id
			
			
			String pageNum = obj.getString("pageNum");// 页码
			String pageRecordNum = obj.getString("pageRecordNum");// 页记录行数
			
			boolean pageNumFlag = Pattern.matches("\\d{1,}", pageNum);
			boolean pageRecordNumFlag = Pattern.matches("\\d{1,}", pageRecordNum);
			if (StringUtils.isEmpty("pageNum") || StringUtils.isEmpty(pageRecordNum)) {
				result.put("message", "参数不完整");
				result.put("returnCode", "4018");
				return result;
			}
			if (!pageNumFlag || !pageRecordNumFlag) {
				result.put("message", "参数格式不正确");
				result.put("returnCode", "3007");
				return result;
			}
			pageNum = String.valueOf((Integer.valueOf(pageNum) - 1) * Integer.valueOf(pageRecordNum));
			
			
			if (!StringUtils.isEmpty(employeeName)) {
				employeeName = "%" + employeeName + "%";
			}
			if (!StringUtils.isEmpty(employeeSex)) {
				boolean employeeSexFlag = Pattern.matches("0|1", employeeSex);
				if (!employeeSexFlag) {
					result.put("message", "参数格式不正确");
					result.put("returnCode", "3007");
					return result;
				}
			}
			if (!StringUtils.isEmpty(departmentName)) {
				departmentName = "%" + departmentName + "%";
			}
			if (!StringUtils.isEmpty(postName)) {
				postName = "%" + postName + "%";
			}
			if (!StringUtils.isEmpty(employeeStatus)) {
				boolean employeeStatusFlag = Pattern.matches("0|1", employeeStatus);
				if (!employeeStatusFlag) {
					result.put("message", "参数格式不正确");
					result.put("returnCode", "3007");
					return result;
				}
			}
			
			Department department = departmentService.selectByDepartment(departmentId, companyId);
			if(department!=null && StringUtils.isNotEmpty(department.getDepartmentParentId())){
				if("0".equals(department.getDepartmentParentId())){
					departmentId ="";
				}
			}
			List<Employee> employeeList = employeeService.selectByAllFnyeEmployee(companyId, pageNum, pageRecordNum,
					employeeName, employeeSex, departmentName, postName, employeeStatus, departmentId,type);
				for(Employee emp:employeeList){
					if(emp!=null){
						if("0".equals(emp.getEmployeeSex())){
							emp.setEmployeeSex("男");
						}else if("1".equals(emp.getEmployeeSex())){
							emp.setEmployeeSex("女");
						}
						if("0".equals(emp.getEmployeeStatus())){
							emp.setEmployeeStatus("在职");
						}else if("1".equals(emp.getEmployeeStatus())){
							emp.setEmployeeStatus("离职");
						}else if("2".equals(emp.getEmployeeStatus())){
							emp.setEmployeeStatus("删除");
						}
					}
				}
			// 查询总记录数
			int intCount = employeeService.selectCountEmployeeFromCompany(companyId, /*pageNum, pageRecordNum,*/
					employeeName, employeeSex, departmentName, postName, employeeStatus, departmentId,type);
			String count = String.valueOf(intCount);
			// 总页数
			int intpagecountNum = (int) Math.ceil((double) intCount / (Double.valueOf(pageRecordNum)));
			String pagecountNum = String.valueOf(intpagecountNum);
			result.put("totalPages", count);
			result.put("pagecountNum", pagecountNum);
			result.put("data", employeeList);
			result.put("message", "成功");
			result.put("returnCode", "3000");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("message", "服务器错误");
			result.put("returnCode", "3001");
			return result;
		}
	}

	/**
	 * @author 李业/查询人员信息
	 * 
	 * @param jsonString
	 * @param request
	 * @return
	 */
	@RequestMapping("/search")
	public Map<String, Object> search(@RequestBody String jsonString, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String companyId = request.getHeader("companyId");// 公司id
			//String userId = request.getHeader("accessUserId");// 操作人id
			JSONObject jsonObj = JSON.parseObject(jsonString);
			String employeeId = jsonObj.getString("employeeId");
			Employee emp = employeeService.selectByEmployee(employeeId, companyId);
			if (emp != null) {
				List<Post> postList = postService.selectVicePositionByEmployeeId(companyId, employeeId);
				if (postList.size() > 0) {
					emp.setPostList(postList);
				}
				if("0".equals(emp.getEmployeeSex())){
					emp.setEmployeeSex("男");
				}else if("1".equals(emp.getEmployeeSex())){
					emp.setEmployeeSex("女");
				}
				if("0".equals(emp.getEmployeeStatus())){
					emp.setEmployeeStatus("在职");
				}else if("1".equals(emp.getEmployeeStatus())){
					emp.setEmployeeStatus("离职");
				}else if("2".equals(emp.getEmployeeStatus())){
					emp.setEmployeeStatus("删除");
				}
				if("0".equals(emp.getMarriageStatus())){
					emp.setMarriageStatus("未婚");
				}else if("1".equals(emp.getMarriageStatus())){
					emp.setMarriageStatus("已婚");
				}else if("2".equals(emp.getMarriageStatus())){
					emp.setMarriageStatus("离异");
				}
			}
			result.put("result", emp);
			result.put("message", "成功");
			result.put("returnCode", "3000");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("message", "服务器错误");
			result.put("returnCode", "3001");
			return result;
		}

	}
	/**
	 * @author 李业/保存app上传的员工头像key
	 * @param key
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/savePersonImageUrl",method=RequestMethod.POST)
	public Map<String,Object>  savePersonImageUrl(@RequestBody String key,HttpServletRequest request){
		Map<String,Object> result = new HashMap<String,Object>();
		try{
			String companyId = request.getHeader("companyId");//公司id
			String userId = request.getHeader("accessUserId");//操作人id
			employeeService.updateEmployeeImgUrl(userId,companyId,JSON.parseObject(key).getString("key"));
			result.put("message", "成功");
			result.put("returnCode", "3000");
			return result;
		}catch(Exception e){
			result.put("message", "服务器错误");
			result.put("returnCode", "3001");
			logger.info(e);
			return result;
		}
		
	}

	/**
	 * @author 李业/app查询人员信息
	 * @param jsonString
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/appSearch", method = RequestMethod.POST)
	public Map<String, Object> appSearch(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String companyId = request.getHeader("companyId");// 公司id
			String userId = request.getHeader("accessUserId");// 操作人id
			String type = request.getHeader("type");
			Employee emp = employeeService.selectByEmployeeFromApp(companyId, userId,type);
			
			String key = emp.getEmployeeImgUrl();
			String companyNo = emp.getCompanyNo();
			String directory = PropertiesUtils.ossProperty("portraitDirectory");
			String url = oSSFileService.getPathByKey(companyNo, directory, key);
			emp.setEmployeeImgUrl(url);
			result.put("result", emp);
			result.put("message", "成功");
			result.put("returnCode", "3000");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("message", "服务器错误");
			result.put("returnCode", "3001");
			return result;
		}

	}

	/**
	 * @author 李业:人员信息删除
	 * @param jsonStrng
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteActivity", method = RequestMethod.POST)
	public Map<String, Object> deleteActivity(@RequestBody String jsonStrng, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String companyId = request.getHeader("companyId");// 公司id
			String userId = request.getHeader("accessUserId");// 操作人id
			JSONArray jsonArray = JSON.parseObject(jsonStrng).getJSONArray("deleteData");
			for (int i = 0; i < jsonArray.size(); i++) {
				String employeeId = JSON.parseObject(jsonArray.get(i).toString()).getString("employeeId");
				//检查人员是否为管理员
				int isAdmin = employeeService.isAdmin(companyId, employeeId);
				if(isAdmin>0){
					Employee emp = employeeService.selectByEmployee(employeeId, companyId);
					result.put("message", "删除人员失败，原因：【"+emp.getEmployeeName()+"】为管理员");
					result.put("returnCode", "4118");
					return result;
				}
			}
			for (int i = 0; i < jsonArray.size(); i++) {
				String employeeId = JSON.parseObject(jsonArray.get(i).toString()).getString("employeeId");
				Employee emp = employeeService.selectByEmployee(employeeId, companyId);
				String departmentId = emp.getDepartmentId();
				String postId = emp.getPostId();
				int num = employeeService.deleteByEmployee(companyId, employeeId);
				if (num < 2) {
					result.put("message", "人员删除错误");
					result.put("returnCode", "4113");
					return result;
				}
				// 岗位移动表添加岗位离职时间
				transferjobService.updateTransferEndTimeWhereDeleteEmployee(companyId, userId, employeeId, departmentId,
						postId);
			}
			result.put("message", "成功");
			result.put("returnCode", "3000");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
			result.put("message", "服务器错误");
			result.put("returnCode", "3001");
			return result;
		}
	}

	/**
	 * @author 李业:人员信息编辑
	 * @param jsonString
	 * @param request
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/updateEmployeeInformation", method = RequestMethod.POST)
	public Map<String, Object> updateEmployeeInformation(@RequestBody String jsonString, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		//Map<String, String> params = new HashMap<String, String>();
		Employee emp = JSON.parseObject(jsonString, Employee.class);
		try {
			System.out.println(jsonString);
			String companyId = request.getHeader("companyId");// 公司id
			String userId = request.getHeader("accessUserId");// 操作人id
			emp.setCompanyId(companyId);
			String loginName = emp.getLoginName();
			boolean loginNameMatch = RegexUtil.matchPhone(loginName);
			if (!loginNameMatch) {
				result.put("message", "登录名格式必须为11位手机号");
				result.put("returnCode", "4102");
				return result;
			}
			//查詢編輯的人員信息是否存在
			Employee oldEmp = employeeService.selectByEmployee(emp.getEmployeeId(), companyId);
			if(oldEmp==null){
				result.put("message", "编辑的人员id不存在");
				result.put("returnCode", "");
				return result;
			}
			if(StringUtils.isNotEmpty(emp.getEmployeeNo()) && !emp.getEmployeeNo().equals(oldEmp.getEmployeeNo())){
				List<Employee> empList = employeeService.findByAllEmployee(companyId);
				if(empList !=null && empList.size()>0){
					for(Employee employee : empList){
						if(StringUtils.isNotEmpty(employee.getEmployeeNo())){
							if(employee.getEmployeeNo().equals(emp.getEmployeeNo())){
								result.put("message", "工号已存在");
								result.put("returnCode", "4101");
								return result;
							}
						}
					}
				}
			}
			List<Post> postList = emp.getPostList();
			if(postList!=null && postList.size()>0){
				for(Post post:postList){
					if("1".equals(post.getPostGrades()) && StringUtils.isNotEmpty(post.getDepartmentId())){
						emp.setDepartmentId(post.getDepartmentId());
						if(StringUtils.isNotEmpty(post.getPostId())){
							emp.setPostId(post.getPostId());
						}
					}
				}
			}else{
				result.put("message", "必传参数为空");
				result.put("returnCode", "3006");
				return result;
			}
			if (StringUtils.isEmpty(emp.getEmployeeName()) || StringUtils.isEmpty(emp.getLoginName()) ||StringUtils.isEmpty(emp.getDepartmentId())
					/*|| StringUtils.isEmpty(emp.getEntryTime()) || StringUtils.isEmpty(emp.getProbationaryExpired()) 
					 || StringUtils.isEmpty(emp.getWorkAddress())*/) {
				result.put("message", "必传参数为空");
				result.put("returnCode", "3006");
				return result;
			}
			/*更换登录手机号*/
			if(!oldEmp.getLoginName().equals(emp.getLoginName())){
				/*检查是否是管理员身份*/
				List<UusersRoles> userRoles = employeeService.selectRoleIdByEmployeeId(emp.getEmployeeId());
				if(userRoles!=null && userRoles.size()>0){
					for(UusersRoles userRole : userRoles){
						//判断是否是管理员
						if(userRole!=null && StringUtils.isNotEmpty(userRole.getRoleId()) && "56b814c2ea7e4f679ee505036f5b030a".equals(userRole.getRoleId())){
							//判断是否是本公司的管理员
							if(StringUtils.isNotEmpty(userRole.getCompanyId()) && companyId.equals(userRole.getCompanyId())){
								//是
								result.put("flag", "1");
								break;
							}else{
								result.put("message", "该员工在其他公司具有管理员身份,无权限修改登录名");
								result.put("returnCode", "4121");
								return result;
							}
						}else{
							result.put("flag", "0");
						}
					}
				}
				employeeService.updateLoginNameByEmployeeId(emp.getLoginName(), emp.getEmployeeId());
				Uusers user = employeeService.selectByPhoneAndStatus(emp.getLoginName());
				if(user ==null || (StringUtils.isNotEmpty(user.getUserid()) && user.getUserid().equals(emp.getEmployeeId()) && !user.getPhone().equals(emp.getLoginName()))){
					employeeService.updatePhoneByUserId(emp.getLoginName(), emp.getEmployeeId());
				}else{
					result.put("message", "登录名已被占用");
					result.put("returnCode", "4115");
					return result;
				}
			}
			//新完善信息
			employeeService.updateEmployeeInformation(emp);
			if(StringUtils.isNotEmpty(emp.getPostId())){
				//查询员工岗位部门关联表
				ConnectEmpPost connect =  connectEmpPostService.selectEmployeePostInformation(emp.getEmployeeId(), companyId);
				if (connect != null && !emp.getPostId().equals(connect.getPostId())) {
					connectEmpPostService.updateEmployeeWithPost(emp.getEmployeeId(), 
							emp.getDepartmentId(), emp.getPostId(),companyId);
	
					// 添加更换之前主岗位的换岗时间(transferEndTime)
					transferjobService.updateTransferEndTimeWhereDeleteEmployee(
							companyId, userId, emp.getEmployeeId(), emp.getDepartmentId(), connect.getPostId());
				}
				if(connect==null){
					connectEmpPostService.deleteEmployeeFromPost(emp.getEmployeeId(), emp.getDepartmentId(),companyId);
					connect = new ConnectEmpPost();
					connect.setEmployeeId(emp.getEmployeeId());
					connect.setDepartmentId(emp.getDepartmentId());
					connect.setPostId(emp.getPostId());
					connect.setPostGrades("1");
					connect.setIsDelete("0");
					connect.setCompanyId(companyId);
					List<ConnectEmpPost> newConnectEmpPost = new ArrayList<ConnectEmpPost>();
					newConnectEmpPost.add(connect);
					connectEmpPostService.insertEmployeeWithPost(newConnectEmpPost);
				}
				// 添加新岗位的记录
				Transferjob transferjob = new Transferjob(FormatUtil.createUuid(), emp.getEmployeeId(), null, emp.getDepartmentId(),
						emp.getTransferJobCause(), null, userId, null, companyId, emp.getDirectPersonId(), emp.getPostId());
	
				transferjobService.insertTransferjob(transferjob);
			}
			connectEmpPostService.deleteEmployeeWithPost(emp.getEmployeeId(), companyId);
			List<ConnectEmpPost> list = new ArrayList<ConnectEmpPost>();
			List<Post> vicePostList = new ArrayList<Post>();
			for(Post post:postList){
				if("0".equals(post.getPostGrades())){
					vicePostList.add(post);
				}
			}
			if(vicePostList!=null && vicePostList.size()>0){
				if (StringUtils.isNotEmpty(vicePostList.get(0).getPostId())) {
					if(vicePostList.get(0).getPostId().equals(emp.getPostId())){
						result.put("message","副岗位不能与主岗位相同");
						result.put("returnCode","4120");
						return result;
					}
				}
			}
			if(vicePostList!=null && vicePostList.size()>1){
				if(StringUtils.isNotEmpty(vicePostList.get(0).getPostId())&&StringUtils.isNotEmpty(vicePostList.get(1).getPostId())){
					if(vicePostList.get(0).getPostId().equals(vicePostList.get(1).getPostId())){
						result.put("message","不能选择两个相同的副岗位");
						result.put("returnCode","4119");
						return result;
					}
					if(vicePostList.get(0).getPostId().equals(emp.getPostId()) || vicePostList.get(1).getPostId().equals(emp.getPostId())){
						result.put("message","副岗位不能与主岗位相同");
						result.put("returnCode","4120");
						return result;
					}
				}
				
				if (StringUtils.isNotEmpty(vicePostList.get(1).getPostId())) {
					if(vicePostList.get(1).getPostId().equals(emp.getPostId())){
						result.put("message","副岗位不能与主岗位相同");
						result.put("returnCode","4120");
						return result;
					}
				}
			}
			for (Post post: vicePostList) {
				ConnectEmpPost connectEmpPost = new ConnectEmpPost();
				connectEmpPost.setEmployeeId(emp.getEmployeeId());
				connectEmpPost.setDepartmentId(post.getDepartmentId());
				connectEmpPost.setPostGrades("0");
				connectEmpPost.setIsDelete("0");
				connectEmpPost.setCompanyId(companyId);
				String vPostId = post.getPostId();
				if (StringUtils.isNotEmpty(vPostId)) {
					connectEmpPost.setPostId(vPostId);
					list.add(connectEmpPost);
				}
			}
			if (list.size() > 0) {
				connectEmpPostService.insertEmployeeWithPost(list);
			}
			emp.setCompanyId(companyId);
			employeeService.updateEmployeeInfoStatus(emp);
			
			
			result.put("message", "成功");
			result.put("returnCode", "3000");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
			result.put("message", "服务器错误");
			result.put("returnCode", "3001");
			return result;
		}
	}

	/**
	 * @author 李业:人员信息导入
	 * @param key
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/speedImport", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public ReturnData speedImport(@RequestBody String jsonString, HttpServletRequest request) {
		JSONObject obj = JSON.parseObject(jsonString);
		String key = obj.getString("key");
		ReturnData returnData = new ReturnData();
		try {
			String companyId = request.getHeader("companyId");// 公司id
			String userId = request.getHeader("accessUserId");// 操作人id
			//Employee emp = employeeService.selectByEmployee(userId,companyId);
			Company company = companyService.selectByCompany(companyId);
			String companyNo = company.getCompanyNo();
			String directory = PropertiesUtils.ossProperty("employeeImportDirectory");
			String filePath = oSSFileService.getImportPathByKey(companyNo, directory, key);
			//String filePath = "http://xiangshangban.oss-cn-hangzhou.aliyuncs.com/test/data/20171124shbf001/EmployeeExcel/employeeModelOne.xlsx";
			
			returnData = employeeSpeedImportService.speedImport(userId, companyId, filePath);
			return returnData;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
			returnData.setMessage("服务器错误");
			returnData.setReturnCode("3001");
			return returnData;
		}
	}
	@RequestMapping(value = "/export/employeeInfo")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response){
		try {
			response.setContentType("octets/stream"); 
			String agent = request.getHeader("USER-AGENT");
			String excelName = "employee.xls";
			if(agent!=null && agent.indexOf("MSIE")==-1&&agent.indexOf("rv:11")==-1 && 
					agent.indexOf("Edge")==-1 && agent.indexOf("Apache-HttpClient")==-1){//非IE
				excelName = new String(excelName.getBytes("UTF-8"), "ISO-8859-1");
				response.addHeader("Content-Disposition", "attachment;filename="+excelName);
			}else{
				response.addHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(excelName,"UTF-8"));  	
			}
			response.addHeader("excelName", java.net.URLEncoder.encode(excelName,"UTF-8"));
			OutputStream out = response.getOutputStream();
			// 获取请求头信息
			String companyId = request.getHeader("companyId");
			employeeService.export(excelName, out, companyId);  
			out.flush();  
		} catch (IOException e) {
			System.out.println("导出文件输出流出错了！"+e);
		}
	}
	/**
	 * 查询一个岗位下的所有员工
	 */
	@RequestMapping(value = "/findByposcounttemp", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public ReturnData findByposcounttemp(@RequestBody String postId, HttpServletRequest request,
			HttpServletResponse response) {
		ReturnData returnData = new ReturnData();
		// String companyId="977ACD3022C24B99AC9586CC50A8F786";
		// 获取请求头信息
		String companyId = request.getHeader("companyId");
		if (!postId.equals("")) {
			List<Employee> emplist = employeeService.findByposcounttemp(postId, companyId);
			returnData.setData(emplist);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
		} else {
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
		}
		return returnData;
	}

	/**
	 * 所有在职员工的信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findByAllEmployee", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public ReturnData findByAllEmployee(@RequestBody String jsonString, HttpServletRequest request,
			HttpServletResponse response) {
		ReturnData returnData = new ReturnData();
		Map<String, String> params = new HashMap<String, String>();
		Map<String, Object> postnamelist = new HashMap<String, Object>();
		// 获取请求头信息
		String companyId = request.getHeader("companyId");
		JSONObject obj = JSON.parseObject(jsonString);
		String pageNum = obj.getString("pageNum");
		String pageRecordNum = obj.getString("pageRecordNum");
		String pageNumPattern = "\\d{1,}";
		boolean pageNumFlag = Pattern.matches(pageNumPattern, pageNum);
		boolean pageRecordNumFlag = Pattern.matches(pageNumPattern, pageRecordNum);
		if (!pageNumFlag || !pageRecordNumFlag) {
			returnData.setMessage("参数格式不正确");
			returnData.setReturnCode("3007");
			return returnData;
		}
		if (!companyId.equals("")) {
			if (pageNum != null && pageNum != "" && pageRecordNum != null && pageRecordNum != "") {
				int number = (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageRecordNum);
				String strNum = String.valueOf(number);
				params.put("pageRecordNum", pageRecordNum);
				params.put("fromPageNum", strNum);
				params.put("companyId", companyId);
				List<Employee> employeelistemp = employeeService.findByAllEmployee(params);
				int s = 0;
				for (int i = 0; i < employeelistemp.size(); i++) {
					s = s + 1;
					String temps = String.valueOf(s);
					Employee employeelist = employeelistemp.get(i);
					String Employeeid = employeelist.getEmployeeId();
					String departmentId = employeelist.getDepartmentId();
					List<Post> PostNamelist = postService.selectByPostName(Employeeid, departmentId);
					employeelist.setPostList(PostNamelist);
					postnamelist.put("employeelist" + temps, employeelist);
				}
				returnData.setData(postnamelist);
				returnData.setMessage("数据请求成功");
				returnData.setReturnCode("3000");
			}
		} else {
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
		}
		return returnData;
	}

	/**
	 * 所有离职员工的信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value = "/findByLiZhiemployee", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public ReturnData findByLiZhiemployee(@RequestBody String jsonString, HttpServletRequest request,
			HttpServletResponse response) {
		int s = 0;
		ReturnData returnData = new ReturnData();
		Map<String, Object> postnamelist = new HashMap<String, Object>();
		// String companyId="977ACD3022C24B99AC9586CC50A8F786";
		// 获取请求头信息
		String companyId = request.getHeader("companyId");
		List<Employee> LiZhiemployeelist = employeeService.findByLiZhiemployee(companyId);
		for (int i = 0; i < LiZhiemployeelist.size(); i++) {
			s = s + 1;
			String temps = String.valueOf(s);
			Employee employeelist = LiZhiemployeelist.get(i);
			String Employeeid = employeelist.getEmployeeId();
			String departmentId = employeelist.getDepartmentId();
			List<Post> PostNamelist = postService.selectByPostName(Employeeid, departmentId);
			employeelist.setPostList(PostNamelist);
			postnamelist.put("employeelist" + temps, employeelist);
		}
		returnData.setData(postnamelist);
		returnData.setMessage("数据请求成功");
		returnData.setReturnCode("3000");
		return returnData;

	}

	/**
	 * 查询申请入职的人员信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/findByruzhiempinfo", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public ReturnData findByruzhiempinfo(@RequestBody String jsonString, HttpServletRequest request,
			HttpServletResponse response) {
		ReturnData returnData = new ReturnData();
		// 获取请求头信息
		String companyId = request.getHeader("companyId");
		if (!companyId.equals("")) {
			List<Employee> employeelist = employeeService.findByruzhiempinfo(companyId);
			returnData.setData(employeelist);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
		} else {
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
		}
		return returnData;
	}


	/**
	 * 删除一个人员对应下的岗位
	 * 
	 * @param employeeId
	 * @param postId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deleteConnect", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	public ReturnData deleteConnect(@RequestBody String jsonString, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = JSON.parseObject(jsonString);
		ReturnData returnData = new ReturnData();
		String employeeId = obj.getString("employeeId");
		String postId = obj.getString("postId");
		Map<String, Object> cmdmap = new HashMap<String, Object>();
		List<String> cmdlist = new ArrayList<String>();
		cmdmap.put("action", "UPDATE_USER_INFO");
		cmdlist.add(employeeId);
		cmdmap.put("employeeIdCollection", cmdlist);
		try {
			HttpRequestFactory.sendRequet(PropertiesUtils.pathUrl("commandGenerate"), cmdmap);
		} catch (IOException e) {
			logger.info("将人员信息更新到设备模块时，获取路径出错");
			e.printStackTrace();
		}
		if (!employeeId.equals("")) {
			connectEmpPostService.deleteConnect(employeeId, postId);
			returnData.setMessage("数据请求成功");
			returnData.setReturnCode("3000");
		} else {
			returnData.setMessage("数据请求失败");
			returnData.setReturnCode("3001");
		}
		return returnData;
	}

}
