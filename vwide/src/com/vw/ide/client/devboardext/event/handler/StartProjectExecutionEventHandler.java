package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.StartProjectExecutionHandler;
import com.vw.ide.client.event.uiflow.StartProjectExecutionEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.processor.CommandProcessorServiceBroker;
import com.vw.ide.shared.servlet.processor.CommandProcessorResult;

public class StartProjectExecutionEventHandler extends Presenter.PresenterEventHandler implements StartProjectExecutionHandler {

	protected static class CompileProjectCallback extends ResultCallback<CommandProcessorResult> {

		@Override
		public void handle(CommandProcessorResult result) {
		}
	}
	
	@Override
	public void onStartProjectExecution(StartProjectExecutionEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (StartProjectExecutionEvent)event);
	}
	
	private void process(DevelopmentBoardPresenter presenter, StartProjectExecutionEvent event) {
		presenter.getView().getTopPanel().enableStarExecution(false);
		CommandProcessorServiceBroker.buildProject(presenter.getLoggedAsUser(),
													 event.getProjectToProcess(),
													 new CompileProjectCallback());
	}
}
