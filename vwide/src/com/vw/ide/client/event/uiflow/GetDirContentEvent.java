package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.GetDirContentHandler;

/**
 * Logout event; Fired when user pressed 'Logout' on development board
 * @author OMelnyk
 *
 */
public class GetDirContentEvent extends GwtEvent<GetDirContentHandler> {

	private String dir;

	public static Type<GetDirContentHandler> TYPE = new Type<GetDirContentHandler>();
	
	public GetDirContentEvent() {
		super();
	}

	public GetDirContentEvent(String dir) {
		super();
		this.dir = dir;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<GetDirContentHandler> getAssociatedType() {
		return TYPE;
	}
	
	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}	

	@Override
	protected void dispatch(GetDirContentHandler handler) {
		handler.onGetDirContent(this);
	}
}
