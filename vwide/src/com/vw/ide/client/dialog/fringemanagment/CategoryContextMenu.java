package com.vw.ide.client.dialog.fringemanagment;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.vw.ide.client.Resources;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.shared.OperationTypes;

public class CategoryContextMenu extends Menu {
	public  FringeManagerPresenter presenter;
	private MenuItem addCategory;
	private MenuItem editCategory;
	private MenuItem deleteCategory;

	public SelectionHandler<Item> selectionHandler = new SelectionHandler<Item>() {
		@Override
		public void onSelection(SelectionEvent<Item> event) {
			if (event.getSelectedItem().getParent() instanceof CategoryContextMenu) {
				CategoryContextMenu menu = (CategoryContextMenu) event.getSelectedItem().getParent();
				if (menu.presenter != null) {
					if (event.getSelectedItem().getItemId().equalsIgnoreCase(OperationTypes.ADD_CATEGORY.getName())) {
						presenter.doAddCategory();
					} else if (event.getSelectedItem().getItemId().equalsIgnoreCase(OperationTypes.EDIT_CATEGORY.getName())) {
						presenter.doEditCategory();
					} else if (event.getSelectedItem().getItemId().equalsIgnoreCase(OperationTypes.DELETE_CATEGORY.getName())) {
						presenter.doDeleteCategory();
					} 
					
				}
			}
		}
	};

	public CategoryContextMenu() {
		addCategory = new MenuItem();
		addCategory.setItemId(OperationTypes.ADD_CATEGORY.getName());
		addCategory.setText("Add");
		addCategory.setIcon(Resources.IMAGES.add());
		addCategory.addSelectionHandler(selectionHandler);
		this.add(addCategory);
		editCategory = new MenuItem();
		editCategory.setItemId(OperationTypes.EDIT_CATEGORY.getName());
		editCategory.setText("Edit");
		editCategory.setIcon(Resources.IMAGES.copy_edit_en());
		editCategory.addSelectionHandler(selectionHandler);
		this.add(editCategory);
		deleteCategory = new MenuItem();
		deleteCategory.setItemId(OperationTypes.DELETE_CATEGORY.getName());
		deleteCategory.setText("Delete");
		deleteCategory.setIcon(Resources.IMAGES.delete_edit_en());
		deleteCategory.addSelectionHandler(selectionHandler);
		this.add(deleteCategory);
	}

	public void associatePresenter(Presenter presenter) {
		this.presenter = (FringeManagerPresenter) presenter;
	}


	
}
