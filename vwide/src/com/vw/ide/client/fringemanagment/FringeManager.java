package com.vw.ide.client.fringemanagment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dev.util.collect.HashMap;
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
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent.StoreRecordChangeHandler;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent.CancelEditHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent.RowClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;
import com.vw.ide.client.dialog.VwmlDialogExt;
import com.vw.ide.client.event.uiflow.FringesContextMenuEvent;
import com.vw.ide.client.event.uiflow.fringes.AddCategoryEvent;
import com.vw.ide.client.event.uiflow.fringes.AddFringeEvent;
import com.vw.ide.client.event.uiflow.fringes.UpdateCategoryEvent;
import com.vw.ide.client.event.uiflow.fringes.UpdateFringeEvent;
import com.vw.ide.client.fringemanagment.contextmenus.CategoryContextMenu;
import com.vw.ide.client.fringemanagment.contextmenus.FringeContextMenu;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.shared.CrudTypes;
import com.vw.ide.shared.OperationTypes;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

public class FringeManager extends VwmlDialogExt implements IsWidget, PresenterViewerLink {

	private static FringManagerUiBinder uiBinder = GWT.create(FringManagerUiBinder.class);
	private FringeManagerPresenter presenter = null;

	private CategoryProperties categoryProperties = GWT.create(CategoryProperties.class);
	private FringeProperties fringeProperties = GWT.create(FringeProperties.class);

	public static final String CLOSE_ID = "CLOSE_ID";

	private CategoryContextMenu contextMenuCategory;
	private FringeContextMenu contextMenuFringe;

	private Boolean isCategoryEditing = false;
	private Boolean isFringeEditing = false;
	
	private LoadingCache<Integer, List<Fringe>> fringesCache;

	public LoadingCache<Integer, List<Fringe>> getFringesCache() {
		return fringesCache;
	}

	public Boolean getIsCategoryEditing() {
		return isCategoryEditing;
	}

	public void setIsCategoryEditing(Boolean isCategoryEditing) {
		this.isCategoryEditing = isCategoryEditing;
	}

	public Boolean getIsFringeEditing() {
		return isFringeEditing;
	}

	public void setIsFringeEditing(Boolean isFringeEditing) {
		this.isFringeEditing = isFringeEditing;
	}

	@UiField
	TextButton buttonAddCategory;
	@UiField
	TextButton buttonEditCategory;
	@UiField
	TextButton buttonDeleteCategory;

	@UiField
	TextButton buttonAddFringe;
	@UiField
	TextButton buttonEditFringe;
	@UiField
	TextButton buttonDeleteFringe;
	@UiField
	TextButton buttonLoadFringeJar;

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
	
//	private Map<Integer,List<Fringe>> mapCategoryWithFringes = new HashMap<>();
	private List<Fringe> allFringes = new ArrayList<>();

	// final GridEditing<Category> editingCategory = new
	// GridInlineEditing<Category>(gridCategories);
	private final GridEditing<Category> editingCategory = new GridRowEditing<Category>(gridCategories);
	private final GridEditing<Fringe> editingFringe = new GridRowEditing<Fringe>(gridFringes);

	private Integer categoryId = -1;
	private Integer fringeId = -1;

	private Category selectedCategory = null;
	private Fringe selectedFringe = null;
	
	private CrudTypes categoryOperationType = null;
	private CrudTypes fringeOperationType = null;

//	NumericFilter<Fringe, Integer> categoryIdFilter;

	public CrudTypes getCategoryOperationType() {
		return categoryOperationType;
	}

	public void setCategoryOperationType(CrudTypes categoryOperationType) {
		this.categoryOperationType = categoryOperationType;
	}

	public CrudTypes getFringeOperationType() {
		return fringeOperationType;
	}

	public void setFringeOperationType(CrudTypes fringeOperationType) {
		this.fringeOperationType = fringeOperationType;
	}

	public void setSelectedCategory(Category selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public void setSelectedFringe(Fringe selectedFringe) {
		this.selectedFringe = selectedFringe;
	}

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

		ValueProvider<Fringe, String> filename();

		ValueProvider<Fringe, String> classname();
		
		ValueProvider<Fringe, Boolean> loaded();

		ValueProvider<Fringe, Integer> categoryId();

		ValueProvider<Fringe, String> description();
	}

	TextButton buttonClose = new TextButton("Close");

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
		
		initCache();
	
