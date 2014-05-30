package com.vw.ide.shared.servlet.remotebrowser;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class RequestedDirScanResult implements Serializable {
	private String parentPath;
	private List<FileItemInfo> files;
	
	public String getParentPath() {
		return parentPath;
	}
	
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
	
	public List<FileItemInfo> getFiles() {
		return files;
	}

	public void setFiles(List<FileItemInfo> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "RequestedDirScanResult [parentPath=" + parentPath + ", files="
				+ files + "]";
	}
}
