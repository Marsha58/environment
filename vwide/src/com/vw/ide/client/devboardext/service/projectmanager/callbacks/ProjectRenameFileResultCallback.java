package com.vw.ide.client.devboardext.service.projectmanager.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerServiceBroker;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectRenameFileResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectUpdateResult;

public class ProjectRenameFileResultCallback extends ResultCallback<RequestProjectRenameFileResult> {

	public static class ProjectUpdateResult extends ResultCallback<RequestProjectUpdateResult> {

		public ProjectUpdateResult() {
		}

		@Override
		public void handle(RequestProjectUpdateResult result) {
			if (result.getRetCode().intValue() != 0) {
				String messageAlert = "The operation '" + result.getOperation()
						+ "' failed.\r\nResult '" + result.getResult() + "'";
				AlertMessageBox alertMessageBox = new AlertMessageBox(
						"Warning", messageAlert);
				alertMessageBox.show();
			}
		}
	}
	
	private DevelopmentBoardPresenter owner;
	private ProjectDescription description;
	
	public ProjectRenameFileResultCallback(DevelopmentBoardPresenter owner, ProjectDescription description) {
		this.owner = owner;
		this.description = description;
	}

	@Override
	public void handle(RequestProjectRenameFileResult result) {
		if (result.getRetCode().intValue() != 0) {
			String messageAlert = "The operation '" + result.getOperation()
					+ "' failed.\r\nResult '" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		}
		else {
			ProjectManagerServiceBroker.requestForUpdatingProject(description, new ProjectUpdateResult());
		}
		owner.fireEvent(new ServerLogEvent(result));
	}

}
