package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.FinishProjectExecutionHandler;
import com.vw.ide.client.event.uiflow.FinishProjectExecutionEvent;
import com.vw.ide.client.presenters.Presenter;

public class FinishProjectExecutionEventHandler extends Presenter.PresenterEventHandler implements FinishProjectExecutionHandler{

	@Override
	public void onFinishProjectExecution(FinishProjectExecutionEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (FinishProjectExecutionEvent)event);
	}

	private void process(DevelopmentBoardPresenter presenter, FinishProjectExecutionEvent event) {
		presenter.getView().getTopPanel().enableStarExecution(true);
	}	
}
