package com.vw.ide.shared.servlet.tracer;

import java.util.List;

import com.vw.ide.shared.servlet.processor.CommandProcessorResult;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;

@SuppressWarnings("serial")
public class TracerBuildVwmlProjectFinishedMessage extends TracerData<CommandProcessorResult> {
	private List<StackTraceElement> stack = null;
	private ProjectDescription project = null;

	public List<StackTraceElement> getStack() {
		return stack;
	}

	public void setStack(List<StackTraceElement> stack) {
		this.stack = stack;
	}

	public ProjectDescription getProject() {
		return project;
	}

	public void setProject(ProjectDescription project) {
		this.project = project;
	}
}
