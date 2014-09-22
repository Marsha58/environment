package com.vw.ide.shared.servlet.security;

import java.io.Serializable;

/**
 * Simple class which is used to pass information about results of authorization and user rights 
 * @author OMelnyk
 *
 */
@SuppressWarnings("serial")
public class LoginInfo implements Serializable {
	private String userName;
	private boolean isAuthorized;
	
	
	public LoginInfo() {
		super();
	}

	public LoginInfo(String userName, boolean isAuthorized) {
		super();
		this.userName = userName;
		this.isAuthorized = isAuthorized;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	public boolean getIsAuthorized() {
		return isAuthorized;
	}
	
	public void setIsAuthorized(boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}
	

	@Override
	public String toString() {
		return "LoginInfo [userName=" + userName + ", isAuthorized=" + isAuthorized + "]";
	}
}
