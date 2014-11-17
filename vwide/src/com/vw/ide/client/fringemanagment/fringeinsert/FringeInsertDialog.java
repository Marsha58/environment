package com.vw.ide.client.fringemanagment.fringeinsert;

import java.util.ArrayList;
import java.util.List;
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
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.vw.ide.client.dialog.VwmlDialogExt;
import com.vw.ide.client.event.uiflow.fringes.InsertFringeInFileEvent;
import com.vw.ide.client.fringemanagment.model.BaseDto;
import com.vw.ide.client.fringemanagment.model.FolderDto;
import com.vw.ide.client.fringemanagment.model.FringeDto;
import com.vw.ide.client.images.ExampleImages;
import com.vw.ide.client.presenters.Presenter;
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

		    FolderDto category1 = makeFolder("communication");
		    category1.setType("category");
		    List<BaseDto> children = new ArrayList<BaseDto>();
		    children.add(category1);
		    root.setChildren(children);

		    Fringe fringe1 = new Fringe();
		    fringe1.setName("fringe1");
		    fringe1.setClassname("com.win.game.model.fringe.gate.async.console.AsyncConsole");
		    category1.addChild(makeItem(fringe1, category1));
		    Fringe fringe2 = new Fringe();
		    fringe2.setName("fringe2");
		    fringe2.setClassname("com.win.game.model.fringe.gate.async.console.AsyncConsole2");
		    category1.addChild(makeItem(fringe2, category1));

		    
		    FolderDto category2 = makeFolder("services");
		    category2.setType("category");
		    children.add(category2);

		    Fringe fringe3 = new Fringe();
		    fringe3.setName("fringe3");
		    fringe3.setClassname("com.vw.lang.beyond.java.fringe.gate.math.Math");
		    category2.addChild(makeItem(fringe3, category2));
		    Fringe fringe4 = new Fringe();
		    fringe4.setName("fringe4");
		    fringe4.setClassname("com.win.game.model.fringe.gate.algorithm.graphloader.GraphLoader");
		    category2.addChild(makeItem(fringe4, category2));
		    
		    
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
			}
		});		
		
		
		initTree();
//		createListField();
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
		// super.onButtonPressed(textButton);
			if(treeSelectedItem == null) {
				ConfirmMessageBox box = new ConfirmMessageBox("Confirm", "You didn't choose fringe from the tree. Exit in any case?");
				box.addDialogHideHandler(new DialogHideHandler() {
					
					@Override
					public void onDialogHide(DialogHideEvent event) {
						if (event.getHideButton().name().equalsIgnoreCase("YES")) {
							hide();
						}
					}
				});
				box.show();
			} else {
				presenter.fireEvent(new InsertFringeInFileEvent(selectedFringe));
				hide();
			}

	}

}
