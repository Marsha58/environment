package com.vw.ide.client.dialog.fringemanagment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.vw.ide.client.dialog.fringemanagment.event.handler.GetCategoriesEventHandler;
import com.vw.ide.client.dialog.fringemanagment.event.handler.GetFringesEventHandler;
import com.vw.ide.client.event.handler.GetCategoriesHandler;
import com.vw.ide.client.event.handler.GetFringesHandler;
import com.vw.ide.client.event.uiflow.GetCategoriesEvent;
import com.vw.ide.client.event.uiflow.GetFringesEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.service.ProcessedResult;
import com.vw.ide.shared.servlet.fringes.RequestGetCategoriesResult;
import com.vw.ide.shared.servlet.fringes.RequestGetFringesResult;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

/**
 * Login screen
 * 
 * @author Omelnyk
 * 
 */
public class FringeManagerPresenter extends Presenter {

	
	public static class GetCategoriesResult extends ProcessedResult<RequestGetCategoriesResult> {

		private FringeManagerPresenter presenter;

		public GetCategoriesResult() {
		}

		public GetCategoriesResult(FringeManagerPresenter presenter) {
			this.presenter = presenter;
		}

		@Override
		public void onFailure(Throwable caught) {
			AlertMessageBox alertMessageBox = new AlertMessageBox("Error", caught.getMessage());
			alertMessageBox.show();
		}

		@Override
		public void onSuccess(RequestGetCategoriesResult result) {
			String messageAlert;
			if (result.getRetCode().intValue() != 0) {
				messageAlert = "Get categories operation '" 
						+ "' failed.\r\nResult'" + result.getResult() + "'";
				AlertMessageBox alertMessageBox = new AlertMessageBox(
						"Warning", messageAlert);
				alertMessageBox.show();
			} else {
				switch (result.getRetCode()) {
					case 0:
						presenter.updateCategoryList(result.getCategories());
						break;
					case -1:
						messageAlert = "something wrong";
						AlertMessageBox alertMessageBox = new AlertMessageBox(
								"Warning", messageAlert);
						alertMessageBox.show();	
						break;
					default:
						break;
				}
			}
		}
	}
	

	public static class GetFringesResult extends ProcessedResult<RequestGetFringesResult> {

		private FringeManagerPresenter presenter;

		public GetFringesResult() {
		}

		public GetFringesResult(FringeManagerPresenter presenter) {
			this.presenter = presenter;
		}

		@Override
		public void onFailure(Throwable caught) {
			AlertMessageBox alertMessageBox = new AlertMessageBox("Error", caught.getMessage());
			alertMessageBox.show();
		}

		@Override
		public void onSuccess(RequestGetFringesResult result) {
			String messageAlert;
			if (result.getRetCode().intValue() != 0) {
				messageAlert = "Get Fringes operation '" 
						+ "' failed.\r\nResult'" + result.getResult() + "'";
				AlertMessageBox alertMessageBox = new AlertMessageBox(
						"Warning", messageAlert);
				alertMessageBox.show();
			} else {
				switch (result.getRetCode()) {
					case 0:
						presenter.updateFringeList(result.getFringes());
						break;
					case -1:
						messageAlert = "something wrong";
						AlertMessageBox alertMessageBox = new AlertMessageBox(
								"Warning", messageAlert);
						alertMessageBox.show();	
						break;
					default:
						break;
				}
			}
		}
	}
	
	



	
	private void updateCategoryList(Category[] categories) {
		for (int i = 0; i < categories.length; i++) {
			if(getView().listStoreCategories.findModelWithKey(categories[i].getId().toString()) == null) {
				getView().listStoreCategories.add(categories[i]);
			} else {
				getView().listStoreCategories.update(categories[i]);
			}
		}
	}
	
	private void updateFringeList(Fringe[] fringes) {
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
		registerOnEventBus(eventBus);
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
		for(Presenter.PresenterEventHandler handler : dispatcher.values()) {
			handler.setPresenter(this);
		}
		eventBus.addHandler(GetCategoriesEvent.TYPE, (GetCategoriesHandler)dispatcher.get(GetCategoriesEvent.TYPE));
		eventBus.addHandler(GetFringesEvent.TYPE, (GetFringesHandler)dispatcher.get(GetFringesEvent.TYPE));
	}

	@Override
	public void unregisterOnEventBus(HandlerManager eventBus) {
		eventBus.removeHandler(GetCategoriesEvent.TYPE, (GetCategoriesHandler)dispatcher.get(GetCategoriesEvent.TYPE));
		eventBus.removeHandler(GetFringesEvent.TYPE, (GetFringesHandler)dispatcher.get(GetFringesEvent.TYPE));
	}
	
	
	
	public void doSaveChanges() {

	}	
	
}
