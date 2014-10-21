/**
 * Sencha GXT 3.1.1 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.vw.ide.client.model;

@SuppressWarnings("serial")
public class FileDto extends BaseDto {

	private String folder;
	private Long projectId;
	private Long fileId;

	protected FileDto() {
		setType("file");
	}

	public FileDto(Integer id, String fileName, String folder, String relPath,
			String absolutePath) {
		super(id, fileName);		
		setType("file");
		this.folder = folder;
		setRelPath(relPath);
		setAbsolutePath(absolutePath);
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
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

}
