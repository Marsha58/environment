package com.vw.ide.client.event.handler.fringes;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.fringes.DeleteFringeEvent;

/**
 * DeleteFringe handler
 * @author Omelnyk
 *
 */
public interface DeleteFringeHandler extends EventHandler {
	void onDeleteFringe(DeleteFringeEvent event);
}
