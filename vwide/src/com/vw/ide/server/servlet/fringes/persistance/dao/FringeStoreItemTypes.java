package com.vw.ide.server.servlet.fringes.persistance.dao;


public enum FringeStoreItemTypes {
	FRINGES("fringes"),
	CATEGORIES("categories");
	
private final String name;
	
	private FringeStoreItemTypes(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}   
	
	public static FringeStoreItemTypes fromString(String text) {
		if (text != null) {
			for (FringeStoreItemTypes b : FringeStoreItemTypes.values()) {
				if (text.equalsIgnoreCase(b.name)) {
					return b;
				}
			}
		}
		return null;
	}	
}
