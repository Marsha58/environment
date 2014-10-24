package com.vw.ide.client.dialog.vwmlproj.tab;

import com.google.gwt.user.client.ui.Widget;

public abstract class VwmlProjTab {
	private Widget widget;
	
	public abstract void initWidgets();
	public abstract void setup();
	public abstract boolean validate();
	
	public void setWidget(Widget widget) {
		this.widget = widget;
	}
	public Widget getWidget() {
		return widget;
	}
}
