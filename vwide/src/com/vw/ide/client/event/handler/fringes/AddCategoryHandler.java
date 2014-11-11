package com.vw.ide.client.event.handler.fringes;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.fringes.AddCategoryEvent;

/**
 * AddCategory handler
 * @author Omelnyk
 *
 */
public interface AddCategoryHandler extends EventHandler {
	void onAddCategory(AddCategoryEvent event);
}

