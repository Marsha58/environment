package com.vw.ide.shared.servlet;

import java.io.Serializable;

import com.vw.ide.shared.OperationTypes;

@SuppressWarnings("serial")
public class RequestResult implements Serializable {

	public static final int GENERAL_OK   = 0;
	public static final int GENERAL_FAIL = -1;
	
	private String result;
	private String operation;
	private Integer retCode = GENERAL_OK;	
	private OperationTypes operationType;
	
	public OperationTypes getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationTypes operationType) {
		this.operationType = operationType;
	}

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
		return "RequestResult [result="  + result + ", retCode="  + retCode + ", operation=" + operation  + ", operationType=" + operationType  +"]";
	}	
	
}