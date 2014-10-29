package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.FileEditedHandler;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;

/**
 * Fired when user pressed 'SelectFile'
 * @author OMelnyk
 *
 */
public class FileEditedEvent extends GwtEvent<FileEditedHandler> {

	private ProjectItemInfo itemInfo;
	
	public static Type<FileEditedHandler> TYPE = new Type<FileEditedHandler>();
	
	public FileEditedEvent() {
		super();
	}

	public FileEditedEvent(ProjectItemInfo itemInfo) {
		super();
		this.itemInfo = itemInfo;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<FileEditedHandler> getAssociatedType() {
		return TYPE;
	}


	public ProjectItemInfo getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(ProjectItemInfo itemInfo) {
		this.itemInfo = itemInfo;
	}

	@Override
	protected void dispatch(FileEditedHandler handler) {
		handler.onFileEdited(this);
	}
}
