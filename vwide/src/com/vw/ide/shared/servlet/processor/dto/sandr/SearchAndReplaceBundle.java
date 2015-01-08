package com.vw.ide.shared.servlet.processor.dto.sandr;

import java.io.Serializable;

import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;

@SuppressWarnings("serial")
public class SearchAndReplaceBundle implements Serializable {
	private String search;
	private String replace;
	private ProjectDescription project;
	
	public SearchAndReplaceBundle() {
		
	}
	
	public SearchAndReplaceBundle(String search, String replace, ProjectDescription project) {
		super();
		this.search = search;
		this.replace = replace;
		this.project = project;
	}
	
	public String getSearch() {
		return search;
	}
	
	public String getReplace() {
		return replace;
	}

	public ProjectDescription getProject() {
		return project;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public void setReplace(String replace) {
		this.replace = replace;
	}

	public void setProject(ProjectDescription project) {
		this.project = project;
	}

	@Override
	public String toString() {
		return "SearchAndReplaceBundle [search=" + search + ", replace="
				+ replace + ", project=" + project.getProjectName() + "]";
	}
}
