package com.vw.ide.client.event.uiflow.fringes;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.vw.ide.client.event.handler.GetCategoriesHandler;
import com.vw.ide.client.event.handler.GetDirContentHandler;
import com.vw.ide.client.event.handler.GetFringesHandler;
import com.vw.ide.client.event.handler.LoginHandler;
import com.vw.ide.client.event.handler.fringes.UpdateFringeHandler;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

/**
 * Fired when user update Fringe
 * @author Omelnyk
 *
 */
public class UpdateFringeEvent extends GwtEvent<UpdateFringeHandler> {

	
	private Fringe fringe;

	public Fringe getFringe() {
		return fringe;
	}

	public void setFringe(Fringe fringe) {
		this.fringe = fringe;
	}


	public static Type<UpdateFringeHandler> TYPE = new Type<UpdateFringeHandler>();
	
	public UpdateFringeEvent() {
		super();
	}

	public UpdateFringeEvent(Fringe fringe) {
		super();
		this.fringe = fringe;
	}
	
	


	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<UpdateFringeHandler> getAssociatedType() {
		return TYPE;
	}


	@Override
	protected void dispatch(UpdateFringeHandler handler) {
		handler.onUpdateFringe(this);
	}
}
