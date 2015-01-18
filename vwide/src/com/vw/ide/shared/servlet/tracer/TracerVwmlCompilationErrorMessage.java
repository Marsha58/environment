package com.vw.ide.shared.servlet.tracer;

import com.vw.ide.shared.servlet.processor.dto.compiler.CompilationErrorResult;

@SuppressWarnings("serial")
public class TracerVwmlCompilationErrorMessage extends TracerData<CompilationErrorResult> {

	public TracerVwmlCompilationErrorMessage() {
	}
	
	public TracerVwmlCompilationErrorMessage(CompilationErrorResult err) {
		setData(err);
	}
}
