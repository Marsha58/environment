package com.vw.ide.server.servlet.fringes;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vw.ide.server.servlet.fringes.persistance.FringePersistanceService;
import com.vw.ide.shared.servlet.fringes.RemoteFringeService;
import com.vw.ide.shared.servlet.fringes.RequestAddCategoryResult;
import com.vw.ide.shared.servlet.fringes.RequestAddFringeResult;
import com.vw.ide.shared.servlet.fringes.RequestDeleteCategoryResult;
import com.vw.ide.shared.servlet.fringes.RequestDeleteFringeResult;
import com.vw.ide.shared.servlet.fringes.RequestGetCategoriesResult;
import com.vw.ide.shared.servlet.fringes.RequestGetFringesResult;
import com.vw.ide.shared.servlet.fringes.RequestLoadFringeJarResult;
import com.vw.ide.shared.servlet.fringes.RequestUpdateCategoryResult;
import com.vw.ide.shared.servlet.fringes.RequestUpdateFringeResult;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;


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
	public RequestAddCategoryResult addCategory(Category category) {
		RequestAddCategoryResult res = new RequestAddCategoryResult();
		res.setRetCode(0);
		try {
			fringePersistanceService.addCategory(category); 
		}
		catch(Exception ex) {
			res.setRetCode(-1);
		}
		return res;
	}
	
	@Override
	public RequestUpdateCategoryResult updateCategory(Category category) {
		RequestUpdateCategoryResult res = new RequestUpdateCategoryResult();
		res.setRetCode(0);
		try {
			fringePersistanceService.updateCategory(category); 
		}
		catch(Exception ex) {
			res.setRetCode(-1);
		}
		return res;
	}
	
	@Override
	public RequestDeleteCategoryResult deleteCategory(Integer categoryId) {
		RequestDeleteCategoryResult res = new RequestDeleteCategoryResult();
		res.setRetCode(0);
		try {
			fringePersistanceService.deleteCategory(categoryId); 
		}
		catch(Exception ex) {
			res.setRetCode(-1);
		}
		return res;
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
	public RequestAddFringeResult addFringe(Fringe fringe) {
		RequestAddFringeResult res = new RequestAddFringeResult();
		res.setRetCode(0);
		try {
			fringePersistanceService.addFringe(fringe);; 
		}
		catch(Exception ex) {
			res.setRetCode(-1);
		}
		return res;
	}

	@Override
	public RequestUpdateFringeResult updateFringe(Fringe fringe) {
		RequestUpdateFringeResult res = new RequestUpdateFringeResult();
		res.setRetCode(0);
		try {
			fringePersistanceService.updateFringe(fringe);; 
		}
		catch(Exception ex) {
			res.setRetCode(-1);
		}
		return res;
	}



	@Override
	public RequestDeleteFringeResult deleteFringe(Integer fringeId) {
		RequestDeleteFringeResult res = new RequestDeleteFringeResult();
		res.setRetCode(0);
		res.setId(fringeId);
		try {
			fringePersistanceService.deleteFringe(fringeId); 
		}
		catch(Exception ex) {
			res.setRetCode(-1);
		}
		return res;
	}


}
