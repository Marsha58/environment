package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.StartProjectExecutionEvent;

public interface StartProjectExecutionHandler extends EventHandler {
	public void onStartProjectExecution(StartProjectExecutionEvent event);
}
