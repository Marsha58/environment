package com.vw.ide.client.fringemanagment.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.GetFringesHandler;
import com.vw.ide.client.event.uiflow.GetFringesEvent;
import com.vw.ide.client.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.fringemanagment.service.fringes.callbacks.GettingFringesResultCallback;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;

public class GetFringesEventHandler extends Presenter.PresenterEventHandler implements GetFringesHandler {
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((FringeManagerPresenter)presenter, (GetFringesEvent)event);
	}

	@Override
	public void onGetFringes(GetFringesEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}	

	protected void process(FringeManagerPresenter presenter, GetFringesEvent event) {
		FringeServiceBroker.requestForFringes(new GettingFringesResultCallback(presenter));
	}


}
