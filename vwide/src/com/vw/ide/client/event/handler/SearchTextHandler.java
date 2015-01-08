package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.SearchTextEvent;

public interface SearchTextHandler extends EventHandler {
	public void onSearchText(SearchTextEvent event);

}
