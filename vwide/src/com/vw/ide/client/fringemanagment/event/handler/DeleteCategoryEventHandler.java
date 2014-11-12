package com.vw.ide.client.fringemanagment.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.fringemanagment.service.fringes.callbacks.DeleteCategoryResultCallback;
import com.vw.ide.client.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.event.handler.fringes.DeleteCategoryHandler;
import com.vw.ide.client.event.uiflow.fringes.DeleteCategoryEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;

public class DeleteCategoryEventHandler extends Presenter.PresenterEventHandler implements DeleteCategoryHandler {

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((FringeManagerPresenter)presenter, (DeleteCategoryEvent)event);
	}

	@Override
	public void onDeleteCategory(DeleteCategoryEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	protected void process(FringeManagerPresenter presenter, DeleteCategoryEvent event) {
		FringeServiceBroker.requestDeleteCategory(event.getCategoryId(), new DeleteCategoryResultCallback(presenter));
	}	

}