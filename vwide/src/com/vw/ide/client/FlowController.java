package com.vw.ide.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.login.LoginViewGxt;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterFactory;

/**
 * Dispatches application events
 * @author Oleg
 *
 */
public class FlowController implements ValueChangeHandler<String> {
	private HasWidgets container;
	private Presenter presenter = null;
	private final HandlerManager eventBus;
	private static String loggedAsUser = null;
	
	private static final String LOGIN_SCREEN = "loginGxt";
	private static final String DEV_SCREEN = "dev";

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
	    Presenter prevPresenter = presenter;
	    if (token != null) {
			if (token.equals(LOGIN_SCREEN)) {
				presenter = PresenterFactory.buildLoginPresenter(eventBus, new LoginViewGxt());
			} else 	if (token.startsWith(DEV_SCREEN)) {
				setLoggedAsUser(getUserNameFromDevBoardLoginString(token));
			    presenter = PresenterFactory.buildDevBoardPresenter(eventBus, new DevelopmentBoard());
			    presenter.setLoggedAsUser(getLoggedAsUser());
			} 
			if (presenter != null) {
				if (prevPresenter != null) {
					prevPresenter.unregisterOnEventBus(eventBus);
				}
				presenter.registerOnEventBus(eventBus);
				presenter.go(container);
			}
	    }
	}

	public void go(HasWidgets container) {
	    this.container = container;
	    if ("".equals(History.getToken())) {
	    	History.newItem(LOGIN_SCREEN);
	    }
	    else {
	    	History.fireCurrentHistoryState();
	    }
	}

	private void bind() {
		History.addValueChangeHandler(this);
		// processing flow's events
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
