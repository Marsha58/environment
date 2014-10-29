package com.vw.ide.client.devboardext.service.browser.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectDeletionResult;

public class ProjectDeletionResultCallback extends ResultCallback<RequestProjectDeletionResult>{

	private DevelopmentBoardPresenter owner = null;

	public ProjectDeletionResultCallback(DevelopmentBoardPresenter owner) {
		this.owner = owner;
	}
	
	@Override
	public void handle(RequestProjectDeletionResult result) {
		if (result.getRetCode().intValue() != 0) {
			String messageAlert = "The operation '" + result.getOperation()
					+ "' failed.\r\nResult'" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		} 
		else {
			owner.getView().getProjectPanel().requestDirContent(null);
		}
		owner.fireEvent(new ServerLogEvent(result));
	}
}
