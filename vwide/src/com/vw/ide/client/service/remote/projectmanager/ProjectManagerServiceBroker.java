package com.vw.ide.client.service.remote.projectmanager;

import com.vw.ide.client.service.remote.Result;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerService.ServiceCallbackForAddFileToProject;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerService.ServiceCallbackForAvailableProjects;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerService.ServiceCallbackForProjectCreation;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerService.ServiceCallbackForProjectDeletion;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerService.ServiceCallbackForRemoveFileFromProject;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerService.ServiceCallbackForRenameFileOnProject;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerService.ServiceCallbackForUpdatingProject;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.projectmanager.RemoteProjectManagerServiceAsync;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectAddFileResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectCreationResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectDeletionResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectRemoveFileResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectRenameFileResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectUpdateResult;
import com.vw.ide.shared.servlet.projectmanager.RequestUserAvailableProjectResult;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

public class ProjectManagerServiceBroker {

	/**
	 * Request for project creation
	 * @param description
	 * @param resultCallback
	 */
	public static void requestForProjectCreation(ProjectDescription description,
												 ResultCallback<RequestProjectCreationResult> resultCallback) {
		RemoteProjectManagerServiceAsync service = ProjectManagerService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForProjectCreation cbk = ProjectManagerService.instance().buildCallbackForProjectCreation();
			cbk.setProcessedResult(new Result<RequestProjectCreationResult>(resultCallback));
			service.createProject(description, cbk);
		}
	}
	
	/**
	 * Requests for deleting project
	 * @param description
	 * @param projectId
	 */
	public static void requestForDeletingProject(ProjectDescription description, ResultCallback<RequestProjectDeletionResult> resultCallback) {
		RemoteProjectManagerServiceAsync service = ProjectManagerService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForProjectDeletion cbk = ProjectManagerService.instance().buildCallbackForProjectDeletion();
			cbk.setProcessedResult(new Result<RequestProjectDeletionResult>(resultCallback));
			service.deleteProject(description, cbk);
		}
	}
	
	/**
	 * Requests for updating project
	 * @param description
	 */
	public static void requestForUpdatingProject(ProjectDescription description, ResultCallback<RequestProjectUpdateResult> resultCallback) {
		RemoteProjectManagerServiceAsync service = ProjectManagerService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForUpdatingProject cbk = ProjectManagerService.instance().buildCallbackForUpdatingProject();
			cbk.setProcessedResult(new Result<RequestProjectUpdateResult>(resultCallback));
			service.updateProject(description, cbk);
		}
	}

	/**
	 * Requests for available projects
	 * @param user
	 * @param description
	 */
	public static void requestForAvailableProjects(String user, ResultCallback<RequestUserAvailableProjectResult> resultCallback) {
		RemoteProjectManagerServiceAsync service = ProjectManagerService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForAvailableProjects cbk = ProjectManagerService.instance().buildCallbackForAvailableProjects();
			cbk.setProcessedResult(new Result<RequestUserAvailableProjectResult>(resultCallback));
			service.getUserAvailableProjects(user, cbk);
		}
	}
	
	/**
	 * Requests for adding file to project
	 * @param user
	 * @param projectDescription
	 * @param fileInfo
	 */
	public static void requestForAddingFileToProject(String user, ProjectDescription projectDescription, FileItemInfo fileInfo, ResultCallback<RequestProjectAddFileResult> resultCallback) {
		RemoteProjectManagerServiceAsync service = ProjectManagerService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForAddFileToProject cbk = ProjectManagerService.instance().buildCallbackForAddFileToProject();
			cbk.setProcessedResult(new Result<RequestProjectAddFileResult>(resultCallback));
			service.addFileToProject(projectDescription, fileInfo, cbk);
		}
	}

	/**
	 * Requests for removing file from project
	 * @param projectDescription
	 * @param fileInfo
	 */
	public static void requestForRemovingFileFromProject(ProjectDescription projectDescription, FileItemInfo fileInfo, ResultCallback<RequestProjectRemoveFileResult> resultCallback) {
		RemoteProjectManagerServiceAsync service = ProjectManagerService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForRemoveFileFromProject cbk = ProjectManagerService.instance().buildCallbackForRemoveFileFromProject();
			cbk.setProcessedResult(new Result<RequestProjectRemoveFileResult>(resultCallback));
			service.removeFileFromProject(projectDescription, fileInfo, cbk);
		}
	}

	/**
	 * Requests for renaming file on project
	 * @param projectDescription
	 * @param fileInfo
	 * @param newName
	 */
	public static void requestForRenamingFileOnProject(ProjectDescription projectDescription, FileItemInfo fileInfo, FileItemInfo newName, ResultCallback<RequestProjectRenameFileResult> resultCallback) {
		RemoteProjectManagerServiceAsync service = ProjectManagerService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForRenameFileOnProject cbk = ProjectManagerService.instance().buildCallbackForRenameFileOnProject();
			cbk.setProcessedResult(new Result<RequestProjectRenameFileResult>(resultCallback));
			service.renameFileFromProject(projectDescription, fileInfo, newName, cbk);
		}
	}
}
