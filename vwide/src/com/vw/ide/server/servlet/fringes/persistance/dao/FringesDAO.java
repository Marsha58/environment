package com.vw.ide.server.servlet.fringes.persistance.dao;

import java.util.List;

import javax.servlet.ServletContext;

import com.vw.ide.shared.servlet.fringes.model.Fringe;


public interface FringesDAO {
	public void setContext(ServletContext value);
	public void add(Fringe fringe);
	public void update(Fringe fringe);
	public void delete(Integer id);
	public Fringe findById(Integer id);
	public Fringe[] findByName(String name);
	public List<Fringe> getAll();
	
}
