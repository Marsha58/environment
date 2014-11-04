package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.MoveFileHandler;
import com.vw.ide.client.event.uiflow.MoveFileEvent;
import com.vw.ide.client.presenters.Presenter;

public class MoveFileEventHandler extends Presenter.PresenterEventHandler implements MoveFileHandler {

	@Override
	public void onMoveFile(MoveFileEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (MoveFileEvent)event);
	}
	
	protected void process(DevelopmentBoardPresenter presenter, MoveFileEvent event) {
		presenter.getView().getMoveOperationBlock().move(event.getMovedItem(), event.getPlaceForItem());
	}
}
