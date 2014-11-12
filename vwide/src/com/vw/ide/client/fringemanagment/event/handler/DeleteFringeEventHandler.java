package com.vw.ide.client.fringemanagment.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.fringemanagment.service.fringes.callbacks.DeleteFringeResultCallback;
import com.vw.ide.client.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.event.handler.fringes.DeleteFringeHandler;
import com.vw.ide.client.event.uiflow.fringes.DeleteFringeEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;

public class DeleteFringeEventHandler  extends Presenter.PresenterEventHandler implements DeleteFringeHandler {

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((FringeManagerPresenter)presenter, (DeleteFringeEvent)event);
	}

	@Override
	public void onDeleteFringe(DeleteFringeEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	protected void process(FringeManagerPresenter presenter, DeleteFringeEvent event) {
		FringeServiceBroker.requestDeleteFringe(event.getFringeId(), new DeleteFringeResultCallback(presenter));
	}	

}
