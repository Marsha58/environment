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
	 * Creates directory
	 * @param user
	 * @param parent
	 * @param filaName
	 */
	public void readFile(String user, String parent, String fileName, AsyncCallback<RequestDirOperationResult> callback);

	/**
	 * Project creating
	 * @param projectName
	 * @param params
	 */
	public void createProject(String userName,String projectName, String packageName,
			String javaSrcPath, String author, String descr, AsyncCallback<RequestProjectCreationResult> callback);
	
}
