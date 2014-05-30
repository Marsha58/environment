package com.vw.ide.client.dialog.simple;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.client.dialog.VwmlDialog;
import com.vw.ide.client.utils.Utils;

/**
 * Allows to edit single line text
 * @author Oleg
 *
 */
public class SimpleSinglelineEditDialog extends VwmlDialog {

	private static SimpleSinglelineEditDialogUiBinder uiBinder = GWT.create(SimpleSinglelineEditDialogUiBinder.class);

	public static interface ResultCallback {
		public void setResult(String result);
	}
	
	interface SimpleSinglelineEditDialogUiBinder extends UiBinder<Widget, SimpleSinglelineEditDialog> {
	}

	public SimpleSinglelineEditDialog() {
		super.setWidget(uiBinder.createAndBindUi(this));
	}

	@UiField Label editLabelField;
	@UiField TextBox editField;
	
	private ResultCallback result;

	public SimpleSinglelineEditDialog(String firstName) {
		super.setWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setEditLabelText(String labelText) {
		editLabelField.setText(labelText);
	}
	
	public void setEditableText(String text) {
		editField.setText(text);
	}
	
	public String getEditableText() {
		return editLabelField.getText();
	}
	
	public ResultCallback getResult() {
		return result;
	}

	public void setResult(ResultCallback result) {
		this.result = result;
	}

	@UiHandler("cancel")
	void onCancelClick(ClickEvent event) {
		this.hide(true);
	}
	
	@UiHandler("ok")
	void onOkClick(ClickEvent event) {
		String text = editField.getText();
		if (text == null || text.length() == 0) {
			Utils.messageBox("Check field", editLabelField.getText() + " must not be empty", null);
		}
		else {
			if (result != null) {
				result.setResult(text);
			}
			this.hide(true);
		}
	}
}
