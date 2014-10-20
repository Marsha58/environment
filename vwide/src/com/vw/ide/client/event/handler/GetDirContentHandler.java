package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.GetDirContentEvent;

/**
 * Logout handler; called upon 'Logout' event
 * @author OMelnyk
 *
 */
public interface GetDirContentHandler extends EventHandler {
	void onGetDirContent(GetDirContentEvent event);
}
