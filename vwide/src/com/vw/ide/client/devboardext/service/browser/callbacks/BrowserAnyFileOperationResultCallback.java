package com.vw.ide.client.devboardext.service.browser.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.shared.servlet.remotebrowser.RequestFileOperationResult;

public class AnyFileOperationResultCallback extends ResultCallback<RequestFileOperationResult> {

	private DevelopmentBoardPresenter owner = null;
	private HandlerOnFileOpertaion handler = null;

	public AnyFileOperationResultCallback(DevelopmentBoardPresenter owner, HandlerOnFileOpertaion handler) {
		this.owner = owner;
		this.handler = handler;
	}
	
	@Override
	public void handle(RequestFileOperationResult result) {
		if (result.getRetCode().intValue() != 0) {
			String messageAlert = "The operation '" + result.getOperation()
					+ "' failed.\r\nResult'" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		}
		else {
			if (handler != null) {
				handler.handle(result);
			}
		}
		owner.fireEvent(new ServerLogEvent(result));
	}
}
