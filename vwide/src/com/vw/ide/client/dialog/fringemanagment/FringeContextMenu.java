package com.vw.ide.client.dialog.fringemanagment;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.vw.ide.client.Resources;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.shared.OperationTypes;

public class FringeContextMenu extends Menu {
	public  FringeManagerPresenter presenter;
	private MenuItem addFringe;
	private MenuItem editFringe;
	private MenuItem moveFringe;
	private MenuItem deleteFringe;

	public SelectionHandler<Item> selectionHandler = new SelectionHandler<Item>() {
		@Override
		public void onSelection(SelectionEvent<Item> event) {
			if (event.getSelectedItem().getParent() instanceof FringeContextMenu) {
				FringeContextMenu menu = (FringeContextMenu) event.getSelectedItem().getParent();
				if (menu.presenter != null) {
					if (event.getSelectedItem().getItemId().equalsIgnoreCase(OperationTypes.ADD_FRINGE.getName())) {
						presenter.doAddFringe();
					} else if (event.getSelectedItem().getItemId().equalsIgnoreCase(OperationTypes.EDIT_FRINGE.getName())) {
						presenter.doEditFringe();
					} else if (event.getSelectedItem().getItemId().equalsIgnoreCase(OperationTypes.DELETE_FRINGE.getName())) {
						presenter.doDeleteFringe();
					} 
					
				}
			}
		}
	};

	public FringeContextMenu() {
		addFringe = new MenuItem();
		addFringe.setItemId(OperationTypes.ADD_FRINGE.getName());
		addFringe.setText("Add");
		addFringe.setIcon(Resources.IMAGES.add());
		addFringe.addSelectionHandler(selectionHandler);
		this.add(addFringe);
		editFringe = new MenuItem();
		editFringe.setItemId(OperationTypes.EDIT_FRINGE.getName());
		editFringe.setText("Edit");
		editFringe.setIcon(Resources.IMAGES.copy_edit_en());
		editFringe.addSelectionHandler(selectionHandler);
		this.add(editFringe);
		deleteFringe = new MenuItem();
		deleteFringe.setItemId(OperationTypes.DELETE_FRINGE.getName());
		deleteFringe.setText("Delete");
		deleteFringe.setIcon(Resources.IMAGES.delete_edit_en());
		deleteFringe.addSelectionHandler(selectionHandler);
		this.add(deleteFringe);
	}

	public void associatePresenter(Presenter presenter) {
		this.presenter = (FringeManagerPresenter) presenter;
	}


	
}

