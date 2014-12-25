package com.vw.ide.client.service.remote.processor;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.vw.ide.client.service.BusConnectivity;
import com.vw.ide.client.service.ServiceCallback;
import com.vw.ide.client.service.VwIdeClientService;
import com.vw.ide.client.service.factory.ServicesStubFactory;
import com.vw.ide.shared.servlet.processor.CommandProcessorAsync;
import com.vw.ide.shared.servlet.processor.CommandProcessorResult;

/**
 * Client side of command processor service which dispatches and processes commands related to project building phases
 * @author Oleg
 *
 */
public class CommandProcessorService implements BusConnectivity, VwIdeClientService {

	public static class ServiceCallbackForCommandProcessor extends ServiceCallback<CommandProcessorResult> {
		
	}
	
	private CommandProcessorAsync serviceImpl = ServicesStubFactory.createRemoteCommandProcessorServiceAsync();
	private HandlerManager bus;
	private static CommandProcessorService s_instance = null;

	/**
	 * Simple singleton implementation
	 * @return
	 */
	public static synchronized CommandProcessorService instance() {
		if (s_instance != null) {
			return s_instance;
		}
		s_instance = new CommandProcessorService();
		s_instance.init();
		return s_instance;
	}

	public CommandProcessorAsync getServiceImpl() {
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

	public ServiceCallbackForCommandProcessor buildCallbackForCommandProcessor() {
		return new ServiceCallbackForCommandProcessor();
	}
	
	private void init() {
		
	}
}
