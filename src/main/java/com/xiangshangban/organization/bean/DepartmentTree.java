package com.xiangshangban.organization.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class DepartmentTree implements Serializable {

	private static final long serialVersionUID = 5363551924502220248L;
	private String label;//部门名称
	private String value;//部门ID
	private String key;	//部门ID	
	private List<Post> postlist;
	private int CountNumber;
	private List<DepartmentTree> children = new ArrayList<DepartmentTree>();	
	private int totalPages;
	
		
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public List<Post> getPostlist() {
		return postlist;
	}
	public void setPostlist(List<Post> postlist) {
		this.postlist = postlist;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public int getCountNumber() {
		return CountNumber;
	}
	public void setCountNumber(int countNumber) {
		CountNumber = countNumber;
	}
	public List<DepartmentTree> getChildren() {
		return children;
	}
	public void setChildren(List<DepartmentTree> children) {
		this.children = children;
	}	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

}
