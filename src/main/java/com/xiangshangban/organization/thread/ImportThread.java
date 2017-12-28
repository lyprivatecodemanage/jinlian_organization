package com.xiangshangban.organization.thread;
import java.util.List;

import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.ImportReturnData;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.service.EmployeeService;
public class ImportThread implements Runnable {
	private EmployeeService employeeService;
	private List<Employee> employeeList;
	private List<ImportReturnData> list;
	
	public ImportThread(EmployeeService employeeService,List<ImportReturnData> list,List<Employee> employeeList) {
		this.employeeService =employeeService;
		this.list = list;
		this.employeeList = employeeList;
	}
	@Override
	public void run() {
		for(Employee employee:employeeList){
			ReturnData serviceReturnData = employeeService.insertEmployee(employee);
			if(!"3000".equals(serviceReturnData.getReturnCode())){
				ImportReturnData importReturnData = new ImportReturnData();
				importReturnData.setImportMessage(serviceReturnData.getMessage());
				list.add(importReturnData);
			}
		}
	}
}
