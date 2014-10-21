package com.vw.ide.shared.servlet.security;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vw.ide.shared.servlet.security.RequestLoginResult;

public interface RemoteSecurityAsync {

	public void login(String userName, String password, AsyncCallback<RequestLoginResult> callback);

	public void logout(String userName, AsyncCallback<RequestLoginResult> callback);

}
