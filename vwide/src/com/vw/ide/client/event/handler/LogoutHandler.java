package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.LogoutEvent;

/**
 * Logout handler; called upon 'Logout' event
 * @author Oleg
 *
 */
public interface LogoutHandler extends EventHandler {
	void onLogout(LogoutEvent event);
}
