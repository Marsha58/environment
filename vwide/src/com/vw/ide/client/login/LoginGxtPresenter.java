package com.vw.ide.client.login;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter.DirOperationReadingFileResult;
import com.vw.ide.client.event.uiflow.LoggedInEvent;
import com.vw.ide.client.event.uiflow.LoginEvent;
import com.vw.ide.client.event.uiflow.SelectFileEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.service.ProcessedResult;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForAnyOperation;
import com.vw.ide.client.service.security.RemoteSecurityService;
import com.vw.ide.client.service.security.RemoteSecurityService.ServiceCallbackForLogin;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowserAsync;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
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
			AlertMessageBox alertMessageBox = new AlertMessageBox("Error",
					caught.getMessage());
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
		if (event instanceof LoginEvent) {
			LoginEvent loginEvent = (LoginEvent)event;  
			requestForLogin(loginEvent.getLoggedAsUser(), loginEvent.getLoggedWithPassword());
		}
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
		    } catch (java.security.NoSuchAlgorithmException e) {
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
	
	
}
