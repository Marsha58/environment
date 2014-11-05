package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.SaveAllFilesHandler;

/**
 * Fired upon 'SaveAllFiles'
 * @author Oleg
 *
 */
public class SaveAllFilesEvent extends GwtEvent<SaveAllFilesHandler> {

	public static Type<SaveAllFilesHandler> TYPE = new Type<SaveAllFilesHandler>();
	
	public SaveAllFilesEvent() {
		super();
	}
	
	
	public static Type<SaveAllFilesHandler> getTYPE() {
		return TYPE;
	}


	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SaveAllFilesHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SaveAllFilesHandler handler) {
		handler.onSaveAllFiles(this);
	}
}
