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
	private Set<String> searchHistory = new HashSet<String>();
	private Set<String> replaceHistory = new HashSet<String>();
	
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

	public void addToSearchHistory(String item) {
		searchHistory.add(item);
	}

	public void addToReplaceHistory(String item) {
		replaceHistory.add(item);
	}
	
	public void removeFromSearchHistory(String item) {
		searchHistory.remove(item);
	}

	public void removeFromReplaceHistory(String item) {
		replaceHistory.remove(item);
	}
	
	public void clearSearchHistory() {
		searchHistory.clear();
	}

	public void clearReplaceHistory() {
		replaceHistory.clear();
	}

	public Set<String> getSearchHistory() {
		return searchHistory;
	}

	public void setSearchHistory(Set<String> searchHistory) {
		this.searchHistory = searchHistory;
	}

	public Set<String> getReplaceHistory() {
		return replaceHistory;
	}

	public void setReplaceHistory(Set<String> replaceHistory) {
		this.replaceHistory = replaceHistory;
	}

	@Override
	public String toString() {
		return "UserStateInfo [projectIdSelected=" + projectIdSelected
				+ ", fileIdSelected=" + fileIdSelected + ", openedFiles="
				+ openedFiles + ", searchHistory=" + searchHistory
				+ ", replaceHistory=" + replaceHistory + "]";
	}	
}
