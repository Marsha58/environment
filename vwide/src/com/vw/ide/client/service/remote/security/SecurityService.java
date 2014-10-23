package com.vw.ide.client.service.remote.security;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.vw.ide.client.service.BusConnectivity;
import com.vw.ide.client.service.ServiceCallback;
import com.vw.ide.client.service.VwIdeClientService;
import com.vw.ide.client.service.factory.ServicesStubFactory;
import com.vw.ide.shared.servlet.security.RemoteSecurityAsync;
import com.vw.ide.shared.servlet.security.RequestLoginResult;

/**
 * Remote browser service for client side (implemented as singleton)
 * @author OMelnyk
 *
 */
public class SecurityService implements BusConnectivity, VwIdeClientService {
	private HandlerManager bus;
	// concrete service's instance
	private RemoteSecurityAsync securityServiceImpl = ServicesStubFactory.createRemoteSecurityAsync();
	
	private static SecurityService s_instance = null;
	
	/**
	 * Callbacks implementation
	 * @author OMelnyk
	 *
	 */
	public static class ServiceCallbackForLogin extends ServiceCallback<RequestLoginResult>  {
	}
	
	private SecurityService() {
		
	}
	
	/**
	 * Simple singleton implementation
	 * @return
	 */
	public static synchronized SecurityService instance() {
		if (s_instance != null) {
			return s_instance;
		}
		s_instance = new SecurityService();
		return s_instance;
	}

	public RemoteSecurityAsync getServiceImpl() {
		return securityServiceImpl;
	}

	public void setBusRef(HandlerManager busRef) {
		bus = busRef;
	}

	public void fireEvent(GwtEvent<?> event) {
		try {
			bus.fireEvent(event);
		}
		catch(Exception e) {
		}
	}
	
	public ServiceCallbackForLogin buildCallbackForLogin() {
		return new ServiceCallbackForLogin();
	}	
}
