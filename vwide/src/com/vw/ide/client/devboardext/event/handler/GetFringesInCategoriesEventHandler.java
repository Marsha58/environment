package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.service.fringes.callbacks.GettingFringesInCategoriesResultCallback;
import com.vw.ide.client.event.handler.GetFringesInCategoriesHandler;
import com.vw.ide.client.event.uiflow.GetFringesInCategoriesEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.fringes.FringeServiceBroker;
import com.vw.ide.shared.servlet.fringes.RequestGetFringesInCategoriesResult;

public class GetFringesInCategoriesEventHandler extends Presenter.PresenterEventHandler implements GetFringesInCategoriesHandler {
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (GetFringesInCategoriesEvent)event);
	}

	@Override
	public void onGetFringesInCategories(GetFringesInCategoriesEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}

	protected void process(DevelopmentBoardPresenter presenter, GetFringesInCategoriesEvent event) {
		FringeServiceBroker.requestForFringesInCategories(new GettingFringesInCategoriesResultCallback(presenter));
	}



}
