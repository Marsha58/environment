package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.MoveFileHandler;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;

/**
 * Fired on end of drag-and-drop operation
 * @author Oleg
 *
 */
public class MoveFileEvent extends GwtEvent<MoveFileHandler>{

	private ProjectItemInfo movedItem;
	private ProjectItemInfo placeForItem;
	
	public static Type<MoveFileHandler> TYPE = new Type<MoveFileHandler>();

	public MoveFileEvent() {
		super();
	}

	public MoveFileEvent(ProjectItemInfo movedItem, ProjectItemInfo placeForItem) {
		super();
		this.movedItem = movedItem;
		this.placeForItem = placeForItem;
	}

	public ProjectItemInfo getMovedItem() {
		return movedItem;
	}

	public ProjectItemInfo getPlaceForItem() {
		return placeForItem;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<MoveFileHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MoveFileHandler handler) {
		handler.onMoveFile(this);
	}
}
