package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.FileStateChangedHandler;

/**
 * Fired when file has been edited or saved
 * @author OMelnyk
 *
 */
public class FileStateChangedEvent extends GwtEvent<FileStateChangedHandler> {

	private Long fileId;
	private boolean isEdited;
	
	public static Type<FileStateChangedHandler> TYPE = new Type<FileStateChangedHandler>();
	
	public FileStateChangedEvent() {
		super();
	}

	public FileStateChangedEvent(Long fileId, boolean isEdited) {
		super();
		this.fileId = fileId;
		this.isEdited = isEdited;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<FileStateChangedHandler> getAssociatedType() {
		return TYPE;
	}

	public boolean getIsEdited() {
		return isEdited;
	}

	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}


	@Override
	protected void dispatch(FileStateChangedHandler handler) {
		handler.onFileStateChanged(this);
	}
	
}
