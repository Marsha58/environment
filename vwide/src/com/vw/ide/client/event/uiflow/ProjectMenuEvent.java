package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.ProjectMenuHandler;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

/**
 * Fired when user pressed 'SelectFile'
 * @author OMelnyk
 *
 */
public class ProjectMenuEvent extends GwtEvent<ProjectMenuHandler> {

	private String menuId;
	
	public static Type<ProjectMenuHandler> TYPE = new Type<ProjectMenuHandler>();
	
	public ProjectMenuEvent() {
		super();
	}

	public ProjectMenuEvent(String menuId) {
		super();
		this.menuId = menuId;
	}	

	public String getMenuId() {
		return menuId;
	}
	
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}	
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ProjectMenuHandler> getAssociatedType() {
		return TYPE;
	}



	@Override
	protected void dispatch(ProjectMenuHandler handler) {
		handler.onProjectMenuClick(this);
	}
}
