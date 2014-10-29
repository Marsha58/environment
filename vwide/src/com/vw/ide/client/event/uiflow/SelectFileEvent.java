package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.SelectFileHandler;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;

/**
 * Fired when user pressed 'SelectFile'
 * @author OMelnyk
 *
 */
public class SelectFileEvent extends GwtEvent<SelectFileHandler> {

	private ProjectItemInfo fileItemInfo;
	
	public static Type<SelectFileHandler> TYPE = new Type<SelectFileHandler>();
	
	public SelectFileEvent() {
		super();
	}

	public SelectFileEvent(ProjectItemInfo fileItemInfo) {
		super();
		this.fileItemInfo = fileItemInfo;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SelectFileHandler> getAssociatedType() {
		return TYPE;
	}

	public ProjectItemInfo getFileItemInfo() {
		return fileItemInfo;
	}

	public void setFileItemInfo(ProjectItemInfo fileItemInfo) {
		this.fileItemInfo = fileItemInfo;
	}


	@Override
	protected void dispatch(SelectFileHandler handler) {
		handler.onSelectFile(this);
	}
}
