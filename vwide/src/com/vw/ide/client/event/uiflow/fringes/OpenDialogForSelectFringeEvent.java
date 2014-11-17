package com.vw.ide.client.event.uiflow.fringes;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.fringes.OpenDialogForSelectFringeHandler;

/**
 * Fired when user pressed menu item 'Insert Fringe '
 * @author OMelnyk
 *
 */
public class OpenDialogForSelectFringeEvent extends GwtEvent<OpenDialogForSelectFringeHandler> {

	private String itemId;
	
	public static Type<OpenDialogForSelectFringeHandler> TYPE = new Type<OpenDialogForSelectFringeHandler>();
	
	public OpenDialogForSelectFringeEvent() {
		super();
	}

	public OpenDialogForSelectFringeEvent(String itemId) {
		super();
		this.itemId = itemId;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<OpenDialogForSelectFringeHandler> getAssociatedType() {
		return TYPE;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}


	@Override
	protected void dispatch(OpenDialogForSelectFringeHandler handler) {
		handler.onOpenDialogForSelectFringe(this);
	}

}

