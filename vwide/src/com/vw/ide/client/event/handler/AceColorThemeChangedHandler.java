package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.AceColorThemeChangedEvent;
import com.vw.ide.client.event.uiflow.LoginEvent;

/**
 * Login handler
 * @author OMelnyk
 *
 */
public interface AceColorThemeChangedHandler extends EventHandler {
	void onAceColorThemeChanged(AceColorThemeChangedEvent event);
}
