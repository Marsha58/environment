package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.History;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.event.handler.LogoutHandler;
import com.vw.ide.client.event.uiflow.LogoutEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.security.RemoteSecurityServiceBroker;
import com.vw.ide.shared.servlet.security.RequestLoginResult;

public class LogoutEventHandler extends Presenter.PresenterEventHandler implements LogoutHandler {

	public static class LogoutResult extends ResultCallback<RequestLoginResult> {

		public LogoutResult() {
		}

		@Override
		public void handle(RequestLoginResult result) {
		}
	}
	
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		RemoteSecurityServiceBroker.requestForLogout(FlowController.getLoggedAsUser(), new LogoutResult());
		FlowController.setLoggedAsUser(null);
		History.newItem("loginGxt");
	}

	@Override
	public void onLogout(LogoutEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
}
