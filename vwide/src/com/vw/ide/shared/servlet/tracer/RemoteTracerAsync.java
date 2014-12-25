package com.vw.ide.shared.servlet.tracer;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteTracerAsync {
	/**
	 * Registers tracer's session
	 * @param userName
	 */
	public void register(String userName, AsyncCallback<TracerRegisterResult> callback);
	
	/**
	 * Unregisters previosly registered session
	 * @param userName
	 */
	public void unregister(String userName, AsyncCallback<TracerUnregisterResult> callback);
}
