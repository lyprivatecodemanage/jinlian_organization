package com.xiangshangban.organization.bean;

import java.util.List;

public class Uroles {
	// 管理员角色
	public static String admin_role = "56b814c2ea7e4f679ee505036f5b030a";
	// 普通用户角色
	public static String user_role = "6a35159b0c834c698b42659735925604";
	
    private String roleid;

    private String rolename;

    private String description;

	public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Uroles() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}