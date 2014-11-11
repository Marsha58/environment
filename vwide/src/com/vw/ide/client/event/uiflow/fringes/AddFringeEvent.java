package com.vw.ide.client.event.uiflow.fringes;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.fringes.AddFringeHandler;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

/**
 * Fired when user add (create) Fringe
 * @author Omelnyk
 *
 */
public class AddFringeEvent extends GwtEvent<AddFringeHandler> {

	
	private Fringe fringe;

	public Fringe getFringe() {
		return fringe;
	}

	public void setFringe(Fringe fringe) {
		this.fringe = fringe;
	}


	public static Type<AddFringeHandler> TYPE = new Type<AddFringeHandler>();
	
	public AddFringeEvent() {
		super();
	}

	public AddFringeEvent(Fringe fringe) {
		super();
		this.fringe = fringe;
	}
	

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AddFringeHandler> getAssociatedType() {
		return TYPE;
	}


	@Override
	protected void dispatch(AddFringeHandler handler) {
		handler.onAddFringe(this);
	}
}

