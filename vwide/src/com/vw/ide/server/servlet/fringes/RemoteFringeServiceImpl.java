package com.vw.ide.server.servlet.fringes;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vw.ide.server.servlet.fringes.persistance.dao.FringesDAOFactory;
import com.vw.ide.server.servlet.fringes.persistance.dao.FringesDAOXMLFactoryImpl;
import com.vw.ide.server.servlet.fringes.persistance.dao.ItemDAO;
import com.vw.ide.shared.servlet.fringes.ItemCache;
import com.vw.ide.shared.servlet.fringes.RemoteFringeService;
import com.vw.ide.shared.servlet.fringes.RequestAddCategoryResult;
import com.vw.ide.shared.servlet.fringes.RequestAddFringeResult;
import com.vw.ide.shared.servlet.fringes.RequestDeleteCategoryResult;
import com.vw.ide.shared.servlet.fringes.RequestDeleteFringeResult;
import com.vw.ide.shared.servlet.fringes.RequestGetCategoriesResult;
import com.vw.ide.shared.servlet.fringes.RequestGetFringesInCategoriesResult;
import com.vw.ide.shared.servlet.fringes.RequestGetFringesResult;
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
	
//	private FringePersistanceService fringePersistanceService;
	private FringesDAOFactory factory;
	private ItemDAO<Category> categoriesDAO = null;
	private ItemDAO<Fringe> fringesDAO = null;
	private ItemCache<Fringe> fringesCache = null;
	private ItemCache<Category> categoriesCache = null;
	
	
	public RemoteFringeServiceImpl() {
		super();
		
		if (logger.isInfoEnabled()) {
			logger.info("RemoteFringeServiceImpl constructed");
		}
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		
		factory =  new FringesDAOXMLFactoryImpl(config.getServletContext());
		categoriesDAO = factory.categoriesDAO();
		categoriesDAO.setContext(config.getServletContext());
		fringesDAO = factory.fringesDAO();
		fringesDAO.setContext(config.getServletContext());

		fringesCache = new ItemCache<Fringe>(fringesDAO);
		categoriesCache = new ItemCache<Category>(categoriesDAO);
		
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
			res.setCategories(categoriesCache.getAll()); 			
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
			categoriesDAO.add(category); 
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
			categoriesDAO.update(category);
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
			categoriesDAO.delete(categoryId); 
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
			res.setFringes(fringesCache.getAll()); 
//			res.setFringes(fringesDAO.getAll()); 
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
			fringesDAO.add(fringe);
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
			fringesDAO.update(fringe);
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
			fringesDAO.delete(fringeId); 
		}
		catch(Exception ex) {
			res.setRetCode(-1);
		}
		return res;
	}

	@Override
	public RequestGetFringesInCategoriesResult getFringesInCategories() {
		RequestGetFringesInCategoriesResult res = new RequestGetFringesInCategoriesResult();
		res.setRetCode(0);
		try {
			res.setCategoriesList(categoriesCache.getAll()); 
			res.setFringesList(fringesCache.getAll()); 
		}
		catch(Exception ex) {
			res.setRetCode(-1);
		}
		return res;
	}


}
