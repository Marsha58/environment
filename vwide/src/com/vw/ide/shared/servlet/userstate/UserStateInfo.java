package com.vw.ide.shared.servlet.userstate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

@SuppressWarnings("serial")
public class UserStateInfo implements Serializable {
	private ProjectDescription projectIdSelected;
	private FileItemInfo fileIdSelected;
	private Set<FileItemInfo> openedFiles = new HashSet<FileItemInfo>(); 
	
	public UserStateInfo() {
		super();
	}

	public ProjectDescription getProjectIdSelected() {
		return projectIdSelected;
	}

	public void setProjectIdSelected(ProjectDescription projectIdSelected) {
		this.projectIdSelected = projectIdSelected;
	}

	public FileItemInfo getFileIdSelected() {
		return fileIdSelected;
	}

	public void setFileIdSelected(FileItemInfo fileIdSelected) {
		this.fileIdSelected = fileIdSelected;
	}

	public Set<FileItemInfo> getOpenedFiles() {
		return openedFiles;
	}

	public void addFileToOpenedFiles(FileItemInfo openedFile) {
		openedFiles.add(openedFile);
	}
	
	public void removeFileFromOpenedFiles(FileItemInfo openedFile) {
		openedFiles.remove(openedFile);
	}
	
	public boolean hasFileAlreadyOpened(FileItemInfo openedFile) {
		return openedFiles.contains(openedFile);
	}

	@Override
	public String toString() {
		return "UserStateInfo [projectIdSelected=" + projectIdSelected
				+ ", fileIdSelected=" + fileIdSelected + ", openedFiles="
				+ openedFiles + "]";
	}	
}
