package com.vw.ide.client.devboardext.service.projectmanager.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectDeletionResult;

public class ProjectDeletionResultCallback extends ResultCallback<RequestProjectDeletionResult>{

	private DevelopmentBoardPresenter owner = null;
	private ProjectItemInfo itemInfo = null;

	public ProjectDeletionResultCallback(DevelopmentBoardPresenter owner, ProjectItemInfo itemInfo) {
		this.owner = owner;
		this.itemInfo = itemInfo;
	}
	
	@Override
	public void handle(RequestProjectDeletionResult result) {
		if (result.getRetCode().intValue() != 0) {
			String messageAlert = "The operation '" + result.getOperation()
					+ "' failed.\r\nResult '" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		} 
		else {
			owner.getView().getProjectPanel().deleteBranchView(itemInfo);
		}
		owner.fireEvent(new ServerLogEvent(result));
	}
}
