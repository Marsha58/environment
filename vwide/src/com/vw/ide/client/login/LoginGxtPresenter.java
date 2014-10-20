package com.vw.ide.client.login;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.event.handler.LoggedInHandler;
import com.vw.ide.client.event.handler.LoginHandler;
import com.vw.ide.client.event.handler.LogoutHandler;
import com.vw.ide.client.event.uiflow.LoggedInEvent;
import com.vw.ide.client.event.uiflow.LoginEvent;
import com.vw.ide.client.event.uiflow.LogoutEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.service.ProcessedResult;
import com.vw.ide.client.service.security.RemoteSecurityService;
import com.vw.ide.client.service.security.RemoteSecurityService.ServiceCallbackForLogin;
import com.vw.ide.shared.servlet.security.RemoteSecurityAsync;
import com.vw.ide.shared.servlet.security.RequestLoginResult;

/**
 * Login screen
 * 
 * @author omelnyk
 * 
 */
public class LoginGxtPresenter extends Presenter {

	public static class LoginResult extends ProcessedResult<RequestLoginResult> {

		private LoginGxtPresenter presenter;

		public LoginResult() {
		}

		public LoginResult(LoginGxtPresenter presenter) {
			this.presenter = presenter;
		}

		@Override
		public void onFailure(Throwable caught) {
			AlertMessageBox alertMessageBox = new AlertMessageBox("Error", caught.getMessage());
			alertMessageBox.show();
		}

		@Override
		public void onSuccess(RequestLoginResult result) {
			String messageAlert;
			if (result.getRetCode().intValue() != 0) {
				messageAlert = "The login operation '" 
						+ "' failed.\r\nResult'" + result.getResult() + "'";
				AlertMessageBox alertMessageBox = new AlertMessageBox(
						"Warning", messageAlert);
				alertMessageBox.show();
			} else {
				switch (result.getResult()) {
					case 0:
						presenter.onSuccessLogin(result.getUserName());
						break;
					case -1:
						messageAlert = "Password is incorrect";
						AlertMessageBox alertMessageBox = new AlertMessageBox(
								"Warning", messageAlert);
						alertMessageBox.show();	
						break;
					case -2:
						messageAlert = "Such user doesn't exist";
						AlertMessageBox alertMessageBox2 = new AlertMessageBox(
								"Warning", messageAlert);
						alertMessageBox2.show();	
						break;
					default:
						break;
				}
			}
		}
	}
	
	private static class LoginEventHandler extends Presenter.PresenterEventHandler implements LoginHandler {
		
		@Override
		public void handler(Presenter presenter, GwtEvent<?> event) {
			LoginEvent loginEvent = (LoginEvent)event;  
			((LoginGxtPresenter)presenter).requestForLogin(loginEvent.getLoggedAsUser(), loginEvent.getLoggedWithPassword());
		}
		
		@Override
		public void onLogin(LoginEvent event) {
			if (getPresenter() != null) {
				getPresenter().delegate(event);
			}
		}
	}

	private static class LoggedInEventHandler extends Presenter.PresenterEventHandler implements LoggedInHandler {

		@Override
		public void handler(Presenter presenter, GwtEvent<?> event) {
			FlowController.setLoggedAsUser(((LoggedInEvent)event).getUserName());
			History.newItem(((LoginGxtPresenter)presenter).formDevBoardLoginString(((LoggedInEvent)event).getUserName()));
		}
		
		@Override
		public void onLoggedIn(LoggedInEvent event) {
			if (getPresenter() != null) {
				getPresenter().delegate(event);
			}
		}
	}

	private static class LogoutEventHandler extends Presenter.PresenterEventHandler implements LogoutHandler {

		@Override
		public void handler(Presenter presenter, GwtEvent<?> event) {
			FlowController.setLoggedAsUser(null);
			History.newItem("loginExt");
		}
		
		@Override
		public void onLogout(LogoutEvent event) {
			if (getPresenter() != null) {
				getPresenter().delegate(event);
			}
		}
	}
	
	@SuppressWarnings("serial")
	private static Map<Type<?>, Presenter.PresenterEventHandler> dispatcher = new HashMap<Type<?>, Presenter.PresenterEventHandler>() {
		{
			put(LoginEvent.TYPE, new LoginEventHandler());
			put(LoggedInEvent.TYPE, new LoggedInEventHandler());
			put(LogoutEvent.TYPE, new LogoutEventHandler());
		}
	};
	
	private final HandlerManager eventBus;
	private final PresenterViewerLink view;

	public LoginGxtPresenter(HandlerManager eventBus, PresenterViewerLink view) {
		this.eventBus = eventBus;
		this.view = view;
		if (view != null) {
			view.associatePresenter(this);
		}
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}

	@Override
	public void handleEvent(GwtEvent<?> event) {
		Presenter.PresenterEventHandler handler = dispatcher.get(event.getAssociatedType());
		if (handler != null) {
			handler.handler(this, event);
		}
	}

	@Override
	public void registerOnEventBus(HandlerManager eventBus) {
		for(Presenter.PresenterEventHandler handler : dispatcher.values()) {
			handler.setPresenter(this);
		}
		// Login event
		eventBus.addHandler(LoginEvent.TYPE, (LoginHandler)dispatcher.get(LoginEvent.TYPE));
		// successful logged in
		eventBus.addHandler(LoggedInEvent.TYPE, (LoggedInEventHandler)dispatcher.get(LoggedInEvent.TYPE));
		// Logout event
		eventBus.addHandler(LogoutEvent.TYPE, (LogoutEventHandler)dispatcher.get(LogoutEvent.TYPE));
	}

	@Override
	public void unregisterOnEventBus(HandlerManager eventBus) {
		// Login event
		eventBus.removeHandler(LoginEvent.TYPE, (LoginHandler)dispatcher.get(LoginEvent.TYPE));
		// successful logged in
		eventBus.removeHandler(LoggedInEvent.TYPE, (LoggedInEventHandler)dispatcher.get(LoggedInEvent.TYPE));
		// Logout event
		eventBus.removeHandler(LogoutEvent.TYPE, (LogoutEventHandler)dispatcher.get(LogoutEvent.TYPE));
	}
	
	public String calculateCheckSum(String md5) {
		try {
	        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(md5.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	        	sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	        }
	        return sb.toString();
		} 
		catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}	
	
	protected void onSuccessLogin(String userName) {
		fireEvent(new LoggedInEvent(userName));
	}
	
	protected void requestForLogin(String userName, String password) {
		String sPasswordMD5 = calculateCheckSum(password.trim());
		RemoteSecurityAsync service = RemoteSecurityService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForLogin cbk = RemoteSecurityService.instance().buildCallbackForLogin();
			cbk.setProcessedResult(new LoginResult(this));
			service.login(userName, sPasswordMD5, cbk);
		}
	}
	
	private String formDevBoardLoginString(String user) {
		return "dev@" + user;
	}
}
