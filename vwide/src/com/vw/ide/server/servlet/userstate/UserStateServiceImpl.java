package com.vw.ide.server.servlet.userstate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vw.ide.server.servlet.IService;
import com.vw.ide.server.servlet.locator.ServiceLocator;
import com.vw.ide.shared.servlet.remotebrowser.RequestUserStateResult;
import com.vw.ide.shared.servlet.userstate.RequestUpdateUserStateResult;
import com.vw.ide.shared.servlet.userstate.UserStateInfo;
import com.vw.ide.shared.servlet.userstate.UserStateService;

@SuppressWarnings("serial")
public class UserStateServiceImpl extends RemoteServiceServlet implements UserStateService, IService {

	public static final int ID = 1026;
	
	private Map<String, UserStateInfo> usersStates = new ConcurrentHashMap<String, UserStateInfo>();
	private Logger logger = Logger.getLogger(UserStateServiceImpl.class);

	public UserStateServiceImpl() {
		super();
		if (logger.isInfoEnabled()) {
			logger.info("User state service constructed");
		}
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServiceLocator.instance().register(this);
		if (logger.isInfoEnabled()) {
			logger.info("User state service started and initialized");
		}
	}
	
	@Override
	public int getId() {
		return ID;
	}

	@Override
	public String getName() {
		return getClass().getName();
	}

	@Override
	public RequestUserStateResult addUserState(String user) {
		RequestUserStateResult res = new RequestUserStateResult();
		addUserStateInfo(user);
		res.setRetCode(RequestUserStateResult.GENERAL_OK);
		return res;
	}
	
	@Override
	public RequestUserStateResult getUserState(String user) {
		RequestUserStateResult res = new RequestUserStateResult();
		UserStateInfo state = getUserStateInfo(user);
		if (state != null) {
			res.setRetCode(RequestUserStateResult.GENERAL_OK);
			res.setUserStateInfo(state);
		}
		else {
			res.setRetCode(RequestUserStateResult.GENERAL_FAIL);
			res.setResult("Coudn't find user state for user '" + user + "'");
		}
		return res;
	}

	@Override
	public RequestUpdateUserStateResult updateUserState(String user, UserStateInfo state) {
		RequestUpdateUserStateResult res = new RequestUpdateUserStateResult();
		updateUserStateInfo(user, state);
		res.setRetCode(RequestUserStateResult.GENERAL_OK);
		return res;
	}

	@Override
	public RequestUserStateResult removeUserState(String user) {
		RequestUserStateResult res = new RequestUserStateResult();
		removeUserStateInfo(user);
		res.setRetCode(RequestUserStateResult.GENERAL_OK);
		return res;
	} 

	/**
	 * Used by remote browser and other internal services
	 * @param user
	 * @return
	 */
	public void addUserStateInfo(String user) {
		UserStateInfo stateInfo = usersStates.get(user);
		if (stateInfo == null) {
			stateInfo = new UserStateInfo();
			usersStates.put(user, stateInfo);
			if (logger.isDebugEnabled()) {
				logger.debug("Added new state info for user '" + user + "'");
			}
		}
	}
	
	/**
	 * Used by remote browser and other internal services
	 * @param user
	 * @return
	 */
	public UserStateInfo getUserStateInfo(String user) {
		UserStateInfo stateInfo = usersStates.get(user);
		if (stateInfo != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Read state info '" + stateInfo + "' for user '" + user + "'");
			}
		}
		else {
			logger.error("State info for user '" + user + "' wasn't found");
		}
		return stateInfo;
	}
	
	/**
	 * Used by remote browser and other internal services
	 * @param user
	 * @param stateInfo
	 * @return
	 */
	public void updateUserStateInfo(String user, UserStateInfo stateInfo) {
		usersStates.put(user, stateInfo);
		if (logger.isDebugEnabled()) {
			logger.debug("State info '" + stateInfo + "' for user '" + user + "' was updated");
		}
	}

	/**
	 * Used by remote browser and other internal services
	 * @param user
	 * @return
	 */
	public void removeUserStateInfo(String user) {
		UserStateInfo stateInfo = usersStates.remove(user);
		if (stateInfo != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("State info '" + stateInfo + "' for user '" + user + "' was removed");
			}
		}
		else {
			logger.error("State info for user '" + user + "' wasn't found");
		}
	}
}
