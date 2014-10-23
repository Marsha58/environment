package com.vw.ide.client.service.factory;

import com.google.gwt.event.shared.HandlerManager;
import com.vw.ide.client.service.BusConnectivity;
import com.vw.ide.client.service.VwIdeClientService;
import com.vw.ide.client.service.remote.browser.DirBrowserService;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerService;
import com.vw.ide.client.service.remote.security.SecurityService;
import com.vw.ide.client.service.remote.userstate.RemoteUserStateService;

/**
 * Creates instances of concrete client side services
 * @author Oleg
 *
 */
public class ServicesBrokerFactory {
	/**
	 * Instantiates all services which are implemented as singleton
	 */
	public static void instantiateAllServices(HandlerManager eventBus) {
		VwIdeClientService services[] = {
											DirBrowserService.instance(),
											SecurityService.instance(),
											RemoteUserStateService.instance(),
											ProjectManagerService.instance()
										};
		for(VwIdeClientService s : services) {
			if (s != null && s instanceof BusConnectivity) {
				((BusConnectivity)s).setBusRef(eventBus);
			}
		}
	}
}
