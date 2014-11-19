package com.vw.ide.server.servlet.fringes.persistance.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

public interface ItemDAO<T> {
	public void setContext(ServletContext value);
	public void add(T item);
	public void update(T item);
	public void delete(Integer id);
	public T findById(Integer id);
	public T[] findByName(String name);
	public List<T> getAll();
	public Map<Integer,T> getAllMap();
	
}
