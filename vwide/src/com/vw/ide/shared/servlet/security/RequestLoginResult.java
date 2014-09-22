package com.vw.ide.shared.servlet.security;

import java.io.Serializable;

/**
 * Result on login operation
 * @author OMelnyk
 *
 */
@SuppressWarnings("serial")
public class RequestLoginResult implements Serializable {
	private String userName;
	private Byte result;
	private Integer retCode;	
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Byte getResult() {
		return result;
	}
	
	public void setResult(Byte result) {
		this.result = result;
	}
	
	public Integer getRetCode() {
		return retCode;
	}
	
	public void setRetCode(Integer retCode) {
		this.retCode = retCode;
	}	
	
}
