package com.xiangshangban.organization.bean;
/**
 * 请求返回数据模型
 * @author 韦友弟
 *
 */
public class ReturnData {
	private Object data;//数据
	private Object totalPages;//总记录数
	private String message;//请求状态描述
	private String returnCode;//请求状态编码
	private Object pagecountNum;//总页数
	
	
	
	public Object getPagecountNum() {
		return pagecountNum;
	}
	public void setPagecountNum(Object pagecountNum) {
		this.pagecountNum = pagecountNum;
	}
	public Object getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Object totalPages) {
		this.totalPages = totalPages;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	
}
