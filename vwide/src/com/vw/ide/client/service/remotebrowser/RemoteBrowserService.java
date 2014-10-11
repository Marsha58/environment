package com.vw.ide.client.service.remotebrowser;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.vw.ide.client.service.BusConnectivity;
import com.vw.ide.client.service.ServiceCallback;
import com.vw.ide.client.service.VwIdeClientService;
import com.vw.ide.client.service.factory.ServicesStubFactory;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowserAsync;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestFileSavingResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestProjectCreationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestedDirScanResult;

/**
 * Remote browser service for client side (implemented as singleton)
 * @author Oleg
 *
 */
public class RemoteBrowserService implements BusConnectivity, VwIdeClientService {
	private HandlerManager bus;
	// concrete service's instance
	private RemoteDirectoryBrowserAsync serviceImpl = ServicesStubFactory.createRemoteDirectoryBrowserAsync();
	
	private static RemoteBrowserService s_instance = null;
	
	/**
	 * Callbacks implementation
	 * @author Oleg
	 *
	 */
	public static class ServiceCallbackForDirList extends ServiceCallback<List<String>>  {

		private RemoteBrowserService service;
		
		public ServiceCallbackForDirList(RemoteBrowserService service) {
			this.service = service;
		}
	}
	
	public static class ServiceCallbackForReadingFile extends ServiceCallback<List<String>>  {

		private RemoteBrowserService service;
		
		public ServiceCallbackForReadingFile(RemoteBrowserService service) {
			this.service = service;
		}
	}	

	public static class ServiceCallbackForCompleteContent extends ServiceCallback<RequestedDirScanResult> {

		private RemoteBrowserService service;
		
		public ServiceCallbackForCompleteContent(RemoteBrowserService service) {
			this.service = service;
		}
	}

	public static class ServiceCallbackForAnyOperation extends ServiceCallback<RequestDirOperationResult> {

		private RemoteBrowserService service;
		
		public ServiceCallbackForAnyOperation(RemoteBrowserService service) {
			this.service = service;
		}
	}
	
	public static class ServiceCallbackForProjectCreation extends ServiceCallback<RequestProjectCreationResult> {

		private RemoteBrowserService service;
		
		public ServiceCallbackForProjectCreation(RemoteBrowserService service) {
			this.service = service;
		}
	}
	
	public static class ServiceCallbackForFileSaving extends ServiceCallback<RequestFileSavingResult> {

		private RemoteBrowserService service;
		
		public ServiceCallbackForFileSaving(RemoteBrowserService service) {
			this.service = service;
		}
	}		
	
	private RemoteBrowserService() {
		
	}
	
	/**
	 * Simple singleton implementation
	 * @return
	 */
	public static synchronized RemoteBrowserService instance() {
		if (s_instance != null) {
			return s_instance;
		}
		s_instance = new RemoteBrowserService();
		return s_instance;
	}

	public RemoteDirectoryBrowserAsync getServiceImpl() {
		return serviceImpl;
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
	
	public ServiceCallbackForCompleteContent buildCallbackForCompleteContent() {
		return new ServiceCallbackForCompleteContent(this);
	}
	
	public ServiceCallbackForDirList buildCallbackForDirListOnly() {
		return new ServiceCallbackForDirList(this);
	}
	
	public ServiceCallbackForAnyOperation buildCallbackForAnyOperation() {
		return new ServiceCallbackForAnyOperation(this);
	}
	
	
	public ServiceCallbackForProjectCreation buildCallbackForProjectCreation() {
		return new ServiceCallbackForProjectCreation(this);
	}		
	
	public ServiceCallbackForFileSaving buildCallbackForSavingFile() {
		return new ServiceCallbackForFileSaving(this);
	}
}
