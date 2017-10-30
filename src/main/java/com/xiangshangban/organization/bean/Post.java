package com.xiangshangban.organization.bean;

import java.util.List;

public class Post {    
    private String postId;//岗位ID
    private String postName;//岗位名称   
    private String postProficiencies; //技能要求  
    private String postWorkType;//工作类型
    private String educationalRequirements;//学历要求
    private String postPrincipalId;//直属上司
    private String departmentId;//部门ID
    private String departmentName;//部门名称
    private String companyId;//公司ID   
    private String employeeName;//直属上司名称
    private String companyName;//公司名称    
    private List<Employee> employeelist;
    
	
    
	public List<Employee> getEmployeelist() {
		return employeelist;
	}
	public void setEmployeelist(List<Employee> employeelist) {
		this.employeelist = employeelist;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public String getPostProficiencies() {
		return postProficiencies;
	}
	public void setPostProficiencies(String postProficiencies) {
		this.postProficiencies = postProficiencies;
	}
	public String getPostWorkType() {
		return postWorkType;
	}
	public void setPostWorkType(String postWorkType) {
		this.postWorkType = postWorkType;
	}
	public String getEducationalRequirements() {
		return educationalRequirements;
	}
	public void setEducationalRequirements(String educationalRequirements) {
		this.educationalRequirements = educationalRequirements;
	}
	public String getPostPrincipalId() {
		return postPrincipalId;
	}
	public void setPostPrincipalId(String postPrincipalId) {
		this.postPrincipalId = postPrincipalId;
	}
	
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
    
    
 

	



	
	
}