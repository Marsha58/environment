package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.ProjectMenuEvent;
import com.vw.ide.client.event.uiflow.SaveFileEvent;
import com.vw.ide.client.event.uiflow.SelectFileEvent;

/**
 * Login handler
 * @author OMelnyk
 *
 */
public interface ProjectMenuHandler extends EventHandler {
	void onProjectMenuClick(ProjectMenuEvent event);
}
