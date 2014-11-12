package com.vw.ide.client.fringemanagment.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.fringemanagment.service.fringes.callbacks.GettingFringeCategoriesResultCallback;
import com.vw.ide.client.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.event.handler.GetCategoriesHandler;
import com.vw.ide.client.event.uiflow.GetCategoriesEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;

public class GetCategoriesEventHandler extends Presenter.PresenterEventHandler implements GetCategoriesHandler {
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((FringeManagerPresenter)presenter, (GetCategoriesEvent)event);
	}


	@Override
	public void onGetCategories(GetCategoriesEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}	
	
	protected void process(FringeManagerPresenter presenter, GetCategoriesEvent event) {
		FringeServiceBroker.requestForCategories(new GettingFringeCategoriesResultCallback(presenter));
	
	}	
}
