package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.service.browser.callbacks.DirOperationFileReadingResultCallback;
import com.vw.ide.client.event.handler.SelectFileHandler;
import com.vw.ide.client.event.uiflow.SelectFileEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserServiceBroker;

public class SelectFileEventHandler extends Presenter.PresenterEventHandler implements SelectFileHandler {
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		RemoteBrowserServiceBroker.requestForReadingFile(
				FlowController.getLoggedAsUser(),
				((SelectFileEvent)event).getFileItemInfo().getAbsolutePath(),
				((SelectFileEvent)event).getFileItemInfo().getProjectId(),
				((SelectFileEvent)event).getFileItemInfo().getFileId(),
				new DirOperationFileReadingResultCallback((DevelopmentBoardPresenter)presenter));
	}

	@Override
	public void onSelectFile(SelectFileEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
}
