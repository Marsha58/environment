package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.AceColorThemeChangedHandler;
import com.vw.ide.client.event.uiflow.AceColorThemeChangedEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.projects.ProjectManager;
import com.vw.ide.client.ui.toppanel.FileSheet;

public class AceColorThemeChangedEventHandler extends Presenter.PresenterEventHandler implements AceColorThemeChangedHandler {
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (AceColorThemeChangedEvent)event);
	}

	@Override
	public void onAceColorThemeChanged(AceColorThemeChangedEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	private void process(DevelopmentBoardPresenter presenter, AceColorThemeChangedEvent event) {
		ProjectManager projectManager = ((DevelopmentBoardPresenter)presenter).getProjectManager();
		for (Long lKey : projectManager.getAssociatedTabWidgetsContext().keySet()) {
			FileSheet curFileSheet = (FileSheet) projectManager.getAssociatedTabWidgetsContext().get(lKey);
			curFileSheet.getAceEditor().setTheme(event.getEvent().getSelectedItem());
		}
	}
}