		listStoreFringes.addStoreRecordChangeHandler(new StoreRecordChangeHandler<Fringe>() {

			@Override
			public void onRecordChange(StoreRecordChangeEvent<Fringe> event) {
				presenter.fireEvent(new UpdateFringeEvent(event.getRecord().getModel()));
			}
		});
		

/*		categoryIdFilter = new NumericFilter<Fringe, Integer>(fringeProperties.categoryId(), new NumberPropertyEditor.IntegerPropertyEditor());

		GridFilters<Fringe> filters = new GridFilters<Fringe>();
		filters.initPlugin(gridFringes);
		filters.setLocal(true);
		filters.addFilter(categoryIdFilter);
*/		
		
		
			

		updateCategoryControlsState();
		updateFringeControlsState();
		
		gridCategories.setSelectionModel(new GridSelectionModel<Category>());
		gridCategories.getColumnModel().getColumn(0).setHideable(false);

		gridCategories.addRowClickHandler(new RowClickHandler() {
			@Override
			public void onRowClick(RowClickEvent event) {
				int rowIndex = event.getRowIndex();
				selectedCategory = listStoreCategories.get(rowIndex);
				getFringesListByCategory(selectedCategory);
				updateCategoryControlsState();
				updateFringeControlsState();
			}
		});

