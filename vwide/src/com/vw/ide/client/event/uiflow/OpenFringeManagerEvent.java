package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.OpenFringeManagerHandler;

/**
 * Fired when user pressed menu item 'Open Fringe Manager'
 * @author OMelnyk
 *
 */
public class OpenFringeManagerEvent extends GwtEvent<OpenFringeManagerHandler> {

	private String itemId;
	
	public static Type<OpenFringeManagerHandler> TYPE = new Type<OpenFringeManagerHandler>();
	
	public OpenFringeManagerEvent() {
		super();
	}

	public OpenFringeManagerEvent(String itemId) {
		super();
		this.itemId = itemId;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<OpenFringeManagerHandler> getAssociatedType() {
		return TYPE;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}


	@Override
	protected void dispatch(OpenFringeManagerHandler handler) {
		handler.onOpenFringeManager(this);
	}
}
