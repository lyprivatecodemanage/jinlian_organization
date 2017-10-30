package com.xiangshangban.organization.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sun.appserv.util.cache.Constants;
import com.xiangshangban.organization.bean.Department;
import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.Post;
import com.xiangshangban.organization.service.DepartmentService;
import com.xiangshangban.organization.service.EmployeeService;

@RestController
@RequestMapping("/ImportExportReporController")
public class ImportExportReporController {
	@Autowired
	EmployeeService employeeService;
	@Autowired
	DepartmentService departmentService;
	
	
	/**
	 * 导出员工信息报表
	 * @param request
	 * @param response
	 * @return
	 * @throws FileNotFoundException
	 */
	/*@RequestMapping(value="/deriveAllEmployee", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public Map<String, String>  deriveAllDepartment(HttpServletRequest request,HttpServletResponse response) throws FileNotFoundException {
		String companyId="977ACD3022C24B99AC9586CC50A8F786";
		Map<String,String> param = new HashMap<String, String>();
		List<Employee>  emplist=new ArrayList<Employee>();
		List<Employee> list =employeeService.findByAllEmployee(companyId);
		
		for (int i = 0; i < list.size(); i++) {
			Employee employee=new Employee();
			String employeeName = list.get(i).getEmployeeName();
			String employeeSex = list.get(i).getEmployeeSex();
			String loginName = list.get(i).getLoginName();
			String employeePhone = list.get(i).getEmployeePhone();
			List<Post> postList = list.get(i).getPostList();
			String departmentName = list.get(i).getDepartmentName();		
			String entryTime = list.get(i).getEntryTime();
			String employeeStatus = list.get(i).getEmployeeStatus();
			String employeestatus="";
			if(employeeStatus.equals("0")){
				employeestatus="在职";
		    }
			if(employeeStatus.equals("1")){
				employeestatus="离职";
			}
			employee.setEmployeeName(employeeName);
			employee.setEmployeeSex(employeeSex);
			employee.setLoginName(loginName);
			employee.setEmployeePhone(employeePhone);
			employee.setPostList(postList);
			employee.setDepartmentName(departmentName);
			employee.setEntryTime(entryTime);			
			employee.setEmployeeStatus(employeestatus);
			emplist.add(employee);
			System.err.println(emplist);
			
		}
		Map<String, String> map = new HashMap<String, String>();
        map.put("title", "员工信息表");      
        exportObj2ExcelByTemplate(map, "web-info-template.xls", new FileOutputStream("C:/temp/"+map.get("title")+".xls"),
        		emplist, Employee.class, true);	
        param.put("message", "导出成功");
        return	param;	
	}*/
	
	

	/**
	 * 导出部门信息报表
	 * @param request
	 * @param response
	 * @return
	 * @throws FileNotFoundException
	 */
	/*@RequestMapping(value="/deriveAllDepartment", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public Map<String, String>  deriveAllEmployee(HttpServletRequest request,HttpServletResponse response) throws FileNotFoundException {
		String companyId="977ACD3022C24B99AC9586CC50A8F786";
		Map<String,String> param = new HashMap<String, String>();
		List<Department> list =departmentService.findByAllDepartment(companyId);
		System.err.println(list);
		Map<String, String> map = new HashMap<String, String>();
        map.put("title", "部门信息表");      
        exportObj2ExcelByTemplates(map, "web-info-template.xls", new FileOutputStream("C:/temp/"+map.get("title")+".xls"),
                list, Department.class, true);	
        param.put("message", "导出成功");
        return	param;	
	}*/

	
	
	
	
	
}
