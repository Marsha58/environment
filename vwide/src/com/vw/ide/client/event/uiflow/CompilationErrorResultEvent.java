package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.CompilationErrorResultHandler;
import com.vw.ide.shared.servlet.processor.dto.compiler.CompilationErrorResult;

/**
 * Fired upon VWML's compilation error
 * @author Oleg
 *
 */
public class CompilationErrorResultEvent extends GwtEvent<CompilationErrorResultHandler> {

	private CompilationErrorResult compilationResult;
	public static Type<CompilationErrorResultHandler> TYPE = new Type<CompilationErrorResultHandler>();

	public CompilationErrorResultEvent(CompilationErrorResult compilationResult) {
		super();
		this.compilationResult = compilationResult;
	}

	public CompilationErrorResult getCompilationResult() {
		return compilationResult;
	}

	public void setCompilationResult(CompilationErrorResult compilationResult) {
		this.compilationResult = compilationResult;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CompilationErrorResultHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CompilationErrorResultHandler handler) {
		handler.onCompilationError(this);
	}
}
