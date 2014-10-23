package com.vw.ide.shared.servlet.remotebrowser;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

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
	 * Adding file to theProject
	 * @param user
	 * @param parent
	 * @param fileName
	 * @param projectId
	 * @param content
	 */
	public void addFile(String user, String parent, String fileName, Long projectId,
			Long fileId, String content, AsyncCallback<RequestDirOperationResult> callback);


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
	 * Closes file
	 * @param user
	 * @param fileName
	 * @param fileId
	 * @param callback
	 */
	void closeFile(String user, String fileName, Long fileId, AsyncCallback<RequestFileOperationResult> callback);

	/**
	 * Save a file
	 * @param user
	 * @param fileName
	 * @param fileId
	 * @param fileNewName
	 */
	void renameFile(String user, String fileName, Long fileId, 	String fileNewName, AsyncCallback<RequestFileOperationResult> callback);
	
}
