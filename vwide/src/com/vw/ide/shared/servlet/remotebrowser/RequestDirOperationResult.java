package com.vw.ide.shared.servlet.remotebrowser;

import java.io.Serializable;

import com.vw.ide.shared.servlet.RequestResult;

/**
 * Result on any operation with directory
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class RequestDirOperationResult extends RequestResult implements Serializable {
	private String path;
	private String sTextFile;
    private Long projectId;
    private Long fileId;
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
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
	
	@Override
	public String toString() {
		return "RequestedDirOperationResult [path=" + path + ", sTextFile=" + sTextFile  + ", projectId=" + projectId + ", fileId=" + fileId +   
				 ", result="  + getResult() + ", retCode="  + getRetCode() + ", operation=" + getOperation()  + "]";
	}	
	
}
