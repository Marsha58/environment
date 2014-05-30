package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.LogoutHandler;

/**
 * Logout event; Fired when user pressed 'Logout' on development board
 * @author Oleg
 *
 */
public class LogoutEvent extends GwtEvent<LogoutHandler> {

	public static Type<LogoutHandler> TYPE = new Type<LogoutHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LogoutHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LogoutHandler handler) {
		handler.onLogout(this);
	}
}
