package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.BeforeCloseEvent;
import com.vw.ide.client.event.handler.EditorTabClosedHandler;

/**
 * Fired when user closes tab in the Ace editor
 * @author OMelnyk
 *
 */
public class EditorTabClosedEvent extends GwtEvent<EditorTabClosedHandler> {

	private BeforeCloseEvent<Widget> event;
	
	public static Type<EditorTabClosedHandler> TYPE = new Type<EditorTabClosedHandler>();
	
	public EditorTabClosedEvent() {
		super();
	}

	public EditorTabClosedEvent(BeforeCloseEvent<Widget> event) {
		super();
		this.event = event;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<EditorTabClosedHandler> getAssociatedType() {
		return TYPE;
	}

	public BeforeCloseEvent<Widget> getEvent() {
		return event;
	}


	@Override
	protected void dispatch(EditorTabClosedHandler handler) {
		handler.onEditorTabClosed(event);
	}
}
