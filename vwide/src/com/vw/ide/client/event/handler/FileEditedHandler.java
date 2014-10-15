package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.FileEditedEvent;

/**
 * Login handler
 * @author OMelnyk
 *
 */
public interface FileEditedHandler extends EventHandler {
	void onFileEdited(FileEditedEvent event);
}
