package com.vw.ide.client.projects;

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

}
