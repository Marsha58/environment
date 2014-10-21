package com.vw.ide.client.service.remote.browser;

import com.vw.ide.client.service.remote.Result;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.browser.RemoteBrowserService.ServiceCallbackForAnyOperation;
import com.vw.ide.client.service.remote.browser.RemoteBrowserService.ServiceCallbackForCompleteContent;
import com.vw.ide.client.service.remote.browser.RemoteBrowserService.ServiceCallbackForDirOperation;
import com.vw.ide.client.service.remote.browser.RemoteBrowserService.ServiceCallbackForFileOperation;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowserAsync;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestFileOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestedDirScanResult;

public class RemoteBrowserServiceBroker {
	
	/**
	 * Requests for content of a remote directory
	 * @param user
	 * @param parent
	 * @param resultCallback
	 */
	public static void requestForDirContent(String user, String parent, ResultCallback<RequestedDirScanResult> resultCallback) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForCompleteContent cbk = RemoteBrowserService.instance().buildCallbackForCompleteContent();
			cbk.setProcessedResult(new Result<RequestedDirScanResult>(resultCallback));
			service.getDirScan(user, parent, cbk);
		}
	}
	
	/**
	 * Requests to read file by its name and project id
	 * @param user
	 * @param fileName
	 * @param projectId
	 * @param fileId
	 */
	public static void requestForReadingFile(String user, String fileName, Long projectId, Long fileId, ResultCallback<RequestDirOperationResult> resultCallback) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForAnyOperation cbk = RemoteBrowserService.instance().buildCallbackForAnyOperation();
			cbk.setProcessedResult(new Result<RequestDirOperationResult>(resultCallback));
			service.readFile(user, "", fileName, projectId, fileId, cbk);
		}
	}

	/**
	 * Requests for deleting project
	 * @param user
	 * @param projectPath
	 * @param projectId
	 */
	public static void requestForDeletingProject(String user, String projectPath, Long projectId, ResultCallback<RequestDirOperationResult> resultCallback) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForDirOperation cbk = RemoteBrowserService.instance().buildCallbackForDirOperation();
			cbk.setProcessedResult(new Result<RequestDirOperationResult>(resultCallback));
			service.deleteProject(user, projectPath, projectId, cbk);
		}
	}
	
	/**
	 * Requests for file closing
	 * @param user 
	 * @param fileName
	 * @param fileId
	 */
	public static void requestForFileClosing(String user, String fileName, Long fileId, ResultCallback<RequestFileOperationResult> resultCallback) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForFileOperation cbk = RemoteBrowserService.instance().buildCallbackForFileOperation();
			cbk.setProcessedResult(new Result<RequestFileOperationResult>(resultCallback));
			service.closeFile(user, fileName, fileId, cbk);
		}
	}

	/**
	 * Requests for file storing
	 * @param user
	 * @param fileName
	 * @param projectId
	 * @param fileId
	 * @param content
	 * @param resultCallback
	 */
	public static void requestForFileSaving(String user, String fileName, Long projectId, Long fileId, String content, ResultCallback<RequestFileOperationResult> resultCallback) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForFileOperation cbk = RemoteBrowserService.instance().buildCallbackForFileOperation();
			cbk.setProcessedResult(new Result<RequestFileOperationResult>(resultCallback));
			service.saveFile(user, fileName, projectId, fileId, content, cbk);
		}
	}
	
	/**
	 * Requests for file deletion
	 * @param user
	 * @param fileName
	 * @param fileId
	 * @param resultCallback
	 */
	public static void requestForFileDeleting(String user, String fileName, Long fileId, ResultCallback<RequestFileOperationResult> resultCallback) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForFileOperation cbk = RemoteBrowserService.instance().buildCallbackForFileOperation();
			cbk.setProcessedResult(new Result<RequestFileOperationResult>(resultCallback));
			service.deleteFile(user, fileName, fileId, cbk);
		}
	}

	/**
	 * Requests for file creation
	 * @param user
	 * @param parent
	 * @param fileName
	 * @param projectId
	 * @param fileId
	 * @param content
	 * @param resultCallback
	 */
	public static void requestForFileCreating(String user, String parent, String fileName, Long projectId, Long fileId, String content, ResultCallback<RequestDirOperationResult> resultCallback) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForDirOperation cbk = RemoteBrowserService.instance().buildCallbackForDirOperation();
			cbk.setProcessedResult(new Result<RequestDirOperationResult>(resultCallback));
			service.addFile(user, parent, fileName, projectId, fileId, content, cbk);
		}
	}
	
	/**
	 * Requests for folder creation
	 * @param user
	 * @param folderParentPath
	 * @param folderName
	 * @param resultCallback
	 */
	public static void requestForFolderCreating(String user, String folderParentPath, String folderName, ResultCallback<RequestDirOperationResult> resultCallback) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForDirOperation cbk = RemoteBrowserService.instance().buildCallbackForDirOperation();
			cbk.setProcessedResult(new Result<RequestDirOperationResult>(resultCallback));
			service.createDir(user, folderParentPath, folderName, cbk);
		}
	}
	
	/**
	 * Requests for renaming of file
	 * @param user
	 * @param fileName
	 * @param fileId
	 * @param fileNewName
	 * @param resultCallback
	 */
	public static void requestForFileRenaming(String user, String fileName, Long fileId, String fileNewName, ResultCallback<RequestFileOperationResult> resultCallback) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForFileOperation cbk = RemoteBrowserService.instance().buildCallbackForFileOperation();
			cbk.setProcessedResult(new Result<RequestFileOperationResult>(resultCallback));
			service.renameFile(user, fileName, fileId, fileNewName, cbk);
		}
	}
}
