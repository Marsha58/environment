package com.vw.ide.server.servlet.fringes.persistance.dao;

import javax.servlet.ServletContext;

import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;


public class FringesDAOXMLFactoryImpl implements FringesDAOFactory {

	ServletContext context = null;

	public FringesDAOXMLFactoryImpl(ServletContext context) {
		this.context = context;
	}
	
	@Override
	public ItemDAO<Category> categoriesDAO() {
		return new CategoriesDAOXMLimpl();
	}

	@Override
	public ItemDAO<Fringe> fringesDAO() {
		return  new FringesDAOXMLimpl();
	}



}
