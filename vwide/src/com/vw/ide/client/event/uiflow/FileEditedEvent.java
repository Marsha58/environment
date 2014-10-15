package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.FileEditedHandler;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

/**
 * Fired when user pressed 'SelectFile'
 * @author OMelnyk
 *
 */
public class FileEditedEvent extends GwtEvent<FileEditedHandler> {

	private FileItemInfo fileItemInfo;
	
	public static Type<FileEditedHandler> TYPE = new Type<FileEditedHandler>();
	
	public FileEditedEvent() {
		super();
	}

	public FileEditedEvent(FileItemInfo fileItemInfo) {
		super();
		this.fileItemInfo = fileItemInfo;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<FileEditedHandler> getAssociatedType() {
		return TYPE;
	}

	public FileItemInfo getFileItemInfo() {
		return fileItemInfo;
	}

	public void setFileItemInfo(FileItemInfo fileItemInfo) {
		this.fileItemInfo = fileItemInfo;
	}


	@Override
	protected void dispatch(FileEditedHandler handler) {
		handler.onFileEdited(this);
	}
}
