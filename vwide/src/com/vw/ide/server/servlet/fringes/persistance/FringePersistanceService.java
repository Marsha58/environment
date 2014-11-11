package com.vw.ide.server.servlet.fringes.persistance;

import java.util.ArrayList;
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

	public List<Category> getCategories() {
		try {
			categoriesDAO.setContext(context);
			return categoriesDAO.getAll();
		} catch (FringeDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void addCategory(Category category) {
		try {
			categoriesDAO.setContext(context);
			categoriesDAO.add(category);
		} catch (FringeDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public void updateCategory(Category category) {
		try {
			categoriesDAO.setContext(context);
			categoriesDAO.update(category);
		} catch (FringeDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	public void deleteCategory(Integer categoryId) {
		try {
			categoriesDAO.setContext(context);
			categoriesDAO.delete(categoryId);
		} catch (FringeDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	
	public List<Fringe> getFringes() {
		try {
			fringesDAO.setContext(context);
			return fringesDAO.getAll();
		} catch (FringeDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	

	public void addFringe(Fringe fringe) {
		try {
			fringesDAO.setContext(context);
			fringesDAO.add(fringe);
		} catch (FringeDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public void updateFringe(Fringe fringe) {
		try {
			fringesDAO.setContext(context);
			fringesDAO.update(fringe);
		} catch (FringeDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	public void deleteFringe(Integer fringeId) {
		try {
			fringesDAO.setContext(context);
			fringesDAO.delete(fringeId);
		} catch (FringeDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
}
