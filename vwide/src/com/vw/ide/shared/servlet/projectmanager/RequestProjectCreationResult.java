package com.vw.ide.shared.servlet.remotebrowser;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class RequestProjectCreationResult extends RequestResult implements Serializable {
	private String projectPath;
// just name without path	
	private String projectName;
	private List<FileItemInfo> files;
	
	public String getProjectPath() {
		return projectPath;
	}
	
	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}	
	
	public List<FileItemInfo> getFiles() {
		return files;
	}

	public void setFiles(List<FileItemInfo> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "RequestedDirScanResult [projectPath=" + projectPath + ", projectName=" + projectName + ", result="  + getResult() + 
				", retCode="  + getRetCode() + ", operation=" + getOperation() + ", files=" + files.toString() + "]";
	}
}
