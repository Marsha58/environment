package com.vw.ide.client.devboardext.event.handler;

import java.util.Date;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.ServerLogHandler;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectCreationResult;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestFileOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestedDirScanResult;

public class ServerLogEventHandler extends Presenter.PresenterEventHandler implements ServerLogHandler {
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (ServerLogEvent)event);
	}

	@Override
	public void onServerLog(ServerLogEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	protected void process(DevelopmentBoardPresenter presenter, ServerLogEvent event) {
		Date curDate = new Date();
		presenter.getView().appendLog("");
	}
}
