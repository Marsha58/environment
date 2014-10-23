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
	 * Adds file
	 * @param user
	 * @param parent
	 * @param filename
	 * @param projectId
	 * @param fileId
	 * @param content
	 */
	public RequestDirOperationResult addFile(String user, String parent,
			String fileName, Long projectId, Long fileId, String content);


	/**
	 * Opens file
	 * @param user
	 * @param parent
	 * @param filename
	 * @param projectId
	 * @param fileId
	 */
	public RequestDirOperationResult readFile(String user, String parent, String fileName, Long projectId, Long fileId);
	
	/**
	 * Delete file
	 * @param user
	 * @param fileName
	 * @param fileId
	 * @return
	 */
	RequestFileOperationResult deleteFile(String user, String fileName,	Long fileId);

	/**
	 * Save file
	 * @param user
	 * @param fileName
	 * @param projectId
	 * @param fileId
	 * @param content
	 * @return
	 */
	RequestFileOperationResult saveFile(String user, String fileName,	Long projectId, Long fileId, String content);

	/**
	 * Closes file
	 * @param user
	 * @param fileName
	 * @param fileId
	 * @return
	 */
	RequestFileOperationResult closeFile(String user, String fileName,	Long fileId);

	/**
	 * Renames file
	 * @param user
	 * @param fileName
	 * @param fileId
	 * @param fileNewName
	 * @return
	 */
	RequestFileOperationResult renameFile(String user, String fileName,	Long fileId, String fileNewName);
	
}
