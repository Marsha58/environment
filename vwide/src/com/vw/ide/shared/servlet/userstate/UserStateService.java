package com.vw.ide.shared.servlet.userstate;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.vw.ide.shared.servlet.remotebrowser.RequestUserStateResult;

/**
 * User state service; manages user state
 * @author Oleg
 *
 */
@RemoteServiceRelativePath("userstate")
public interface UserStateService extends RemoteService {

	/**
	 * Adds user state information if not exists
	 * @param user
	 */	
	public RequestUserStateResult addUserState(String user);
	
	
	/**
	 * Getting information about user state
	 * @param user
	 */	
	public RequestUserStateResult getUserState(String user);

	/**
	 * Updates user state; the user state is added in case if it is absent
	 * @param user
	 * @param state
	 * @return
	 */
	public RequestUpdateUserStateResult updateUserState(String user, UserStateInfo state);
	
	/**
	 * Removes user state
	 * @param user
	 * @return
	 */
	public RequestUserStateResult removeUserState(String user);
	
}
