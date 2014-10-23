package com.vw.ide.client.service.remote.projectmanager;

import com.vw.ide.client.service.remote.Result;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerService.ServiceCallbackForProjectCreation;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerService.ServiceCallbackForProjectDeletion;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.projectmanager.RemoteProjectManagerServiceAsync;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectCreationResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectDeletionResult;

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
	 * @param user
	 * @param projectPath
	 * @param projectId
	 */
	public static void requestForDeletingProject(String user, String projectPath, Long projectId, ResultCallback<RequestProjectDeletionResult> resultCallback) {
		RemoteProjectManagerServiceAsync service = ProjectManagerService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForProjectDeletion cbk = ProjectManagerService.instance().buildCallbackForProjectDeletion();
			cbk.setProcessedResult(new Result<RequestProjectDeletionResult>(resultCallback));
			service.deleteProject(user, projectPath, projectId, cbk);
		}
	}
}
