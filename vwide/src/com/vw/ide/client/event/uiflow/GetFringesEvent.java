package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.GetCategoriesHandler;
import com.vw.ide.client.event.handler.GetFringesHandler;
import com.vw.ide.client.event.handler.LoginHandler;

/**
 * Fired when user pressed 'Login'
 * @author Omelnyk
 *
 */
public class GetFringesEvent extends GwtEvent<GetFringesHandler> {


	public static Type<GetFringesHandler> TYPE = new Type<GetFringesHandler>();
	
	public GetFringesEvent() {
		super();
	}


	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<GetFringesHandler> getAssociatedType() {
		return TYPE;
	}




	@Override
	protected void dispatch(GetFringesHandler handler) {
		handler.onGetFringes(this);
	}
}
