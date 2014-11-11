package com.vw.ide.shared.servlet.fringes;

import java.io.Serializable;

import com.vw.ide.shared.servlet.remotebrowser.RequestResult;

@SuppressWarnings("serial")
public class RequestLoadFringeJarResult  extends RequestResult implements Serializable {
	private String fileName;
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}	
	
	@Override
	public String toString() {
		return "RequestLoadFringeJarResult [fileName=" + fileName + ", result="  + getResult() + 
				", retCode="  + getRetCode() + ", operation=" + getOperation()  +  "]";
	}	
	
}
