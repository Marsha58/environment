package com.vw.ide.client.dialog.fringemanagment;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent.CancelEditHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent.RowClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.NumericFilter;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.vw.ide.client.dialog.VwmlDialogExt;
import com.vw.ide.client.event.uiflow.GetCategoriesEvent;
import com.vw.ide.client.event.uiflow.GetFringesEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

public class FringeManager extends VwmlDialogExt implements IsWidget, PresenterViewerLink {

	private static FringManagerUiBinder uiBinder = GWT.create(FringManagerUiBinder.class);
	private FringeManagerPresenter presenter = null;

	private CategoryProperties categoryProperties = GWT.create(CategoryProperties.class);
	private FringeProperties fringeProperties = GWT.create(FringeProperties.class);

	public static final String SAVE_ID = "SAVE_ID";
	public static final String CANCEL_ID = "CANCEL_ID";

	private CategoryContextMenu contextMenuCategory;
	private FringeContextMenu contextMenuFringe;
	
	@UiField
	TextButton buttonAddCategory;

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

	// final GridEditing<Category> editingCategory = new
	// GridInlineEditing<Category>(gridCategories);
	private final GridEditing<Category> editingCategory = new GridRowEditing<Category>(gridCategories);
	private final GridEditing<Fringe> editingFringe = new GridRowEditing<Fringe>(gridFringes);

	private Integer categoryId = -1;
	private Integer fringeId = -1;
	
	private Category selectedCategory = null;
	private Fringe selectedFringe = null;
	
	NumericFilter<Fringe,Integer> categoryIdFilter;
	
	
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
		createFringeUi();
		super.setWidget(uiBinder.createAndBindUi(this));
		setPredefinedButtons();
		addButtons();

		buildContextMenuCategory();
		editingCategory.setEditableGrid(null);
		buildContextMenuFringe();
		editingFringe.setEditableGrid(null);

		categoryIdFilter = new NumericFilter<Fringe, Integer>(fringeProperties.categoryId(), new NumberPropertyEditor.IntegerPropertyEditor());
		
		GridFilters<Fringe> filters = new GridFilters<Fringe>();
	    filters.initPlugin(gridFringes);
	    filters.setLocal(true);
	    filters.addFilter(categoryIdFilter);
	    
	    
		
		// gridCategories.setSelectionModel(new CellSelectionModel<Category>());
		gridCategories.setSelectionModel(new GridSelectionModel<Category>());
		gridCategories.getColumnModel().getColumn(0).setHideable(false);

		gridCategories.addRowClickHandler(new RowClickHandler() {
			@Override
			public void onRowClick(RowClickEvent event) {
				int rowIndex = event.getRowIndex();
				selectedCategory = listStoreCategories.get(rowIndex);
				setFilterOnFringesList(selectedCategory);
			}
		});
		
		
		editingCategory.addCompleteEditHandler(new CompleteEditHandler<Category>() {
			@Override
			public void onCompleteEdit(CompleteEditEvent<Category> event) {
				editingCategory.setEditableGrid(null);
			}
		});
		
		editingCategory.addCancelEditHandler(new CancelEditHandler<Category>() {
			@Override
			public void onCancelEdit(CancelEditEvent<Category> event) {
				editingCategory.setEditableGrid(null);
			}
			
		});
		
		gridFringes.addRowClickHandler(new RowClickHandler() {
			@Override
			public void onRowClick(RowClickEvent event) {
				int rowIndex = event.getRowIndex();
				selectedFringe = listStoreFringes.get(rowIndex);
			}
		});
		
		editingFringe.addCompleteEditHandler(new CompleteEditHandler<Fringe>() {
			@Override
			public void onCompleteEdit(CompleteEditEvent<Fringe> event) {
				editingFringe.setEditableGrid(null);
			}
		});
		
		editingFringe.addCancelEditHandler(new CancelEditHandler<Fringe>() {
			@Override
			public void onCancelEdit(CancelEditEvent<Fringe> event) {
				editingFringe.setEditableGrid(null);
			}
			
		});		
		

