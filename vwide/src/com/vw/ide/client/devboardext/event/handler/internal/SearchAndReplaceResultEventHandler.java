package com.vw.ide.client.devboardext.event.handler.internal;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.SearchAndReplaceResultHandler;
import com.vw.ide.client.event.uiflow.SearchAndReplaceResultEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.ui.consolespanel.tab.SearchConsoleTab;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;

public class SearchAndReplaceResultEventHandler extends Presenter.PresenterEventHandler implements SearchAndReplaceResultHandler {

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (SearchAndReplaceResultEvent)event);
	}

	@Override
	public void onSearchAndReplaceResult(SearchAndReplaceResultEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}

	private void process(DevelopmentBoardPresenter presenter, SearchAndReplaceResultEvent event) {
		if (!event.getSearchResult().isFileAsReplaced()) {
			processSearch(presenter, event);
		}
		else {
			processReplace(presenter, event);
		}
	}
	
	private void processSearch(DevelopmentBoardPresenter presenter, SearchAndReplaceResultEvent event) {
		presenter.getView().getConsoles().getSearchConsoleTab().add(event.getSearchResult());
		if (event.getSearchResult().getReplace() != null && event.getSearchResult().getReplace().length() != 0) {
			presenter.getView().getConsoles().getSearchConsoleTab().enableSelectMenuItem(SearchConsoleTab.applyReplace, true);
		}
		presenter.getView().getConsoles().getSearchConsoleTab().enableSelectMenuItem(SearchConsoleTab.clearAll, true);
		presenter.getView().getConsoles().getSearchConsoleTab().enableSelectMenuItem(SearchConsoleTab.selectAll, true);
		presenter.getView().getConsoles().getSearchConsoleTab().enableSelectMenuItem(SearchConsoleTab.unselectAll, true);
	}
	
	private void processReplace(DevelopmentBoardPresenter presenter, SearchAndReplaceResultEvent event) {
		ProjectItemInfo pi = presenter.getView().getProjectPanel().getProjectItemByFileItemInfo(event.getSearchResult().getFileInfo());
		if (pi != null) {
			if (pi.isAlreadyOpened()) {
				// update content
				pi.getFileSheet().setText(event.getSearchResult().getFileInfo().getContent());
			}
			else {
				presenter.getView().getProjectPanel().select(pi);
			}
			// remove search and result item from search console tab
			presenter.getView().getConsoles().getSearchConsoleTab().removeByFileItemInfo(event.getSearchResult().getFileInfo());
		}
	}
}
