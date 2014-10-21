package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.ServerLogEvent;

/**
 * Login handler
 * @author OMelnyk
 *
 */
public interface ServerLogHandler extends EventHandler {
	void onServerLog(ServerLogEvent event);
}
