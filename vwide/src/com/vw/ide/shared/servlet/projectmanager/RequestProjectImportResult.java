package com.vw.ide.shared.servlet.projectmanager;

import java.util.List;

import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

@SuppressWarnings("serial")
public class RequestProjectImportResult extends RequestProjectResult {
	// list of files which should be imported in order to complete procedure
	private List<FileItemInfo> listOfExpectedFiles = null;

	public List<FileItemInfo> getListOfExpectedFiles() {
		return listOfExpectedFiles;
	}

	public void setListOfExpectedFiles(List<FileItemInfo> listOfExpectedFiles) {
		this.listOfExpectedFiles = listOfExpectedFiles;
	}
}