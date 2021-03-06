package com.xiangshangban.organization.thread;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.xiangshangban.organization.bean.Employee;
import com.xiangshangban.organization.bean.ImportReturnData;
import com.xiangshangban.organization.bean.ReturnData;
import com.xiangshangban.organization.service.EmployeeService;
import com.xiangshangban.organization.service.EmployeeSpeedServiceImpl;
public class ImportThread implements Runnable {
	private EmployeeService employeeService;
	private List<Employee> employeeList;
	private List<ImportReturnData> list;
	private int num = 0;
	
	public ImportThread(EmployeeService employeeService,List<ImportReturnData> list,List<Employee> employeeList) {
		this.employeeService =employeeService;
		this.list = list;
		this.employeeList = employeeList;
	}
	@Override
	public void run() {
		
		for(Employee employee:employeeList){
			ReturnData serviceReturnData;
			/*synchronized(EmployeeSpeedServiceImpl.successNum){*/
				serviceReturnData = new ReturnData();
				serviceReturnData = employeeService.insertEmployee(employee);
				/*++EmployeeSpeedServiceImpl.successNum;*/
			/*}*/
			if(!"3000".equals(serviceReturnData.getReturnCode())){
				ImportReturnData importReturnData = new ImportReturnData();
				importReturnData.setImportMessage(serviceReturnData.getMessage());
				list.add(importReturnData);
			}
			if(StringUtils.isEmpty(serviceReturnData.getMessage()) && StringUtils.isEmpty(serviceReturnData.getReturnCode())){
				String employeeId = serviceReturnData.getEmployeeId();
				employeeService.activeEmp(employee.getCompanyId(), employeeId);
				employeeService.resetEmployeeStatus(employee.getCompanyId(), employeeId);
			}
			num = num + 1;
			
		}
	}
}
