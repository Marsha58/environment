package com.vw.ide.shared.servlet.remotebrowser;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class RequestFileOperationResult extends RequestResult implements Serializable {
	private String fileName;
	private Long fileId;
	
	
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
				", retCode="  + getRetCode() + ", operation=" + getOperation()  + "]";
	}
}
