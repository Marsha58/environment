package com.vw.ide.shared.servlet.processor;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.vw.ide.shared.servlet.processor.dto.sandr.SearchAndReplaceBundle;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;

/**
 * Dispacthes and processes IDE's commands related to project building phases
 * @author Oleg
 *
 */
@RemoteServiceRelativePath("processor")
public interface CommandProcessor extends RemoteService {

	/**
	 * Compiles given project described by projectDescription
	 * @param userName
	 * @param projectDescription
	 * @return
	 */
	public CommandProcessorResult buildProject(String userName, ProjectDescription projectDescription);
	
	/**
	 * Runs compiled project
	 * @param userName
	 * @param projectDescription
	 * @return
	 */
	public CommandProcessorResult runProject(String userName, ProjectDescription projectDescription);
	
	/**
	 * Compiles, runs static test and export project as WAR file
	 * @param userName
	 * @param projectDescription
	 * @return
	 */
	public CommandProcessorResult buildAndExportProjectToExecBin(String userName, ProjectDescription projectDescription);

	/**
	 * Performs search and replace action
	 * @param userName
	 * @param searchAndReplaceBundle
	 * @return
	 */
	public CommandProcessorResult performSearchAndReplace(String userName, SearchAndReplaceBundle searchAndReplaceBundle);
}
