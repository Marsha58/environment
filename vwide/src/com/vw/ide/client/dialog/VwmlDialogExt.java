package com.vw.ide.client.dialog;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.widget.core.client.Dialog;

/**
 * Must be inherited by any VWML custom dialog
 * @author Olmel
 *
 */

//public class VwmlDialogExt extends MessageBox {

public class VwmlDialogExt extends Dialog{


	private String loggedAsUser;
	private Widget toDisable = null;
	
	public Widget getToDisable() {
		return toDisable;
	}

	public VwmlDialogExt() {
		super();
	}	
	

//	public void setSize(int width, int height) {
//		super.setWidth(width); 
//		super.setHeight(height); 
//	}

	
	public Dialog show(String caption, Widget toDisable, int left, int top) {
		this.toDisable = toDisable;
		super.setHeadingText(caption);
//		setGlassEnabled(true);
		setPosition(left, top);
		show();
		return this;
	}
	
	public Dialog showCenter(String caption, Widget toDisable) {
		this.toDisable = toDisable;
		super.setHeadingText(caption);
//		setGlassEnabled(true);
		
		
		int iX = Integer.parseInt( this.width.substring(0, this.width.length()-2))/2; 
		int iY = Integer.parseInt(this.height.substring(0, this.height.length()-2))/2; 
	    Point p = getElement().getAlignToXY(Document.get().getBody(), new AnchorAlignment(Anchor.CENTER, Anchor.CENTER), -iX, -iY);
	    setPagePosition(p.getX(), p.getY());		
		show();
		return this;
	}	

	public String getLoggedAsUser() {
		return loggedAsUser;
	}

	public void setLoggedAsUser(String loggedAsUser) {
		this.loggedAsUser = loggedAsUser;
	}
	
	/**
	 * Should be overridden by concrete dialog implementations
	 */
	public void prepare() {
		
	}
	
	
	
	
}
