package com.vw.ide.client.ui.consolespanel.tab;

import com.vw.ide.client.ui.consolespanel.ConsolesPanelView;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;

public class InfoConsoleTab extends ConsoleTab {
	private ConsolesPanelView owner = null;
	private AceEditor control = null;
	
	public InfoConsoleTab() {
		super();
	}

	public InfoConsoleTab(ConsolesPanelView owner) {
		super();
		this.owner = owner;
	}

	public ConsolesPanelView getOwner() {
		return owner;
	}

	public void setOwner(ConsolesPanelView owner) {
		this.owner = owner;
	}
	
	public void setControl(AceEditor control) {
		this.control = control;
	}

	public void addInfo(String info) {
		String t = control.getText();
		if (t != null) {
			t += info;
		}
		else {
			t = info;
		}
		control.setText(t);
		int lines = control.getLines();
		control.gotoLine(lines - 1);
	}
	
	public void clearInfo() {
		
	}
}
