package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.SaveAllFilesHandler;
import com.vw.ide.client.event.uiflow.SaveAllFilesEvent;
import com.vw.ide.client.event.uiflow.SaveFileEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.ui.toppanel.FileSheet;

public class SaveAllFilesEventHandler extends Presenter.PresenterEventHandler implements SaveAllFilesHandler {

	@Override
	public void onSaveAllFiles(SaveAllFilesEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (SaveAllFilesEvent)event);
	}

	protected void process(DevelopmentBoardPresenter presenter, SaveAllFilesEvent event) {
		for(int i = 0; i < presenter.getView().getEditorPanel().getTabPanel().getWidgetCount(); i++) {
			FileSheet fs = (FileSheet)presenter.getView().getEditorPanel().getTabPanel().getWidget(i);
			presenter.fireEvent(new SaveFileEvent(fs.getItemInfo()));
		}
	}
}
