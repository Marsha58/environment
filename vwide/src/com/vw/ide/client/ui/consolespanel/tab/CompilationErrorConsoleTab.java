package com.vw.ide.client.ui.consolespanel.tab;

import com.sencha.gxt.data.shared.ListStore;
import com.vw.ide.client.ui.consolespanel.ConsolesPanelView;
import com.vw.ide.shared.servlet.processor.dto.compiler.CompilationErrorResult;

public class CompilationErrorConsoleTab extends ConsoleTab {
	private ConsolesPanelView owner = null;
	private ListStore<CompilationErrorResult> compilationErrorResult;

	public CompilationErrorConsoleTab() {
		super();
	}
	
	public CompilationErrorConsoleTab(ConsolesPanelView owner) {
		super();
		this.owner = owner;
	}

	public ConsolesPanelView getOwner() {
		return owner;
	}

	public void setOwner(ConsolesPanelView owner) {
		this.owner = owner;
	}

	public void setCompilationErrorResult(ListStore<CompilationErrorResult> compilationErrorResult) {
		this.compilationErrorResult = compilationErrorResult;
	}
	
	public void addCompilationError(CompilationErrorResult error) {
		compilationErrorResult.add(error);
	}
	
	public void clear() {
		compilationErrorResult.clear();
	}
	
}
