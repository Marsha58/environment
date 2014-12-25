package com.vw.ide.client.service.factory;

import com.google.gwt.core.shared.GWT;
import com.vw.ide.shared.servlet.processor.CommandProcessor;
import com.vw.ide.shared.servlet.processor.CommandProcessorAsync;
import com.vw.ide.shared.servlet.projectmanager.RemoteProjectManagerService;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowser;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowserAsync;
import com.vw.ide.shared.servlet.security.RemoteSecurity;
import com.vw.ide.shared.servlet.security.RemoteSecurityAsync;
import com.vw.ide.shared.servlet.tracer.RemoteTracer;
import com.vw.ide.shared.servlet.tracer.RemoteTracerAsync;
import com.vw.ide.shared.servlet.userstate.UserStateService;
import com.vw.ide.shared.servlet.userstate.UserStateServiceAsync;
import com.vw.ide.shared.servlet.projectmanager.RemoteProjectManagerServiceAsync;
/**
 * Factory of internal services (stubs)
 * @author Oleg
 *
 */
public class ServicesStubFactory {
	/**
	 * Instantiates directory remote browser service
	 * @return
	 */
	public static RemoteDirectoryBrowserAsync createRemoteDirectoryBrowserAsync() {
		return GWT.create(RemoteDirectoryBrowser.class);
	}	

	/**
	 * Instantiates y remote security service
	 * @return
	 */
	public static RemoteSecurityAsync createRemoteSecurityAsync() {
		return GWT.create(RemoteSecurity.class);
	}	
	
	/**
	 * Instantiates remote user state service
	 * @return
	 */
	public static UserStateServiceAsync createRemoteUserStateAsync() {
		return GWT.create(UserStateService.class);
	}	
	
	/**
	 * Instantiates remote project manager service stub
	 * @return
	 */
	public static RemoteProjectManagerServiceAsync createRemoteProjectManagerServiceAsync() {
		return GWT.create(RemoteProjectManagerService.class);
	}
	
	/**
	 * Instantiates remote tracer service stub
	 * @return
	 */
	public static RemoteTracerAsync createRemoteTracerServiceAsync() {
		return GWT.create(RemoteTracer.class);
	}
	
	/**
	 * Instantiates remote command processor service stub
	 * @return
	 */
	public static CommandProcessorAsync createRemoteCommandProcessorServiceAsync() {
		return GWT.create(CommandProcessor.class);
	}
}
