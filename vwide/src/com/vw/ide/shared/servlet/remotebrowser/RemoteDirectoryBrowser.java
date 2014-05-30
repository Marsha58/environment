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
	 * Creates dirrectory
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
}
