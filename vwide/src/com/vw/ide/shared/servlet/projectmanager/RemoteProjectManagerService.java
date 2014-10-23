package com.vw.ide.shared.servlet.projectmanager;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Defines project managment's logic on server side
 * @author Oleg
 *
 */
@RemoteServiceRelativePath("projectmanager")
public interface RemoteProjectManagerService extends RemoteService {

	/**
	 * Creates new project
	 * @param description
	 */
	public RequestProjectCreationResult createProject(ProjectDescription description);

	/**
	 * Deletes existing project
	 * @param userName
	 * @param projectName
	 * @param projectId
	 * @return
	 */
	public RequestProjectDeletionResult deleteProject(String userName, String projectName, Long projectId);
}
