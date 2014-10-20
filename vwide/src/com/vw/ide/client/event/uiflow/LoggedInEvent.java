package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.LoggedInHandler;

/**
 * Fired upon successful login operation
 * @author Oleg
 *
 */
public class LoggedInEvent extends GwtEvent<LoggedInHandler> {

	private String userName;
	public static Type<LoggedInHandler> TYPE = new Type<LoggedInHandler>();

	public LoggedInEvent() {
		super();
	}
	
	public LoggedInEvent(String userName) {
		super();
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LoggedInHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoggedInHandler handler) {
		handler.onLoggedIn(this);
	}
}
