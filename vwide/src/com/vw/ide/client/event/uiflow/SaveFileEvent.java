package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.SaveFileHandler;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;

/**
 * Fired when user pressed 'SelectFile'
 * @author OMelnyk
 *
 */
public class SaveFileEvent extends GwtEvent<SaveFileHandler> {

	private ProjectItemInfo saveProjectItemInfo;
	private boolean closeAfterSave;
	
	public static Type<SaveFileHandler> TYPE = new Type<SaveFileHandler>();
	
	public SaveFileEvent(ProjectItemInfo saveProjectItemInfo) {
		super();
		this.saveProjectItemInfo = saveProjectItemInfo;
	}

	public ProjectItemInfo getSaveProjectItemInfo() {
		return saveProjectItemInfo;
	}

	public boolean isCloseAfterSave() {
		return closeAfterSave;
	}

	public void setCloseAfterSave(boolean closeAfterSave) {
		this.closeAfterSave = closeAfterSave;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SaveFileHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SaveFileHandler handler) {
		handler.onSaveFile(this);
	}
}
