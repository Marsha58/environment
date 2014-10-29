package com.vw.ide.client.devboardext.service.projectmanager.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectRemoveFileResult;

public class ProjectRemoveFileResultCallback extends ResultCallback<RequestProjectRemoveFileResult> {

	private DevelopmentBoardPresenter owner;
	
	public ProjectRemoveFileResultCallback(DevelopmentBoardPresenter owner) {
		this.owner = owner;
	}

	@Override
	public void handle(RequestProjectRemoveFileResult result) {
		if (result.getRetCode().intValue() != 0) {
			String messageAlert = "The operation '" + result.getOperation()
					+ "' failed.\r\nResult'" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		} 
		owner.fireEvent(new ServerLogEvent(result));
	}

}
