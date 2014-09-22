/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vw.ide.client.ui.toppanel;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
//import com.google.gwt.sample.mail.client.AboutDialog;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.BindingPropertySet;
import com.sencha.gxt.core.client.BindingPropertySet.PropertyName;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.vw.ide.client.dialog.about.AboutDialogExt;
import com.vw.ide.client.dialog.newvwmlproj.NewVwmlProjectDialogExt;
import com.vw.ide.client.event.uiflow.LogoutEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.sencha.gxt.widget.core.client.button.TextButton;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;



/**
 * The top panel, which contains the 'welcome' message and various links.
 */
public class TopPanel extends Composite implements	PresenterViewerLink {

  interface Binder extends UiBinder<Widget, TopPanel> { }
  private static final Binder binder = GWT.create(Binder.class);

	private Presenter presenter = null;
	
	private static String s_HelpAboutCaption = "About";
	private static String s_newVwmlProjectCaption = "New VWML project";

  
	public ComboBox<AceEditorTheme> comboATh;
	
//	 @UiField MenuItem logoutField;
	 @UiField MenuItem newVwmlProjField;
	 @UiField MenuItem miHelpAboutField;
//	 @UiField FlowPanel panelEditor;
	 @UiField SimpleContainer comboAceEditorPlaceCont;
	 @UiField SimpleContainer userNamePlaceCont;
	 @UiField SimpleContainer comboPlaceCont;
	 @UiField SimpleContainer outer;
	 @UiField Label userName;
	 @UiField TextButton userLogout;
	 
		@UiField(provided = true)
		MarginData centerData = new MarginData();
		@UiField(provided = true)
		BorderLayoutData eastData = new BorderLayoutData(490);
		@UiField
		BorderLayoutContainer con;   	 
  
		
	  public enum Theme {
		    BLUE("Blue Theme"), GRAY("Gray Theme"), NEPTUNE("Neptune Theme");

		    private final String value;

		    private Theme(String value) {
		      this.value = value;
		    }

		    public String value() {
		      return value;
		    }

		    public boolean isActive() {
		      ActiveTheme theme = GWT.create(ActiveTheme.class);
		      switch (this) {
		        case BLUE:
		          return theme.isBlue();
		        case GRAY:
		          return theme.isGray();
		        case NEPTUNE:
		          return theme.isNeptune();
		      }
		      return false;
		    }

		    @Override
		    public String toString() {
		      return value();
		    }
	  }
	
	  @PropertyName("gxt.theme")
	  public interface ActiveTheme extends BindingPropertySet {
	    @PropertyValue(value = "gray", warn = false)
	    boolean isGray();
	    @PropertyValue(value = "blue", warn = false)
	    boolean isBlue();
	    @PropertyValue(value = "neptune", warn = false)
	    boolean isNeptune();
	  }
	  
	  
	 	 

  
  public TopPanel() {
    initWidget(binder.createAndBindUi(this));
    
    
    centerData.setMargins(new Margins(2, 2, 0, 1));
    eastData.setMargins(new Margins(3, 2, 0, 0));
//    centerData.setMargins(new Margins(0, 5, 0, 5));    
    
    
    
	// ------Ace theme combo
    ListStore<AceEditorTheme> aceColors = new ListStore<AceEditorTheme>(new ModelKeyProvider<AceEditorTheme>() {

		@Override
		public String getKey(AceEditorTheme item) {
			return item.name();
		}

      });

    aceColors.addAll(Arrays.asList(AceEditorTheme.values()));
    

	comboATh = new ComboBox<AceEditorTheme>(aceColors, new StringLabelProvider<AceEditorTheme>());
	comboATh.setTriggerAction(TriggerAction.ALL);
	
	comboATh.setForceSelection(true);
	comboATh.setEditable(false);
	// combo.getElement().getStyle().setFloat(Float.RIGHT);
	comboATh.setWidth(200);
//	comboATh.setEmptyText("Select a theme...");		
	comboATh.setValue(AceEditorTheme.TOMORROW_NIGHT_BRIGHT);
	comboATh.addSelectionHandler(new SelectionHandler<AceEditorTheme>() {
		@Override
		public void onSelection(SelectionEvent<AceEditorTheme> event) {
//			editor1.setTheme(event.getSelectedItem());
			
		}
    });			

	comboAceEditorPlaceCont.add(comboATh);
    
    
    
    
    
    ListStore<Theme> colors = new ListStore<Theme>(new ModelKeyProvider<Theme>() {

        @Override
        public String getKey(Theme item) {
          return item.name();
        }

      });
      colors.addAll(Arrays.asList(Theme.values()));
      
    
      ComboBox<Theme> combo = new ComboBox<Theme>(colors, new StringLabelProvider<Theme>());
      combo.setTriggerAction(TriggerAction.ALL);
      combo.setForceSelection(true);
      combo.setEditable(false);
 //     combo.getElement().getStyle().setFloat(Float.RIGHT);
      combo.setWidth(125);
      combo.setValue(Theme.GRAY.isActive() ? Theme.GRAY : Theme.BLUE.isActive() ? Theme.BLUE : Theme.NEPTUNE);
      combo.addSelectionHandler(new SelectionHandler<Theme>() {
        @Override
        public void onSelection(SelectionEvent<Theme> event) {
          switch (event.getSelectedItem()) {
            case BLUE:
              Window.Location.assign(GWT.getHostPageBaseURL() + "Vwide.html" + Window.Location.getHash());
              break;
            case GRAY:
              Window.Location.assign(GWT.getHostPageBaseURL() + "Vwide-gray.html" + Window.Location.getHash());
              break;
            case NEPTUNE:
              Window.Location.assign(GWT.getHostPageBaseURL() + "Vwide-neptune.html" + Window.Location.getHash());
              break;
            default:
              assert false : "Unsupported theme enum";
          }
        }


      });

      comboPlaceCont.add(combo);
      

      bind();
    
  }
  