		addShowHandler(new ShowHandler() {
			@Override
			public void onShow(ShowEvent event) {
				fireEvent(new GetCategoriesEvent());
				fireEvent(new GetFringesEvent());
			}
		});

	}
	
	
	private void setFilterOnFringesList(Category selectedCategory){
		if (selectedCategory.getId() != null) {
			if (categoryIdFilter.isActive()) {
				categoryIdFilter.setActive(false,false);
			}
			Integer priorValue = selectedCategory.getId() - 1;
			Integer nextValue = selectedCategory.getId() + 1;
			categoryIdFilter.setGreaterThanValue(priorValue);
			categoryIdFilter.setLessThanValue(nextValue);
			categoryIdFilter.setActive(true,false);
			
		}
	}
	

	private void createCategoryUi() {
		columnModelCategories = initColumnModelCategory();
		listStoreCategories = initListStoreCategory();
		// gridViewCategories.setAutoExpandColumn(columnModelCategories.getColumn(0));
	}

	private ColumnConfig<Category, Integer> ccCategoryId;
	private ColumnConfig<Category, String> ccCategoryName;
	private ColumnConfig<Category, String> ccCategoryIcon;
	private ColumnConfig<Category, String> ccCategoryDescription;

	private ColumnModel<Category> initColumnModelCategory() {
		// Create the configurations for each column in the grid
		List<ColumnConfig<Category, ?>> ccs = new ArrayList<ColumnConfig<Category, ?>>();

		ccCategoryId = new ColumnConfig<Category, Integer>(categoryProperties.id(), 20, "Id");
		ccs.add(ccCategoryId);

		ccCategoryName = new ColumnConfig<Category, String>(categoryProperties.name(), 80, "Name");
		ccs.add(ccCategoryName);
		editingCategory.addEditor(ccCategoryName, new TextField());

		ccCategoryIcon = new ColumnConfig<Category, String>(categoryProperties.icon(), 70, "Icon");
		ccs.add(ccCategoryIcon);
		editingCategory.addEditor(ccCategoryIcon, new TextField());

		ccCategoryDescription = new ColumnConfig<Category, String>(categoryProperties.description(), 200, "Description");
		ccs.add(ccCategoryDescription);
		editingCategory.addEditor(ccCategoryDescription, new TextField());
		
		return new ColumnModel<Category>(ccs);
	}

	private ListStore<Category> initListStoreCategory() {
		ListStore<Category> store = new ListStore<Category>(categoryProperties.key());
		store.setAutoCommit(true);
		return store;
	}

	private void createFringeUi() {
		columnModelFringes = initColumnModelFringe();
		listStoreFringes = initListStoreFringe();

	}
	
	private ColumnConfig<Fringe, Integer> ccFringeId;
	private ColumnConfig<Fringe, String> ccFringeName;
	private ColumnConfig<Fringe, String> ccFringePath;
	private ColumnConfig<Fringe, Integer> ccFringeCategoryId;
	private ColumnConfig<Fringe, String> ccFringeDescription;
	

	private ColumnModel<Fringe> initColumnModelFringe() {
		// Create the configurations for each column in the grid
		List<ColumnConfig<Fringe, ?>> ccs = new ArrayList<ColumnConfig<Fringe, ?>>();
		ccFringeId = new ColumnConfig<Fringe, Integer>(fringeProperties.id(), 20, "Id"); 
		ccs.add(ccFringeId);
		editingFringe.addEditor(ccFringeId, new IntegerField());
		ccFringeName = new ColumnConfig<Fringe, String>(fringeProperties.name(), 80, "Name");
		ccs.add(ccFringeName);
		editingFringe.addEditor(ccFringeName, new TextField());
		ccFringePath = new ColumnConfig<Fringe, String>(fringeProperties.path(), 100, "Path");
		ccs.add(ccFringePath);
		editingFringe.addEditor(ccFringePath, new TextField());
		ccFringeCategoryId = new ColumnConfig<Fringe, Integer>(fringeProperties.categoryId(), 70, "Category Id");
		ccs.add(ccFringeCategoryId);
		editingFringe.addEditor(ccFringeCategoryId, new IntegerField());
		ccFringeDescription = new ColumnConfig<Fringe, String>(fringeProperties.description(), 200, "Description");
		ccs.add(ccFringeDescription);
		editingFringe.addEditor(ccFringeDescription, new TextField());

		return new ColumnModel<Fringe>(ccs);
	}

	private ListStore<Fringe> initListStoreFringe() {
		ListStore<Fringe> store = new ListStore<Fringe>(fringeProperties.key());
		store.setAutoCommit(true);
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
		this.presenter = (FringeManagerPresenter) presenter;

	}

	protected Presenter getAssociatedPresenter() {
		return this.presenter;
	}

	public GridEditing<Category> getEditingCategory() {
		return editingCategory;
	}

	public ListStore<Category> getListStoreCategories() {
		return listStoreCategories;
	}
	
	public Grid<Category> getGridCategories() {
		return gridCategories;
	}

	public Category getSelectedCategory() {
		return selectedCategory; 
	}	

	public GridEditing<Fringe> getEditingFringe() {
		return editingFringe;
	}

	public ListStore<Fringe> getListStoreFringes() {
		return listStoreFringes;
	}
	
	public Grid<Fringe> getGridFringes() {
		return gridFringes;
	}
	
	public Fringe getSelectedFringe() {
		return selectedFringe; 
	}
	
	

	
	
	public void buildContextMenuCategory() {
		contextMenuCategory = new CategoryContextMenu();
		contextMenuCategory.setWidth(80);
		contextMenuCategory.addBeforeShowHandler(new BeforeShowHandler() {
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				contextMenuCategory.associatePresenter(getAssociatedPresenter());
			}
		});
		gridCategories.setContextMenu(contextMenuCategory);
	}		

	public void buildContextMenuFringe() {
		contextMenuFringe = new FringeContextMenu();
		contextMenuFringe.setWidth(80);
		contextMenuFringe.addBeforeShowHandler(new BeforeShowHandler() {
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				contextMenuFringe.associatePresenter(getAssociatedPresenter());
			}
		});
		gridFringes.setContextMenu(contextMenuFringe);
	}		

	
	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer value) {
		categoryId = value;
	}
	
	public Integer getNewCategoryId() {
		return ++categoryId;
	}
	
	public Integer getFringeId() {
		return fringeId;
	}
	
	public void setFringeId(Integer value) {
		fringeId = value;
	}
	

	public Integer getNewFringeId() {
		return ++fringeId;
	}	
	
	@UiHandler({ "buttonRefreshCategory" })
	public void onButtonRefreshCategoryClick(SelectEvent event) {
		presenter.fireEvent(new GetCategoriesEvent());
	}

	@UiHandler({ "buttonAddCategory" })
	public void onButtonAddCategoryClick(SelectEvent event) {
		presenter.doAddCategory();
	}

	@UiHandler({ "buttonEditCategory" })
	public void onButtonEditCategoryClick(SelectEvent event) {
		presenter.doEditCategory();
	}
	
	@UiHandler({ "buttonDeleteCategory" })
	public void onButtonDeleteCategoryClick(SelectEvent event) {
		presenter.doDeleteCategory();
	}		

	@UiHandler({ "buttonRefreshFringe" })
	public void onButtonRefreshFringeClick(SelectEvent event) {
		presenter.fireEvent(new GetFringesEvent());
	}
	
	@UiHandler({ "buttonAddFringe" })
	public void onButtonAddFringeClick(SelectEvent event) {
		presenter.doAddFringe();
	}

	@UiHandler({ "buttonEditFringe" })
	public void onButtonEditFringeClick(SelectEvent event) {
		presenter.doEditFringe();
	}
	
	@UiHandler({ "buttonMoveFringe" })
	public void onButtonMoveFringeClick(SelectEvent event) {
		presenter.doMoveFringe();
	}
	
	
	@UiHandler({ "buttonDeleteFringe" })
	public void onButtonDeleteFringeClick(SelectEvent event) {
		presenter.doDeleteFringe();
	}		

}
