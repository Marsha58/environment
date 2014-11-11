package com.vw.ide.shared;

public enum CrudTypes {
	ADD("add"),
	EDIT("edit"),
	DELETE("delete");

	private final String name;
	
	private CrudTypes(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}   
	
	public static CrudTypes fromString(String text) {
		if (text != null) {
			for (CrudTypes b : CrudTypes.values()) {
				if (text.equalsIgnoreCase(b.name)) {
					return b;
				}
			}
		}
		return null;
	}		

}
