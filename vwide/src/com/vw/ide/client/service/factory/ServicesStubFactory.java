package com.vw.ide.client.service.factory;

import com.google.gwt.core.shared.GWT;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowser;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowserAsync;

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
}
