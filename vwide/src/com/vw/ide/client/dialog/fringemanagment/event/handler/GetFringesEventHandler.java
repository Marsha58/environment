package com.vw.ide.client.dialog.fringemanagment.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.dialog.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.dialog.fringemanagment.FringeManagerPresenter.GetFringesResult;
import com.vw.ide.client.event.handler.GetFringesHandler;
import com.vw.ide.client.event.uiflow.GetFringesEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.fringes.FringeService;
import com.vw.ide.client.service.fringes.FringeService.ServiceCallbackForGetFringes;
import com.vw.ide.shared.servlet.fringes.RemoteFringeServiceAsync;

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
		RemoteFringeServiceAsync service = FringeService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForGetFringes cbk = FringeService.instance().buildCallbackForGetFringes();
			cbk.setProcessedResult(new GetFringesResult(presenter));
			service.getFringes(cbk);
		}		
	}


}
