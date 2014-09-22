package com.vw.ide.shared.servlet.security;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {

	private String userName;	
	private String password;
	private List<String> roles;
	
	public UserInfo() {
		super();
		roles= new ArrayList<String>();		
	}
	
	public UserInfo(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password; 
		roles= new ArrayList<String>();
	}	
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}	
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<String> getRoles() {
		return roles;
	}
	
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public boolean addRole(String role) {
		
		if (!roles.contains(role)) {
			return roles.add(role);
		} else return false;
	}	
	
	public boolean removeRole(String role) {
		if (roles.contains(role)) {
			return roles.remove(role);
		} else return false;
	}	

	
}
