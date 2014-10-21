package com.vw.ide.client.service.remote.userstate;

import com.vw.ide.client.service.remote.Result;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.userstate.RemoteUserStateService.ServiceCallbackForUserState;
import com.vw.ide.shared.servlet.remotebrowser.RequestUserStateResult;
import com.vw.ide.shared.servlet.userstate.UserStateServiceAsync;

public class RemoteUserStateServiceBroker {

	/**
	 * Requests for user's state
	 * @param user
	 */
	public static void requestForGettingUserState(String user, ResultCallback<RequestUserStateResult> resultCallback) {
		UserStateServiceAsync service = RemoteUserStateService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForUserState cbk = RemoteUserStateService.instance().buildCallbackForUserState();
			cbk.setProcessedResult(new Result<RequestUserStateResult>(resultCallback));
			service.getUserState(user, cbk);
		}
	}
}
