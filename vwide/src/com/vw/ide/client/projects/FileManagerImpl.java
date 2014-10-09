package com.vw.ide.client.projects;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

public class FileManagerImpl implements FileManager {

	private static Long idsCounter = 0l;
	
	Map<Long, FileItemInfo> filesFileInfoContext =  new HashMap <Long, FileItemInfo>();
	Map<Long, String> filesContext =  new HashMap <Long, String>();
	Map<Long, Widget> associatedTabWidgets =  new HashMap <Long, Widget>();
	
	
	private Long getNewFileId() {
		return ++idsCounter;
	}

	public Long getLastFileId() {
		return idsCounter;
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
			filesContext.put(res, "");
		}
		
		return res;
	}

	@Override
	public boolean removeFile(Long fileId) {
		try {
			filesContext.remove(fileId);
			filesFileInfoContext.remove(fileId);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean checkIfFileExists(String fileName) {
		boolean isSuchFileExists = false;
		for(Object key : filesFileInfoContext.keySet()) {
			FileItemInfo value = filesFileInfoContext.get(key);
			if(value.getName().equalsIgnoreCase(fileName)) {
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
	public void setFileContent(Long fileId, String content) {
		filesContext.put(fileId, content);
	}
	
	@Override
	public String getFileContent(Long fileId) {
		String value = filesContext.get(fileId);
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


	public boolean checkIsFileOpened(String path) {
		boolean res = false;
		for(Object key : filesFileInfoContext.keySet()) {
			FileItemInfo value = filesFileInfoContext.get(key);
			if(value.getAbsolutePath().equalsIgnoreCase(path)) {
				res = true; 	
				break;
			}
		}
		return res;
	}

	@Override
	public void setAssociatedTabWidget(Long fileId, Widget widget) {
		associatedTabWidgets.put(fileId, widget);
		
	}

	@Override
	public Widget getAssociatedTabWidget(Long fileId) {
		return associatedTabWidgets.get(fileId);
	}	
	

	@Override
	public Map<Long, Widget> getAssociatedTabWidgets() {
		return associatedTabWidgets;
	}	
	
	@Override
	public Map<Long, FileItemInfo> getFilesFileInfoContext() {
		return filesFileInfoContext;
	}	
	
	@Override
	public Map<Long, String> getFilesContext() {
		return filesContext;
	}	
	

}
