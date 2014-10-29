package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.FileEditedHandler;
import com.vw.ide.client.event.uiflow.FileEditedEvent;
import com.vw.ide.client.presenters.Presenter;

public class FileEditedEventHandler extends Presenter.PresenterEventHandler implements FileEditedHandler {
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (FileEditedEvent)event);
	}

	@Override
	public void onFileEdited(FileEditedEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	private void process(DevelopmentBoardPresenter presenter, FileEditedEvent event) {
		presenter.getView().markFileAsEdited(event.getItemInfo(), true);
	}
}
