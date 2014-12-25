package com.vw.ide.shared.servlet.tracer;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Implemented by client side in order to get asynchronous messages from IDE's server side (aka tracer)
 * @author Oleg
 *
 */
@RemoteServiceRelativePath("tracer")
public interface RemoteTracer extends RemoteService {

	/**
	 * Registers tracer's session
	 * @param userName
	 */
	public TracerRegisterResult register(String userName);
	
	/**
	 * Unregisters previosly registered session
	 * @param userName
	 */
	public TracerUnregisterResult unregister(String userName);
}
