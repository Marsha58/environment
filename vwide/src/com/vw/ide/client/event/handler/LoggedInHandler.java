package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.LoggedInEvent;

public interface LoggedInHandler extends EventHandler {
	void onLoggedIn(LoggedInEvent event);
}
