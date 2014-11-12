package com.vw.ide.client.fringemanagment;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.vw.ide.client.fringemanagment.event.handler.AddCategoryEventHandler;
import com.vw.ide.client.fringemanagment.event.handler.AddFringeEventHandler;
import com.vw.ide.client.fringemanagment.event.handler.DeleteCategoryEventHandler;
import com.vw.ide.client.fringemanagment.event.handler.DeleteFringeEventHandler;
import com.vw.ide.client.fringemanagment.event.handler.FringesContextMenuEventHandler;
import com.vw.ide.client.fringemanagment.event.handler.GetCategoriesEventHandler;
import com.vw.ide.client.fringemanagment.event.handler.GetFringesEventHandler;
import com.vw.ide.client.fringemanagment.event.handler.UpdateCategoryEventHandler;
import com.vw.ide.client.fringemanagment.event.handler.UpdateFringeEventHandler;
import com.vw.ide.client.fringemanagment.fringeditors.FringeEditDialog;
import com.vw.ide.client.event.handler.FringesContextMenuHandler;
import com.vw.ide.client.event.handler.GetCategoriesHandler;
import com.vw.ide.client.event.handler.GetFringesHandler;
import com.vw.ide.client.event.handler.fringes.AddCategoryHandler;
import com.vw.ide.client.event.handler.fringes.AddFringeHandler;
import com.vw.ide.client.event.handler.fringes.DeleteCategoryHandler;
import com.vw.ide.client.event.handler.fringes.DeleteFringeHandler;
import com.vw.ide.client.event.handler.fringes.UpdateCategoryHandler;
import com.vw.ide.client.event.handler.fringes.UpdateFringeHandler;
import com.vw.ide.client.event.uiflow.FringesContextMenuEvent;
import com.vw.ide.client.event.uiflow.GetCategoriesEvent;
import com.vw.ide.client.event.uiflow.GetFringesEvent;
import com.vw.ide.client.event.uiflow.fringes.AddCategoryEvent;
import com.vw.ide.client.event.uiflow.fringes.AddFringeEvent;
import com.vw.ide.client.event.uiflow.fringes.DeleteCategoryEvent;
import com.vw.ide.client.event.uiflow.fringes.DeleteFringeEvent;
import com.vw.ide.client.event.uiflow.fringes.UpdateCategoryEvent;
import com.vw.ide.client.event.uiflow.fringes.UpdateFringeEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.shared.CrudTypes;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

/**
 * Login screen
 * 
 * @author Omelnyk
 * 
 */
public class FringeManagerPresenter extends Presenter {



	public void updateCategoryList(Category[] categories) {
		for (int i = 0; i < categories.length; i++) {
			if (getView().listStoreCategories.findModelWithKey(categories[i].getId().toString()) == null) {
				getView().listStoreCategories.add(categories[i]);
			} else {
				getView().listStoreCategories.update(categories[i]);
			}
		}
	}

	public void updateCategoryLastUsedId(Integer lastUsedId) {
		getView().setCategoryId(lastUsedId);
	}

	public void updateFringeList(Fringe[] fringes) {
		getView().setAllFringes(fringes);
	}

	public void updateFringeLastUsedId(Integer lastUsedId) {
		getView().setFringeId(lastUsedId);
	}

	public FringeManager getView() {
		return (FringeManager) view;
	}

	@SuppressWarnings("serial")
	private static Map<Type<?>, Presenter.PresenterEventHandler> dispatcher = new HashMap<Type<?>, Presenter.PresenterEventHandler>() {
		{
			put(GetCategoriesEvent.TYPE, new GetCategoriesEventHandler());
			put(AddCategoryEvent.TYPE, new AddCategoryEventHandler());
			put(UpdateCategoryEvent.TYPE, new UpdateCategoryEventHandler());
			put(DeleteCategoryEvent.TYPE, new DeleteCategoryEventHandler());

			put(FringesContextMenuEvent.TYPE, new FringesContextMenuEventHandler());
			put(GetFringesEvent.TYPE, new GetFringesEventHandler());
			put(AddFringeEvent.TYPE, new AddFringeEventHandler());
			put(UpdateFringeEvent.TYPE, new UpdateFringeEventHandler());
			put(DeleteFringeEvent.TYPE, new DeleteFringeEventHandler());
		}
	};

	private final HandlerManager eventBus;
	private final PresenterViewerLink view;

