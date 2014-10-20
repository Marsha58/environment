package com.vw.ide.client.projects;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

public class ProjectManagerImpl implements ProjectManager{

	private static Long idsProjectsCounter = 0l;
	private static Long idsFilesCounter = 0l;
	
	private  Map<Long, ProjectItem> projectsContext =  new HashMap <Long, ProjectItem>();
	private  Map<Long, FileItemInfo> filesFileInfoContext = new HashMap <Long, FileItemInfo>(); 
	Map<Long, FileItemInfo> openedFilesContext =  new HashMap <Long, FileItemInfo>();
	Map<Long, Widget> associatedTabWidgetsContext =  new HashMap <Long, Widget>();
	
	
	private Long getNewProjectId() {
		return ++idsProjectsCounter;
	}

	public Long getLastProjectId() {
		return idsProjectsCounter;
	}	
	
	public Long getNewFileId() {
		return ++idsFilesCounter;
	}

	public Long getLastFileId() {
		return idsFilesCounter;
	}		
	
	@Override
	public Long addProject(ProjectItem projectInfo) {
		Long res = -1l;
		boolean isSuchProjectExists = false;
		for(Object key : projectsContext.keySet()) {
			ProjectItem value = projectsContext.get(key);
			if(value.getProjectPath().equalsIgnoreCase(projectInfo.getProjectPath())) {
				isSuchProjectExists = true;
				res = (Long) key; 	
				break;
			}
		}
		
		if(!isSuchProjectExists) {
			res = getNewProjectId();
			projectsContext.put(res, (ProjectItemImpl) projectInfo);
		}
		
		return res;
	}

