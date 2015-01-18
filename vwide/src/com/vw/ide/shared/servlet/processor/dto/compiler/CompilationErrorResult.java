package com.vw.ide.shared.servlet.processor.dto.compiler;

import java.io.Serializable;

import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

@SuppressWarnings("serial")
public class CompilationErrorResult implements Serializable {
	private String userName;
	private Integer position;
	private Integer line;
	private String projectName;
	private String cause;
	private FileItemInfo fileInfo;
	
	@SuppressWarnings("unused")
	transient private String key;
	@SuppressWarnings("unused")
	transient private String file;
	@SuppressWarnings("unused")
	transient private String place;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Integer getPosition() {
		return position;
	}
	
	public void setPosition(Integer position) {
		this.position = position;
	}
	
	public Integer getLine() {
		return line;
	}
	
	public void setLine(Integer line) {
		this.line = line;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public FileItemInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(FileItemInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}
	
	public String getKey() {
		return "project: [" + getProjectName() + "]; file: [" + getFileInfo().getAbsolutePath() + "/" + getFileInfo().getName() + "]; line: [" + getLine() + "]; position: [" + getPosition() + "]";
	}
	
	public String getFile() {
		return getFileInfo().getAbsolutePath() + "/" + getFileInfo().getName();
	}
	
	public String getPlace() {
		return getLine() + ":" + getPosition();
	}
	
}
