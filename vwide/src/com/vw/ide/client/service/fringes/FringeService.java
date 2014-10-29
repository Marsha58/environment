package com.vw.ide.client.service.fringes;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.vw.ide.client.service.BusConnectivity;
import com.vw.ide.client.service.ServiceCallback;
import com.vw.ide.client.service.VwIdeClientService;
import com.vw.ide.client.service.factory.ServicesStubFactory;
import com.vw.ide.client.service.security.RemoteSecurityService.ServiceCallbackForLogin;
import com.vw.ide.shared.servlet.fringes.RemoteFringeServiceAsync;
import com.vw.ide.shared.servlet.fringes.RequestGetCategoriesResult;
import com.vw.ide.shared.servlet.fringes.RequestGetFringesResult;
import com.vw.ide.shared.servlet.security.RemoteSecurityAsync;
import com.vw.ide.shared.servlet.security.RequestLoginResult;

public class FringeService implements BusConnectivity, VwIdeClientService  {

	private HandlerManager bus;
	
	private RemoteFringeServiceAsync fringeServiceImpl = ServicesStubFactory. createRemoteFringeServiceAsync();
	
	private static FringeService s_instance = null;
	
	
	
	public static class ServiceCallbackForGetCategories extends ServiceCallback<RequestGetCategoriesResult>  {
	}
	
	public static class ServiceCallbackForGetFringes extends ServiceCallback<RequestGetFringesResult>  {
	}	
	
	private FringeService() {
		
	}	
	
	/**
	 * Simple singleton implementation
	 * @return
	 */
	public static synchronized FringeService instance() {
		if (s_instance != null) {
			return s_instance;
		}
		s_instance = new FringeService();
		return s_instance;
	}
	
	
	@Override
	public void setBusRef(HandlerManager busRef) {
		bus = busRef;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		try {
			bus.fireEvent(event);
		}
		catch(Exception e) {
		}
	}


	public RemoteFringeServiceAsync getServiceImpl() {
		return fringeServiceImpl;
	}

	
	public ServiceCallbackForGetCategories buildCallbackForGetCategories() {
		return new ServiceCallbackForGetCategories();
	}
	
	public ServiceCallbackForGetFringes buildCallbackForGetFringes() {
		return new ServiceCallbackForGetFringes();
	}		
	

}
