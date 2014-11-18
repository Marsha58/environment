package com.vw.ide.client.service.fringes;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.vw.ide.client.service.BusConnectivity;
import com.vw.ide.client.service.ServiceCallback;
import com.vw.ide.client.service.VwIdeClientService;
import com.vw.ide.client.service.factory.ServicesStubFactory;
import com.vw.ide.shared.servlet.fringes.RemoteFringeServiceAsync;
import com.vw.ide.shared.servlet.fringes.RequestAddCategoryResult;
import com.vw.ide.shared.servlet.fringes.RequestAddFringeResult;
import com.vw.ide.shared.servlet.fringes.RequestDeleteCategoryResult;
import com.vw.ide.shared.servlet.fringes.RequestDeleteFringeResult;
import com.vw.ide.shared.servlet.fringes.RequestGetCategoriesResult;
import com.vw.ide.shared.servlet.fringes.RequestGetFringesInCategoriesResult;
import com.vw.ide.shared.servlet.fringes.RequestGetFringesResult;
import com.vw.ide.shared.servlet.fringes.RequestUpdateCategoryResult;
import com.vw.ide.shared.servlet.fringes.RequestUpdateFringeResult;

public class FringeService implements BusConnectivity, VwIdeClientService  {

	private HandlerManager bus;
	
	private RemoteFringeServiceAsync fringeServiceImpl = ServicesStubFactory. createRemoteFringeServiceAsync();
	
	private static FringeService s_instance = null;
	
	
	public static class ServiceCallbackForGetCategories extends ServiceCallback<RequestGetCategoriesResult>  {
	}

	public static class ServiceCallbackForAddCategory extends ServiceCallback<RequestAddCategoryResult>  {
	}	

	public static class ServiceCallbackForUpdateCategory extends ServiceCallback<RequestUpdateCategoryResult>  {
	}
	
	public static class ServiceCallbackForDeleteCategory extends ServiceCallback<RequestDeleteCategoryResult>  {
	}		
	
	public static class ServiceCallbackForGetFringes extends ServiceCallback<RequestGetFringesResult>  {
	}	

	public static class ServiceCallbackForAddFringe extends ServiceCallback<RequestAddFringeResult>  {
	}	

	public static class ServiceCallbackForUpdateFringe extends ServiceCallback<RequestUpdateFringeResult>  {
	}
	
	public static class ServiceCallbackForDeleteFringe extends ServiceCallback<RequestDeleteFringeResult>  {
	}	
	
	public static class ServiceCallbackForGetFringesInCategories extends ServiceCallback<RequestGetFringesInCategoriesResult>  {
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

	public ServiceCallbackForAddCategory buildCallbackForAddCategory() {
		return new ServiceCallbackForAddCategory();
	}
	
	public ServiceCallbackForUpdateCategory buildCallbackForUpdateCategory() {
		return new ServiceCallbackForUpdateCategory();
	}			

	public ServiceCallbackForDeleteCategory buildCallbackForDeleteCategory() {
		return new ServiceCallbackForDeleteCategory();
	}		
	
	public ServiceCallbackForGetFringes buildCallbackForGetFringes() {
		return new ServiceCallbackForGetFringes();
	}		

	public ServiceCallbackForAddFringe buildCallbackForAddFringe() {
		return new ServiceCallbackForAddFringe();
	}
	
	public ServiceCallbackForUpdateFringe buildCallbackForUpdateFringe() {
		return new ServiceCallbackForUpdateFringe();
	}			

	public ServiceCallbackForDeleteFringe buildCallbackForDeleteFringe() {
		return new ServiceCallbackForDeleteFringe();
	}
	
	public ServiceCallbackForGetFringesInCategories buildCallbackForGetFringesInCategories() {
		return new ServiceCallbackForGetFringesInCategories();
	}		
	
	
}
