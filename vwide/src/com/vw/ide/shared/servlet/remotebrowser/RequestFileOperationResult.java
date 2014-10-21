package com.vw.ide.shared.servlet.remotebrowser;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RequestFileOperationResult extends RequestResult implements Serializable {
	private String fileName;
	private Long fileId;
	private String fileNewName;
	
	
	public String getFileNewName() {
		return fileNewName;
	}

	public void setFileNewName(String fileNewName) {
		this.fileNewName = fileNewName;
	}

	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Long getFileId() {
		return fileId;
	}
	
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}		
	
	
	@Override
	public String toString() {
		return "RequestFileOperationResult [fileName=" + fileName + ", result="  + getResult() + 
				", retCode="  + getRetCode() + ", operation=" + getOperation()  + ",fileNewName=" + fileName + "]";
	}
}
