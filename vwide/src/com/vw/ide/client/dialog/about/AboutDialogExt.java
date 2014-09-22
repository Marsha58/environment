package com.vw.ide.client.dialog.about;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.vw.ide.client.dialog.VwmlDialogExt;

public class AboutDialogExt  extends VwmlDialogExt{

	private static AboutDialogExtUiBinder uiBinder = GWT
			.create(AboutDialogExtUiBinder.class);

	interface AboutDialogExtUiBinder extends UiBinder<Widget, AboutDialogExt> {
	}


	public AboutDialogExt() {
		super.setWidget(uiBinder.createAndBindUi(this));
		setPredefinedButtons(PredefinedButton.CLOSE);
//		super.setGlassEnabled(true);
//		super.setAnimationEnabled(true);		
	}

	public AboutDialogExt(String firstName) {
		super.setWidget(uiBinder.createAndBindUi(this));
//		super.setGlassEnabled(true);
//		super.setAnimationEnabled(true);
	}

	public String getText() {
		return null;
	}

	public void setText(String text) {
	}	
	
	

}
