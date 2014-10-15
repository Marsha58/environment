package com.vw.ide.client.projects;

public enum MenuItemsEnum {
   NEW_FOLDER("new_folder"),	
   NEW_PROJECT("new_project"),
   IMPORT_PROJECT("import_project"),
   DELETE_PROJECT("delete_project"),
   NEW_FILE("new_file"),
   IMPORT_FILE("import_file"),
   OPEN_FILE("open_file"),
   SAVE_FILE("save_file"),
   DELETE_FILE("delete_file"),
   SAVE_ALL_FILES("save_all_files");
   
	private final String name;
	
	private MenuItemsEnum(String name) {
		this.name = name;
	}
	
	/**
	 * @return the theme name (e.g., "eclipse")
	 */
	public String getName() {
		return name;
	}   
}
