package com.vw.ide.client.projects;

public enum FilesTypesEnum {
	NOT_DEF,
	VWML,
	JAVA,
	XML,
	C,
	CPP,
	OBJECTIVEC,
	HTML,
	JSON,
	CSS,
	JS;
	
	 @Override public String toString() {
		   String s = super.toString();
		   return s.toLowerCase();
		 }
    
}
