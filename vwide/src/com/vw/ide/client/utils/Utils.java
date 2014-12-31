package com.vw.ide.client.utils;


import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.kfuntak.gwt.json.serialization.client.JsonSerializable;
import com.kfuntak.gwt.json.serialization.client.Serializer;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;

/**
 * Some aux. methods
 * @author Oleg
 *
 */
public class Utils {

	public static class DialogBoxRegularClickHandler implements SelectHandler {

		private TextButton toDisable;
		private DialogBox dialogBox;
		
		public DialogBoxRegularClickHandler(DialogBox dialogBox, TextButton toDisable) {
			this.toDisable = toDisable;
			this.dialogBox = dialogBox;
		}
		
		public void onSelect(SelectEvent event) {
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
		
		public void onClick(SelectEvent event) {
			super.onSelect(event);
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
		final TextButton closeButton = new TextButton("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b><br>" + text + "<br><br><br></b>"));
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
		// Add a handler to close the DialogBox
		closeButton.addSelectHandler(new DialogBoxRegularClickHandler(dialogBox, toDisable));
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
		final TextButton yesButton = new TextButton("Yes");
		final TextButton noButton = new TextButton("No   ");
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
		yesButton.addSelectHandler(new DialogBoxYesNoClickHandler(dialogBox, toDisable, YesNoMsgBoxSelctionCallback.SELECTION.YES, yesNoCbk));
		noButton.addSelectHandler(new DialogBoxYesNoClickHandler(dialogBox, toDisable, YesNoMsgBoxSelctionCallback.SELECTION.NO, yesNoCbk));
		dialogBox.center();
		return dialogBox;
	}
	
	public static void hideDialogBox(DialogBox dialogBox) {
		if (dialogBox != null) {
			dialogBox.hide();
		}
	}
	
	public static String extractJustPath(String input) {
		String output = "";
		String delims = "[\\\\/]+";
		if (input != null) {
			String[] arrPath = input.split(delims);
			String sLastItemName = arrPath[arrPath.length - 1];
			if (sLastItemName.indexOf(".") == -1) {
				return input;
			} else {
				for (int i = 0; i < arrPath.length - 2; i++) {
					output += arrPath[i] + "/";
				}
				output += arrPath[arrPath.length - 2];
			}
		}
		return output;
	}

	public static String extractJustFileName(String input) {
		String output = "";
		String delims = "[\\\\/]+";
		
		if (input != null) {		
			String[] arrPath = input.split(delims);
			String sLastItemName = arrPath[arrPath.length - 1];
			if (sLastItemName.indexOf(".") != -1) {
				output = arrPath[arrPath.length - 1];
			}
		}
		return output;
	}	
	
	
	public static String createFullProjectPath(ProjectDescription projectDescription) {
		return projectDescription.getProjectPath() + "/" + projectDescription.getMainModuleName();
	}
	
	public static String jsonSerialization(Serializer serializer, JsonSerializable obj) {
		return serializer.serialize(obj);
	}
}
