package com.xiangshangban.organization.bean;

import java.util.List;

public class Transferjob {
   
    private String transferJobId; //调动记录id
    private String employeeId;//员工id
    private String transferBeginTime;//调动开始日期
    private String departmentId;//调动部门ID     
    private List<Post> postList;//岗位    
    private String transferJobCause;//调动原因
    private String transferEndTime;//调动结束日期  
    private String userId;//操作人ID
    private String operatingTime;//操作时间
    private String departmentName;//部门名称  
    private String companyId;//公司ID
    
  

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public List<Post> getPostList() {
		return postList;
	}

	public void setPostList(List<Post> postList) {
		this.postList = postList;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	
    public Transferjob(){}

	public String getTransferJobId() {
		return transferJobId;
	}

	public void setTransferJobId(String transferJobId) {
		this.transferJobId = transferJobId;
	}


	public String getTransferBeginTime() {
		return transferBeginTime;
	}

	public void setTransferBeginTime(String transferBeginTime) {
		this.transferBeginTime = transferBeginTime;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	

	public String getTransferJobCause() {
		return transferJobCause;
	}

	public void setTransferJobCause(String transferJobCause) {
		this.transferJobCause = transferJobCause;
	}

	public String getTransferEndTime() {
		return transferEndTime;
	}

	public void setTransferEndTime(String transferEndTime) {
		this.transferEndTime = transferEndTime;
	}

	public String getOperatingTime() {
		return operatingTime;
	}

	public void setOperatingTime(String operatingTime) {
		this.operatingTime = operatingTime;
	}

	
	
   
}