package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.MoveFileEvent;

/**
 * Move file handler
 * @author Oleg
 *
 */
public interface MoveFileHandler extends EventHandler {
	void onMoveFile(MoveFileEvent event);
}
