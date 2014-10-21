package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.SaveFileHandler;

/**
 * Fired when user pressed 'SelectFile'
 * @author OMelnyk
 *
 */
public class SaveFileEvent extends GwtEvent<SaveFileHandler> {

	public static Type<SaveFileHandler> TYPE = new Type<SaveFileHandler>();
	
	public SaveFileEvent() {
		super();
	}



	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SaveFileHandler> getAssociatedType() {
		return TYPE;
	}



	@Override
	protected void dispatch(SaveFileHandler handler) {
		handler.onSaveFile(this);
	}
}
