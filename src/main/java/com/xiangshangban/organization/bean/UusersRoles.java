package com.xiangshangban.organization.bean;

public class UusersRoles {
    private String roleId;

    private String userId;
    
    private String companyId;
    
    private String historyUserIds;
    
    public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
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

	public String gethistoryUserIds() {
		return historyUserIds;
	}

	public void sethistoryUserIds(String historyUserIds) {
		this.historyUserIds = historyUserIds;
	}

	public UusersRoles() {
    }

	public UusersRoles(String roleId, String userId, String companyId) {
		super();
		this.roleId = roleId;
		this.userId = userId;
		this.companyId = companyId;
	}

	public UusersRoles(String roleId, String userId, String companyId, String historyUserIds) {
		super();
		this.roleId = roleId;
		this.userId = userId;
		this.companyId = companyId;
		this.historyUserIds = historyUserIds;
	}
	
}