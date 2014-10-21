package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.BeforeCloseEvent;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.service.browser.callbacks.AnyFileOperationResultCallback;
import com.vw.ide.client.event.handler.EditorTabClosedHandler;
import com.vw.ide.client.event.uiflow.EditorTabClosedEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.projects.ProjectManager;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserServiceBroker;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.client.utils.Utils;

public class EditorTabClosedEventHandler extends Presenter.PresenterEventHandler implements EditorTabClosedHandler {
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (EditorTabClosedEvent)event);
	}

	@Override
	public void onEditorTabClosed(BeforeCloseEvent<Widget> event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	private void process(DevelopmentBoardPresenter presenter, EditorTabClosedEvent event) {
		ProjectManager projectManager = presenter.getProjectManager();
		Long fileId = ((FileSheet)event.getEvent().getItem()).getFileId();
		projectManager.getOpenedFilesContext().remove(fileId);
		projectManager.getAssociatedTabWidgetsContext().remove(fileId);
		if (projectManager.getOpenedFilesContext().isEmpty()) {
			presenter.getView().setTextForEditorContentPanel("files have not been selected");
		}
		presenter.getView().deleteFileItemId(fileId);
		String fileFullName = ((FileSheet) event.getEvent().getItem())
				.getFilePath()
				+ Utils.FILE_SEPARATOR
				+ ((FileSheet) event.getEvent().getItem()).getFileName();
		RemoteBrowserServiceBroker.requestForFileClosing(FlowController.getLoggedAsUser(),
														fileFullName,
														fileId,
														new AnyFileOperationResultCallback(presenter, true));
		
	}
}

