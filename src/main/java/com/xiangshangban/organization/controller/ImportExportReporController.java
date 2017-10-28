package com.xiangshangban.organization.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import com.xiangshangban.organization.service.DepartmentService;
import com.xiangshangban.organization.service.EmployeeService;
import com.xiangshangban.organization.util.ExcelUtil;

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
	@RequestMapping(value="/deriveAllEmployee", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public Map<String, String>  deriveAllDepartment(HttpServletRequest request,HttpServletResponse response) throws FileNotFoundException {
		String companyId="977ACD3022C24B99AC9586CC50A8F786";
		Map<String,String> param = new HashMap<String, String>();
		List<Employee> list =employeeService.findByAllEmployee(companyId);
		System.err.println(list);
		Map<String, String> map = new HashMap<String, String>();
        map.put("title", "员工信息表");      
        ExcelUtil.getInstance().exportObj2ExcelByTemplate(map, "web-info-template.xls", new FileOutputStream("C:/temp/"+map.get("title")+".xls"),
                list, Employee.class, true);	
        param.put("message", "导出成功");
        return	param;	
	}
	
	/**
	 * 导出部门信息报表
	 * @param request
	 * @param response
	 * @return
	 * @throws FileNotFoundException
	 */
	@RequestMapping(value="/deriveAllDepartment", produces = "application/json;charset=UTF-8", method=RequestMethod.POST)
	public Map<String, String>  deriveAllEmployee(HttpServletRequest request,HttpServletResponse response) throws FileNotFoundException {
		String companyId="977ACD3022C24B99AC9586CC50A8F786";
		Map<String,String> param = new HashMap<String, String>();
		List<Department> list =departmentService.findByAllDepartment(companyId);
		System.err.println(list);
		Map<String, String> map = new HashMap<String, String>();
        map.put("title", "部门信息表");      
        ExcelUtil.getInstance().exportObj2ExcelByTemplate(map, "web-info-template.xls", new FileOutputStream("C:/temp/"+map.get("title")+".xls"),
                list, Department.class, true);	
        param.put("message", "导出成功");
        return	param;	
	}
	
}
