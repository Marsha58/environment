package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.service.browser.callbacks.AnyFileOperationResultCallback;
import com.vw.ide.client.event.handler.SaveFileHandler;
import com.vw.ide.client.event.uiflow.SaveFileEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserServiceBroker;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.client.utils.Utils;

public class SaveFileEventHandler extends Presenter.PresenterEventHandler implements SaveFileHandler {
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (SaveFileEvent)event);
	}

	@Override
	public void onSaveFile(SaveFileEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	protected void process(DevelopmentBoardPresenter presenter, SaveFileEvent event) {
		FileSheet currentWidget = presenter.getView().getActiveFileSheetWidget();
		String sFullName = currentWidget.getFilePath() + Utils.FILE_SEPARATOR + currentWidget.getFileName();
		RemoteBrowserServiceBroker.requestForFileSaving(FlowController.getLoggedAsUser(),
														sFullName, currentWidget.getProjectId(),
														currentWidget.getFileId(), currentWidget.getAceEditor().getText(),
														new AnyFileOperationResultCallback(presenter, true));
	}	
}
