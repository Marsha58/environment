package com.vw.ide.client.event.handler.fringes;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.fringes.AddFringeEvent;

/**
 * AddFringe handler
 * @author Omelnyk
 *
 */
public  interface AddFringeHandler extends EventHandler {
	void onAddFringe(AddFringeEvent event);
}
