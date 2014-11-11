package com.vw.ide.client.event.handler.fringes;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.fringes.DeleteCategoryEvent;

/**
 * DeleteCategory handler
 * @author Omelnyk
 *
 */
public interface DeleteCategoryHandler  extends EventHandler {
	void onDeleteCategory(DeleteCategoryEvent event);
}
