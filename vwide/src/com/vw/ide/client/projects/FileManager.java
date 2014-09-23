package com.vw.ide.client.projects;

import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

public interface FileManager {
	
	public Long addFile(FileItemInfo fileItemInfo);
	public boolean removeFile(Long fileId);
	public boolean checkIfFileExists(String fileName);
	public FileItemInfo getFileItemInfo(Long fileId);
	public void setFileContent(Long fileId, String content);
	public String getFileContent(Long fileId);
	public Long getFileIdByFileInfo(FileItemInfo FileItemInfo);
	public Long getFileIdByFilePath(String path);
	public boolean checkIsFileOpened(String path);
	public void setAssociatedTabWidget(Long fileId, Widget widget);
	public Widget getAssociatedTabWidget(Long fileId);
	public Map<Long, FileItemInfo> getFilesFileInfoContext();
	public Map<Long, String> getFilesContext();
	public Map<Long, Widget> getAssociatedTabWidgets();
}
