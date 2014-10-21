package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.ProjectMenuEvent;

/**
 * Login handler
 * @author OMelnyk
 *
 */
public interface ProjectMenuHandler extends EventHandler {
	void onProjectMenuClick(ProjectMenuEvent event);
}
