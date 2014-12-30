package com.vw.ide.shared.servlet.processor;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;

public interface CommandProcessorAsync {
	/**
	 * Compiles given project described by projectDescription
	 * @param userName
	 * @param projectDescription
	 * @return
	 */
	public void buildProject(String userName, ProjectDescription projectDescription, AsyncCallback<CommandProcessorResult> callback);
	
	/**
	 * Runs compiled project
	 * @param userName
	 * @param projectDescription
	 * @return
	 */
	public void runProject(String userName, ProjectDescription projectDescription, AsyncCallback<CommandProcessorResult> callback);
	
	/**
	 * Compiles, runs static test and export project as executable BIN file
	 * @param userName
	 * @param projectDescription
	 * @return
	 */
	public void buildAndExportProjectToExecBin(String userName, ProjectDescription projectDescription, AsyncCallback<CommandProcessorResult> callback);
}
