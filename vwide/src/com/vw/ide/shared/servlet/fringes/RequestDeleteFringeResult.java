package com.vw.ide.shared.servlet.fringes;

import com.vw.ide.shared.servlet.remotebrowser.RequestResult;

@SuppressWarnings("serial")
public class RequestDeleteFringeResult extends RequestResult{

	private Integer id;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	@Override
	public String toString() {
		return "RequestDeleteFringeResult [result="  + getResult() + ", id="  + id + 
				", retCode="  + getRetCode() + ", operation=" + getOperation()  +  "]";
	}		
	
}
