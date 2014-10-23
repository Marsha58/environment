package com.vw.ide.shared.servlet.projectmanager;

import java.io.Serializable;

/**
 * Interperter's description
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class InterpreterDescription implements Serializable {
	private String name;
	
	public InterpreterDescription() {
		
	}
	
	public InterpreterDescription(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
