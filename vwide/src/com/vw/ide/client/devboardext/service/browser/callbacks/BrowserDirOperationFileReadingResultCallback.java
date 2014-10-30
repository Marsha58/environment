package com.vw.ide.client.devboardext.service.browser.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;

public class DirOperationFileReadingResultCallback extends ResultCallback<RequestDirOperationResult> {

	private DevelopmentBoardPresenter owner = null;
	private ProjectItemInfo projectItemInfo = null;
	
	public DirOperationFileReadingResultCallback(DevelopmentBoardPresenter owner, ProjectItemInfo projectItemInfo) {
		this.owner = owner;
		this.projectItemInfo = projectItemInfo;
	}

	@Override
	public void handle(RequestDirOperationResult result) {
		if (result.getRetCode().intValue() != 0) {
			String messageAlert = "The operation '" + result.getOperation()
								+ "' failed.\r\nResult'" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox("Warning", messageAlert);
			alertMessageBox.show();
		} 
		else {
			projectItemInfo.getAssociatedData().setContent(result.getTextFile());
			owner.getView().addNewFileTabItem(projectItemInfo);
		}
		owner.fireEvent(new ServerLogEvent(result));
	}
}
