package com.vw.ide.shared.servlet.projectmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vw.ide.shared.servlet.projectmanager.specific.CompilerSwitchesDescription;
import com.vw.ide.shared.servlet.projectmanager.specific.InterpreterDescription;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

/**
 * Contains information about VWML project
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class ProjectDescription implements Serializable {
	private Integer id;
	private String userName;
	private String projectName;
	private String projectPath;
	private String packageName;
	private String javaSrcPath;
	private String author;
	private String descr;
	private String mainModuleName;
	private InterpreterDescription interpreterDescription = new InterpreterDescription();
	private CompilerSwitchesDescription compilerSwitches = new CompilerSwitchesDescription();
	private FileItemInfo mainProjectFile = new FileItemInfo();
	private FileItemInfo projectDescriptionFile = new FileItemInfo();
	private List<FileItemInfo> projectFiles = new ArrayList<FileItemInfo>();
	
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

	public List<FileItemInfo> getProjectFiles() {
		return projectFiles;
	}

	public void setProjectFiles(List<FileItemInfo> projectFiles) {
		this.projectFiles = projectFiles;
	}

	public FileItemInfo getProjectDescriptionFile() {
		return projectDescriptionFile;
	}

	public void setProjectDescriptionFile(FileItemInfo projectDescriptionFile) {
		this.projectDescriptionFile = projectDescriptionFile;
	}

	public FileItemInfo getMainProjectFile() {
		return mainProjectFile;
	}

	public void setMainProjectFile(FileItemInfo mainProjectFile) {
		this.mainProjectFile = mainProjectFile;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void wellForm() {
		final String unknown = "<unknown>";
		if (id == null) {
			id = new Integer(0x1234);
		}
		if (userName == null) {
			userName = unknown;
		}
		if (projectName == null) {
			projectName = unknown;
		}
		if (projectPath == null) {
			projectPath = unknown;
		}
		if (packageName == null) {
			packageName = unknown;
		}
		if (javaSrcPath == null) {
			javaSrcPath = unknown;
		}
		if (author == null) {
			author = unknown;
		}
		if (descr == null) {
			descr = unknown;
		}
		if (mainModuleName == null) {
			mainModuleName = unknown;
		}
	}
	
	@Override
	public String toString() {
		return "ProjectDescription [userName=" + userName + ", projectName="
				+ projectName + ", projectPath=" + projectPath
				+ ", packageName=" + packageName + ", javaSrcPath="
				+ javaSrcPath + ", author=" + author + ", descr=" + descr
				+ ", mainModuleName=" + mainModuleName
				+ ", interpreterDescription=" + ((interpreterDescription != null) ? interpreterDescription.readable() : "null")
				+ ", compilerSwitches=" + ((compilerSwitches != null) ? compilerSwitches.readable() : "null") + "]";
	}
}
