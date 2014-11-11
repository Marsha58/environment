package com.vw.ide.client.dialog.fringemanagment.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.service.fringes.callbacks.UpdateFringeResultCallback;
import com.vw.ide.client.dialog.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.event.handler.fringes.UpdateFringeHandler;
import com.vw.ide.client.event.uiflow.fringes.UpdateFringeEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;

public class UpdateFringeEventHandler extends Presenter.PresenterEventHandler implements UpdateFringeHandler {

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((FringeManagerPresenter)presenter, (UpdateFringeEvent)event);
	}

	@Override
	public void onUpdateFringe(UpdateFringeEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	protected void process(FringeManagerPresenter presenter, UpdateFringeEvent event) {
		FringeServiceBroker.requestUpdateFringe(event.getFringe(), new UpdateFringeResultCallback(presenter));
	}	

}
