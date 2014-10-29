package com.vw.ide.shared.servlet.projectmanager;

import com.vw.ide.shared.servlet.RequestResult;

@SuppressWarnings("serial")
public class RequestProjectCreationResult extends RequestResult {
	private ProjectDescription projectDescription;

	public ProjectDescription getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(ProjectDescription projectDescription) {
		this.projectDescription = projectDescription;
	}

	@Override
	public String toString() {
		return "RequestProjectCreationResult [projectDescription="
				+ projectDescription + "]";
	}
}
