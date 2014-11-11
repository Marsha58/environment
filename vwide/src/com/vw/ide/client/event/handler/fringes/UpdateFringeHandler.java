package com.vw.ide.client.event.handler.fringes;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.fringes.UpdateFringeEvent;

/**
 * UpdateFringe handler
 * @author Omelnyk
 *
 */
public interface UpdateFringeHandler extends EventHandler {
	void onUpdateFringe(UpdateFringeEvent event);
}
