package com.vw.ide.client.ui.consolespanel.tab;

import com.sencha.gxt.data.shared.ListStore;
import com.vw.ide.client.ui.consolespanel.ConsolesPanelView;
import com.vw.ide.shared.servlet.processor.dto.compiler.CompilationErrorResult;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

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
	
	public void removeByFileItemInfo(FileItemInfo fi) {
		if (compilationErrorResult == null || compilationErrorResult.size() == 0 || fi == null) {
			return;
		}
		int i = 0;
		while(i != compilationErrorResult.size()) {
			for(i = 0; i < compilationErrorResult.size(); i++) {
				CompilationErrorResult r = compilationErrorResult.get(i);
				if (r.getFileInfo() != null && r.getFileInfo().equals(fi)) {
					compilationErrorResult.remove(r);
					break;
				}
			}
		}
	}

	public void removeByProjectName(String projectName) {
		if (compilationErrorResult == null || compilationErrorResult.size() == 0 || projectName == null) {
			return;
		}
		int i = 0;
		while(i != compilationErrorResult.size()) {
			for(i = 0; i < compilationErrorResult.size(); i++) {
				CompilationErrorResult r = compilationErrorResult.get(i);
				if (r.getProjectName() != null && r.getProjectName().equals(projectName)) {
					compilationErrorResult.remove(r);
					break;
				}
			}
		}
	}
	
	public void renameByFileItemInfo(FileItemInfo fi, FileItemInfo newFi) {
		if (compilationErrorResult == null || compilationErrorResult.size() == 0 || fi == null) {
			return;
		}
		for(int i = 0; i < compilationErrorResult.size(); i++) {
			CompilationErrorResult r = compilationErrorResult.get(i);
			if (r.getFileInfo() != null && r.getFileInfo().equals(fi)) {
				compilationErrorResult.remove(i);
				r.setFileInfo(newFi);
				compilationErrorResult.add(r);
			}
		}
	}
	
	public void clear() {
		compilationErrorResult.clear();
	}
	
}
