package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.CompilationErrorResultHandler;
import com.vw.ide.client.event.uiflow.CompilationErrorResultEvent;
import com.vw.ide.client.presenters.Presenter;

public class CompilationErrorResultEventHandler extends Presenter.PresenterEventHandler implements CompilationErrorResultHandler  {

	@Override
	public void onCompilationError(CompilationErrorResultEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (CompilationErrorResultEvent)event);
	}

	private void process(DevelopmentBoardPresenter presenter, CompilationErrorResultEvent event) {
		presenter.getView().getConsoles().getCompilationErrorConsoleTab().addCompilationError(event.getCompilationResult());
	}
}
