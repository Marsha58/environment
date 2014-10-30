package com.vw.ide.client.service.remote.browser;

import com.vw.ide.client.service.remote.Result;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.browser.DirBrowserService.ServiceCallbackForAnyOperation;
import com.vw.ide.client.service.remote.browser.DirBrowserService.ServiceCallbackForCompleteContent;
import com.vw.ide.client.service.remote.browser.DirBrowserService.ServiceCallbackForDirOperation;
import com.vw.ide.client.service.remote.browser.DirBrowserService.ServiceCallbackForFileOperation;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowserAsync;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestFileOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestedDirScanResult;

public class DirBrowserServiceBroker {
	
	/**
	 * Requests for content of a remote directory
	 * @param user
	 * @param parent
	 * @param resultCallback
	 */
	public static void requestForDirContent(String user, String parent, ResultCallback<RequestedDirScanResult> resultCallback) {
		RemoteDirectoryBrowserAsync service = DirBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForCompleteContent cbk = DirBrowserService.instance().buildCallbackForCompleteContent();
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
	public static void requestForReadingFile(String user, String path, String fileName, Long projectId, Long fileId, ResultCallback<RequestDirOperationResult> resultCallback) {
		RemoteDirectoryBrowserAsync service = DirBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForAnyOperation cbk = DirBrowserService.instance().buildCallbackForAnyOperation();
			cbk.setProcessedResult(new Result<RequestDirOperationResult>(resultCallback));
			service.readFile(user, path, fileName, projectId, fileId, cbk);
		}
	}

	/**
	 * Requests for file closing
	 * @param user 
	 * @param fileName
	 * @param fileId
	 */
	public static void requestForFileClosing(String user, String fileName, Long fileId, ResultCallback<RequestFileOperationResult> resultCallback) {
		RemoteDirectoryBrowserAsync service = DirBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForFileOperation cbk = DirBrowserService.instance().buildCallbackForFileOperation();
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
	public static void requestForFileSaving(String user, String path, String fileName, Long projectId, Long fileId, String content, ResultCallback<RequestFileOperationResult> resultCallback) {
		RemoteDirectoryBrowserAsync service = DirBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForFileOperation cbk = DirBrowserService.instance().buildCallbackForFileOperation();
			cbk.setProcessedResult(new Result<RequestFileOperationResult>(resultCallback));
			service.saveFile(user, path, fileName, projectId, fileId, content, cbk);
		}
	}
	
	/**
	 * Requests for file deletion
	 * @param user
	 * @param fileName
	 * @param fileId
	 * @param resultCallback
	 */
	public static void requestForFileDeleting(String user, String path, String fileName, Long fileId, ResultCallback<RequestFileOperationResult> resultCallback) {
		RemoteDirectoryBrowserAsync service = DirBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForFileOperation cbk = DirBrowserService.instance().buildCallbackForFileOperation();
			cbk.setProcessedResult(new Result<RequestFileOperationResult>(resultCallback));
			service.deleteFile(user, path, fileName, fileId, cbk);
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
		RemoteDirectoryBrowserAsync service = DirBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForDirOperation cbk = DirBrowserService.instance().buildCallbackForDirOperation();
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
		RemoteDirectoryBrowserAsync service = DirBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForDirOperation cbk = DirBrowserService.instance().buildCallbackForDirOperation();
			cbk.setProcessedResult(new Result<RequestDirOperationResult>(resultCallback));
			service.createDir(user, folderParentPath, folderName, cbk);
		}
	}

	/**
	 * Requests for folder deletion
	 * @param user
	 * @param folderParentPath
	 * @param folderName
	 * @param resultCallback
	 */
	public static void requestForFolderDeletion(String user, String folderParentPath, String folderName, ResultCallback<RequestDirOperationResult> resultCallback) {
		RemoteDirectoryBrowserAsync service = DirBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForDirOperation cbk = DirBrowserService.instance().buildCallbackForDirOperation();
			cbk.setProcessedResult(new Result<RequestDirOperationResult>(resultCallback));
			service.removeDir(user, folderParentPath, folderName, cbk);
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
		RemoteDirectoryBrowserAsync service = DirBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForFileOperation cbk = DirBrowserService.instance().buildCallbackForFileOperation();
			cbk.setProcessedResult(new Result<RequestFileOperationResult>(resultCallback));
			service.renameFile(user, fileName, fileId, fileNewName, cbk);
		}
	}
}
