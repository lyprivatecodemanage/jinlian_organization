package com.xiangshangban.organization.bean;

public class ConnectEmpPost {		
	private String employeeId;//员工ID
	private String postId;//岗位ID
	private String departmentId;//部门ID
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
