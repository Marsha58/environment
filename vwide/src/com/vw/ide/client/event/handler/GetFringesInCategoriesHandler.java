package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.GetFringesInCategoriesEvent;

/**
 * Login handler
 * @author Omelnyk
 *
 */
public interface GetFringesInCategoriesHandler extends EventHandler {
	void onGetFringesInCategories(GetFringesInCategoriesEvent event);
}
