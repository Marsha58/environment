package com.vw.ide.server.servlet.fringes.persistance.dao;

import java.util.List;

import javax.servlet.ServletContext;

import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;


public interface CategoriesDAO {
	public void setContext(ServletContext value);
	public Category create(Integer id, String name, String icon, String description) throws FringeDAOException;
	public Category delete(Integer id) throws FringeDAOException;
	public Category update(Integer id, Category Category) throws FringeDAOException;
	public Category findById(Integer Id) throws FringeDAOException;
	public Category[] findByName(String name) throws FringeDAOException;
	public List<Category> getAll() throws FringeDAOException;
}
