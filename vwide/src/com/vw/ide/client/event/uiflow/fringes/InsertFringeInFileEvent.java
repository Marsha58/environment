package com.vw.ide.client.event.uiflow.fringes;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.fringes.InsertFringeInFileHandler;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

/**
 * Fired when user selected fringe to insert in the file in the editor
 * @author OMelnyk
 *
 */
public class InsertFringeInFileEvent extends GwtEvent<InsertFringeInFileHandler> {

	private Fringe fringe;
	
	public static Type<InsertFringeInFileHandler> TYPE = new Type<InsertFringeInFileHandler>();
	
	public InsertFringeInFileEvent() {
		super();
	}

	public InsertFringeInFileEvent(Fringe fringe) {
		super();
		this.fringe = fringe;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<InsertFringeInFileHandler> getAssociatedType() {
		return TYPE;
	}

	public Fringe getFringe() {
		return fringe;
	}

	public void setFringe(Fringe fringe) {
		this.fringe = fringe;
	}


	@Override
	protected void dispatch(InsertFringeInFileHandler handler) {
		handler.onInsertFringeInFile(this);
	}

}


