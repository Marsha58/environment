package com.vw.ide.client.dialog.fringemanagment;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.vw.ide.client.dialog.VwmlDialogExt;
import com.vw.ide.client.event.uiflow.GetCategoriesEvent;
import com.vw.ide.client.event.uiflow.GetFringesEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

public class FringeManager extends VwmlDialogExt implements IsWidget, PresenterViewerLink{

	private static FringManagerUiBinder uiBinder = GWT.create(FringManagerUiBinder.class);
	private FringeManagerPresenter presenter = null;
	
	private CategoryProperties categoryProperties = GWT.create(CategoryProperties.class);
	private FringeProperties fringeProperties = GWT.create(FringeProperties.class);
	
	public static final String SAVE_ID = "SAVE_ID";
	public static final String CANCEL_ID = "CANCEL_ID";

	
	@UiField TextButton buttonAddCategory;

	@UiField(provided = true)
	ColumnModel<Category> columnModelCategories;
	@UiField(provided = true)
	ListStore<Category> listStoreCategories;
	@UiField
	GridView<Category> gridViewCategories;
	@UiField
	Grid<Category> gridCategories;

	@UiField(provided = true)
	ColumnModel<Fringe> columnModelFringes;
	@UiField(provided = true)
	ListStore<Fringe> listStoreFringes;
	@UiField
	GridView<Fringe> gridViewFringes;
	@UiField
	Grid<Fringe> gridFringes;
	
	
	interface FringManagerUiBinder extends UiBinder<Widget, FringeManager> {
	}
	
	 // Property access definitions for the values in the Category
	public interface CategoryProperties extends PropertyAccess<Category> {
		    @Path("id")
		    ModelKeyProvider<Category> key();
		    
		    ValueProvider<Category, Integer> id();
		    ValueProvider<Category, String> name();
		    ValueProvider<Category, String> icon();
		    ValueProvider<Category, String> description();
	}
	
	 // Property access definitions for the values in the Fringe
	public interface FringeProperties extends PropertyAccess<Fringe> {
		    @Path("id")
		    ModelKeyProvider<Fringe> key();
		    
		    ValueProvider<Fringe, Integer> id();
		    ValueProvider<Fringe, String> name();
		    ValueProvider<Fringe, String> path();
		    ValueProvider<Fringe, Integer> categoryId();
		    ValueProvider<Fringe, String> description();
	}		

	TextButton buttonSave = new TextButton("Save");
	TextButton buttonCancel = new TextButton("Cancel");
	
	public FringeManager() {
		createCategoryUi();
		super.setWidget(uiBinder.createAndBindUi(this));
		setPredefinedButtons();
		addButtons();
		addShowHandler(new ShowHandler() {
			
			@Override
			public void onShow(ShowEvent event) {
				presenter.fireEvent(new GetCategoriesEvent());
			}
		});
		
		addBeforeShowHandler(new BeforeShowHandler() {
			
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
//				presenter.fireEvent(new GetCategoriesEvent());
			}
		});
	}
	
	
	private void createCategoryUi() {
		columnModelCategories = initColumnModelCategory();
		listStoreCategories = initListStoreCategory();
//		gridViewCategories.setAutoExpandColumn(columnModelCategories.getColumn(0));
	}	
	
	private ColumnModel<Category> initColumnModelCategory() {
		 // Create the configurations for each column in the grid
	    List<ColumnConfig<Category, ?>> ccs = new ArrayList<ColumnConfig<Category, ?>>();
	    ccs.add(new ColumnConfig<Category, Integer>(categoryProperties.id(), 20, "Id"));
	    ccs.add(new ColumnConfig<Category, String>(categoryProperties.name(), 70, "Name"));
	    ccs.add(new ColumnConfig<Category, String>(categoryProperties.icon(), 50, "Icon"));
	    ccs.add(new ColumnConfig<Category, String>(categoryProperties.description(), 100, "Description"));
	    return new ColumnModel<Category>(ccs);
	}	
	
	private ListStore<Category> initListStoreCategory() {
		ListStore<Category> store = new ListStore<Category>(categoryProperties.key()); 
		return store;
	}	
	
	private void createFringeUi() {
		columnModelFringes = initColumnModelFringe();
		listStoreFringes = initListStoreFringe();
		
	}
	
	private ColumnModel<Fringe> initColumnModelFringe() {
		 // Create the configurations for each column in the grid
	    List<ColumnConfig<Fringe, ?>> ccs = new ArrayList<ColumnConfig<Fringe, ?>>();
	    ccs.add(new ColumnConfig<Fringe, Integer>(fringeProperties.id(), 20, "Id"));
	    ccs.add(new ColumnConfig<Fringe, String>(fringeProperties.name(), 70, "Name"));
	    ccs.add(new ColumnConfig<Fringe, String>(fringeProperties.path(), 50, "Icon"));
	    ccs.add(new ColumnConfig<Fringe, Integer>(fringeProperties.categoryId(), 50, "Categori Id"));
	    ccs.add(new ColumnConfig<Fringe, String>(fringeProperties.description(), 100, "Description"));
	    return new ColumnModel<Fringe>(ccs);
	}	
	
	private ListStore<Fringe> initListStoreFringe() {
		ListStore<Fringe> store = new ListStore<Fringe>(fringeProperties.key()); 
		return store;
	}		
	
	public void addButtons() {
		buttonSave.setId(SAVE_ID);
		buttonSave.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				presenter.doSaveChanges();
				hide();
			}
		});
		getButtonBar().add(buttonSave);
		buttonCancel.setId(CANCEL_ID);
		buttonCancel.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				hide();
			}
		});
		getButtonBar().add(buttonCancel);
	}
	


	@Override
	public void associatePresenter(Presenter presenter) {
		this.presenter = (FringeManagerPresenter)presenter;
		
	}		

	protected Presenter getAssociatedPresenter() {
		return this.presenter;
	}
	
	
   @UiHandler({"buttonAddCategory"})
   public void onClick(SelectEvent event) {
//	   presenter.fireEvent(new GetFringesEvent());
	   
   }	

	

}
