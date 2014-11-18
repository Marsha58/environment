package com.vw.ide.client.fringemanagment.fringeinsert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.dialog.VwmlDialogExt;
import com.vw.ide.client.event.uiflow.fringes.InsertFringeInFileEvent;
import com.vw.ide.client.fringemanagment.model.BaseDto;
import com.vw.ide.client.fringemanagment.model.FolderDto;
import com.vw.ide.client.fringemanagment.model.FringeDto;
import com.vw.ide.client.images.ExampleImages;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.ui.editorpanel.EditorPanelContextMenu;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

/**
 * Allows to edit single line text
 * 
 * @author OMelnyk
 * 
 */
public class FringeInsertDialog extends VwmlDialogExt {

	private static Logger logger = Logger.getLogger("");

	@UiField(provided = true)
	TreeStore<BaseDto> store = new TreeStore<BaseDto>(new KeyProvider());
	@UiField
	Tree<BaseDto, String> treeFringesByCategories;
	@UiField
	ExampleImages images;

	private static FringeInsertDialogUiBinder uiBinder = GWT.create(FringeInsertDialogUiBinder.class);
	private String parentPath;
	private Presenter presenter;
	private BaseDto treeSelectedItem = null;
	private Fringe selectedFringe = null;


	private FringeInsertDialogContextMenu contextMenu;

	interface FringeInsertDialogUiBinder extends UiBinder<Widget, FringeInsertDialog> {
	}

	 class KeyProvider implements ModelKeyProvider<BaseDto> {
		    @Override
		    public String getKey(BaseDto item) {
		      return (item instanceof FolderDto ? "f-" : "m-") + item.getId().toString();
		    }
		  }
	 

	  @UiFactory
	  public ValueProvider<BaseDto, String> createValueProvider() {
	    return new ValueProvider<BaseDto, String>() {
	 
	      @Override
	      public String getValue(BaseDto object) {
	        return object.getName();
	      }
	 
	      @Override
	      public void setValue(BaseDto object, String value) {
	      }
	 
	      @Override
	      public String getPath() {
	        return "name";
	      }
	    };
	  }
	
	
	  public FolderDto getRootFolder() {
		    FolderDto root = makeFolder("Root");
		    Map<Category,List<Fringe>> fringesInCategories = ((DevelopmentBoardPresenter) presenter).getView().getFringesInCategories();
		    List<BaseDto> children = new ArrayList<BaseDto>();
		    root.setChildren(children);
		    if (fringesInCategories != null ) {
		    	for (Category curCategory : fringesInCategories.keySet()) {
		    		FolderDto categoryFolder = makeFolder(curCategory.getName());
		    		categoryFolder.setType("category");
		    		children.add(categoryFolder);
					List<Fringe> curFringeList = fringesInCategories.get(curCategory);
					for(Fringe curFringe : curFringeList) {
						Fringe fringeItem = new Fringe();
						fringeItem.setName(curFringe.getName());
						fringeItem.setClassname(curFringe.getClassname());
						categoryFolder.addChild(makeItem(fringeItem, categoryFolder));
					}
				}
		    }
		    
		    return root;
		  }
	  
	private static int autoId = 0;
	  
	private FolderDto makeFolder(String name) {
		    FolderDto theReturn = new FolderDto(++autoId, name);
		    theReturn.setChildren((List<BaseDto>) new ArrayList<BaseDto>());
		    return theReturn;
	}

	private FringeDto makeItem(Fringe fringe, FolderDto category) {
		    return makeItem(fringe, category.getName());
	}

	private FringeDto makeItem(Fringe fringe, String category) {
		    return new FringeDto(++autoId, fringe, category);
	}	  


	public Presenter getPresenter() {
		return presenter;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}


	public FringeInsertDialog(Presenter presenter) {
		setPredefinedButtons(PredefinedButton.CLOSE);
		this.presenter = presenter;
		super.setWidget(uiBinder.createAndBindUi(this));
		
		treeFringesByCategories.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		treeFringesByCategories.getSelectionModel().addSelectionHandler(new SelectionHandler<BaseDto>() {
			public void onSelection(SelectionEvent<BaseDto> event) {
				treeSelectedItem = event.getSelectedItem();
				if (treeSelectedItem.getType() == "fringe") {
					selectedFringe = ((FringeDto)treeSelectedItem).getFringe();
					Category category = new Category(((FringeDto)treeSelectedItem).getFringe().getCategoryId(),
							((FringeDto)treeSelectedItem).getCategory());
					selectedFringe.setCategory(category);
					logger.log(Level.INFO, category.getName() + " : " + selectedFringe.getName() +  " : " + selectedFringe.getClassname());
				}
				contextMenu.getInsertFringe().setEnabled(treeSelectedItem.getType() == "fringe");
			}
		});		
		
		buildContextMenu();
		initTree();
//		createListField();
	}

	
	public void buildContextMenu() {
		contextMenu = new FringeInsertDialogContextMenu(this);
		contextMenu.setWidth(130);
		contextMenu.addBeforeShowHandler(new BeforeShowHandler() {
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				contextMenu.associatePresenter(presenter);
			}
		});
		treeFringesByCategories.setContextMenu(contextMenu);
	}		


	private void initTree() {
	    FolderDto root = getRootFolder();
	    for (BaseDto base : root.getChildren()) {
	      store.add(base);
	      if (base instanceof FolderDto) {
	        processFolder(store, (FolderDto) base);
	      }
	    }
	}
	
	private void processFolder(TreeStore<BaseDto> store, FolderDto folder) {
	    for (BaseDto child : folder.getChildren()) {
	      store.add(folder, child);
	      if (child instanceof FolderDto) {
	        processFolder(store, (FolderDto) child);
	      }
	    }
	  }


	protected void onButtonPressed(TextButton textButton) {
		hide();
	}
	
	public Fringe getSelectedFringe() {
		return selectedFringe;
	}


	public void setSelectedFringe(Fringe selectedFringe) {
		this.selectedFringe = selectedFringe;
	}	

}
