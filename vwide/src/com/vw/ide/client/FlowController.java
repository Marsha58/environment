package com.vw.ide.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.LoggedInHandler;
import com.vw.ide.client.event.handler.LoginHandler;
import com.vw.ide.client.event.handler.LogoutHandler;
import com.vw.ide.client.event.uiflow.LoggedInEvent;
import com.vw.ide.client.event.uiflow.LoginEvent;
import com.vw.ide.client.event.uiflow.LogoutEvent;
import com.vw.ide.client.login.LoginGxtPresenter;
import com.vw.ide.client.login.LoginViewGxt;
import com.vw.ide.client.presenters.Presenter;

/**
 * Dispatches application events
 * @author Oleg
 *
 */
public class FlowController implements ValueChangeHandler<String> {
	private HasWidgets container;
	private Presenter presenter = null;
	private static String loggedAsUser = null;

	private final HandlerManager eventBus;
	
	public FlowController(HandlerManager eventBus) {
		this.eventBus = eventBus;
		bind();
	}

	public static String getLoggedAsUser() {
		return loggedAsUser;
	}

	public static void setLoggedAsUser(String userName) {
		loggedAsUser = userName;
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
	    String token = event.getValue();
	    
	    if (token != null) {
			if (token.equals("loginGxt")) {
				presenter = new LoginGxtPresenter(eventBus, new LoginViewGxt());
			} else 	if (token.startsWith("dev")) {
				setLoggedAsUser(getUserNameFromDevBoardLoginString(token));
			    presenter = new DevelopmentBoardPresenter(eventBus, new DevelopmentBoard());
			    presenter.setLoggedAsUser(getLoggedAsUser());
			} 
			if (presenter != null) {
				presenter.go(container);
			}
	    }
	}

	public void go(HasWidgets container) {
	    this.container = container;
	    if ("".equals(History.getToken())) {
	    	History.newItem("loginGxt");
	    }
	    else {
	    	History.fireCurrentHistoryState();
	    }
	}

	private void bind() {
		History.addValueChangeHandler(this);
		// processing flow's events
		// Login event
		eventBus.addHandler(LoginEvent.TYPE, new LoginHandler() {
			public void onLogin(LoginEvent event) {
				doLogin(event);
			}
		});
		// successful logged in
		eventBus.addHandler(LoggedInEvent.TYPE, new LoggedInHandler() {
			public void onLoggedIn(LoggedInEvent event) {
				doLoggedIn(event);
			}
		});
		// Logout event
		eventBus.addHandler(LogoutEvent.TYPE, new LogoutHandler() {
			public void onLogout(LogoutEvent event) {
				doLogout(event);
			}
		});
	}

	private void doLogin(LoginEvent event) {
		presenter.handleEvent(event);
	}

	private void doLoggedIn(LoggedInEvent event) {
		setLoggedAsUser(event.getUserName());
		History.newItem(formDevBoardLoginString(event.getUserName()));
	}
	
	private void doLogout(LogoutEvent event) {
		setLoggedAsUser(null);
		History.newItem("loginExt");
	}
	
	private String formDevBoardLoginString(String user) {
		return "dev@" + user;
	}
	
	private String getUserNameFromDevBoardLoginString(String loginString) {
		if (loginString != null) {
			String s[] = loginString.split("@");
			if (s != null && s.length == 2) {
				return s[1];
			}
		}
		return "";
	}
}
