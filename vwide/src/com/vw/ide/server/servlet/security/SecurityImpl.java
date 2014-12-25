package com.vw.ide.server.servlet.security;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vw.ide.server.servlet.IService;
import com.vw.ide.server.servlet.locator.ServiceLocator;
import com.vw.ide.server.servlet.tracer.TracerServiceImpl;
import com.vw.ide.server.servlet.userstate.UserStateServiceImpl;
import com.vw.ide.shared.servlet.security.RemoteSecurity;
import com.vw.ide.shared.servlet.security.RequestLoginResult;


/**
 * Implementation of remote directory browser
 * @author OMelnyk
 *
 */
@SuppressWarnings("serial")
public class SecurityImpl extends RemoteServiceServlet implements RemoteSecurity, IService {

	private Logger logger = Logger.getLogger(SecurityImpl.class);
	public static final int ID = 1025;
	
	private UsersManager usersManager;
	
	public SecurityImpl() {
		super();
		if (logger.isInfoEnabled()) {
			logger.info("SecurityImpl constructed");
		}
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = getServletContext();
		usersManager = UsersManager.getInstance();
		usersManager.openAndParseUsersXml(context);
		ServiceLocator.instance().register(this);
		if (logger.isInfoEnabled()) {
			logger.info("SecurityImpl started and initialized");
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
	public RequestLoginResult login(String userName, String password) {
		RequestLoginResult res = new RequestLoginResult();
		res.setUserName(userName);
		res.setRetCode(0);
		try {
			res.setResult(usersManager.checkUserNameAndPassword(userName, password));
			if (res.getResult() == 0) {
				UserStateServiceImpl sis = (UserStateServiceImpl)ServiceLocator.instance().locate(UserStateServiceImpl.ID);
				if (sis != null) {
					sis.addUserStateInfo(userName);
					if (logger.isInfoEnabled()) {
						logger.info("User '" + userName + "' successfully logged in");
					}
					TracerServiceImpl tsi = (TracerServiceImpl)ServiceLocator.instance().locate(TracerServiceImpl.ID);
					if (tsi != null) {
						tsi.registerInternal(getThreadLocalRequest().getSession(), userName);
					}
				}
				else {
					logger.error("User '" + userName + "' couldn't be logged in since state service wasn't found");
					res.setRetCode(-1);
				}
			}
		}
		catch(Exception ex) {
			res.setResult((byte) -4);
			res.setRetCode(-1);
		}
		return res;
	}

	@Override
	public RequestLoginResult logout(String userName) {
		RequestLoginResult res = new RequestLoginResult();
		res.setUserName(userName);
		res.setRetCode(0);
		UserStateServiceImpl sis = (UserStateServiceImpl)ServiceLocator.instance().locate(UserStateServiceImpl.ID);
		if (sis != null) {
			sis.removeUserStateInfo(userName);
			if (logger.isInfoEnabled()) {
				logger.info("User '" + userName + "' successfully logged out");
			}
			TracerServiceImpl tsi = (TracerServiceImpl)ServiceLocator.instance().locate(TracerServiceImpl.ID);
			if (tsi != null) {
				tsi.unregisterInternal(getThreadLocalRequest().getSession(), userName);
			}
		}
		else {
			logger.error("User '" + userName + "' couldn't be logged out since state service wasn't found");
			res.setRetCode(-1);
		}
		return res;
	}
}
