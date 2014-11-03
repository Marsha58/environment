package com.vw.ide.client.devboardext.service.projectmanager;

import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;

/**
 * Some helpers methods for operation with local project manager's data
 * @author Oleg
 *
 */
public class ProjectManagerUtils {
	public static void updateLocalProjectDescription(ProjectDescription local, ProjectDescription remote) {
		local.getProjectFiles().clear();
		local.setProjectFiles(remote.getProjectFiles());
	}
}
