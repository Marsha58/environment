package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.SaveAllFilesEvent;

/**
 * Save all files
 * @author Oleg
 *
 */
public interface SaveAllFilesHandler extends EventHandler {
	public void onSaveAllFiles(SaveAllFilesEvent event);
}
