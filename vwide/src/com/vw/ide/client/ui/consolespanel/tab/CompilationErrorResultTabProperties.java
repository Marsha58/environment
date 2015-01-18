package com.vw.ide.client.ui.consolespanel.tab;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.vw.ide.shared.servlet.processor.dto.compiler.CompilationErrorResult;

public interface CompilationErrorResultTabProperties extends PropertyAccess<CompilationErrorResult>{
	@Path("key")
	ModelKeyProvider<CompilationErrorResult> key();
	
	@Path("userName")
	LabelProvider<CompilationErrorResult> nameLabel();
	
	ValueProvider<CompilationErrorResult, String> projectName();
	
	ValueProvider<CompilationErrorResult, String> file();
	
	ValueProvider<CompilationErrorResult, String> cause();
	
	ValueProvider<CompilationErrorResult, String> place();
}
