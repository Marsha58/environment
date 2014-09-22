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

		if (event instanceof LoginEvent) {
			LoginEvent evt = (LoginEvent) event;
			requestForLogin(((LoginViewGxt) view).userNameField.getText(), 
 					((LoginViewGxt) view).passwordField.getText());
		}
	}

	public static class LoginResult extends
			ProcessedResult<RequestLoginResult> {

		private Presenter presenter;

		public LoginResult() {
		}

		public LoginResult(Presenter presenter) {
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
				// String path = extractJustPath(result.getPath());
				// String name = extractJustFileName(result.getPath());
				
				switch (result.getResult()) {
				case 0:
					History.newItem("dev");
					break;
				case -1:
					messageAlert = "Password is incorrect";
					AlertMessageBox alertMessageBox = new AlertMessageBox(
							"Warning", messageAlert);
					alertMessageBox.show();	
//					History.newItem("dev");
					break;
				case -2:
					messageAlert = "Such user doesn't exist";
					AlertMessageBox alertMessageBox2 = new AlertMessageBox(
							"Warning", messageAlert);
					alertMessageBox2.show();	
//					History.newItem("dev");
					break;

				default:
					break;
				}  ;

				// editor.tabPanel.add(newFileSheet,"test");
			}
		}


	}

	protected void requestForLogin(String userName, String password) {
		
		byte[] bPasswordMD5 = calculateCheckSum(password.trim());
		String sPasswordMD5 = bPasswordMD5.toString();		
		
		RemoteSecurityAsync service = RemoteSecurityService.instance()
				.getServiceImpl();
		if (service != null) {
			ServiceCallbackForLogin cbk = RemoteSecurityService.instance()
					.buildCallbackForLogin();
			cbk.setProcessedResult(new LoginResult(this));
			service.login(userName, password, cbk);
		}
	}
	
	
	public static byte[] calculateCheckSum(String input) {
		byte[] bytesOfMessage = null;
		try {
			bytesOfMessage = input.trim().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] thedigest = md.digest(bytesOfMessage);		
		return thedigest;
	}	
}
