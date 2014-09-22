package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.SelectFileEvent;

/**
 * Login handler
 * @author OMelnyk
 *
 */
public interface SelectFileHandler extends EventHandler {
	void onSelectFile(SelectFileEvent event);
}
