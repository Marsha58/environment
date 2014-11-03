package com.vw.ide.shared.servlet.projectmanager;

import com.vw.ide.shared.servlet.RequestResult;

/**
 * General result on any project's request
 * @author Oleg
 *
 */

@SuppressWarnings("serial")
public class RequestProjectResult extends RequestResult {
	private ProjectDescription description;

	public ProjectDescription getDescription() {
		return description;
	}

	public void setDescription(ProjectDescription description) {
		this.description = description;
	}
}
