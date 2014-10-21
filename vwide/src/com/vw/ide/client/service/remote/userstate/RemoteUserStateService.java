package com.vw.ide.client.service.remote.userstate;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.vw.ide.client.service.BusConnectivity;
import com.vw.ide.client.service.ServiceCallback;
import com.vw.ide.client.service.VwIdeClientService;
import com.vw.ide.client.service.factory.ServicesStubFactory;
import com.vw.ide.shared.servlet.remotebrowser.RequestUserStateResult;
import com.vw.ide.shared.servlet.userstate.UserStateServiceAsync;

/**
 * Remote management of states of the user 
 * @author Oleg
 *
 */
public class RemoteUserStateService implements BusConnectivity, VwIdeClientService {

	public static class ServiceCallbackForUserState extends ServiceCallback<RequestUserStateResult> {
	}	
	
	private HandlerManager bus;
	private static RemoteUserStateService s_instance = null;
	// concrete service's instance
	private UserStateServiceAsync serviceImpl = ServicesStubFactory.createRemoteUserStateAsync();

	/**
	 * Simple singleton implementation
	 * @return
	 */
	public static synchronized RemoteUserStateService instance() {
		if (s_instance != null) {
			return s_instance;
		}
		s_instance = new RemoteUserStateService();
		return s_instance;
	}
	
	public UserStateServiceAsync getServiceImpl() {
		return serviceImpl;
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

	public ServiceCallbackForUserState buildCallbackForUserState() {
		return new ServiceCallbackForUserState();
	}
}
