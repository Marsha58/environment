package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.FringesContextMenuEvent;

/**
 * Fringe Manager. Fringes grid's context menu handler
 * @author OMelnyk
 *
 */
public interface FringesContextMenuHandler extends EventHandler {
	void onFringesContextMenuClick(FringesContextMenuEvent event);
}
