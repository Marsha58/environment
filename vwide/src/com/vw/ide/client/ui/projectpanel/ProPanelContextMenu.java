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
import com.vw.ide.shared.OperationTypes;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

public class ProPanelContextMenu extends Menu{
	private MenuItem importFile; 
	private MenuItem newFile; 
	private MenuItem renameFile; 
	private MenuItem delFile;
	private MenuItem newProject;
	private MenuItem importProject;
	private MenuItem delProject;
	private MenuItem newFolder;
	public Presenter presenter;
	
	public SelectionHandler<Item> selectionHandler = new SelectionHandler<Item>() {
		@Override
		public void onSelection(SelectionEvent<Item> event) {
			if (event.getSelectedItem().getParent() instanceof ProPanelContextMenu) {
				ProPanelContextMenu menu = (ProPanelContextMenu) event.getSelectedItem().getParent();
				if (menu.presenter != null) {
					menu.presenter.fireEvent(new ProjectMenuEvent(event.getSelectedItem().getItemId())); 
				}
				
			}
		}
	};  
	
	
	public ProPanelContextMenu() {
		
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
		renameFile.setText("Rename file");
		renameFile.setIcon(Resources.IMAGES.rename_file_en());
		renameFile.addSelectionHandler(selectionHandler);
		this.add(renameFile);
		
		delFile = new MenuItem();
		delFile.setItemId(OperationTypes.DELETE_FILE.getName());
		delFile.setText("Delete file");
		delFile.setIcon(Resources.IMAGES.delete_edit_en());
		delFile.addSelectionHandler(selectionHandler);
		this.add(delFile);		

		this.add(new SeparatorMenuItem());

		importProject = new MenuItem();
		importProject.setItemId(OperationTypes.IMPORT_PROJECT.getName());
		importProject.setText("Import project");
		importProject.setIcon(Resources.IMAGES.new_wiz_en());
		importProject.addSelectionHandler(selectionHandler);
		importProject.disable();
		this.add(importProject);

		this.add(new SeparatorMenuItem());

		newProject = new MenuItem();
		newProject.setItemId(OperationTypes.NEW_PROJECT.getName());
		newProject.setText("New project");
		newProject.setIcon(Resources.IMAGES.new_wiz_en());
		newProject.addSelectionHandler(selectionHandler);
		this.add(newProject);
		
		delProject = new MenuItem();
		delProject.setItemId(OperationTypes.DELETE_PROJECT.getName());
		delProject.setText("Delete project");
		delProject.setIcon(Resources.IMAGES.delete_edit_en());
		delProject.addSelectionHandler(selectionHandler);
		this.add(delProject);

		this.add(new SeparatorMenuItem());
		
		newFolder = new MenuItem();
		newFolder.setItemId("idNewFolder");
		newFolder.setText("New folder");
		newFolder.setIcon(Resources.IMAGES.folder());
		newFolder.addSelectionHandler(selectionHandler);		
		this.add(newFolder);
		
		
	}

// doesn't implemented yet	
	public boolean checkIsFileProXml(FileItemInfo fileItemInfo) {
		  return true;
	}		
	
	
	public void checkEnabling(FileItemInfo fileItemInfo) {
	  Boolean isNewFileEnabled = true;	
	  Boolean isNewProjectEnabled = true;	
	  Boolean isDelFileEnabled = true;	
	  Boolean isDelProjectEnabled = true;	
		
	  if(fileItemInfo != null) {
		  if(fileItemInfo.getFileType() == FilesTypesEnum.XML) {
			  isDelFileEnabled = !checkIsFileProXml(fileItemInfo);
		  } else {
			  if (fileItemInfo.isDir()) {
				  isDelFileEnabled = false;
			  } else {
				  isDelProjectEnabled = false;
			  }
		  }
	  } else {
		  isNewFileEnabled = false;	
		  isNewProjectEnabled = true;	
		  isDelFileEnabled = false;	
		  isDelProjectEnabled = false;	
	  }
	  
	  
	  newFile.setEnabled(isNewFileEnabled);
	  newProject.setEnabled(isNewProjectEnabled);

	  delFile.setEnabled(isDelFileEnabled);
	  delProject.setEnabled(isDelProjectEnabled);
	  
	}
	
	public void associatePresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
}
