package com.vw.ide.client.ui.projectpanel;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;
import com.vw.ide.client.Resources;
import com.vw.ide.client.event.uiflow.ProjectMenuEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.projects.FilesTypesEnum;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.shared.OperationTypes;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

public class ProjectPanelContextMenu extends Menu{
	private MenuItem importFile; 
	private MenuItem newFile; 
	private MenuItem renameFile; 
	private MenuItem delFile;
	private MenuItem newProject;
	private MenuItem importProject;
	private MenuItem editProject;
	private MenuItem delProject;
	private MenuItem newFolder;
	private MenuItem startExecutionProject;
	public Presenter presenter;
	
	public SelectionHandler<Item> selectionHandler = new SelectionHandler<Item>() {
		@Override
		public void onSelection(SelectionEvent<Item> event) {
			if (event.getSelectedItem().getParent() instanceof ProjectPanelContextMenu) {
				ProjectPanelContextMenu menu = (ProjectPanelContextMenu) event.getSelectedItem().getParent();
				if (menu.presenter != null) {
					menu.presenter.fireEvent(new ProjectMenuEvent(event.getSelectedItem().getItemId())); 
				}
			}
		}
	};  
	
	public ProjectPanelContextMenu() {
		
		importFile = new MenuItem();
		importFile.setItemId(OperationTypes.IMPORT_FILE.getName());
		importFile.setText("Import file");
		importFile.setIcon(Resources.IMAGES.new_con_en());
		importFile.addSelectionHandler(selectionHandler);
		this.add(importFile);

		this.add(new SeparatorMenuItem());
		
		newFile = new MenuItem();
		newFile.setItemId(OperationTypes.NEW_FILE.getName());
		newFile.setText("New file");
		newFile.setIcon(Resources.IMAGES.new_con_en());
		newFile.addSelectionHandler(selectionHandler);
		this.add(newFile);

		renameFile = new MenuItem();
		renameFile.setItemId(OperationTypes.RENAME_FILE.getName());
		renameFile.setText("Renaming selected file");
		renameFile.setIcon(Resources.IMAGES.rename_file_en());
		renameFile.addSelectionHandler(selectionHandler);
		this.add(renameFile);
		
		delFile = new MenuItem();
		delFile.setItemId(OperationTypes.DELETE_FILE.getName());
		delFile.setText("Delete selected file/folder");
		delFile.setIcon(Resources.IMAGES.delete_edit_en());
		delFile.addSelectionHandler(selectionHandler);
		this.add(delFile);		

		this.add(new SeparatorMenuItem());

		importProject = new MenuItem();
		importProject.setItemId(OperationTypes.IMPORT_PROJECT.getName());
		importProject.setText("Import project");
		importProject.setIcon(Resources.IMAGES.new_wiz_en());
		importProject.addSelectionHandler(selectionHandler);
		this.add(importProject);

		this.add(new SeparatorMenuItem());

		editProject = new MenuItem();
		editProject.setItemId(OperationTypes.EDIT_PROJECT.getName());
		editProject.setText("Edit project");
		editProject.setIcon(Resources.IMAGES.rename_file_en());
		editProject.addSelectionHandler(selectionHandler);
		editProject.disable();
		this.add(editProject);

		this.add(new SeparatorMenuItem());

		newProject = new MenuItem();
		newProject.setItemId(OperationTypes.NEW_PROJECT.getName());
		newProject.setText("New project");
		newProject.setIcon(Resources.IMAGES.new_wiz_en());
		newProject.addSelectionHandler(selectionHandler);
		this.add(newProject);
		
		delProject = new MenuItem();
		delProject.setItemId(OperationTypes.DELETE_PROJECT.getName());
		delProject.setText("Delete selected project");
		delProject.setIcon(Resources.IMAGES.delete_edit_en());
		delProject.addSelectionHandler(selectionHandler);
		this.add(delProject);

		this.add(new SeparatorMenuItem());

		startExecutionProject = new MenuItem();
		startExecutionProject.setItemId(OperationTypes.START_EXECUTION_PROJECT.getName());
		startExecutionProject.setText("Start execution project");
		startExecutionProject.setIcon(Resources.IMAGES.exec_en());
		startExecutionProject.addSelectionHandler(selectionHandler);
		this.add(startExecutionProject);

		this.add(new SeparatorMenuItem());
		
		newFolder = new MenuItem();
		newFolder.setItemId(OperationTypes.NEW_FOLDER.getName());
		newFolder.setText("New folder");
		newFolder.setIcon(Resources.IMAGES.folder());
		newFolder.addSelectionHandler(selectionHandler);		
		this.add(newFolder);
		
		
	}

	public void checkEnabling(ProjectItemInfo projectItemInfo) {
	  Boolean isNewFileEnabled = false;
	  Boolean isImportFileEnabled = false;
	  Boolean isImportProjectEnabled = false;
	  Boolean isNewProjectEnabled = false;	
	  Boolean isDelFileEnabled = false;	
	  Boolean isDelProjectEnabled = false;
	  Boolean isNewFolderEnabled = false;
	  Boolean isRenameFileEnabled = false;
	  Boolean isEditProjectEnabled = false;
	  Boolean isStartExecutionProject = false;
	  
	  if (projectItemInfo != null) {
		  if (projectItemInfo.isMarkAsUserRoot()) {
			  isNewProjectEnabled = true;
			  isImportProjectEnabled = true;
		  }
		  else
		  if (projectItemInfo.isMarkAsProject()) {
			  isStartExecutionProject = true;
			  isNewFileEnabled = true;	
			  isDelProjectEnabled = true;
			  isImportFileEnabled = true;
			  isNewFolderEnabled = true;
			  isEditProjectEnabled = true;
		  }
		  else {
			  FileItemInfo fileItemInfo = projectItemInfo.getAssociatedData();
			  if (fileItemInfo.isDir()) {
				  isNewFileEnabled = true;
				  isImportFileEnabled = true;
				  isNewFolderEnabled = true;
				  isDelFileEnabled = true;
				  isRenameFileEnabled = true;
			  }
			  else
			  if (!fileItemInfo.isDir()) {
				  isDelFileEnabled = true;
				  isRenameFileEnabled = true;
			  }
			  if (FileItemInfo.recognizeFileType(fileItemInfo.getName()) == FilesTypesEnum.VWML_PROJ) {
				  isDelFileEnabled = false;
			  }
		  }
	  }
	  newFile.setEnabled(isNewFileEnabled);
	  newProject.setEnabled(isNewProjectEnabled);
	  delFile.setEnabled(isDelFileEnabled);
	  delProject.setEnabled(isDelProjectEnabled);
	  importProject.setEnabled(isImportProjectEnabled);
	  newFolder.setEnabled(isNewFolderEnabled);
	  importFile.setEnabled(isImportFileEnabled);
	  renameFile.setEnabled(isRenameFileEnabled);
	  editProject.setEnabled(isEditProjectEnabled);
	  startExecutionProject.setEnabled(isStartExecutionProject);
	}
	
	public void associatePresenter(Presenter presenter) {
		this.presenter = presenter;
	}
}
