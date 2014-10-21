package com.vw.ide.shared.servlet.userstate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

@SuppressWarnings("serial")
public class UserStateInfo implements Serializable {
	private Long projectIdSelected;
	private Long fileIdSelected;
	private Map<Long, FileItemInfo> openedFiles; 
	
	public UserStateInfo() {
		super();
		openedFiles = new HashMap <Long, FileItemInfo>();
	}

	public Long getProjectIdSelected() {
		return projectIdSelected;
	}

	public void setProjectIdSelected(Long projectIdSelected) {
		this.projectIdSelected = projectIdSelected;
	}

	public Long getFileIdSelected() {
		return fileIdSelected;
	}

	public void setFileIdSelected(Long fileIdSelected) {
		this.fileIdSelected = fileIdSelected;
	}

	public Map<Long, FileItemInfo> getOpenedFiles() {
		return openedFiles;
	}

	public void setOpenedFiles(Map<Long, FileItemInfo> openedFiles) {
		this.openedFiles = openedFiles;
	}

	public void addFile2OpenedFiles(Long fileId, FileItemInfo openedFile) {
		openedFiles.put(fileId, openedFile);
	}

	@Override
	public String toString() {
		return "UserStateInfo [projectIdSelected=" + projectIdSelected
				+ ", fileIdSelected=" + fileIdSelected + ", openedFiles="
				+ openedFiles + "]";
	}	
}
