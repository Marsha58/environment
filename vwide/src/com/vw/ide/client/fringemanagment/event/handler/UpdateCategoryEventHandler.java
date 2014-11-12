package com.vw.ide.client.fringemanagment.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.fringemanagment.service.fringes.callbacks.UpdateCategoryResultCallback;
import com.vw.ide.client.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.event.handler.fringes.UpdateCategoryHandler;
import com.vw.ide.client.event.uiflow.fringes.UpdateCategoryEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;

public class UpdateCategoryEventHandler extends Presenter.PresenterEventHandler implements UpdateCategoryHandler {

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((FringeManagerPresenter)presenter, (UpdateCategoryEvent)event);
	}

	@Override
	public void onUpdateCategory(UpdateCategoryEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	protected void process(FringeManagerPresenter presenter, UpdateCategoryEvent event) {
		FringeServiceBroker.requestUpdateCategory(event.getCategory(), new UpdateCategoryResultCallback(presenter));
	}	

}
