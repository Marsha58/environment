package com.vw.ide.shared.servlet.remotebrowser;

import java.io.Serializable;

/**
 * Result on any operation with directory
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class RequestDirOperationResult implements Serializable {
	private String path;
	private String result;
	private String operation;
	private Integer retCode;
	private String sTextFile;
    private Long projectId;
    private Long fileId;
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
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
	
	public String getTextFile() {
		return sTextFile;
	}
	
	public void setTextFile(String sTextFile) {
		this.sTextFile = sTextFile;
	}
	
	public Long getProjectId() {
		return projectId;
	}
	
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}	

	public Long getFileId() {
		return fileId;
	}
	
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}	
	
	
}
