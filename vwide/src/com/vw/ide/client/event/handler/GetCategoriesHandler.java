package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.GetCategoriesEvent;

/**
 * Login handler
 * @author Omelnyk
 *
 */
public interface GetCategoriesHandler extends EventHandler {
	void onGetCategories(GetCategoriesEvent event);
}
