package com.xiangshangban.organization.bean;

public class ConnectEmpPost {		
	private String employeeId;//员工ID
	private String postId;//岗位ID
	private String departmentId;//部门ID
	private String postStaus;//岗位状态（0.现在所在岗位  1原先所在岗位）	
	private String isDelete;//岗位是否删除
	private String postGrades;//岗位等级（0.副岗位  1.主岗位）

	
	
	public String getPostGrades() {
		return postGrades;
	}


	public void setPostGrades(String postGrades) {
		this.postGrades = postGrades;
	}


	public String getIsDelete() {
		return isDelete;
	}


	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}


	public String getDepartmentId() {
		return departmentId;
	}


	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}


	public String getPostStaus() {
		return postStaus;
	}


	public void setPostStaus(String postStaus) {
		this.postStaus = postStaus;
	}


	public ConnectEmpPost(){}
	

	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}

}