		editingCategory.addCompleteEditHandler(new CompleteEditHandler<Category>() {
			@Override
			public void onCompleteEdit(CompleteEditEvent<Category> event) {
				if (categoryOperationType == CrudTypes.ADD) {
					presenter.fireEvent(new AddCategoryEvent(selectedCategory));
				} else {
					presenter.fireEvent(new UpdateCategoryEvent(selectedCategory));
				}  				
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
				updateFringeControlsState();
			}
		});

		
		editingFringe.addCompleteEditHandler(new CompleteEditHandler<Fringe>() {
			@Override
			public void onCompleteEdit(CompleteEditEvent<Fringe> event) {
				if (fringeOperationType == CrudTypes.ADD) {
					presenter.fireEvent(new AddFringeEvent(selectedFringe));
				} else {
					presenter.fireEvent(new UpdateFringeEvent(selectedFringe));
				}  
				
				updateEditedFringeInFringesList(selectedFringe,fringeOperationType);
				deleteFringeFromFringeCache(selectedFringe);
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
				presenter.registerOnEventBus();
				presenter.callerGetGategories();
				presenter.callerGetFringes();
			}
		});
		
		
		addHideHandler(new HideHandler() {
			@Override
			public void onHide(HideEvent event) {
				presenter.unregisterOnEventBus();
			}
		});
		
		
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
		for (Fringe fringe : allFringes) {
			if (categoryId == fringe.getCategoryId()) {
				fringesList.add(fringe);
			}
		}
		return fringesList;
	}
	
	public void deleteFringeFromFringeCache(Fringe fringe) {
		try {
			List<Fringe> curCategory = fringesCache.get(fringe.getCategoryId());
			if (curCategory != null) {
				for (Fringe curFringe : curCategory) {
					if (curFringe.getId() == fringe.getId()) {
						curCategory.remove(curFringe);
						break;
					}
				}
			}
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fringesCache.invalidate(fringe.getCategoryId());
	}
	
	public Boolean isCategoryHasFringes(Category category) {
		Boolean hasFringes = false;
		try {
			List<Fringe> curCategory = fringesCache.get(category.getId());
			hasFringes = curCategory.size()>0;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hasFringes;
	}	


	public void getFringesListByCategory(Category selectedCategory) {
		if (selectedCategory.getId() != null) {
			listStoreFringes.clear();
			listStoreFringes.addAll(getFringesList(selectedCategory.getId()));
		}
	}
	
	public void getFringesListByCategoryId(Integer id) {
		if (id != null) {
			listStoreFringes.clear();
			listStoreFringes.addAll(getFringesList(id));
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
	private ColumnConfig<Fringe, String> ccFringeFileName;
	private ColumnConfig<Fringe, String> ccFringeClassName;
	private ColumnConfig<Fringe, Boolean> ccFringeLoaded;
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
		
		ccFringeFileName = new ColumnConfig<Fringe, String>(fringeProperties.filename(), 80, "Filename");
		ccs.add(ccFringeFileName);
		editingFringe.addEditor(ccFringeFileName, new TextField());

		ccFringeClassName = new ColumnConfig<Fringe, String>(fringeProperties.classname(), 120, "Classname");
		ccs.add(ccFringeClassName);
		editingFringe.addEditor(ccFringeClassName, new TextField());
		
		ccFringeLoaded = new ColumnConfig<Fringe, Boolean>(fringeProperties.loaded(), 100, "Jar loaded");
		ccs.add(ccFringeLoaded);
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
		buttonClose.setId(CLOSE_ID);
		buttonClose.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				hide();
			}
		});
		getButtonBar().add(buttonClose);
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
				updateContextMenuCategoryControlsState(contextMenuCategory);
			}
		});
		gridCategories.setContextMenu(contextMenuCategory);
	}

	public void buildContextMenuFringe() {
		contextMenuFringe = new FringeContextMenu();
		contextMenuFringe.setWidth(110);
		contextMenuFringe.addBeforeShowHandler(new BeforeShowHandler() {
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				contextMenuFringe.associatePresenter(getAssociatedPresenter());
				updateContextMenuFringeControlsState(contextMenuFringe);
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

	@UiHandler({ "buttonAddFringe" })
	public void onButtonAddFringeClick(SelectEvent event) {
		presenter.doAddFringe();
	}

	@UiHandler({ "buttonEditFringe" })
	public void onButtonEditFringeClick(SelectEvent event) {
		presenter.doEditFringe();
	}


	@UiHandler({ "buttonLoadFringeJar" })
	public void onButtonLoadFringeJarClick(SelectEvent event) {
		presenter.fireEvent(new FringesContextMenuEvent(OperationTypes.LOAD_FRINGE_JAR.getName()));
	}

	@UiHandler({ "buttonDeleteFringe" })
	public void onButtonDeleteFringeClick(SelectEvent event) {
		presenter.doDeleteFringe();
	}

	
	public void updateContextMenuCategoryControlsState(CategoryContextMenu contextMenuCategory) {
		
		contextMenuCategory.getAddCategory().setEnabled(true);
		if (selectedCategory == null) {
			contextMenuCategory.getEditCategory().setEnabled(false);
			contextMenuCategory.getDeleteCategory().setEnabled(false);
		} else {
			contextMenuCategory.getEditCategory().setEnabled(true);
			contextMenuCategory.getDeleteCategory().setEnabled(!isCategoryHasFringes(selectedCategory));
		}
	}
	
	
	public void updateContextMenuFringeControlsState(FringeContextMenu contextMenuFringe) {
		contextMenuFringe.getAddFringe().setEnabled(true);
		if (selectedFringe == null) {
			contextMenuFringe.getLoadFringeJar().setEnabled(false);
			contextMenuFringe.getEditFringe().setEnabled(false);
			contextMenuFringe.getDeleteFringe().setEnabled(false);
		} else {
			contextMenuFringe.getLoadFringeJar().setEnabled(true);
			contextMenuFringe.getEditFringe().setEnabled(true);
			contextMenuFringe.getDeleteFringe().setEnabled(true);
		}
	}	

	public void updateCategoryControlsState() {
		buttonAddCategory.setEnabled(true);
		if (selectedCategory == null) {
			buttonEditCategory.setEnabled(false);
			buttonDeleteCategory.setEnabled(false);
		} else {
			buttonEditCategory.setEnabled(true);
			
			buttonDeleteCategory.setEnabled(!isCategoryHasFringes(selectedCategory));
		}
	}
	
	public void updateFringeControlsState() {
		buttonAddFringe.setEnabled(true);
		if (selectedFringe == null) {
			buttonLoadFringeJar.setEnabled(false);
			buttonEditFringe.setEnabled(false);
			buttonDeleteFringe.setEnabled(false);
		} else {
			buttonLoadFringeJar.setEnabled(true);
			buttonEditFringe.setEnabled(true);
			buttonDeleteFringe.setEnabled(true);
		}
	}
	
	public void setAllFringes(Fringe[] fringes){
		for (int i = 0; i < fringes.length; i++) {
			allFringes.add(fringes[i]);
		}
	}
	
	public List<Fringe> getAllFringes(){
		return allFringes;
	}	
	
	public void updateEditedFringeInFringesList(Fringe fringe, CrudTypes operType) {
	  if(operType != CrudTypes.ADD) {
		  
		  for (Fringe curFringe : allFringes) {
			  if (curFringe.getId() == fringe.getId()) {
				  allFringes.remove(curFringe);
				  break;
			  }
		  }
		  
	  }
	  if(operType != CrudTypes.DELETE) {
		  allFringes.add(fringe);
	  }
	}
	
	public void updateFringeListAndCache(Fringe fringe, CrudTypes editingType) {
		updateEditedFringeInFringesList(fringe, editingType);
		deleteFringeFromFringeCache(fringe);
		int row = getListStoreFringes().indexOf(fringe);
		if(editingType == CrudTypes.ADD) {
			editingCategory.startEditing(new GridCell(row, 0));
			editingFringe.setEditableGrid(gridFringes);
			listStoreFringes.add(0, fringe);
			editingCategory.completeEditing();
			editingFringe.setEditableGrid(null);			
		}
		selectedFringe = fringe;
	}	
	

	

	
	
}
