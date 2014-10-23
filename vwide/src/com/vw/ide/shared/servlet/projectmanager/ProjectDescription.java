package com.vw.ide.shared.servlet.projectmanager;

import java.io.Serializable;

/**
 * Contains information about VWML project
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class ProjectDescription implements Serializable {
	private String userName;
	private String projectName;
	private String projectPath;
	private String packageName;
	private String javaSrcPath;
	private String author;
	private String descr;
	private String mainModuleName;
	private InterpreterDescription interpreterDescription;
	private CompilerSwitchesDescription compilerSwitches;
	
	public ProjectDescription() {
		super();
	}

	public ProjectDescription(String userName, String projectName,
			String projectPath, String packageName, String javaSrcPath,
			String author, String descr) {
		super();
		this.userName = userName;
		this.projectName = projectName;
		this.projectPath = projectPath;
		this.packageName = packageName;
		this.javaSrcPath = javaSrcPath;
		this.author = author;
		this.descr = descr;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getJavaSrcPath() {
		return javaSrcPath;
	}

	public void setJavaSrcPath(String javaSrcPath) {
		this.javaSrcPath = javaSrcPath;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public InterpreterDescription getInterpreterDescription() {
		return interpreterDescription;
	}

	public void setInterpreterDescription(InterpreterDescription interpreterDescription) {
		this.interpreterDescription = interpreterDescription;
	}

	public CompilerSwitchesDescription getCompilerSwitches() {
		return compilerSwitches;
	}

	public void setCompilerSwitches(CompilerSwitchesDescription compilerSwitches) {
		this.compilerSwitches = compilerSwitches;
	}

	public String getMainModuleName() {
		return mainModuleName;
	}

	public void setMainModuleName(String mainModuleName) {
		this.mainModuleName = mainModuleName;
	}
}
