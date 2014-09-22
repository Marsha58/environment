package com.vw.ide.server.servlet.security;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vw.ide.shared.servlet.security.RemoteSecurity;
import com.vw.ide.shared.servlet.security.RequestLoginResult;
import com.vw.ide.server.servlet.security.UsersManager;


/**
 * Implementation of remote directory browser
 * @author OMelnyk
 *
 */
@SuppressWarnings("serial")
public class SecurityImpl extends RemoteServiceServlet implements RemoteSecurity{

	private Logger logger = Logger.getLogger(SecurityImpl.class);
	
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
		if (logger.isInfoEnabled()) {
			logger.info("SecurityImpl started and initialized");
		}
	}
	

	
	private boolean checkUserNameAndPassword(String userName, String password){
		Boolean bIsLogged = false;
		return bIsLogged;
	}

	@Override
	public RequestLoginResult login(String userName, String password) {
		RequestLoginResult res = new RequestLoginResult();
		res.setUserName(userName);
		res.setRetCode(0);
		try {
			res.setResult(usersManager.checkUserNameAndPassword(userName, password));
		}
		catch(Exception ex) {
			res.setResult((byte) -4);
			res.setRetCode(-1);
		}
		return res;
	}

	
}
