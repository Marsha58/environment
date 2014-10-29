package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.service.browser.callbacks.AnyFileOperationResultCallback;
import com.vw.ide.client.event.handler.FileStateChangedHandler;
import com.vw.ide.client.event.uiflow.FileStateChangedEvent;
import com.vw.ide.client.event.uiflow.SaveFileEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserServiceBroker;
import com.vw.ide.client.ui.editorpanel.FileSheet;

/**
 * Login handler
 * @author OMelnyk
 *
 */
public class FileStateChangedEventHandler extends Presenter.PresenterEventHandler implements FileStateChangedHandler {
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (FileStateChangedEvent)event);		
	}
	
	@Override
	public	void onFileStateChanged(FileStateChangedEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}		
	}
	
	protected void process(DevelopmentBoardPresenter presenter, FileStateChangedEvent event) {
		Long fileId = event.getFileId();
		presenter.getProjectManager().setFileState(fileId, event.getIsEdited());
		presenter.getEditorPanel().setFileEditedState(presenter.getProjectManager().getAssociatedTabWidget(fileId), event.getIsEdited());

	}		

}