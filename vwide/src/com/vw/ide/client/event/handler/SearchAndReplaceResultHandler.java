package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.SearchAndReplaceResultEvent;

public interface SearchAndReplaceResultHandler extends EventHandler {
	public void onSearchAndReplaceResult(SearchAndReplaceResultEvent event);
}
