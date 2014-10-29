package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.GetDirContentHandler;
import com.vw.ide.client.event.uiflow.GetDirContentEvent;
import com.vw.ide.client.presenters.Presenter;

public class GetDirContentEventHandler extends Presenter.PresenterEventHandler implements GetDirContentHandler {
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, null);
	}

	@Override
	public void onGetDirContent(GetDirContentEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	protected void process(DevelopmentBoardPresenter presenter, GetDirContentEvent event) {
		// presenter.getView().requestInitialDirContent();
	}
}
