package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.History;
import com.vw.ide.client.event.handler.LogoutHandler;
import com.vw.ide.client.event.uiflow.LogoutEvent;
import com.vw.ide.client.presenters.Presenter;

public class LogoutEventHandler extends Presenter.PresenterEventHandler implements LogoutHandler {
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		History.newItem("loginGxt");
	}

	@Override
	public void onLogout(LogoutEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
}
