package com.vw.ide.client.utils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.widget.client.TextButton;

/**
 * Some aux. methods
 * @author Oleg
 *
 */
public class Utils {

	public static class DialogBoxRegularClickHandler implements ClickHandler {

		private TextButton toDisable;
		private DialogBox dialogBox;
		
		public DialogBoxRegularClickHandler(DialogBox dialogBox, TextButton toDisable) {
			this.toDisable = toDisable;
			this.dialogBox = dialogBox;
		}
		
		public void onClick(ClickEvent event) {
			if (toDisable != null) {
				toDisable.setEnabled(true);
			}
			dialogBox.hide();
		}
	}
	
	public static interface YesNoMsgBoxSelctionCallback {
		enum SELECTION {
			YES,
			NO
		}		
		public void selected(SELECTION selection);
	}
	
	public static class DialogBoxYesNoClickHandler extends DialogBoxRegularClickHandler {
		private YesNoMsgBoxSelctionCallback cbk;
		private YesNoMsgBoxSelctionCallback.SELECTION expectedSelection;
		
		public DialogBoxYesNoClickHandler(DialogBox dialogBox,
										  TextButton toDisable,
										  YesNoMsgBoxSelctionCallback.SELECTION expectedSelection,
										  YesNoMsgBoxSelctionCallback cbk ) {
			super(dialogBox, toDisable);
			this.cbk = cbk;
			this.expectedSelection = expectedSelection;
		}

		public YesNoMsgBoxSelctionCallback getCbk() {
			return cbk;
		}
		
		public void onClick(ClickEvent event) {
			super.onClick(event);
			cbk.selected(expectedSelection);
		}
	}
	
	public static DialogBox messageBox(String caption, String text, TextButton toDisable) {
		if (toDisable != null) {
			toDisable.setEnabled(false);
		}
		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText(caption);
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b><br>" + text + "<br><br><br></b>"));
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new DialogBoxRegularClickHandler(dialogBox, toDisable));
		dialogBox.center();
		return dialogBox;
	}

	public static DialogBox messageBoxYesNo(String caption, String text, TextButton toDisable, YesNoMsgBoxSelctionCallback yesNoCbk) {
		if (toDisable != null) {
			toDisable.setEnabled(false);
		}
		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText(caption);
		dialogBox.setAnimationEnabled(true);
		final Button yesButton = new Button("Yes");
		final Button noButton = new Button("No   ");
		// We can set the id of a widget by accessing its Element
		yesButton.getElement().setId("yesButton");
		noButton.getElement().setId("noButton");
		HorizontalPanel dialogVPanel = new HorizontalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b><br>" + text + "<br><br><br></b>"));
		dialogVPanel.setVerticalAlignment(HorizontalPanel.ALIGN_BOTTOM);
		dialogVPanel.add(yesButton);
		dialogVPanel.add(noButton);
		dialogBox.setWidget(dialogVPanel);
		// Add a handler to close the DialogBox
		yesButton.addClickHandler(new DialogBoxYesNoClickHandler(dialogBox, toDisable, YesNoMsgBoxSelctionCallback.SELECTION.YES, yesNoCbk));
		noButton.addClickHandler(new DialogBoxYesNoClickHandler(dialogBox, toDisable, YesNoMsgBoxSelctionCallback.SELECTION.NO, yesNoCbk));
		dialogBox.center();
		return dialogBox;
	}
	
	public static void hideDialogBox(DialogBox dialogBox) {
		if (dialogBox != null) {
			dialogBox.hide();
		}
	}
}
