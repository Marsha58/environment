package com.vw.ide.server.servlet.fringes.persistance.dao;

import javax.servlet.ServletContext;



public class FringesDAOXMLFactoryImpl implements FringesDAOFactory {

	ServletContext context = null;

	public FringesDAOXMLFactoryImpl(ServletContext context) {
		this.context = context;
	}
	
	@Override
	public CategoriesDAO categoriesDAO() {
		return new CategoriesDAOXMLimpl();
	}

	@Override
	public FringesDAO fringesDAO() {
		return new FringesDAOXMLimpl();
	}



}
