package com.vw.ide.client.presenters;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * Root class for all presenters
 * @author Oleg
 *
 */
public abstract class Presenter {

	private String loggedAsUser;
	
	public abstract void go(final HasWidgets container);
	public abstract void fireEvent(GwtEvent<?> event);
	
	public String getLoggedAsUser() {
		return loggedAsUser;
	}
	public void setLoggedAsUser(String loggedAsUser) {
		this.loggedAsUser = loggedAsUser;
	}
}