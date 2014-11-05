package com.vw.ide.server.servlet.fringes.persistance.dao;

import java.util.List;

import javax.servlet.ServletContext;

import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;


public interface FringesDAO {
	public void setContext(ServletContext value);
	public Fringe create(Integer id, String name, String path, String description, Integer categoryId) throws FringeDAOException;
	public Fringe delete(Integer id) throws FringeDAOException;
	public Fringe update(Integer id, Fringe fringe) throws FringeDAOException;
	public Fringe findById(Integer Id) throws FringeDAOException;
	public Fringe[] findByName(String name) throws FringeDAOException;
	public List<Fringe> getAll() throws FringeDAOException;
	
}
