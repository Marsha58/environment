package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.BeforeCloseEvent;

/**
 * Login handler
 * @author OMelnyk
 *
 */
public interface EditorTabClosedHandler extends EventHandler {
	void onEditorTabClosed(BeforeCloseEvent<Widget> event);
}
