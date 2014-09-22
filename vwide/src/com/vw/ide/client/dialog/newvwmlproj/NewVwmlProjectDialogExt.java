package com.vw.ide.client.dialog.newvwmlproj;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.client.dialog.VwmlDialogExt;
import com.vw.ide.client.dialog.remotebrowser.RemoteDirectoryBrowserDialog;
import com.vw.ide.client.dialog.remotebrowser.RemoteDirectoryBrowserDialogExt;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * New VWML project dialog
 * @author Oleg
 *
 */
public class NewVwmlProjectDialogExt extends VwmlDialogExt {

	private static String s_remoteDirectoryBrowserCaption = "Select project's folder";
	private static NewVwmlProjectDialogExtUiBinder uiBinder = GWT.create(NewVwmlProjectDialogExtUiBinder.class);
	
//	@UiField TextField projectNameField;
//	@UiField FlowPanel dialogMainPanel;
//	@UiField TextField vwmlProjectPathField;
	@UiField TextButton browseVwmlProjPath;
//	@UiField TextField vwmlMainModuleField;
//	@UiField TextField vwmlAuthorField;
//	@UiField TextField vwmlDescrField;
//	@UiField TextField javaPackageName;
//	@UiField TextField javaSrcPath;
//	@UiField TextButton javaSrcPathBrowse;

//	@UiField TextButton ok;
//	@UiField TextButton cancel;

	interface NewVwmlProjectDialogExtUiBinder extends UiBinder<Widget, NewVwmlProjectDialogExt> {
	}

	public NewVwmlProjectDialogExt() {
		super.setWidget(uiBinder.createAndBindUi(this));
	}

	public NewVwmlProjectDialogExt(String firstName) {
		super.setWidget(uiBinder.createAndBindUi(this));
	}

	public String getText() {
		return null;
	}

	public void setText(String text) {
	}	
	
//	@UiHandler("ok")
//	void onOkClick(ClickEvent event) {
//
//	}
	
//	@UiHandler("cancel")
//	void onCancelClick(ClickEvent event) {
//		this.hide();	
//	}
	
	
	@UiHandler("browseVwmlProjPath")
	void onBrowseVwmlProjPathClick(SelectEvent event) {
		RemoteDirectoryBrowserDialogExt d = new RemoteDirectoryBrowserDialogExt();
		d.setLoggedAsUser(getLoggedAsUser());
		d.prepare();
//		d.show(s_remoteDirectoryBrowserCaption, null,0,0);
		d.setSize("550", "400");
		d.showCenter(s_remoteDirectoryBrowserCaption, null);
	}
	
	
	  protected void onButtonPressed(TextButton textButton) {
		  super.onButtonPressed(textButton);
		    if (textButton == getButton(PredefinedButton.OK)) {
		      hide();
		    }
	  }	
	
			
}
