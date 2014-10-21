package com.vw.ide.client.service.remote.security;

import com.vw.ide.client.service.remote.Result;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.security.RemoteSecurityService.ServiceCallbackForLogin;
import com.vw.ide.shared.servlet.security.RemoteSecurityAsync;
import com.vw.ide.shared.servlet.security.RequestLoginResult;

public class RemoteSecurityServiceBroker {

	/**
	 * Performs user login remote operation
	 * @param userName
	 * @param password
	 * @param resultCallback
	 */
	public static void requestForLogin(String userName, String password, ResultCallback<RequestLoginResult> resultCallback) {
		String sPasswordMD5 = calculateCheckSum(password.trim());
		RemoteSecurityAsync service = RemoteSecurityService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForLogin cbk = RemoteSecurityService.instance().buildCallbackForLogin();
			cbk.setProcessedResult(new Result<RequestLoginResult>(resultCallback));
			service.login(userName, sPasswordMD5, cbk);
		}
	}

	/**
	 * Performs user logout remote operation
	 * @param userName
	 * @param resultCallback
	 */
	public static void requestForLogout(String userName, ResultCallback<RequestLoginResult> resultCallback) {
		RemoteSecurityAsync service = RemoteSecurityService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForLogin cbk = RemoteSecurityService.instance().buildCallbackForLogin();
			cbk.setProcessedResult(new Result<RequestLoginResult>(resultCallback));
			service.logout(userName, cbk);
		}
	}
	
	private static String calculateCheckSum(String md5) {
		try {
	        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(md5.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	        	sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	        }
	        return sb.toString();
		} 
		catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}	
}
