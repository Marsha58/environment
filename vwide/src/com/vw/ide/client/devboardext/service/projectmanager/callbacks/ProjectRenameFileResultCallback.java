package com.vw.ide.client.devboardext.service.projectmanager.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectRenameFileResult;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

public class ProjectRenameFileResultCallback extends ResultCallback<RequestProjectRenameFileResult> {

	private DevelopmentBoardPresenter owner;
	private ProjectItemInfo itemInfo;
	private FileItemInfo toRename;
	
	public ProjectRenameFileResultCallback(DevelopmentBoardPresenter owner, ProjectItemInfo itemInfo, FileItemInfo toRename) {
		this.owner = owner;
		this.itemInfo = itemInfo;
		this.toRename = toRename;
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
			owner.getView().renameFileTabItem(itemInfo, toRename);
		}
		owner.fireEvent(new ServerLogEvent(result));
	}

}
