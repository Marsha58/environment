package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.FileEditedHandler;
import com.vw.ide.client.event.uiflow.FileEditedEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.projects.ProjectManager;

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
		ProjectManager projectManager = presenter.getProjectManager();
		Long fileId = event.getFileItemInfo().getFileId();
		projectManager.setFileState(fileId, true);
		Widget editedWidget = projectManager.getAssociatedTabWidget(fileId);
		presenter.getView().markFileAsEdited(editedWidget, true);
	}
}
