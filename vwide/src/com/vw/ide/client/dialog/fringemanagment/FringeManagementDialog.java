package com.vw.ide.client.dialog.fringemanagment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public class FringeManagementDialog extends DialogBox {

	private static FringeManagementDialogUiBinder uiBinder = GWT.create(FringeManagementDialogUiBinder.class);

	interface FringeManagementDialogUiBinder extends
			UiBinder<Widget, FringeManagementDialog> {
	}

	public FringeManagementDialog() {
		setWidget(uiBinder.createAndBindUi(this));
	}

	public FringeManagementDialog(String firstName) {
		setWidget(uiBinder.createAndBindUi(this));
	}
}
