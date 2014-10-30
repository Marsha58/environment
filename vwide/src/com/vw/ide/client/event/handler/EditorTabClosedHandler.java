package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.EditorTabClosedEvent;

/**
 * Login handler
 * @author OMelnyk
 *
 */
public interface EditorTabClosedHandler extends EventHandler {
	void onEditorTabClosed(EditorTabClosedEvent event);
}
