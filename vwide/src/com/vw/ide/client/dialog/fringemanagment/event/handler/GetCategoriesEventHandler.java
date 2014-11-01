package com.vw.ide.client.dialog.fringemanagment.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.dialog.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.dialog.fringemanagment.FringeManagerPresenter.GetCategoriesResult;
import com.vw.ide.client.event.handler.GetCategoriesHandler;
import com.vw.ide.client.event.uiflow.GetCategoriesEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.fringes.FringeService;
import com.vw.ide.client.service.fringes.FringeService.ServiceCallbackForGetCategories;
import com.vw.ide.shared.servlet.fringes.RemoteFringeServiceAsync;

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
		RemoteFringeServiceAsync service = FringeService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForGetCategories cbk = FringeService.instance().buildCallbackForGetCategories();
			cbk.setProcessedResult(new GetCategoriesResult(presenter));
//			cbk.setProcessedResult(new Result<RequestFileOperationResult>(resultCallback));
			service.getCategories(cbk);
		}		
	}	
}
