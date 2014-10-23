package com.vw.ide.shared.servlet.projectmanager;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteProjectManagerServiceAsync {
	
	/**
	 * Project creation
	 * @param description
	 */
	public void createProject(ProjectDescription description, AsyncCallback<RequestProjectCreationResult> callback);

	/**
	 * Deletes existing project
	 * @param userName
	 * @param projectName
	 * @param projectId
	 * @return
	 */
	public void deleteProject(String userName, String projectName, Long projectId, AsyncCallback<RequestProjectDeletionResult> callback);
}
