package com.vw.ide.client.devboardext.service.browser.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.browser.DirBrowserServiceBroker;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RequestUserStateResult;

public class GettingUserStateResultCallback extends ResultCallback<RequestUserStateResult> {

	private DevelopmentBoardPresenter owner;

	public GettingUserStateResultCallback(DevelopmentBoardPresenter presenter) {
		this.owner = presenter;
	}

	@Override
	public void handle(RequestUserStateResult result) {
		if (result.getRetCode().intValue() != 0) {
			String messageAlert = "The operation '" + result.getOperation()
					+ "' failed.\r\nResult'" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox("Warning", messageAlert);
			alertMessageBox.show();
		}
		else {
			if (result.getUserStateInfo().getOpenedFiles() != null) {
				Long fileIdSelected = result.getUserStateInfo().getFileIdSelected();
				FileItemInfo value = null;
				
				for (Object key : result.getUserStateInfo().getOpenedFiles().keySet()) {
					value = result.getUserStateInfo().getOpenedFiles().get(key);
					if (fileIdSelected != key) {
						DirBrowserServiceBroker.requestForReadingFile(
								FlowController.getLoggedAsUser(),
								value.getAbsolutePath()+ "/" + value.getName(),
								value.getProjectId(),
								value.getFileId(),
								new DirOperationFileReadingResultCallback(owner));
					}
				}
				if (fileIdSelected != null) {
					value = result.getUserStateInfo().getOpenedFiles().get(fileIdSelected);
					DirBrowserServiceBroker.requestForReadingFile(
							FlowController.getLoggedAsUser(),
							value.getAbsolutePath()+ "/" + value.getName(),
							value.getProjectId(),
							value.getFileId(),
							new DirOperationFileReadingResultCallback(owner));
				}
			}
		}
		owner.fireEvent(new ServerLogEvent(result));
	}
}
