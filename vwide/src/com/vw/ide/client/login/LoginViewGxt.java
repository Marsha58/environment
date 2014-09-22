package com.vw.ide.client.login;


import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.client.event.uiflow.LoginEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.utils.Utils;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class LoginViewGxt extends Composite implements HasText, PresenterViewerLink{

	private static LoginViewGxtUiBinder uiBinder = GWT.create(LoginViewGxtUiBinder.class);
	
	private Presenter presenter = null;
	
	@UiField TextField userNameField;
	@UiField PasswordField passwordField;
	@UiField TextButton loginProceed;
	@UiField CenterLayoutContainer clc;

	interface LoginViewGxtUiBinder extends UiBinder<Widget, LoginViewGxt> {
	}

	public LoginViewGxt() {
		initWidget(uiBinder.createAndBindUi(this));
	}


	
	
	public LoginViewGxt(String firstName) {
	}


	public Widget asWidget() {
		return this;
	}


	public void associatePresenter(Presenter presenter) {
		this.presenter = presenter;
	}


	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}


	public void setText(String text) {
		// TODO Auto-generated method stub
		
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
//				Utils.messageBox("Login", "Please wait...", null);
				presenter.fireEvent(new LoginEvent(userName, password));
			}
		}
		
		presenter.setLoggedAsUser(userName);
	}	


}
