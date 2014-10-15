package com.vw.ide.shared.servlet.remotebrowser;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Implemented by Remote directory browser servlet
 * @author Oleg
 *
 */
@RemoteServiceRelativePath("dirbrowser")
public interface RemoteDirectoryBrowser extends RemoteService {
	/**
	 * Returns list of directories, for given user, from the parent 'dir'
	 * @param user
	 * @param dir
	 * @return
	 */
	public List<String> getListOfDirectories(String user, String dir);
	
	/**
	 * Returns combined list of subdirs and files
	 * @param user
	 * @param dir
	 * @return
	 */
	public RequestedDirScanResult getDirScan(String user, String dir);
	
	/**
	 * Creates directory
	 * @param user
	 * @param parent
	 * @param dir
	 */
	public RequestDirOperationResult createDir(String user, String parent, String dir);
	
	/**
	 * Removes directory
	 * @param user
	 * @param parent
	 * @param dir
	 */
	public RequestDirOperationResult removeDir(String user, String parent, String dir);
	
	
	/**
	 * Creates new project
	 * @param userName
	 * @param projectName
	 * @param projectPath 
	 * @param packageName
	 * @param javaSrcPath
	 * @param author
	 * @param descr
	 */
	public RequestProjectCreationResult createProject(String userName,
			String projectName, String projectPath, String packageName, String javaSrcPath,
			String author, String descr);

	/**
	 * Deletes project
	 * @param userName
	 * @param projectName
	 * @param projectId
	 */
	public RequestDirOperationResult deleteProject(String userName,
			String projectName, Long projectId);

	
	/**
	 * Adds file
	 * @param user
	 * @param parent
	 * @param filename
	 * @param projectId
	 * @param fileId
	 */
	public RequestDirOperationResult addFile(String user, String parent,
			String fileName, Long projectId, Long fileId);


	/**
	 * Opens file
	 * @param user
	 * @param parent
	 * @param filename
	 * @param projectId
	 * @param fileId
	 */
	public RequestDirOperationResult readFile(String user, String parent,
			String fileName, Long projectId, Long fileId);
	
	
	RequestFileOperationResult deleteFile(String user, String fileName,	Long fileId);

	
	RequestFileOperationResult saveFile(String user, String fileName,	Long projectId, Long fileId, String content);
	
	RequestUserStateResult getUserState(String user);

	
	
}
