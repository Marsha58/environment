package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.StartProjectExecutionHandler;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;

/**
 * Fired when project is going to be compiled and executed
 * @author Oleg
 *
 */
public class StartProjectExecutionEvent extends GwtEvent<StartProjectExecutionHandler> {
	private ProjectDescription projectToProcess = null;
	
	public static Type<StartProjectExecutionHandler> TYPE = new Type<StartProjectExecutionHandler>();

	public StartProjectExecutionEvent() {
		super();
	}

	public StartProjectExecutionEvent(ProjectDescription projectToProcess) {
		super();
		this.projectToProcess = projectToProcess;
	}

	public ProjectDescription getProjectToProcess() {
		return projectToProcess;
	}

	public void setProjectToProcess(ProjectDescription projectToProcess) {
		this.projectToProcess = projectToProcess;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<StartProjectExecutionHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(StartProjectExecutionHandler handler) {
		handler.onStartProjectExecution(this);
	}
}
