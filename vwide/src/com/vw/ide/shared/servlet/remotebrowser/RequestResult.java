package com.vw.ide.shared.servlet.remotebrowser;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RequestResult implements Serializable {

	private String result;
	private String operation;
	private Integer retCode;	
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public Integer getRetCode() {
		return retCode;
	}
	
	public void setRetCode(Integer retCode) {
		this.retCode = retCode;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}	
	
	@Override
	public String toString() {
		return "RequestResult [result="  + result + ", retCode="  + retCode + ", operation=" + operation  + "]";
	}	
	
}