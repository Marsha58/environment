package com.vw.ide.client.dialog.fringemanagment.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.service.fringes.callbacks.AddFringeResultCallback;
import com.vw.ide.client.dialog.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.event.handler.fringes.AddFringeHandler;
import com.vw.ide.client.event.uiflow.fringes.AddFringeEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;

public class AddFringeEventHandler extends Presenter.PresenterEventHandler implements AddFringeHandler {

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((FringeManagerPresenter)presenter, (AddFringeEvent)event);
	}

	
	protected void process(FringeManagerPresenter presenter, AddFringeEvent event) {
		FringeServiceBroker.requestAddFringe(event.getFringe(), new AddFringeResultCallback(presenter));
	}

	@Override
	public void onAddFringe(AddFringeEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}	

}

