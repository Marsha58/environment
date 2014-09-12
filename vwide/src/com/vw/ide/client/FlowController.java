package com.vw.ide.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.LoginHandler;
import com.vw.ide.client.event.handler.LogoutHandler;
import com.vw.ide.client.event.uiflow.LoginEvent;
import com.vw.ide.client.event.uiflow.LogoutEvent;
import com.vw.ide.client.login.LoginGxtPresenter;
import com.vw.ide.client.login.LoginViewGxt;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.factory.ServicesBrokerFactory;

/**
 * Dispatches application events
 * @author Oleg
 *
 */
public class FlowController extends Presenter implements ValueChangeHandler<String> {
	private HasWidgets container;

	private final HandlerManager eventBus;
	
	public FlowController(HandlerManager eventBus) {
		this.eventBus = eventBus;
		bind();
		ServicesBrokerFactory.instantiateAllServices(eventBus);
	}

	
	public void onValueChange(ValueChangeEvent<String> event) {
	    String token = event.getValue();
	    
	    if (token != null) {
			Presenter presenter = null;
			
			if (token.equals("loginGxt")) {
				presenter = new LoginGxtPresenter(eventBus, new LoginViewGxt());
			} else 	if (token.equals("dev")) {
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
//	    	History.newItem("login");
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
		// Logout event
		eventBus.addHandler(LogoutEvent.TYPE, new LogoutHandler() {
			public void onLogout(LogoutEvent event) {
				doLogout(event);
			}
		});
	}
	
	private void doLogin(LoginEvent event) {
		setLoggedAsUser(event.getLoggedAsUser());
		History.newItem("dev");
	}
	
	private void doLogout(LogoutEvent event) {
		setLoggedAsUser(null);
		History.newItem("login");
	}
	
	public void fireEvent(GwtEvent<?> event) {
	}
}
