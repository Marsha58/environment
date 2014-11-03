package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.GetFringesEvent;

/**
 * Login handler
 * @author Omelnyk
 *
 */
public interface GetFringesHandler extends EventHandler {
	void onGetFringes(GetFringesEvent event);
}
