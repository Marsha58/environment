package com.vw.ide.shared.servlet.processor.command.sandr;

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

	@Override
	public String toString() {
		return "SearchAndReplaceResult [userName=" + userName + ", position="
				+ position + ", line=" + line + ", projectName=" + projectName
				+ ", fileInfo=" + fileInfo + ", search=" + search
				+ ", replace=" + replace + "]";
	}
	
	
}
