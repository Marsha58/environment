package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.AceColorThemeChangedHandler;
import com.vw.ide.client.event.handler.LoginHandler;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;

/**
 * Fired when user change color theme of Ace editor
 * @author OMelnyk
 *
 */
public class AceColorThemeChangedEvent extends GwtEvent<AceColorThemeChangedHandler> {

	private SelectionEvent<AceEditorTheme> event;
	
	public static Type<AceColorThemeChangedHandler> TYPE = new Type<AceColorThemeChangedHandler>();
	
	public AceColorThemeChangedEvent() {
		super();
	}

	public AceColorThemeChangedEvent(SelectionEvent<AceEditorTheme> event) {
		super();
		this.event = event;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AceColorThemeChangedHandler> getAssociatedType() {
		return TYPE;
	}

	public SelectionEvent<AceEditorTheme> getEvent() {
		return event;
	}


	@Override
	protected void dispatch(AceColorThemeChangedHandler handler) {
		handler.onAceColorThemeChanged(this);
	}
}
