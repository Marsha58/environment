package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.GetCategoriesHandler;
import com.vw.ide.client.event.handler.GetFringesInCategoriesHandler;
import com.vw.ide.client.event.handler.LoginHandler;

/**
 * Fired when user pressed 'Login'
 * @author Omelnyk
 *
 */
public class GetFringesInCategoriesEvent extends GwtEvent<GetFringesInCategoriesHandler> {


	public static Type<GetFringesInCategoriesHandler> TYPE = new Type<GetFringesInCategoriesHandler>();
	
	public GetFringesInCategoriesEvent() {
		super();
	}


	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<GetFringesInCategoriesHandler> getAssociatedType() {
		return TYPE;
	}




	@Override
	protected void dispatch(GetFringesInCategoriesHandler handler) {
		handler.onGetFringesInCategories(this);
	}
}
