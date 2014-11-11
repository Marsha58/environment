package com.vw.ide.client.event.uiflow.fringes;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.fringes.DeleteFringeHandler;

/**
 * Fired when user delete Fringe
 * @author Omelnyk
 *
 */
public class DeleteFringeEvent  extends GwtEvent<DeleteFringeHandler> {

	
	private Integer fringeId;

	public Integer getFringeId() {
		return fringeId;
	}

	public void setFringeId(Integer fringeId) {
		this.fringeId = fringeId;
	}


	public static Type<DeleteFringeHandler> TYPE = new Type<DeleteFringeHandler>();
	
	public DeleteFringeEvent() {
		super();
	}

	public DeleteFringeEvent(Integer fringeId) {
		super();
		this.fringeId = fringeId;
	}
	

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DeleteFringeHandler> getAssociatedType() {
		return TYPE;
	}


	@Override
	protected void dispatch(DeleteFringeHandler handler) {
		handler.onDeleteFringe(this);
	}
}


