package com.vw.ide.client.dialog.fringemanagment.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.service.fringes.callbacks.AddCategoryResultCallback;
import com.vw.ide.client.devboardext.service.fringes.callbacks.AddFringeResultCallback;
import com.vw.ide.client.dialog.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.event.handler.fringes.AddCategoryHandler;
import com.vw.ide.client.event.handler.fringes.AddFringeHandler;
import com.vw.ide.client.event.uiflow.fringes.AddCategoryEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;

public class AddCategoryEventHandler  extends Presenter.PresenterEventHandler implements AddCategoryHandler {

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((FringeManagerPresenter)presenter, (AddCategoryEvent)event);
	}

	
	protected void process(FringeManagerPresenter presenter, AddCategoryEvent event) {
		FringeServiceBroker.requestAddCategory(event.getCategory(), new AddCategoryResultCallback(presenter));
	}

	@Override
	public void onAddCategory(AddCategoryEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}	

}
