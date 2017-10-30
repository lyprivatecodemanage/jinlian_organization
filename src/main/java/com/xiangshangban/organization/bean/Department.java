package com.xiangshangban.organization.bean;


public class Department {   
	private String departmentId;//部门ID
    private String departmentNumbe;//部门编号
    private String departmentName;//部门名称
    private String departmentDescribe;//部门描述
    private String employeeId;//部门负责人ID
    private String departmentPhone;//部门办公电话
    private String departmentFax;//部门传真
    private String departmentParentId;//部门父ID
    private String companyName;//上级组织机构（公司名称）
    private String companyId;//公司ID
    private String employeeName;//部门负责人
    private String CountNumber;//部门总人数
    
    
    
	public String getDepartmentNumbe() {
		return departmentNumbe;
	}
	public void setDepartmentNumbe(String departmentNumbe) {
		this.departmentNumbe = departmentNumbe;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getDepartmentDescribe() {
		return departmentDescribe;
	}
	public void setDepartmentDescribe(String departmentDescribe) {
		this.departmentDescribe = departmentDescribe;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}	
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	public String getCountNumber() {
		return CountNumber;
	}
	public void setCountNumber(String countNumber) {
		CountNumber = countNumber;
	}	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}	 
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}		
	public String getDepartmentPhone() {
		return departmentPhone;
	}
	public void setDepartmentPhone(String departmentPhone) {
		this.departmentPhone = departmentPhone;
	}
	public String getDepartmentFax() {
		return departmentFax;
	}
	public void setDepartmentFax(String departmentFax) {
		this.departmentFax = departmentFax;
	}
	public String getDepartmentParentId() {
		return departmentParentId;
	}
	public void setDepartmentParentId(String departmentParentId) {
		this.departmentParentId = departmentParentId;
	}
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
    
	
	
    
    
	
}