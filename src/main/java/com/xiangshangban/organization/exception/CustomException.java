package com.xiangshangban.organization.exception;

public class CustomException extends Exception {
	private String exceptionMessage;
	 public CustomException(String message)
	 {
		 	super(message);
		 	setExceptionMessage(message);
	 }
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	 
}
