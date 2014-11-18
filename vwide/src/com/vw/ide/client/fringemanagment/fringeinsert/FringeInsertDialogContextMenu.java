package com.vw.ide.client.fringemanagment.fringeinsert;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;
import com.vw.ide.client.Resources;
import com.vw.ide.client.event.uiflow.ProjectMenuEvent;
import com.vw.ide.client.event.uiflow.fringes.InsertFringeInFileEvent;
import com.vw.ide.client.event.uiflow.fringes.OpenDialogForSelectFringeEvent;
import com.vw.ide.client.fringemanagment.FringeManagerDialogHandlers;
import com.vw.ide.client.fringemanagment.fringeditors.FringeEditDialog;
import com.vw.ide.client.fringemanagment.fringeinsert.FringeInsertDialog;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.projects.FilesTypesEnum;
import com.vw.ide.shared.CrudTypes;
import com.vw.ide.shared.OperationTypes;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

public class FringeInsertDialogContextMenu extends Menu{

	private MenuItem insertFringe; 
	public Presenter presenter;
	private FringeInsertDialog owner;
	
	public SelectionHandler<Item> selectionHandler = new SelectionHandler<Item>() {
		@Override
		public void onSelection(SelectionEvent<Item> event) {
			if (event.getSelectedItem().getParent() instanceof FringeInsertDialogContextMenu) {
				FringeInsertDialogContextMenu menu = (FringeInsertDialogContextMenu) event.getSelectedItem().getParent();
				if (menu.presenter != null) {
					if (event.getSelectedItem().getItemId() == OperationTypes.INSERT_FRINGE.getName()) {
						presenter.fireEvent(new InsertFringeInFileEvent(owner.getSelectedFringe()));
					}
				}
			}
		}
	};  
	
	
	public FringeInsertDialogContextMenu(FringeInsertDialog owner) {
		this.owner = owner;
		insertFringe = new MenuItem();
		insertFringe.setItemId(OperationTypes.INSERT_FRINGE.getName());
		insertFringe.setText("Insert fringe");
		insertFringe.setIcon(Resources.IMAGES.new_con_en());
		insertFringe.addSelectionHandler(selectionHandler);
		this.add(insertFringe);
	}

	
	public void associatePresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public MenuItem getInsertFringe() {
		return insertFringe;
	}
}
