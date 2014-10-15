package com.vw.ide.shared.servlet.remotebrowser;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RequestUserStateResult  extends RequestResult implements Serializable {
	private UserStateInfo userStateInfo;

	public UserStateInfo getUserStateInfo() {
		return userStateInfo;
	}

	public void setUserStateInfo(UserStateInfo userStateInfo) {
		this.userStateInfo = userStateInfo;
	}
	
	
	@Override
	public String toString() {
		return "RequestUserStateResult [result="  + getResult() + 
				", retCode="  + getRetCode() + ", operation=" + getOperation()  + ", userStateInfo=" + userStateInfo + "]";
	}	

}
