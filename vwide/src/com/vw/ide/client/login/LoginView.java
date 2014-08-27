package com.vw.ide.client.login;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.client.event.uiflow.LoginEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.utils.Utils;

public class LoginView extends Composite implements HasText, PresenterViewerLink {

	private static LoginViewUiBinder uiBinder = GWT.create(LoginViewUiBinder.class);

	private Presenter presenter = null;
	
	@UiField TextBox userNameField;
	@UiField PasswordTextBox passwordField;
	@UiField Button loginProceed;

	interface LoginViewUiBinder extends UiBinder<Widget, LoginView> {
	}
	
	public LoginView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public LoginView(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setText(String text) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public Widget asWidget() {
		return this;
	}
	
	public void associatePresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@UiHandler("loginProceed")
	void onLoginProceedClick(ClickEvent event) {
/*		boolean proceed = true;
		String userName = userNameField.getText();
		String password = passwordField.getText();
		if (userName == null || userName.length() == 0) {
			Utils.messageBox("Login", "Please enter your user name", loginProceed);
			proceed = false;
		}
		else
		if (password == null || password.length() == 0) {
			Utils.messageBox("Login", "Please enter your password", loginProceed);
			proceed = false;
		}
		if (proceed) {
			System.out.println("proceeding to login");
			if (presenter != null) {
				Utils.messageBox("Login", "Please wait...", null);
				presenter.fireEvent(new LoginEvent(userName, password));
			}
		}
*/		
	}
}
