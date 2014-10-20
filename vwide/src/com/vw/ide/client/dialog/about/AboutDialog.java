package com.vw.ide.client.dialog.about;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.client.dialog.VwmlDialog;

public class AboutDialog  extends VwmlDialog{

	private static AboutDialogUiBinder uiBinder = GWT
			.create(AboutDialogUiBinder.class);

	interface AboutDialogUiBinder extends UiBinder<Widget, AboutDialog> {
	}

	public AboutDialog() {
		super.setWidget(uiBinder.createAndBindUi(this));
//		super.setGlassEnabled(true);
//		super.setAnimationEnabled(true);		
	}

	public AboutDialog(String firstName) {
		super.setWidget(uiBinder.createAndBindUi(this));
//		super.setGlassEnabled(true);
//		super.setAnimationEnabled(true);
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

}
