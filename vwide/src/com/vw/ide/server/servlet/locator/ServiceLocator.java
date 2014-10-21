package com.vw.ide.server.servlet.locator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.vw.ide.server.servlet.IService;

public class ServiceLocator {

	private Map<Integer, IService> services = new ConcurrentHashMap<Integer, IService>();
	private static final ServiceLocator s_instance = new ServiceLocator();
	private Logger logger = Logger.getLogger(ServiceLocator.class);

	private ServiceLocator() {
		
	}
	
	public static synchronized ServiceLocator instance() {
		return s_instance;
	}
	
	/**
	 * Registers given service
	 * @param service
	 */
	public void register(IService service) {
		if (service != null) {
			if (!services.containsKey(service.getId())) {
				services.put(service.getId(), service);
				if (logger.isInfoEnabled()) {
					logger.info("Locator reports: service '" + service.getName() + "' registered");
				}
			}
		}
	}
	
	/**
	 * Unregister service
	 * @param service
	 */
	public void unregister(IService service) {
		if (service != null) {
			services.remove(service);
			if (logger.isInfoEnabled()) {
				logger.info("Locator reports: service '" + service.getName() + "' unregistered");
			}
		}
	}
	
	/**
	 * Locate service
	 * @param id
	 * @return
	 */
	public IService locate(int id) {
		return services.get(id);
	}
}
