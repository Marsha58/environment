package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.SaveFileEvent;
import com.vw.ide.client.event.uiflow.SelectFileEvent;

/**
 * Login handler
 * @author OMelnyk
 *
 */
public interface SaveFileHandler extends EventHandler {
	void onSaveFile(SaveFileEvent event);
}
