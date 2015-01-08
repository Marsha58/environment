package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.SearchTextHandler;

/**
 * Fired when user presses button 'Search'
 * @author Oleg
 *
 */
public class SearchTextEvent extends GwtEvent<SearchTextHandler> {

	public static Type<SearchTextHandler> TYPE = new Type<SearchTextHandler>();
	
	@Override
	protected void dispatch(SearchTextHandler handler) {
		handler.onSearchText(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SearchTextHandler> getAssociatedType() {
		return TYPE;
	}

}
