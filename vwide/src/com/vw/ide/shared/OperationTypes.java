package com.vw.ide.shared;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;

public enum OperationTypes {
   NEW_FOLDER("new_folder"),	
   NEW_PROJECT("new_project"),
   IMPORT_PROJECT("import_project"),
   DELETE_PROJECT("delete_project"),
   NEW_FILE("new_file"),
   IMPORT_FILE("import_file"),
   OPEN_FILE("open_file"),
   SAVE_FILE("save_file"),
   DELETE_FILE("delete_file"),
   RENAME_FILE("rename_file"),
   SAVE_ALL_FILES("save_all_files"),
   CLEAR_CONSOLE_WINDOW("clear_console_window"),
   INSERT_FRINGE("insert_fringe"),
   ADD_CATEGORY("add_category"),
   EDIT_CATEGORY("edit_category"),
   DELETE_CATEGORY("delete_category"),
   ADD_FRINGE("add_fringe"),
   EDIT_FRINGE("edit_fringe"),
   DELETE_FRINGE("delete_fringe"),
   LOAD_FRINGE_JAR("load_fringe_jar");
   
	private final String name;
	
	private OperationTypes(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}   
	
	public static OperationTypes fromString(String text) {
		if (text != null) {
			for (OperationTypes b : OperationTypes.values()) {
				if (text.equalsIgnoreCase(b.name)) {
					return b;
				}
			}
		}
		return null;
	}	
}