	@Override
	public boolean removeProject(Long projectId) {
		try {
			Long fileId;
			for(Object key :  filesFileInfoContext.keySet()) {
				FileItemInfo value = filesFileInfoContext.get(key);
				if(value.getProjectId() == projectId) {
					fileId = (Long) key;
					openedFilesContext.remove(fileId);
					filesFileInfoContext.remove(fileId);
				}
			}			
			projectsContext.remove(projectId);
			
			
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public Long getProjectIdByProjectInfo(ProjectItem projectInfo) {
		Long res = -1l;
		for(Object key :  projectsContext.keySet()) {
			ProjectItem value = projectsContext.get(key);
			if(value.getProjectPath().equalsIgnoreCase(projectInfo.getProjectPath())) {
				res = (Long) key; 	
				break;
			}
		}
		return res;
	}

	@Override
	public Long getProjectIdByProjectPath(String user, String projectPath) {
		Long lRes = -1L;
		String[] arrRelPath = projectPath.split(user+"\\\\");
		if(arrRelPath.length == 2) {
			String projectName = arrRelPath[1]; 
			for(Object key : projectsContext.keySet()) {
				ProjectItem value = projectsContext.get(key);
				String sProjectName = value.getProjectName(); 
				if(sProjectName.equalsIgnoreCase(projectName) ) {
					lRes = (Long) key;
					break;
				}
			}			
			
		}
		return lRes;
	}

	@Override
	public Long getProjectIdByProjectConfXml(String projectConfXml) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Long, ProjectItemImpl> getProjectsContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkFile(String fullFileNameWithPath) {
		boolean bRes = false;
		String sPath = Utils.extractJustPath(fullFileNameWithPath);
		for(Object key : projectsContext.keySet()) {
			ProjectItem value = projectsContext.get(key);
			if(value.getProjectPath().equalsIgnoreCase(sPath)) {
				break;
			}
		}
		return bRes;
	}

	@Override
	public boolean checkIsFileOpened(String path) {
		boolean res = false;
		for(Object key : openedFilesContext.keySet()) {
			FileItemInfo value = openedFilesContext.get(key);
			if(value.getAbsolutePath().equalsIgnoreCase(path)) {
				res = true; 	
				break;
			}
		}
		return res;
	}

	@Override
	public Map<Long, FileItemInfo> getOpenedFilesContext() {

		return openedFilesContext;
	}
	
	@Override
	public void setAssociatedTabWidget(Long fileId, Widget widget) {
		associatedTabWidgetsContext.put(fileId, widget);
	}

	@Override
	public Widget getAssociatedTabWidget(Long fileId) {
		return associatedTabWidgetsContext.get(fileId);
	}

/*	@Override
	public Long getProjectIdByFilePath(String fileFullPath) {
		for(Object key : filesFileInfoContext.keySet()) {
			FileItemInfo value = filesFileInfoContext.get(key);
			
			if(value.getAbsolutePath().equalsIgnoreCase(fileFullPath)) {
				
				value.
				
				return (Long) key; 	
			}
		}
		return null;
	}*/


	@Override
	public Map<Long, Widget> getAssociatedTabWidgetsContext() {
		return  associatedTabWidgetsContext;
	}

	@Override
	public void addFileToOpenedFilesContext(Long projectId, Long fileId,
			FileItemInfo fileItemInfo) {
		boolean isSuchFileExists = false;
		for(Object key : openedFilesContext.keySet()) {
			FileItemInfo value = openedFilesContext.get(key);
			if(value.getAbsolutePath().equalsIgnoreCase(fileItemInfo.getAbsolutePath())) {
				isSuchFileExists = true;
				break;
			}
		}
		if(!isSuchFileExists) {
			openedFilesContext.put(fileId, fileItemInfo);
		}
	}

	@Override
	public Long getProjectIdByRelPath(String relPath) {
		boolean isProjectFound = false;
		Long lRes = -1L;
		String regex = "[\\\\/]+";
		String[] arrRelPath = relPath.split(regex);
		
		
		for(int i=arrRelPath.length-1; i>=0; i--) {
			String curWord = arrRelPath[i]; 
			for(Object key : projectsContext.keySet()) {
				ProjectItem value = projectsContext.get(key);
				String sProjectName = value.getProjectName(); 
				if(sProjectName.equalsIgnoreCase(curWord) ) {
					lRes = (Long) key;
					isProjectFound = true;
					break;
				}
			}			
			if (isProjectFound) {
				break;
			}
		}
		
		return lRes;
	}


	@Override
	public Long addFile(FileItemInfo fileItemInfo) {
		Long res = -1l;
		boolean isSuchFileExists = false;
		for(Object key : filesFileInfoContext.keySet()) {
			FileItemInfo value = filesFileInfoContext.get(key);
			if(value.getAbsolutePath().equalsIgnoreCase(fileItemInfo.getAbsolutePath())) {
				isSuchFileExists = true;
				res = (Long) key; 	
				break;
			}
		}
		
		if(!isSuchFileExists) {
			res = getNewFileId();
			filesFileInfoContext.put(res, fileItemInfo);
		}
		
		return res;
	}

	@Override
	public boolean removeFile(Long fileId) {
		try {
			filesFileInfoContext.remove(fileId);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean checkIfFileExists(String fileFullNameWithPath) {
		boolean isSuchFileExists = false;
		for(Object key : filesFileInfoContext.keySet()) {
			FileItemInfo value = filesFileInfoContext.get(key);
			if(value.getAbsolutePath().equalsIgnoreCase(fileFullNameWithPath)) {
				isSuchFileExists = true;
				break;
			}
		}
		return isSuchFileExists;
	}

	@Override
	public FileItemInfo getFileItemInfo(Long fileId) {
		FileItemInfo value = filesFileInfoContext.get(fileId);
		return value;
	}
	
	
	
	@Override
	public Long getFileIdByFileInfo(FileItemInfo fileItemInfo) {
		Long res = -1l;
		for(Object key : filesFileInfoContext.keySet()) {
			FileItemInfo value = filesFileInfoContext.get(key);
			if(value.getAbsolutePath().equalsIgnoreCase(fileItemInfo.getAbsolutePath())) {
				res = (Long) key; 	
				break;
			}
		}
		return res;
	}

	@Override
	public Long getFileIdByFilePath(String path) {
		Long res = -1l;
		for(Object key : filesFileInfoContext.keySet()) {
			FileItemInfo value = filesFileInfoContext.get(key);
			if(value.getAbsolutePath().equalsIgnoreCase(path)) {
				res = (Long) key; 	
				break;
			}
		}
		return res;
	}

	@Override
	public void setFileState(Long fileId, Boolean bIsEdited) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeFileName(Long fileId, String FileNewName) {
		
		if (filesFileInfoContext.get(fileId) != null) {
			filesFileInfoContext.get(fileId).setAbsolutePath(FileNewName); 
			filesFileInfoContext.get(fileId).setName(Utils.extractJustFileName(FileNewName)); 
		}
		
		if (openedFilesContext.get(fileId) != null) {
			openedFilesContext.get(fileId).setAbsolutePath(FileNewName); 
			openedFilesContext.get(fileId).setName(Utils.extractJustFileName(FileNewName)); 
		}
		
		
	}

	
/*	@Override
	public Long getFileIdByFilePath(String fileFullPath) {
		for(Object key : projectsContext.keySet()) {
			ProjectItem value = projectsContext.get(key);
			if(value.getProjectPath().equalsIgnoreCase(fileFullPath)) {
				return value.getFileIdByFilePath(fileFullPath) ; 	
			}
		}
		return null;
	}*/
	
/*	
	@Override
	public void setFileContent(Long fileId, String content) {
//		filesContext.put(fileId, content);
	}
*/

/*	@Override
	public String getFileContent(Long fileId) {
//		String value = filesContext.get(fileId);
		return "";
	}	
*/	
	
}
