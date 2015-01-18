package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.SearchAndReplaceResultHandler;
import com.vw.ide.shared.servlet.processor.dto.sandr.SearchAndReplaceResult;

/**
 * Fired upon received result on SearchTextEvent
 * @author Oleg
 *
 */
public class SearchAndReplaceResultEvent extends GwtEvent<SearchAndReplaceResultHandler> {

	private SearchAndReplaceResult searchResult;
	
	public static Type<SearchAndReplaceResultHandler> TYPE = new Type<SearchAndReplaceResultHandler>();
	
	public SearchAndReplaceResultEvent() {
		
	}
	
	public SearchAndReplaceResultEvent(SearchAndReplaceResult searchResult) {
		setSearchResult(searchResult);
	}
	
	public SearchAndReplaceResult getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(SearchAndReplaceResult searchResult) {
		this.searchResult = searchResult;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SearchAndReplaceResultHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SearchAndReplaceResultHandler handler) {
		handler.onSearchAndReplaceResult(this);
	}
}
