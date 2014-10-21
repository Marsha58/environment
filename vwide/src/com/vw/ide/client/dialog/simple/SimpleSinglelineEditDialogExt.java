package com.vw.ide.client.dialog.simple;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.vw.ide.client.dialog.VwmlDialogExt;

/**
 * Allows to edit single line text
 * @author Oleg
 *
 */
public class SimpleSinglelineEditDialogExt extends VwmlDialogExt {

	private static SimpleSinglelineEditDialogUiBinderExt uiBinder = GWT.create(SimpleSinglelineEditDialogUiBinderExt.class);

	public static interface ResultCallback {
		public void setResult(String result);
	}
	
	interface SimpleSinglelineEditDialogUiBinderExt extends UiBinder<Widget, SimpleSinglelineEditDialogExt> {
	}

	public SimpleSinglelineEditDialogExt() {
		setPredefinedButtons(PredefinedButton.OK,PredefinedButton.CANCEL);
		super.setWidget(uiBinder.createAndBindUi(this));
	}

	@UiField FieldLabel editLabelField;
	@UiField TextField editField;
	
	private ResultCallback result;
	
	public SimpleSinglelineEditDialogExt(String firstName) {
		setPredefinedButtons(PredefinedButton.OK,PredefinedButton.CANCEL);
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

	protected void onButtonPressed(TextButton textButton) {
		super.onButtonPressed(textButton);
		   if (textButton == getButton(PredefinedButton.OK)) {
		      String text = editField.getText();
		      if (text == null || text.length() == 0) {
//		    	  Utils.messageBox("Check field", editLabelField.getText() + " must not be empty", null);
		    	  String messageAlert = editLabelField.getText() + " must not be empty";
		    	  AlertMessageBox alertMessageBox = new AlertMessageBox("Check field", messageAlert);
		    	  alertMessageBox.show();		    	  
		      }
		      else {
		    	  if (result != null) {
		    		  result.setResult(text);
		    	  }
		    	  this.hide();
		      }
		   } else if (textButton == getButton(PredefinedButton.OK)) {
			   hide();
		   }
	}		
	
}
