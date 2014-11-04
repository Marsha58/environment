package com.vw.ide.shared.servlet.projectmanager;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

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
	 * @param description
	 * @return
	 */
	public RequestProjectDeletionResult deleteProject(ProjectDescription description);

	/**
	 * Updates project
	 * @param description
	 * @return
	 */
	public RequestProjectUpdateResult updateProject(ProjectDescription description);
	
	/**
	 * Returns list of user's projects
	 * @param userName
	 * @return
	 */
	public RequestUserAvailableProjectResult getUserAvailableProjects(String userName);

	/**
	 * Adding file to project
	 * @param description
	 * @param toAdd
	 * @return
	 */
	public RequestProjectAddFileResult addFileToProject(ProjectDescription description, FileItemInfo toAdd);

	/**
	 * Removing file from project
	 * @param description
	 * @param toRemove
	 * @return
	 */
	public RequestProjectRemoveFileResult removeFileFromProject(ProjectDescription description, FileItemInfo toRemove);

	/**
	 * Renaming file from project
	 * @param description
	 * @param toRemove
	 * @param newName
	 * @return
	 */
	public RequestProjectRenameFileResult renameFileFromProject(ProjectDescription description, FileItemInfo toRename, FileItemInfo newName);

	/**
	 * Moving file/directory inside the given project
	 * @param description
	 * @param fromItem
	 * @param toItem
	 * @return
	 */
	public RequestProjectMoveItemResult moveItemOnProject(ProjectDescription description, FileItemInfo fromItem, FileItemInfo toItem);
}
