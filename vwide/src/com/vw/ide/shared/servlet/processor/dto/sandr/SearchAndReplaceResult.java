package com.vw.ide.shared.servlet.processor.dto.sandr;

import java.io.Serializable;

import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

@SuppressWarnings("serial")
public class SearchAndReplaceResult implements Serializable {
	private String userName;
	private Integer position;
	private Integer line;
	private String projectName;
	private FileItemInfo fileInfo;
	private String search;
	private String replace;
	// fileInfo is considered as result of replace operation
	private Boolean fileAsReplaced;

	@SuppressWarnings("unused")
	transient private String key;
	@SuppressWarnings("unused")
	transient private String file;
	@SuppressWarnings("unused")
	transient private String place;
	
	public SearchAndReplaceResult() {
		super();
	}

	public SearchAndReplaceResult(String userName, Integer position, Integer line,
			String search, String replace, String projectName, FileItemInfo fileInfo) {
		super();
		this.userName = userName;
		this.position = position;
		this.line = line;
		this.projectName = projectName;
		this.search = search;
		this.replace = replace;
		this.fileInfo = fileInfo;
	}

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

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getReplace() {
		return replace;
	}

	public void setReplace(String replace) {
		this.replace = replace;
	}

	
	public FileItemInfo getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(FileItemInfo fileInfo) {
		this.fileInfo = fileInfo;
	}

	public String getKey() {
		return "Found: " + getSearch() + "; project: [" + getProjectName() + "]; file: [" + getFileInfo().getAbsolutePath() + "/" + getFileInfo().getName() + "]; line: [" + getLine() + "]; position: [" + getPosition() + "]";
	}
	
	public String getFile() {
		return getFileInfo().getAbsolutePath() + "/" + getFileInfo().getName();
	}
	
	public String getPlace() {
		return getLine() + ":" + getPosition();
	}
	
	public boolean isFileAsReplaced() {
		return (fileAsReplaced == null) ? false : fileAsReplaced;
	}

	public void setFileAsReplaced(Boolean fileAsReplaced) {
		this.fileAsReplaced = fileAsReplaced;
	}

	@Override
	public String toString() {
		return "SearchAndReplaceResult [userName=" + userName + ", position="
				+ position + ", line=" + line + ", projectName=" + projectName
				+ ", fileInfo=" + fileInfo + ", search=" + search
				+ ", replace=" + replace + "]";
	}
}
