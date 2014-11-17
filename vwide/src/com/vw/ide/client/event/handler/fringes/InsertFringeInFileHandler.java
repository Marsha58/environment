package com.vw.ide.client.event.handler.fringes;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.fringes.InsertFringeInFileEvent;

/**
 * 
 * @author Omelnyk
 *
 */
public interface InsertFringeInFileHandler extends EventHandler {
	void onInsertFringeInFile(InsertFringeInFileEvent event);
}

