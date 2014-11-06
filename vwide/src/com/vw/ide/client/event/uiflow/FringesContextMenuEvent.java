package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.vw.ide.client.event.handler.FringesContextMenuHandler;
import com.vw.ide.client.event.handler.ProjectMenuHandler;

/**
 * Fired when user pressed 'Load fringe jar'
 * @author OMelnyk
 *
 */
public class FringesContextMenuEvent extends GwtEvent<FringesContextMenuHandler>{

	private String menuId;
	
	public static com.google.gwt.event.shared.GwtEvent.Type<FringesContextMenuHandler> TYPE = new Type<FringesContextMenuHandler>();
	
	public FringesContextMenuEvent() {
		super();
	}

	public FringesContextMenuEvent(String menuId) {
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
	public com.google.gwt.event.shared.GwtEvent.Type<FringesContextMenuHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FringesContextMenuHandler handler) {
		handler.onFringesContextMenuClick(this);
	}

}
