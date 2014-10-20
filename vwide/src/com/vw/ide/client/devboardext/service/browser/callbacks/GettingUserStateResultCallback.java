package com.vw.ide.client.devboardext.service.browser.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserServiceBroker;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RequestUserStateResult;

public class GettingUserStateResultCallback extends RemoteBrowserServiceBroker.ResultCallback<RequestUserStateResult> {

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
			Long fileIdSelected = result.getUserStateInfo().getFileIdSelected();
			FileItemInfo value = null;
			for (Object key : result.getUserStateInfo().getOpenedFiles().keySet()) {
				value = result.getUserStateInfo().getOpenedFiles().get(key);
				if (fileIdSelected != key) {
					RemoteBrowserServiceBroker.requestForReadingFile(
							FlowController.getLoggedAsUser(),
							value.getAbsolutePath()+ "/" + value.getName(),
							value.getProjectId(),
							value.getFileId(),
							new DirOperationFileReadingResultCallback(owner));
				}
			}
			value = result.getUserStateInfo().getOpenedFiles().get(fileIdSelected);
			RemoteBrowserServiceBroker.requestForReadingFile(
					FlowController.getLoggedAsUser(),
					value.getAbsolutePath()+ "/" + value.getName(),
					value.getProjectId(),
					value.getFileId(),
					new DirOperationFileReadingResultCallback(owner));
		}
		owner.fireEvent(new ServerLogEvent(result));
	}
}
