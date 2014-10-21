package com.vw.ide.server.servlet;

/**
 * Root interface for all IDE's remote services
 * @author Oleg
 *
 */
public interface IService {
	/**
	 * Returns service's id
	 * @return
	 */
	public int getId();
	
	/**
	 * Returns service's name
	 * @return
	 */
	public String getName();
}