  public TopPanel(Integer size) {
	    initWidget(binder.createAndBindUi(this));
	    this.setSize("100%", size.toString());
	    bind();
  }


	public void associatePresenter(Presenter presenter) {
		this.presenter = presenter;
		userName.setText("logged as '" + presenter.getLoggedAsUser() + "'");
	}

	protected Presenter getAssociatedPresenter() {
		return this.presenter;
	}
  

	@UiHandler({"userLogout"})
	  public void onButtonClick(SelectEvent event) {
	    Info.display("Click", ((TextButton) event.getSource()).getText() + " clicked");
//	    fireEvent(new LogoutEvent());
	    History.newItem("loginGxt");
    }
	
    
    
    @UiHandler(value = {"miHelpAboutField","newVwmlProjField"})
	public void onMenuSelection(SelectionEvent<Item> event) {
	    MenuItem item = (MenuItem) event.getSelectedItem();
	    Info.display("Action", "You selected the " + item.getText());
	    if(item.getText().equalsIgnoreCase("about")) {
	    	executeHelpAbout();
	    } else {
	    	executeProjectNew();
	    }
	}	

    
    public void executeHelpAbout() {
		AboutDialogExt d = new AboutDialogExt();
		if (getAssociatedPresenter() != null) {
			
			String user = getAssociatedPresenter().getLoggedAsUser();
			if(user == null) {
				user = "olmel";
			}
			d.setLoggedAsUser(user);	
		} else {
			d.setLoggedAsUser("olmel");	
		}
		d.setSize("350", "270");
		d.showCenter(s_HelpAboutCaption, null);
	}    
    
    
    public void executeProjectNew() {
		NewVwmlProjectDialogExt d = new NewVwmlProjectDialogExt();
		if (getAssociatedPresenter() != null) {
			
			String user = getAssociatedPresenter().getLoggedAsUser();
			if(user == null) {
				user = "olmel";
			}
			d.setLoggedAsUser(user);	
		} else {
			d.setLoggedAsUser("olmel");	
		}
		d.setSize("480", "380");
		d.showCenter(s_newVwmlProjectCaption, null);
	}
    
    
	private void bind() {
		 if (newVwmlProjField != null) {
//			 newVwmlProjField. setScheduledCommand(new NewVwmlProjectCommand(this));
		 }
	}  
  
	
	
	
	public static class NewVwmlProjectCommand implements ScheduledCommand {

		private TopPanel view = null;

		public NewVwmlProjectCommand(TopPanel view) {
			this.view = view;
		}

		public void execute() {
//			if (view.getAssociatedPresenter() != null) {
//				NewVwmlProjectDialog d = new NewVwmlProjectDialog();
//				d.setLoggedAsUser(view.getAssociatedPresenter()
//						.getLoggedAsUser());
//				d.show(s_newVwmlProjectCaption, null, 0, 0);
//			}
		}
	}	
	
  
//  @UiHandler("aboutLink")
//  void onAboutClicked(ClickEvent event) {
//    // When the 'About' item is selected, show the AboutDialog.
//    // Note that showing a dialog box does not block -- execution continues
//    // normally, and the dialog fires an event when it is closed.
//    AboutDialog dlg = new AboutDialog();
//    dlg.show();
//    dlg.center();
//  }

//  @UiHandler("signOutLink")
//  void onSignOutClicked(ClickEvent event) {
//    Window.alert("If this were implemented, you would be signed out now.");
//  }
}
