package com.vw.ide.client.devboardext.event.handler.internal;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.SearchAndReplaceResultHandler;
import com.vw.ide.client.event.uiflow.SearchAndReplaceResultEvent;
import com.vw.ide.client.presenters.Presenter;

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
		presenter.getView().getConsoles().getSearchConsoleTab().add(event.getSearchResult());
	}
}
