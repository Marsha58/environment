package com.vw.ide.client.devboardext.service.projectmanager.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.service.projectmanager.ProjectManagerUtils;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectAddFileResult;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

public class ProjectAddFileResultCallback extends ResultCallback<RequestProjectAddFileResult> {

	private DevelopmentBoardPresenter owner;
	private ProjectItemInfo parent;
	private FileItemInfo toAdd;
	
	public ProjectAddFileResultCallback(DevelopmentBoardPresenter owner, ProjectItemInfo parent, FileItemInfo toAdd) {
		this.owner = owner;
		this.parent = parent;
		this.toAdd = toAdd;
	}
	
	@Override
	public void handle(RequestProjectAddFileResult result) {
		if (result.getRetCode().intValue() != 0) {
			String messageAlert = "The operation '" + result.getOperation()
					+ "' failed.\r\nResult '" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		} 
		else {
			owner.getView().getAddOperationBlock().addProjectItemAndSelect(parent, toAdd);
			ProjectManagerUtils.updateLocalProjectDescription(parent.getProjectDescription(),
					result.getDescription());
		}
		owner.fireEvent(new ServerLogEvent(result));
	}
}
