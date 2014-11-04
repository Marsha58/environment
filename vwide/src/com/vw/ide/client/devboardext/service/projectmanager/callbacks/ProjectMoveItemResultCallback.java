package com.vw.ide.client.devboardext.service.projectmanager.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectMoveItemResult;

public class ProjectMoveItemResultCallback extends ResultCallback<RequestProjectMoveItemResult> {

	private DevelopmentBoardPresenter owner;
	@SuppressWarnings("unused")
	private ProjectDescription description;
	
	public ProjectMoveItemResultCallback(DevelopmentBoardPresenter owner, ProjectDescription description) {
		this.owner = owner;
		this.description = description;
	}
	
	@Override
	public void handle(RequestProjectMoveItemResult result) {
		if (result.getRetCode().intValue() != 0) {
			String messageAlert = "The operation '" + result.getOperation()
					+ "' failed.\r\nResult '" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		}
		owner.fireEvent(new ServerLogEvent(result));
	}
}
