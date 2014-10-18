package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.FileStateChangedEvent;

/**
 * Login handler
 * @author OMelnyk
 *
 */
public interface FileStateChangedHandler extends EventHandler {
	void onFileStateChanged(FileStateChangedEvent event);
}




