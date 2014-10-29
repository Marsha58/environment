package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.service.browser.callbacks.AnyFileOperationResultCallback;
import com.vw.ide.client.devboardext.service.browser.callbacks.HandlerOnFileOpertaion;
import com.vw.ide.client.event.handler.SaveFileHandler;
import com.vw.ide.client.event.uiflow.SaveFileEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.remote.browser.DirBrowserServiceBroker;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.shared.servlet.remotebrowser.RequestFileOperationResult;

public class SaveFileEventHandler extends Presenter.PresenterEventHandler implements SaveFileHandler {
	
	private static class HandlerOnSaveFile implements HandlerOnFileOpertaion {

		private ProjectItemInfo itemInfo;
		private DevelopmentBoardPresenter presenter;
		
		public HandlerOnSaveFile(DevelopmentBoardPresenter presenter, ProjectItemInfo itemInfo) {
			this.presenter = presenter;
			this.itemInfo = itemInfo;
		}
		
		@Override
		public void handle(RequestFileOperationResult result) {
			itemInfo.setEdited(false);
			presenter.getView().markFileAsEdited(itemInfo, false);
		}
	}
	
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
		String sFullName = currentWidget.getItemInfo().getAssociatedData().getAbsolutePath();
		DirBrowserServiceBroker.requestForFileSaving(FlowController.getLoggedAsUser(),
													sFullName,
													null,
													null,
													currentWidget.getAceEditor().getText(),
													new AnyFileOperationResultCallback(presenter, new HandlerOnSaveFile(presenter, currentWidget.getItemInfo())));
	}	
}
