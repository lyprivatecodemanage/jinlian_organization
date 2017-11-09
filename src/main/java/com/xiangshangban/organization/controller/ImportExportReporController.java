package com.xiangshangban.organization.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.xiangshangban.organization.bean.Department;
import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.exportexcel.ExcelUtil;
import com.xiangshangban.organization.service.DepartmentService;
import com.xiangshangban.organization.service.EmployeeService;
import com.xiangshangban.organization.service.PostService;

@RestController
@RequestMapping("/ImportExportReporController")
public class ImportExportReporController {	
	@Autowired	
	EmployeeService employeeService;	
	@Autowired	
	DepartmentService departmentService;
	@Autowired
	PostService postService;
	
	
	/**	
	 * 导出部门信息报表	 
	 * @param request	 
	 * @param response	 
	 * @return	 
	 * @throws FileNotFoundException	 
	 */	
	 @RequestMapping(value="/deriveAllDepartment", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	 public Map<String, String>  deriveAllEmployee(HttpServletRequest request,HttpServletResponse response) throws FileNotFoundException {
		// String companyId="977ACD3022C24B99AC9586CC50A8F786";
		 //获取请求头信息			
		 String companyId = request.getHeader("companyId");
		 Map<String,String> param = new HashMap<String, String>();		
		 List<Department> list =departmentService.findByAllDepartment(companyId);					
		 if(list !=null ){
			 Map<String, String> map = new HashMap<String, String>();        
			 map.put("title", "部门信息表");              
			 ExcelUtil.getInstance().exportObj2Excel(new FileOutputStream("C:/temp/"+map.get("title")+".xls"),                
				list, Department.class);	
			 param.put("message", "导出成功"); 
		 }else{
			 param.put("message", "导出失败");    
		 }		     
		 return	param;		
		 }
	
	
	/**	 * 导出员工信息报表	 
	* @param request	 
	* @param response	 
	* @return	 
	* @throws FileNotFoundException	 
	*/	
	@SuppressWarnings("unused")
	@RequestMapping(value="/deriveAllEmployee", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)	
	public Map<String, String>  deriveAllDepartment(HttpServletRequest request,HttpServletResponse response) throws FileNotFoundException {		
	//String companyId="977ACD3022C24B99AC9586CC50A8F786";
	//获取请求头信息			
	String companyId = request.getHeader("companyId");
	Map<String,String> param = new HashMap<String, String>();
	List<Employee> list =employeeService.selectByAllEmployee(companyId);
	List<Employee> emplist =new ArrayList<Employee>();
	for (int i = 0; i < list.size(); i++) {
		Employee employee=new Employee();
		String employeeId = list.get(i).getEmployeeId();
		String departmentId = list.get(i).getDepartmentId();
		String employeeName = list.get(i).getEmployeeName();
		String employeeSex = list.get(i).getEmployeeSex();
		String loginName = list.get(i).getLoginName();
		String employeePhone = list.get(i).getEmployeePhone();				
		String departmentName = list.get(i).getDepartmentName();
		String entryTime = list.get(i).getEntryTime();
		String employeeStatus = list.get(i).getEmployeeStatus(); 
		List<Post> postList = postService.selectByPostemp(employeeId, departmentId);
		if(postList!=null){
			StringBuffer postname = new StringBuffer();
			for (int j = 0; j < postList.size(); j++) {								
				postname.append(postList.get(j).getPostName()+"\n");				
				employee.setPostName(postname.toString());
			}
		}		
		employee.setEmployeeName(employeeName);		
		employee.setEmployeePhone(employeePhone);
		employee.setLoginName(loginName);			
		employee.setDepartmentName(departmentName);
		employee.setEntryTime(entryTime);		
		String employeestatus="";
		if(employeeStatus.equals("0")){
				employeestatus="在职";
				}
		if(employeeStatus.equals("1")){
			employeestatus="离职";
			}
		String employeesex="";
		if(employeeSex.equals("0")){
			employeesex="男";
				}
		if(employeeSex.equals("1")){
			employeesex="女";
			}
		employee.setEmployeeSex(employeesex);
		employee.setEmployeeStatus(employeestatus);						
		emplist.add(employee);
	}
	if(emplist !=null){
		Map<String, String> map = new HashMap<String, String>();        
		map.put("title", "员工信息表");              
		ExcelUtil.getInstance().exportObj2Excel(new FileOutputStream("C:/temp/"+map.get("title")+".xls"),               
				emplist, Employee.class);	        
		 param.put("message", "导出成功");
		 return	param;	
	}else{
		param.put("message", "导出失败");
		 return	param;	
	}				        
}
		
	
	
}
