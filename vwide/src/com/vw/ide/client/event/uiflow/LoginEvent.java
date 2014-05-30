package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.LoginHandler;

/**
 * Fired when user pressed 'Login'
 * @author Oleg
 *
 */
public class LoginEvent extends GwtEvent<LoginHandler> {

	private String loggedAsUser;
	private String loggedWithPassword;
	
	public static Type<LoginHandler> TYPE = new Type<LoginHandler>();
	
	public LoginEvent() {
		super();
	}

	public LoginEvent(String loggedAsUser, String loggedWithPassword) {
		super();
		this.loggedAsUser = loggedAsUser;
		this.loggedWithPassword = loggedWithPassword;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LoginHandler> getAssociatedType() {
		return TYPE;
	}

	public String getLoggedAsUser() {
		return loggedAsUser;
	}

	public void setLoggedAsUser(String loggedAsUser) {
		this.loggedAsUser = loggedAsUser;
	}

	public String getLoggedWithPassword() {
		return loggedWithPassword;
	}

	@Override
	protected void dispatch(LoginHandler handler) {
		handler.onLogin(this);
	}
}
