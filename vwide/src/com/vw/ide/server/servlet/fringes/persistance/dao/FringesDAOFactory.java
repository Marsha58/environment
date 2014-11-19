package com.vw.ide.server.servlet.fringes.persistance.dao;

import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;


public interface FringesDAOFactory {
	ItemDAO<Category> categoriesDAO();
	ItemDAO<Fringe> fringesDAO();
}