	public FringeManagerPresenter(HandlerManager eventBus, PresenterViewerLink view) {
		this.eventBus = eventBus;
		this.view = view;
		if (view != null) {
			view.associatePresenter(this);
		}

		
	}

	


	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());

	}

	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}

	@Override
	public void handleEvent(GwtEvent<?> event) {
		Presenter.PresenterEventHandler handler = dispatcher.get(event.getAssociatedType());
		if (handler != null) {
			handler.handler(this, event);
		}
	}

	@Override
	public void registerOnEventBus(HandlerManager eventBus) {
		for (Presenter.PresenterEventHandler handler : dispatcher.values()) {
			handler.setPresenter(this);
		}
		eventBus.addHandler(GetCategoriesEvent.TYPE, (GetCategoriesHandler) dispatcher.get(GetCategoriesEvent.TYPE));
		eventBus.addHandler(AddCategoryEvent.TYPE, (AddCategoryHandler) dispatcher.get(AddCategoryEvent.TYPE));
		eventBus.addHandler(UpdateCategoryEvent.TYPE, (UpdateCategoryHandler) dispatcher.get(UpdateCategoryEvent.TYPE));
		eventBus.addHandler(DeleteCategoryEvent.TYPE, (DeleteCategoryHandler) dispatcher.get(DeleteCategoryEvent.TYPE));

		eventBus.addHandler(FringesContextMenuEvent.TYPE, (FringesContextMenuHandler) dispatcher.get(FringesContextMenuEvent.TYPE));
		eventBus.addHandler(GetFringesEvent.TYPE, (GetFringesHandler) dispatcher.get(GetFringesEvent.TYPE));
		eventBus.addHandler(AddFringeEvent.TYPE, (AddFringeHandler) dispatcher.get(AddFringeEvent.TYPE));
		eventBus.addHandler(UpdateFringeEvent.TYPE, (UpdateFringeHandler) dispatcher.get(UpdateFringeEvent.TYPE));
		eventBus.addHandler(DeleteFringeEvent.TYPE, (DeleteFringeHandler) dispatcher.get(DeleteFringeEvent.TYPE));
  
	}

	@Override
	public void unregisterOnEventBus(HandlerManager eventBus) {
		eventBus.removeHandler(GetCategoriesEvent.TYPE, (GetCategoriesHandler) dispatcher.get(GetCategoriesEvent.TYPE));
		eventBus.removeHandler(AddCategoryEvent.TYPE, (AddCategoryHandler) dispatcher.get(AddCategoryEvent.TYPE));
		eventBus.removeHandler(UpdateCategoryEvent.TYPE, (UpdateCategoryHandler) dispatcher.get(UpdateCategoryEvent.TYPE));
		eventBus.removeHandler(DeleteCategoryEvent.TYPE, (DeleteCategoryHandler) dispatcher.get(DeleteCategoryEvent.TYPE));
		eventBus.removeHandler(FringesContextMenuEvent.TYPE, (FringesContextMenuHandler) dispatcher.get(FringesContextMenuEvent.TYPE));
		eventBus.removeHandler(GetFringesEvent.TYPE, (GetFringesHandler) dispatcher.get(GetFringesEvent.TYPE));
		eventBus.removeHandler(AddFringeEvent.TYPE, (AddFringeHandler) dispatcher.get(AddFringeEvent.TYPE));
		eventBus.removeHandler(UpdateFringeEvent.TYPE, (UpdateFringeHandler) dispatcher.get(UpdateFringeEvent.TYPE));
		eventBus.removeHandler(DeleteFringeEvent.TYPE, (DeleteFringeHandler) dispatcher.get(DeleteFringeEvent.TYPE));
	}

	public void callerGetGategories() {
		Timer t = new Timer() {
			@Override
			public void run() {
				fireEvent(new GetCategoriesEvent());
			}
		};
		t.schedule(0);
	}
	
	public void callerGetFringes() {
		Timer t = new Timer() {
			@Override
			public void run() {
				fireEvent(new GetFringesEvent());
			}
		};
		t.schedule(500);
	}	

	public void registerOnEventBus() {
		registerOnEventBus(eventBus);
	}

	public void unregisterOnEventBus() {
		unregisterOnEventBus(eventBus);
	}


	
	public void doAddCategory() {
		Category newCategory = new Category();
		newCategory.setId(getView().getNewCategoryId());
		newCategory.setName("New category 1");
		newCategory.setIcon("*.png");

		getView().getEditingCategory().cancelEditing();
		getView().getListStoreCategories().add(0, newCategory);

		getView().getEditingCategory().setEditableGrid(getView().getGridCategories());

		int row = getView().getListStoreCategories().indexOf(newCategory);
		getView().getEditingCategory().startEditing(new GridCell(row, 0));
		getView().setSelectedCategory(newCategory);
		getView().setCategoryOperationType(CrudTypes.ADD);
	}

	public void doEditCategory() {
		int row = getView().getListStoreCategories().indexOf(getView().getSelectedCategory());
		if (getView().getSelectedCategory() != null) {
			getView().getEditingCategory().setEditableGrid(getView().getGridCategories());
			getView().getEditingCategory().startEditing(new GridCell(row, 0));
		}
		getView().setCategoryOperationType(CrudTypes.EDIT);
	}

	public void doDeleteCategory() {

		if (getView().getSelectedCategory() != null) {
			String msg = Format.substitute("Are you sure you want to delete the category '{0}'?", getView().getSelectedCategory().getName());
			ConfirmMessageBox box = new ConfirmMessageBox("Confirm", msg);
			box.addDialogHideHandler(new DialogHideHandler() {
				@Override
				public void onDialogHide(DialogHideEvent event) {
					if (event.getHideButton().name().equalsIgnoreCase("YES")) {
						getView().getListStoreCategories().remove(getView().getSelectedCategory());
						fireEvent(new DeleteCategoryEvent(getView().getSelectedCategory().getId()));
					}
					;
				}
			});
			box.show();
		}
	}

	public void doAddFringe() {
		Fringe newFringe = new Fringe();
		newFringe.setId(getView().getNewFringeId());
		newFringe.setName("New fringe");
		newFringe.setPath("");
		newFringe.setFilename("");
		newFringe.setLoaded(false);
		if (getView().getSelectedCategory() != null) {
			newFringe.setCategoryId(getView().getSelectedCategory().getId());
		}

//		getView().getEditingFringe().cancelEditing();
//		getView().getListStoreFringes().add(0, newFringe);

//		getView().getEditingFringe().setEditableGrid(getView().getGridFringes());
//
//		int row = getView().getListStoreFringes().indexOf(newFringe);
//		getView().getEditingCategory().startEditing(new GridCell(row, 0));
//		getView().setSelectedFringe(newFringe);
//		getView().setFringeOperationType(CrudTypes.ADD);
		
		final FringeEditDialog box = new FringeEditDialog(this, newFringe);
//		box.setEditLabelText("Select file");
		box.setHeadingText("Fringe add dialog");
		box.setEditingType(CrudTypes.ADD);
		box.addDialogHideHandler(new FringeManagerDialogHandlers.FringeEditDialogHideHandler(box, this));
		box.show();	
		
	}
	


	public void doEditFringe() {
		int row = getView().getListStoreFringes().indexOf(getView().getSelectedFringe());
		if (getView().getSelectedFringe() != null) {
			getView().getEditingFringe().setEditableGrid(getView().getGridFringes());
			getView().getEditingFringe().startEditing(new GridCell(row, 0));
		}
		getView().setFringeOperationType(CrudTypes.EDIT);
	}

	public void doMoveFringe() {
		int row = getView().getListStoreFringes().indexOf(getView().getSelectedFringe());
		if (getView().getSelectedFringe() != null) {
		}
	}


	public void doDeleteFringe() {
		if (getView().getSelectedFringe() != null) {
			String msg = Format.substitute("Are you sure you want to delete the fringe '{0}'?", getView().getSelectedFringe().getName());
			ConfirmMessageBox box = new ConfirmMessageBox("Confirm", msg);
			box.addDialogHideHandler(new DialogHideHandler() {
				@Override
				public void onDialogHide(DialogHideEvent event) {
					if (event.getHideButton().name().equalsIgnoreCase("YES")) {
						getView().getListStoreFringes().remove(getView().getSelectedFringe());
						getView().updateEditedFringeInFringesList(getView().getSelectedFringe(),CrudTypes.DELETE);
						getView().deleteFringeFromFringeCache(getView().getSelectedFringe());
						fireEvent(new DeleteFringeEvent(getView().getSelectedFringe().getId()));
					}
					;
				}
			});
			box.show();
		}
	}

	

	
	
	public void doSaveChanges() {

	}

}
