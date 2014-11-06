package com.vw.ide.server.servlet.fringes;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vw.ide.server.servlet.fringes.persistance.FringePersistanceService;
import com.vw.ide.shared.servlet.fringes.RemoteFringeService;
import com.vw.ide.shared.servlet.fringes.RequestGetCategoriesResult;
import com.vw.ide.shared.servlet.fringes.RequestGetFringesResult;
import com.vw.ide.shared.servlet.fringes.RequestLoadFringeJarResult;


/**
 * Implementation of remote fringe service
 * @author OMelnyk
 *
 */
@SuppressWarnings("serial")
public class RemoteFringeServiceImpl extends RemoteServiceServlet implements RemoteFringeService{

	private Logger logger = Logger.getLogger(RemoteFringeServiceImpl.class);
	
	private FringePersistanceService fringePersistanceService;
	
	public RemoteFringeServiceImpl() {
		super();
		
		if (logger.isInfoEnabled()) {
			logger.info("RemoteFringeServiceImpl constructed");
		}
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = getServletContext();
		FringePersistanceService.setContext(context);
		fringePersistanceService = FringePersistanceService.getInstance();
		

//		fringePersistanceService.openAndParseUsersXml(context);
//		if (logger.isInfoEnabled()) {
//			logger.info("SecurityImpl started and initialized");
//		}
	}
	
	
	
	
	@Override
	public RequestGetFringesResult getFringes() {
		RequestGetFringesResult res = new RequestGetFringesResult();
		res.setRetCode(0);
		try {
			res.setFringes(fringePersistanceService.getFringes()); 
		}
		catch(Exception ex) {
			res.setRetCode(-1);
		}
		return res;
	}

	@Override
	public RequestGetCategoriesResult getCategories() {
		RequestGetCategoriesResult res = new RequestGetCategoriesResult();
		res.setRetCode(0);
		try {
			res.setCategories(fringePersistanceService.getCategories()); 
		}
		catch(Exception ex) {
			res.setRetCode(-1);
		}
		return res;
	}

	@Override
	public RequestLoadFringeJarResult loadFringeJar(String user, String parent, String fileName, String content) {
		// TODO Auto-generated method stub
		return null;
	}
}
