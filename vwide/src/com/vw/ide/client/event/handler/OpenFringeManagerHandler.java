package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.OpenFringeManagerEvent;

/**
 * Login handler
 * @author Omelnyk
 *
 */
public interface OpenFringeManagerHandler extends EventHandler {
	void onOpenFringeManager(OpenFringeManagerEvent event);
}
