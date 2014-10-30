package com.vw.ide.client.dialog.vwmlproj.tab;

import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.client.dialog.vwmlproj.VwmlProjectDialog;

public abstract class VwmlProjTab {
	private Widget widget;
	private VwmlProjectDialog.EditMode editMode;
	
	public abstract void initWidgets();
	public abstract void setup();
	public abstract boolean validate();
	
	public void setWidget(Widget widget) {
		this.widget = widget;
	}
	
	public Widget getWidget() {
		return widget;
	}
	
	public VwmlProjectDialog.EditMode getEditMode() {
		return editMode;
	}

	public void setEditMode(VwmlProjectDialog.EditMode editMode) {
		this.editMode = editMode;
	}
}
