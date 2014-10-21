package com.vw.ide.shared.servlet.userstate;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vw.ide.shared.servlet.remotebrowser.RequestUserStateResult;

/**
 * Async version of service
 * @author Oleg
 *
 */
public interface UserStateServiceAsync {

	/**
	 * Adds user state information if not exists
	 * @param user
	 * @param callback
	 */	
	public void addUserState(String user, AsyncCallback<RequestUserStateResult> callback);
	
	/**
	 * Getting information about user state
	 * @param user
	 * @param callback
	 */	
	public void getUserState(String user, AsyncCallback<RequestUserStateResult> callback);

	/**
	 * Updates user state; the user state is added in case if it is absent
	 * @param user
	 * @param state
	 * @param callback
	 * @return
	 */
	public void updateUserState(String user, UserStateInfo state, AsyncCallback<RequestUserStateResult> callback);

	/**
	 * Removes user state
	 * @param user
	 * @param callback
	 * @return
	 */
	public void removeUserState(String user, AsyncCallback<RequestUserStateResult> callback);

}
