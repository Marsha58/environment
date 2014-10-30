package com.vw.ide.client.devboardext.service.userstate.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.service.userstate.callbacks.custom.handler.UserStateHandler;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.shared.servlet.remotebrowser.RequestUserStateResult;

public class UserStateGettingResultCallback extends ResultCallback<RequestUserStateResult> {

	private DevelopmentBoardPresenter owner;
	private UserStateHandler handler;

	public UserStateGettingResultCallback(DevelopmentBoardPresenter presenter, UserStateHandler handler) {
		this.owner = presenter;
		this.handler = handler;
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
			if (handler != null) {
				handler.handle(result.getUserStateInfo());
			}
		}
		owner.fireEvent(new ServerLogEvent(result));
	}
}
