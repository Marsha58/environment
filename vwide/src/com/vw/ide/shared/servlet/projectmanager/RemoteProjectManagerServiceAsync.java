package com.vw.ide.shared.servlet.projectmanager;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

public interface RemoteProjectManagerServiceAsync {
	
	/**
	 * Project creation
	 * @param description
	 */
	public void createProject(ProjectDescription description, AsyncCallback<RequestProjectCreationResult> callback);

	/**
	 * Deletes existing project
	 * @param description
	 * @return
	 */
	public void deleteProject(ProjectDescription description, AsyncCallback<RequestProjectDeletionResult> callback);

	/**
	 * Updates project
	 * @param description
	 * @return
	 */
	public void updateProject(ProjectDescription description, AsyncCallback<RequestProjectUpdateResult> callback);

	/**
	 * Import project
	 * @param projDescr
	 * @param userName
	 * @param vwmlProjFile
	 * @param phase
	 * @return
	 */
	public void importProject(ProjectDescription projDescr, String userName, FileItemInfo mainVWMLProjFile, Integer phase, AsyncCallback<RequestProjectImportResult> callback);

	/**
	 * Returns list of user's projects
	 * @param userName
	 * @return
	 */
	public void getUserAvailableProjects(String userName, AsyncCallback<RequestUserAvailableProjectResult> callback);

	/**
	 * Adding file to project
	 * @param description
	 * @param toAdd
	 * @return
	 */
	public void addFileToProject(ProjectDescription description, FileItemInfo toAdd, AsyncCallback<RequestProjectAddFileResult> callback);

	/**
	 * Removing file from project
	 * @param description
	 * @param toRemove
	 * @return
	 */
	public void removeFileFromProject(ProjectDescription description, FileItemInfo toRemove, AsyncCallback<RequestProjectRemoveFileResult> callback);

	/**
	 * Renaming file from project
	 * @param description
	 * @param toRemove
	 * @patam newName
	 * @return
	 */
	public void renameFileFromProject(ProjectDescription description, FileItemInfo toRename, FileItemInfo newName, AsyncCallback<RequestProjectRenameFileResult> callback);

	/**
	 * Moving file/directory inside the given project
	 * @param description
	 * @param fromItem
	 * @param toItem
	 * @return
	 */
	public void moveItemOnProject(ProjectDescription description, FileItemInfo fromItem, FileItemInfo toItem, AsyncCallback<RequestProjectMoveItemResult> callback);
}

