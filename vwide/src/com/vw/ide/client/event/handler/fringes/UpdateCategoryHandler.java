package com.vw.ide.client.event.handler.fringes;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.fringes.UpdateCategoryEvent;

/**
 * UpdateCategory handler
 * @author Omelnyk
 *
 */
public interface UpdateCategoryHandler extends EventHandler {
	void onUpdateCategory(UpdateCategoryEvent event);
}

