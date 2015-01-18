package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.vw.ide.client.event.uiflow.CompilationErrorResultEvent;

public interface CompilationErrorResultHandler extends EventHandler {
	public void onCompilationError(CompilationErrorResultEvent event);
}
