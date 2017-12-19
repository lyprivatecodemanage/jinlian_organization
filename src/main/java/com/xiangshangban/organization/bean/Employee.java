package com.xiangshangban.organization.bean;

import java.util.ArrayList;
import java.util.List;

import com.xiangshangban.organization.exportexcel.ExcelResources;

public class Employee {
   // ---现在用到的----
    private String employeeId;//员工ID
    private String employeeName;//员工姓名
    private String loginName;//登录姓名
    private String employeeSex;//员工性别(0：男，1：女)
    private String departmentId;//部门ID
    private String companyId;//公司ID
    private String companyNo;//公司编号
    private String employeeNo;//员工工号
    private String directPersonId;//直接汇报人id
    private String employeePhone;//联系方式1   
   	private String employeeTwophone;//联系方式2
    private String entryTime;//入职日期
    private String employeeStatus;//员工状态，0：在职，1：离职
    private String probationaryExpired;//试用到期日
    private String employeeImgUrl;//头像 
    private String workAddress;//工作地
    private String operaterTime;//操作时间
    //---连表查询的---
    private List<Post> postList = new ArrayList<Post>();//父岗位   
    private String companyName;//
    private String postId;//主岗位ID
    private String departmentName;//部门名称
    private String postName;//岗位名称
    private String directPersonName;//直接汇报人名称    
    private String operateUserId;// 操作人ID
    private String isActive;//激活状态，0：未激活，1：已激活
    private String infoStatus;//信息完整状态 0：不完整，1：完整
    private String marriageStatus;// 婚姻状况 0：未婚，1：已婚 ，2：离异
    private String seniority;// 工龄
    private String phone;//登录手机号
    private String directPersonDepartmentId;//汇报人的部门id
    private String directPersonDepartmentName;//汇报人的部门name
    private String directPersonLoginName;//汇报人登录名
    private String description;//角色描述
    //----现在没用到的----
//    private String employeeBirthday;// 出生年月日    
//    private String employeeTell;//电话号码  
//    private String employeeEmail;//邮箱号
//    private String employeeWorkPlace;//工作地     
   private String contractPeriod;//合同期（月） 
   private String transferJobCause;//调动原因
//    private String isManager;//是否为部门经理（1:是，2否）
//    private String contractExpired;//合同到期日   
//    private String probationaryPeriod;//试用期（月）
//    private String emergencyContactPhone;//紧急联系电话
//    private String emergencyContact;//紧急联系人
//    private String phonePublic;//是否公开手机号  0：公开  1：不公开
//    private String shiftTime;//可用调休时长（秒）
//    private String employeeCardNumber;//员工工资卡
    
    
    
    
    @ExcelResources(title="岗位",order=5)
	public String getPostName() {
		return postName;
	}
    public void setPostName(String postName) {
    	this.postName = postName;
    }
    public Employee(){}
    public Employee(String companyId,String employeeId,String loginName){
    		this.companyId=companyId;
    		this.employeeId = employeeId;
    		this.loginName = loginName;
    }
	public Employee(String employeeNo,String employeeName,String employeeSex, 
			String workAddress,String marriageStatus,String loginName,
			String departmentName,String directPersonName,String directPersonLoginName,
			String employeeStatus,String entryTime,String probationaryExpired,String postName,
			List<Post> postList,String employeePhone,String employeeTwophone, String seniority) {
		this.employeeName = employeeName;
		this.loginName = loginName;
		this.employeeSex = employeeSex;
		this.employeeNo = employeeNo;
		this.employeePhone = employeePhone;
		this.employeeTwophone = employeeTwophone;
		this.entryTime = entryTime;
		this.employeeStatus = employeeStatus;
		this.probationaryExpired = probationaryExpired;
		this.workAddress = workAddress;
		this.postName = postName;
		this.postList = postList;
		this.departmentName = departmentName;
		this.directPersonName = directPersonName;
		this.directPersonLoginName = directPersonLoginName;
		this.marriageStatus = marriageStatus;
		this.seniority = seniority;
	}
	public String getDirectPersonLoginName() {
		return directPersonLoginName;
	}
	public void setDirectPersonLoginName(String directPersonLoginName) {
		this.directPersonLoginName = directPersonLoginName;
	}
	public String getDirectPersonDepartmentName() {
		return directPersonDepartmentName;
	}
	public void setDirectPersonDepartmentName(String directPersonDepartmentName) {
		this.directPersonDepartmentName = directPersonDepartmentName;
	}
	public String getDirectPersonDepartmentId() {
		return directPersonDepartmentId;
	}
	public void setDirectPersonDepartmentId(String directPersonDepartmentId) {
		this.directPersonDepartmentId = directPersonDepartmentId;
	}
	public String getTransferJobCause() {
		return transferJobCause;
	}
	public void setTransferJobCause(String transferJobCause) {
		this.transferJobCause = transferJobCause;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getMarriageStatus() {
		return marriageStatus;
	}
	public void setMarriageStatus(String marriageStatus) {
		this.marriageStatus = marriageStatus;
	}
	public String getSeniority() {
		return seniority;
	}
	public void setSeniority(String seniority) {
		this.seniority = seniority;
	}
	public String getContractPeriod() {
		return contractPeriod;
	}
	public void setContractPeriod(String contractPeriod) {
		this.contractPeriod = contractPeriod;
	}

	public String getEmployeeImgUrl() {
		return employeeImgUrl;
	}
	public void setEmployeeImgUrl(String employeeImgUrl) {
		this.employeeImgUrl = employeeImgUrl;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	@ExcelResources(title="员工姓名",order=1)
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	@ExcelResources(title="性别",order=2)
	public String getEmployeeSex() {
		return employeeSex;
	}
	public void setEmployeeSex(String employeeSex) {
		this.employeeSex = employeeSex;
	}
	@ExcelResources(title="登录名",order=3)
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	@ExcelResources(title="联系方式",order=4)
	public String getEmployeePhone() {
		return employeePhone;
	}
	public void setEmployeePhone(String employeePhone) {
		this.employeePhone = employeePhone;
	}
	
	public List<Post> getPostList() {
		return postList;
	}	
	public void setPostList(List<Post> postList) {
		this.postList = postList;
	}
	@ExcelResources(title="部门",order=6)
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	@ExcelResources(title="入职时间",order=7)
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
	@ExcelResources(title="在职状态",order=8)
	public String getEmployeeStatus() {
		return employeeStatus;
	}
	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public String getDirectPersonId() {
		return directPersonId;
	}
	public void setDirectPersonId(String directPersonId) {
		this.directPersonId = directPersonId;
	}
	public String getDirectPersonName() {
		return directPersonName;
	}
	public void setDirectPersonName(String directPersonName) {
		this.directPersonName = directPersonName;
	}

	public String getEmployeeTwophone() {
		return employeeTwophone;
	}
	public void setEmployeeTwophone(String employeeTwophone) {
		this.employeeTwophone = employeeTwophone;
	}
	
	public String getProbationaryExpired() {
		return probationaryExpired;
	}
	public void setProbationaryExpired(String probationaryExpired) {
		this.probationaryExpired = probationaryExpired;
	}
	
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getOperateUserId() {
		return operateUserId;
	}
	public void setOperateUserId(String operateUserId) {
		this.operateUserId = operateUserId;
	}
	public String getWorkAddress() {
		return workAddress;
	}
	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}
	public String getInfoStatus() {
		return infoStatus;
	}
	public void setInfoStatus(String infoStatus) {
		this.infoStatus = infoStatus;
	}
	public String getCompanyNo() {
		return companyNo;
	}
	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}
	public String getOperate_time() {
		return operaterTime;
	}
	public void setOperate_time(String operaterTime) {
		this.operaterTime = operaterTime;
	}
	
	
    
    
	
    
   
}