package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.GetCategoriesHandler;
import com.vw.ide.client.event.handler.LoginHandler;

/**
 * Fired when user pressed 'Login'
 * @author Omelnyk
 *
 */
public class GetCategoriesEvent extends GwtEvent<GetCategoriesHandler> {


	public static Type<GetCategoriesHandler> TYPE = new Type<GetCategoriesHandler>();
	
	public GetCategoriesEvent() {
		super();
	}


	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<GetCategoriesHandler> getAssociatedType() {
		return TYPE;
	}




	@Override
	protected void dispatch(GetCategoriesHandler handler) {
		handler.onGetCategories(this);
	}
}
