package com.vw.ide.shared.servlet.security;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Implemented by Remote directory browser servlet
 * @author OMelnyk
 *
 */
@RemoteServiceRelativePath("security")
public interface RemoteSecurity extends RemoteService {

	/**
	 * Login user
	 * @param user
	 * @param password
	 * @return
	 */
	public RequestLoginResult login(String userName, String password);
	
	/**
	 * Logout user
	 * @param user
	 * @return
	 */
	public RequestLoginResult logout(String userName);
	
}
