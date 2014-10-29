package com.vw.ide.shared.servlet.projectmanager;

import java.util.ArrayList;
import java.util.List;

import com.vw.ide.shared.servlet.RequestResult;

@SuppressWarnings("serial")
/**
 * Requests upon user's available projects
 * @author Oleg
 *
 */
public class RequestUserAvailableProjectResult extends RequestResult {
	private List<ProjectDescription> availableProjects = new ArrayList<ProjectDescription>();

	public List<ProjectDescription> getAvailableProjects() {
		return availableProjects;
	}

	public void setAvailableProjects(List<ProjectDescription> availableProjects) {
		this.availableProjects = availableProjects;
	}
}
