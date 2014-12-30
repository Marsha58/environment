package com.vw.ide.shared;

public enum OperationTypes {
   NEW_FOLDER("new_folder"),	
   NEW_PROJECT("new_project"),
   EDIT_PROJECT("edit_project"),
   IMPORT_PROJECT("import_project"),
   DELETE_PROJECT("delete_project"),
   NEW_FILE("new_file"),
   IMPORT_FILE("import_file"),
   OPEN_FILE("open_file"),
   SAVE_FILE("save_file"),
   DELETE_FILE("delete_file"),
   RENAME_FILE("rename_file"),
   RENAME_DIR("rename_dir"),
   MOVE_DIR_OR_FILE("move_dir_file"),
   READ_FILE("read_file"),
   SAVE_ALL_FILES("save_all_files"),
   START_EXECUTION_PROJECT("start_execution_project"),
   LOG_OPERATION("log");
   
	private final String name;
	
	private OperationTypes(String name) {
		this.name = name;
	}
	
	/**
	 * @return the theme name (e.g., "eclipse")
	 */
	public String getName() {
		return name;
	}   
}
