package com.xiangshangban.organization.bean;

public class UserCompanyDefault {
	private String userId;
	private String companyId;
	private String currentOption;//默认公司(1:默认选择，2:备选公司)
	private String isActive;//是否激活 0：未激活  1：已激活
	private String infoStatus;//信息是否完善  0:否  1：是
	public UserCompanyDefault(){}
	
	public UserCompanyDefault(String userId, String companyId, String currentOption, String isActive,
			String infoStatus) {
		super();
		this.userId = userId;
		this.companyId = companyId;
		this.currentOption = currentOption;
		this.isActive = isActive;
		this.infoStatus = infoStatus;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCurrentOption() {
		return currentOption;
	}
	public void setCurrentOption(String currentOption) {
		this.currentOption = currentOption;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getInfoStatus() {
		return infoStatus;
	}
	public void setInfoStatus(String infoStatus) {
		this.infoStatus = infoStatus;
	}
	
	
}
