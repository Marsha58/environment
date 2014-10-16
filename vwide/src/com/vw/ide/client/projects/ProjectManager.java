package com.vw.ide.client.projects;

import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;


public interface ProjectManager {
	public Long addProject(ProjectItem projectInfo);
	public boolean removeProject(Long projectId);
	public Long getProjectIdByProjectInfo(ProjectItem projectInfo);
	public Long getProjectIdByProjectPath(String user,String projectPath);
	public Long getProjectIdByProjectConfXml(String projectConfXml);
//	public Long getProjectIdByFilePath(String fileFullPath);

	public Long addFile(FileItemInfo fileItemInfo);
	public boolean removeFile(Long fileId);
	public boolean checkIfFileExists(String fileName);
	public boolean checkFile(String fullFileNameWithPath);
	public FileItemInfo getFileItemInfo(Long fileId);
//	public void setFileContent(Long fileId, String content);
//	public String getFileContent(Long fileId);
	public Long getFileIdByFileInfo(FileItemInfo fileItemInfo);
	public Long getFileIdByFilePath(String path);
	public boolean checkIsFileOpened(String path);
    public void changeFileName(Long fileId, String FileNewName);
	
	public void setAssociatedTabWidget(Long fileId, Widget widget);
	public Widget getAssociatedTabWidget(Long fileId);	
	
	
	public void setFileState(Long fileId,Boolean bIsEdited);
	
 	public Long getProjectIdByRelPath(String relPath);

	
	public Map<Long, ProjectItemImpl> getProjectsContext();
	public Map<Long, FileItemInfo> getOpenedFilesContext();
	public Map<Long, Widget> getAssociatedTabWidgetsContext();
	
	
	public void addFileToOpenedFilesContext(Long projectId, Long fileId, FileItemInfo fileItemInfo);
}
