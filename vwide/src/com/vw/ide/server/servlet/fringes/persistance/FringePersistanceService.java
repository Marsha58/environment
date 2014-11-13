package com.vw.ide.server.servlet.fringes.persistance;

import java.util.List;

import javax.servlet.ServletContext;

import com.vw.ide.server.servlet.fringes.persistance.dao.CategoriesDAO;
import com.vw.ide.server.servlet.fringes.persistance.dao.FringeDAOException;
import com.vw.ide.server.servlet.fringes.persistance.dao.FringesDAO;
import com.vw.ide.server.servlet.fringes.persistance.dao.FringesDAOFactory;
import com.vw.ide.server.servlet.fringes.persistance.dao.FringesDAOXMLFactoryImpl;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;


public class FringePersistanceService {

	private static ServletContext context;


	private static FringePersistanceService instance = new FringePersistanceService();
	

	private CategoriesDAO categoriesDAO = null;
	private FringesDAO fringesDAO = null;
	
	
	private FringePersistanceService() {
		createCategoryAndFringeLists(new FringesDAOXMLFactoryImpl(context));
	}
	
	public void createCategoryAndFringeLists(FringesDAOFactory factory) {
		categoriesDAO = factory.categoriesDAO();
		fringesDAO = factory.fringesDAO();
	}	

	
	public static FringePersistanceService getInstance() {
		return instance;
	}

	public static void setContext(ServletContext value) {
		context = value;
	}

	public List<Category> getCategories() throws FringeDAOException {
		categoriesDAO.setContext(context);
		return categoriesDAO.getAll();
	}

	public void addCategory(Category category) throws FringeDAOException {
		categoriesDAO.setContext(context);
		categoriesDAO.add(category);
	}	
	
	public void updateCategory(Category category) throws FringeDAOException {
		categoriesDAO.setContext(context);
		categoriesDAO.update(category);
	}	

	public void deleteCategory(Integer categoryId) throws FringeDAOException {
		categoriesDAO.setContext(context);
		categoriesDAO.delete(categoryId);
	}	

	
	public List<Fringe> getFringes() throws FringeDAOException {
		fringesDAO.setContext(context);
		return fringesDAO.getAll();
	}	

	public void addFringe(Fringe fringe) throws FringeDAOException {
		fringesDAO.setContext(context);
		fringesDAO.add(fringe);
	}	
	
	public void updateFringe(Fringe fringe) throws FringeDAOException {
		fringesDAO.setContext(context);
		fringesDAO.update(fringe);
	}	

	public void deleteFringe(Integer fringeId) throws FringeDAOException {
		fringesDAO.setContext(context);
		fringesDAO.delete(fringeId);
	}	
	
	
}
