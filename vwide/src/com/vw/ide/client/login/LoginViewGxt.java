package com.vw.ide.client.login;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.vw.ide.client.event.uiflow.LoginEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.utils.Utils;

public class LoginViewGxt extends Composite implements HasText, PresenterViewerLink{

	private static LoginViewGxtUiBinder uiBinder = GWT.create(LoginViewGxtUiBinder.class);
	
	private Presenter presenter = null;
	
	@UiField TextField userNameField;
	@UiField PasswordField passwordField;
	@UiField TextButton loginProceed;
	@UiField CenterLayoutContainer clc;

	interface LoginViewGxtUiBinder extends UiBinder<Widget, LoginViewGxt> {
	}

	KeyPressHandler keyPressHandler = new KeyPressHandler() {
		@Override
		public void onKeyPress(KeyPressEvent event) {
			if((event.getCharCode() == KeyCodes.KEY_ENTER)){
				onLoginProceedClicked(new SelectEvent());
			}
		}
	};  
	
	public void addHandlers() {
		userNameField.addKeyPressHandler(keyPressHandler);
		passwordField.addKeyPressHandler(keyPressHandler);
	}
	
	public LoginViewGxt() {
		initWidget(uiBinder.createAndBindUi(this));
		addHandlers();
	}
	
	public LoginViewGxt(String firstName) {
		addHandlers();
	}

	public Widget asWidget() {
		return this;
	}

	public void associatePresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public String getText() {
		return null;
	}

	public void setText(String text) {
	}
	
	@UiHandler("loginProceed")
	void onLoginProceedClicked(SelectEvent event) {
		boolean proceed = true;
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
				LoginEvent login = new LoginEvent(userNameField.getText(), passwordField.getText());
				presenter.fireEvent(login);
			}
		}
	}	
}
