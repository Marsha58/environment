package com.vw.ide.server.servlet.fringes.persistance.dao;

import java.util.List;

import javax.servlet.ServletContext;

import com.vw.ide.shared.servlet.fringes.model.Category;


public interface CategoriesDAO {
	public void setContext(ServletContext value);
	public void add(Category category);
	public void update(Category category);
	public void delete(Integer id);
	public Category findById(Integer id);
	public Category[] findByName(String name);
	public List<Category> getAll();
}
