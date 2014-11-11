package com.vw.ide.server.servlet.fringes.persistance.dao;

import java.util.List;

import javax.servlet.ServletContext;

import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;


public interface FringesDAO {
	public void setContext(ServletContext value);
	public void add(Fringe fringe) throws FringeDAOException;
	public void update(Fringe fringe) throws FringeDAOException;
	public void delete(Integer id) throws FringeDAOException;
	public Fringe findById(Integer id) throws FringeDAOException;
	public Fringe[] findByName(String name) throws FringeDAOException;
	public List<Fringe> getAll() throws FringeDAOException;
	
}
