package com.vw.ide.client.ui.projectpanel;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;
import com.vw.ide.client.Resources;
import com.vw.ide.client.event.uiflow.ProjectMenuEvent;
import com.vw.ide.client.event.uiflow.SelectFileEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.projects.FilesTypesEnum;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

public class ProPanelContextMenu extends Menu{
	
	private MenuItem newFile;
	private MenuItem delFile;
	private MenuItem newProject; 
	private MenuItem delProject;
	private MenuItem newFolder;
	
	public Presenter presenter;
	private FileItemInfo selectedItemInTheTree;

	
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
		
		newFile = new MenuItem();
		newFile.setItemId("idNewFile");
		newFile.setText("New file");
		newFile.setIcon(Resources.IMAGES.new_con_en());
		newFile.addSelectionHandler(selectionHandler);
		this.add(newFile);

		delFile = new MenuItem();
		delFile.setItemId("idDelFile");
		delFile.setText("Delete selected file");
		delFile.setIcon(Resources.IMAGES.delete_edit_en());
		delFile.addSelectionHandler(selectionHandler);
		this.add(delFile);

		SeparatorMenuItem smiFile = new SeparatorMenuItem();
		this.add(smiFile);

		newProject = new MenuItem();
		newProject.setItemId("idNewProject");
		newProject.setText("New project");
		newProject.setIcon(Resources.IMAGES.new_wiz_en());
		newProject.addSelectionHandler(selectionHandler);
		this.add(newProject);

		delProject = new MenuItem();
		delProject.setItemId("idDelProject");
		delProject.setText("Delete selected project");
		delProject.setIcon(Resources.IMAGES.delete_edit_en());
		delProject.addSelectionHandler(selectionHandler);
		this.add(delProject);

		SeparatorMenuItem smiProject = new SeparatorMenuItem();
		this.add(smiProject);
		
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
		  
		  selectedItemInTheTree = fileItemInfo;
		  
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
