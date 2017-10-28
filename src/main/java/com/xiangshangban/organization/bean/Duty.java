package com.xiangshangban.organization.bean;

public class Duty {   
    private String dutyId; //职责ID 
    private String dutyName;//职责名称 
    private String dutyMatter; //职责描述 
    private String postId;//岗位ID

    
    public String getDutyId() {
        return dutyId;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public String getDutyName() {
        return dutyName;
    }

    
    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    
    public String getDutyMatter() {
        return dutyMatter;
    }

    
    public void setDutyMatter(String dutyMatter) {
        this.dutyMatter = dutyMatter;
    }

    
    public String getPostId() {
        return postId;
    }

    
    public void setPostId(String postId) {
        this.postId = postId;
    }
}