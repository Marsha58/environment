package com.vw.ide.server.servlet.fringes.persistance.dao;

import java.util.List;

import javax.servlet.ServletContext;

import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;


public interface CategoriesDAO {
	public void setContext(ServletContext value);
	public void add(Category category) throws FringeDAOException;
	public void update(Category category) throws FringeDAOException;
	public void delete(Integer id) throws FringeDAOException;
	public Category findById(Integer id) throws FringeDAOException;
	public Category[] findByName(String name) throws FringeDAOException;
	public List<Category> getAll() throws FringeDAOException;
}
