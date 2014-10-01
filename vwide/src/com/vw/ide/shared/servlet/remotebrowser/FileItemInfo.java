package com.vw.ide.shared.servlet.remotebrowser;

import java.io.Serializable;

import com.vw.ide.client.projects.FilesTypesEnum;

/**
 * Simple class which is used to pass inforemation about file
 * 
 * @author Oleg
 * 
 */
@SuppressWarnings("serial")
public class FileItemInfo implements Serializable {
	private String name;
	private String path;
	private boolean isDir;
	private String checkSumOnOpen;
	private String checkSumOnClose;
	private boolean isEdited;

	public FileItemInfo() {
		super();
	}

	public FileItemInfo(String name, String path, boolean isDir) {
		super();
		this.name = name;
		this.path = path;
		this.isDir = isDir;
	}

	public static FilesTypesEnum getFileType(String fileName) {
		if (fileName.indexOf(".") != -1) {
			String sSuffix = fileName.substring(fileName.indexOf(".") + 1);
			switch (sSuffix) {
			case "vwml": return FilesTypesEnum.VWML;
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

	public FilesTypesEnum getFileType() {
		return getFileType(this.name);
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	@Override
	public String toString() {
		return "FileItemInfo [name=" + name + ", path=" + path + ", isDir="
				+ isDir + "]";
	}
}
