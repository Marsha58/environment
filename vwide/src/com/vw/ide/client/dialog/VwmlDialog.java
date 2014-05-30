package com.vw.ide.client.dialog;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Must be inherited by any VWML custom dialog
 * @author Oleg
 *
 */
public class VwmlDialog extends DialogBox {

	private String loggedAsUser;
	private Widget toDisable = null;
	
	public Widget getToDisable() {
		return toDisable;
	}
	
	public DialogBox show(String caption, Widget toDisable, int left, int top) {
		this.toDisable = toDisable;
		super.setText(caption);
		setGlassEnabled(true);
		if (left == 0 && top == 0) {
			center();
		}
		else {
			setPopupPosition(left, top);
			show();
		}
		return this;
	}

	public String getLoggedAsUser() {
		return loggedAsUser;
	}

	public void setLoggedAsUser(String loggedAsUser) {
		this.loggedAsUser = loggedAsUser;
	}
	
	/**
	 * Should be overridden by concrete dialog implementations
	 */
	public void prepare() {
		
	}
}
