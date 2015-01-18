package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.FinishProjectExecutionEvent;

public interface FinishProjectExecutionHandler  extends EventHandler {
	public void onFinishProjectExecution(FinishProjectExecutionEvent event);
}
