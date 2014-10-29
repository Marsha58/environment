package com.vw.ide.client.service.factory;

import com.google.gwt.core.shared.GWT;
import com.vw.ide.shared.servlet.fringes.RemoteFringeService;
import com.vw.ide.shared.servlet.fringes.RemoteFringeServiceAsync;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowser;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowserAsync;
import com.vw.ide.shared.servlet.security.RemoteSecurity;
import com.vw.ide.shared.servlet.security.RemoteSecurityAsync;

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
	 * Instantiates remote security service
	 * @return
	 */
	public static RemoteSecurityAsync createRemoteSecurityAsync() {
		return GWT.create(RemoteSecurity.class);
	}	

	/**
	 * Instantiates fringe service
	 * @return
	 */
	public static RemoteFringeServiceAsync createRemoteFringeServiceAsync() {
		return GWT.create(RemoteFringeService.class);
	}		
	
}
