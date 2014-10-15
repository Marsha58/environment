package com.vw.ide.client.projects;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

/**
 * Simple class which is used to pass information about file
 * 
 * @author OMelnyk
 * 
 */
public class ProjectItemImpl implements ProjectItem{
	private String projectName;
	private String projectPath;
	private String packageName;
	private String javaSourcePath;
	private String author;
	private String descr;
	
	
	public ProjectItemImpl() {
		super();
	}

	public ProjectItemImpl(String projectName, String  projectPath) {
		super();
		this.projectName = projectName;
		this.projectPath = projectPath;
	}

	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getJavaSourcePath() {
		return javaSourcePath;
	}

	public void setJavaSourcePath(String javaSourcePath) {
		this.javaSourcePath = javaSourcePath;
	}	

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String geDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}	

	
	@Override
	public String toString() {
		return "ProjectInfo [projectName=" + projectName + ", projectPath=" + projectPath + ", packageName="
				+ packageName + ", javaSourcePath=" + javaSourcePath + ", author=" + author + ", descr=" + descr + "]";
	}
	
}
