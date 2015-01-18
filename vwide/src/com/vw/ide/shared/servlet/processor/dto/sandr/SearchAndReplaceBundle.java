package com.vw.ide.shared.servlet.processor.dto.sandr;

import java.io.Serializable;
import java.util.List;

import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

@SuppressWarnings("serial")
public class SearchAndReplaceBundle implements Serializable {
	private String search;
	private String replace;
	private List<FileItemInfo> replacedItems = null;
	private Integer phase = PHASE_SEARCH;
	private ProjectDescription project;
	
	transient public static final Integer PHASE_SEARCH  = 0x01;
	transient public static final Integer PHASE_REPLACE = 0x02;
	
	public SearchAndReplaceBundle() {
		
	}
	
	public SearchAndReplaceBundle(Integer phase, String search, String replace, ProjectDescription project, List<FileItemInfo> replacedItems) {
		super();
		this.phase = phase;
		this.search = search;
		this.replace = replace;
		this.project = project;
		this.replacedItems = replacedItems;
	}
	
	public Integer getPhase() {
		return phase;
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

	public List<FileItemInfo> getReplacedItems() {
		return replacedItems;
	}

	@Override
	public String toString() {
		return "SearchAndReplaceBundle [search=" + search + ", replace="
				+ replace + ", project=" + project.getProjectName() + "]";
	}
}
