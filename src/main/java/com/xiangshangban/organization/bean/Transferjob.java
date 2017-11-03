package com.xiangshangban.organization.bean;



public class Transferjob {
   
    private String transferJobId; //调动记录id
    private String employeeId;//员工id
    private String transferBeginTime;//调动开始日期
    private String departmentId;//调动部门ID     
    private String transferJobCause;//调动原因
    private String transferEndTime;//调动结束日期  
    private String userId;//操作人ID
    private String operatingTime;//操作时间
    private String departmentName;//部门名称  
    private String companyId;//公司ID
    private String directPersonId;//调动直接汇报人id    
    private String employeeName;//员工姓名
    private String loginName;//登录姓名
    private String employeeSex;//员工性别(0：男，1：女)
    private String employeeNo;//员工工号
    private String employeePhone;//联系方式1   
   	private String employeeTwophone;//联系方式2
    private String entryTime;//入职日期
    private String employeeStatus;//员工状态，0：在职，1：离职
    private String probationaryExpired;//试用到期日
    //---连表查询的--- 
    private String companyName;//公司名称
    private String postName;//岗位名称
    private String postStaus;//岗位状态（0.现在所在岗位  1原先所在岗位）	
	private String isDelete;//岗位是否删除
	private String postGrades;//岗位等级（0.副岗位  1.主岗位）
	private String directPersonName;//调动直接汇报人名称
	private String postId;//岗位ID
  
    
    
    
	public String getDirectPersonId() {
		return directPersonId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getDirectPersonName() {
		return directPersonName;
	}

	public void setDirectPersonName(String directPersonName) {
		this.directPersonName = directPersonName;
	}

	public String getPostStaus() {
		return postStaus;
	}

	public void setPostStaus(String postStaus) {
		this.postStaus = postStaus;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getPostGrades() {
		return postGrades;
	}

	public void setPostGrades(String postGrades) {
		this.postGrades = postGrades;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getEmployeeSex() {
		return employeeSex;
	}

	public void setEmployeeSex(String employeeSex) {
		this.employeeSex = employeeSex;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getEmployeePhone() {
		return employeePhone;
	}

	public void setEmployeePhone(String employeePhone) {
		this.employeePhone = employeePhone;
	}

	public String getEmployeeTwophone() {
		return employeeTwophone;
	}

	public void setEmployeeTwophone(String employeeTwophone) {
		this.employeeTwophone = employeeTwophone;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getEmployeeStatus() {
		return employeeStatus;
	}

	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}

	public String getProbationaryExpired() {
		return probationaryExpired;
	}

	public void setProbationaryExpired(String probationaryExpired) {
		this.probationaryExpired = probationaryExpired;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public void setDirectPersonId(String directPersonId) {
		this.directPersonId = directPersonId;
	}

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