package com.vw.ide.client.dialog.newvwmlproj;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widget.client.TextButton;
import com.vw.ide.client.dialog.VwmlDialog;
import com.vw.ide.client.dialog.remotebrowser.RemoteDirectoryBrowserDialog;

/**
 * New VWML project dialog
 * @author Oleg
 *
 */
public class NewVwmlProjectDialog extends VwmlDialog {

	private static String s_remoteDirectoryBrowserCaption = "Select project's folder";
	private static NewVwmlProjectDialogUiBinder uiBinder = GWT.create(NewVwmlProjectDialogUiBinder.class);
	
	@UiField TextBox projectNameField;
	@UiField FlowPanel dialogMainPanel;
	@UiField TextBox vwmlProjectPathField;
	@UiField TextButton browseVwmlProjPath;
	@UiField TextBox vwmlMainModuleField;
	@UiField TextBox vwmlAuthorField;
	@UiField TextBox vwmlDescrField;
	@UiField TextBox javaPackageName;
	@UiField TextBox javaSrcPath;
	@UiField TextButton javaSrcPathBrowse;

	@UiField TextButton ok;
	@UiField TextButton cancel;

	interface NewVwmlProjectDialogUiBinder extends UiBinder<Widget, NewVwmlProjectDialog> {
	}

	public NewVwmlProjectDialog() {
		super.setWidget(uiBinder.createAndBindUi(this));
	}

	public NewVwmlProjectDialog(String firstName) {
		super.setWidget(uiBinder.createAndBindUi(this));
	}

	public String getText() {
		return null;
	}

	public void setText(String text) {
	}	
	
	@UiHandler("ok")
	void onOkClick(ClickEvent event) {

	}
	
	@UiHandler("cancel")
	void onCancelClick(ClickEvent event) {
		this.hide(true);	
	}
	
	@UiHandler("browseVwmlProjPath")
	void onBrowseVwmlProjPathClick(ClickEvent event) {
		RemoteDirectoryBrowserDialog d = new RemoteDirectoryBrowserDialog();
		d.setLoggedAsUser(getLoggedAsUser());
		d.prepare();
		d.show(s_remoteDirectoryBrowserCaption, null, getPopupLeft() - 30, getPopupTop() + 30);
	}		
}
