package com.vw.ide.shared.servlet.remotebrowser;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForFileOperation;

public interface RemoteDirectoryBrowserAsync {
	/**
	 * Returns list of directories, for given user, from the parent 'dir'
	 * @param user
	 * @param dir
	 * @return
	 */
	public void getListOfDirectories(String user, String dir, AsyncCallback<List<String>> callback);
	
	/**
	 * Returns combined list of subdirs and files
	 * @param user
	 * @param dir
	 * @return
	 */
	public void getDirScan(String user, String dir, AsyncCallback<RequestedDirScanResult> callback);
	
	/**
	 * Creates directory
	 * @param user
	 * @param parent
	 * @param dir
	 */
	public void createDir(String user, String parent, String dir, AsyncCallback<RequestDirOperationResult> callback);
	
	/**
	 * Removes directory
	 * @param user
	 * @param parent
	 * @param dir
	 */
	public void removeDir(String user, String parent, String dir, AsyncCallback<RequestDirOperationResult> callback);

	

	/**
	 * Project creating
	 * @param userName
	 * @param projectName
	 * @param packageName
	 * @param javaSrcPath
	 * @param author
	 * @param descr
	 */
	public void createProject(String userName,String projectName, String projectPath, String packageName,
			String javaSrcPath, String author, String descr, AsyncCallback<RequestProjectCreationResult> callback);

	/**
	 * Project deleting
	 * @param userName
	 * @param projectName
	 * @param projectId
	 */
	public void deleteProject(String userName, String projectName, Long projectId,
			AsyncCallback<RequestDirOperationResult> cbk);

	/**
	 * Adding file to theProject
	 * @param user
	 * @param parent
	 * @param fileName
	 * @param projectId
	 */
	public void addFile(String user, String parent, String fileName, Long projectId,
			Long fileId, AsyncCallback<RequestDirOperationResult> callback);


	/**
	 * Reads a file
	 * @param user
	 * @param parent
	 * @param fileName
	 * @param fileId
	 */
	public void readFile(String user, String parent, String fileName, Long projectId, Long fileId,  AsyncCallback<RequestDirOperationResult> callback);
	

	/**
	 * Save a file
	 * @param user
	 * @param parent
	 * @param fileName
	 * @param projectId
	 * @param fileId
	 */
	public void saveFile(String user, String fileName, Long projectId, Long fileId, String content,  AsyncCallback<RequestFileOperationResult> callback);
	
	/**
	 * Deleting file
	 * @param user
	 * @param fileName
	 * @param fileId
	 */
	public void deleteFile(String user, String fileName, Long fileId, 	AsyncCallback<RequestFileOperationResult> callback);

	/**
	 * Getting information about user state
	 * @param user
	 */	
	public void getUserState(String user, AsyncCallback<RequestUserStateResult> callback);
	
}
