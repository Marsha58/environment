package com.vw.ide.shared.servlet.remotebrowser;

import java.io.Serializable;

import com.vw.ide.client.projects.FilesTypesEnum;

/**
 * Simple class which is used to pass information about file
 * 
 * @author Oleg
 * 
 */
@SuppressWarnings("serial")
public class FileItemInfo implements Serializable {
	private Integer id;
	private String name;
	private String absolutePath;
	private String relPath;
	private boolean isDir;
	private Long projectId;
	private Long fileId;
	private String checkSumOnOpen;
	private String checkSumOnClose;
	private String content;
	private boolean isEdited;
	
	private static final int offsetId = 0x1023;
	private static int s_autoInc = 0;
	
	public FileItemInfo() {
		super();
		id = generatedId();
	}

	public FileItemInfo(String name, String absolutePath, boolean isDir) {
		super();
		this.name = name;
		this.absolutePath = absolutePath;
		this.isDir = isDir;
		id = generatedId();
	}

	public FileItemInfo(String name, String absolutePath, String relPath, boolean isDir) {
		super();
		this.name = name;
		this.absolutePath = absolutePath;
		this.relPath = relPath;
		this.isDir = isDir;
		id = generatedId();
	}
	
	public static FilesTypesEnum recognizeFileType(String fileName) {
		if (fileName.indexOf(".") != -1) {
			String sSuffix = fileName.substring(fileName.indexOf(".") + 1);
			switch (sSuffix) {
			case "vwml": return FilesTypesEnum.VWML;
			case "vwml_proj": return FilesTypesEnum.VWML_PROJ;
			case "java": return FilesTypesEnum.JAVA;
			case "xml": return FilesTypesEnum.XML;
			case "c": return FilesTypesEnum.CPP;
			case "cpp": return FilesTypesEnum.CPP;
			case "json": return FilesTypesEnum.JSON;
			case "html": return FilesTypesEnum.HTML;
			case "css": return FilesTypesEnum.CSS;
			case "js": return FilesTypesEnum.JS;
			default: return FilesTypesEnum.NOT_DEF;
			}
		} else return FilesTypesEnum.NOT_DEF;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	
	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	
	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String path) {
		this.absolutePath = path;

	}
	
	public String getRelPath() {
		return relPath;
	}

	public void setRelPath(String path) {
		this.relPath = path;
	}

	
	public boolean isDir() {
		return isDir;
	}

	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}

	public String getCheckSumOnOpen() {
		return checkSumOnOpen;
	}

	public void setCheckSumOnOpen(String checkSumOnOpen) {
		this.checkSumOnOpen = checkSumOnOpen;
	}

	public String getCheckSumOnClose() {
		return checkSumOnClose;
	}

	public void setCheckSumOnClose(String checkSumOnClose) {
		this.checkSumOnClose = checkSumOnClose;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((absolutePath == null) ? 0 : absolutePath.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileItemInfo other = (FileItemInfo) obj;
		if (absolutePath == null) {
			if (other.absolutePath != null)
				return false;
		} else if (!absolutePath.equals(other.absolutePath))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FileItemInfo [name=" + name + ", absolutePath=" + absolutePath + ", relPath=" + relPath +", isDir="
				+ isDir + "]";
	}
	
	private int generatedId() {
		return ++s_autoInc + offsetId;
	}
}
