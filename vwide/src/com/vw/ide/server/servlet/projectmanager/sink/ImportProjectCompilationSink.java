package com.vw.ide.server.servlet.projectmanager.sink;

import java.util.ArrayList;
import java.util.List;

import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.lang.processor.model.sink.CompilationSink;
import com.vw.lang.sink.OperationInfo;

/**
 * VWML compiler publishes events here
 * @author Oleg
 *
 */
public class ImportProjectCompilationSink extends CompilationSink {

	private String author;
	private String fileLocation;
	private String modulePackage;
	private String description;
	private String projectName;
	private String moduleName;
	private List<FileItemInfo> includes = new ArrayList<FileItemInfo>();
	
	public ImportProjectCompilationSink() {
		super.setMode(CompilationSink.Mode.SCAN_ONLY);
	}
	
	
	public void init() {
		
	}
	
	@Override
	public void handleInclude(String includeVwmlPath) {
		FileItemInfo fi = new FileItemInfo();
		fi.setRelPath(includeVwmlPath);
		fi.setAbsolutePath(includeVwmlPath);
		includes.add(fi);
	}

	@Override
	public void publishAuthor(String author) {
		this.author = author;
	}

	@Override
	public void publishFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	@Override
	public void publishModulePackage(String modulePackage) {
		this.modulePackage = modulePackage;
	}

	@Override
	public void publishProjectDescription(String description) {
		this.description = description;
	}

	@Override
	public void publishProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public void skippedCode(OperationInfo arg0) {
	}

	@Override
	public void publishModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public String getAuthor() {
		return author;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public String getModulePackage() {
		return modulePackage;
	}

	public String getDescription() {
		return description;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public List<FileItemInfo> getIncludes() {
		return includes;
	}
	
	public void delegateErrorCompilationMessage(OperationInfo opInfo) {
		
	}
}
