package com.vw.ide.client.dialog.fringemanagment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
import com.vw.ide.client.dialog.fringeload.FringeLoadDialog;
import com.vw.ide.client.dialog.fringemanagment.event.handler.GetCategoriesEventHandler;
import com.vw.ide.client.dialog.fringemanagment.event.handler.GetFringesEventHandler;
import com.vw.ide.client.event.handler.GetCategoriesHandler;
import com.vw.ide.client.event.handler.GetFringesHandler;
import com.vw.ide.client.event.uiflow.GetCategoriesEvent;
import com.vw.ide.client.event.uiflow.GetFringesEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

/**
 * Login screen
 * 
 * @author Omelnyk
 * 
 */
public class FringeManagerPresenter extends Presenter {

	private CacheLoader<Integer, List<Fringe>> fringeLoader;
	private LoadingCache<Integer, List<Fringe>> fringesCache;

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
			put(GetFringesEvent.TYPE, new GetFringesEventHandler());
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

		initCache();
	}

	public void initCache() {
		fringesCache = (LoadingCache<Integer, List<Fringe>>) CacheBuilder.newBuilder().build(new CacheLoader<Integer, List<Fringe>>() {
			@Override
			public List<Fringe> load(Integer categoryId) throws Exception {
				return  getFringesListFromAllFringesArray(categoryId);
			}
		});
	}

	public List<Fringe> getFringesList(Integer categoryId) {
		return fringesCache.getUnchecked(categoryId);
	}

	public List<Fringe> getFringesListFromAllFringesArray(Integer categoryId) {
		List<Fringe> fringesList = new ArrayList<>();
		for (Fringe fringe : getView().getAllFringes()) {
			if (categoryId == fringe.getCategoryId()) {
				fringesList.add(fringe);
			}
		}
		return fringesList;
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
		eventBus.addHandler(GetFringesEvent.TYPE, (GetFringesHandler) dispatcher.get(GetFringesEvent.TYPE));

	}

	@Override
	public void unregisterOnEventBus(HandlerManager eventBus) {
		eventBus.removeHandler(GetCategoriesEvent.TYPE, (GetCategoriesHandler) dispatcher.get(GetCategoriesEvent.TYPE));
		eventBus.removeHandler(GetFringesEvent.TYPE, (GetFringesHandler) dispatcher.get(GetFringesEvent.TYPE));
	}

	public void callerGetGategoriesAndFringes() {
		Timer t = new Timer() {
			@Override
			public void run() {
				fireEvent(new GetCategoriesEvent());
				fireEvent(new GetFringesEvent());
			}
		};
		t.schedule(0);
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
	}

	public void doEditCategory() {
		int row = getView().getListStoreCategories().indexOf(getView().getSelectedCategory());
		if (getView().getSelectedCategory() != null) {
			getView().getEditingCategory().setEditableGrid(getView().getGridCategories());
			getView().getEditingCategory().startEditing(new GridCell(row, 0));
		}
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
		newFringe.setPath("/");
		if (getView().getSelectedCategory() != null) {
			newFringe.setCategoryId(getView().getSelectedCategory().getId());
		}

		getView().getEditingFringe().cancelEditing();
		getView().getListStoreFringes().add(0, newFringe);

		getView().getEditingFringe().setEditableGrid(getView().getGridFringes());

		int row = getView().getListStoreFringes().indexOf(newFringe);
		getView().getEditingCategory().startEditing(new GridCell(row, 0));
	}

	public void doEditFringe() {
		int row = getView().getListStoreFringes().indexOf(getView().getSelectedFringe());
		if (getView().getSelectedFringe() != null) {
			getView().getEditingFringe().setEditableGrid(getView().getGridFringes());
			getView().getEditingFringe().startEditing(new GridCell(row, 0));
		}
	}

	public void doMoveFringe() {
		int row = getView().getListStoreFringes().indexOf(getView().getSelectedFringe());
		if (getView().getSelectedFringe() != null) {
		}
	}

	public void doLoadFringeJar() {
		if (getView().getSelectedFringe() != null) {
			// ProjectManager projectManager = presenter.getProjectManager();
			// Long projectId =
			// projectManager.getProjectIdByProjectPath(FlowController.getLoggedAsUser(),
			// fileItemInfo.getAbsolutePath());
			// String parentPath =
			// Utils.extractJustPath(fileItemInfo.getAbsolutePath());
			final FringeLoadDialog box = new FringeLoadDialog();
			box.setEditLabelText("Select file");
			// box.setParentPath(parentPath);
			box.addDialogHideHandler(new FringeManagerDialogHandlers.LoadFringeDialogHideHandler(box, this));
			box.show();
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
					}
					;
				}
			});
			box.show();
		}
	}

	public void doTestCache() {

	}

	public void doSaveChanges() {

	}

}
