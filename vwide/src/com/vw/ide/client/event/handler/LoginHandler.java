package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.LoginEvent;

/**
 * Login handler
 * @author Oleg
 *
 */
public interface LoginHandler extends EventHandler {
	void onLogin(LoginEvent event);
}
