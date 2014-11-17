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

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
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
import com.sencha.gxt.state.client.StateManager;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.dialog.about.AboutDialogExt;
import com.vw.ide.client.dialog.newvwmlproj.NewVwmlProjectDialogExt;
import com.vw.ide.client.event.uiflow.AceColorThemeChangedEvent;
import com.vw.ide.client.event.uiflow.LogoutEvent;
import com.vw.ide.client.event.uiflow.ProjectMenuEvent;
import com.vw.ide.client.event.uiflow.SaveFileEvent;
import com.vw.ide.client.event.uiflow.fringes.OpenDialogForSelectFringeEvent;
import com.vw.ide.client.event.uiflow.fringes.OpenFringeManagerEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.ui.editorpanel.FileSheet;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.sencha.gxt.widget.core.client.button.TextButton;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;

/**
 * The top panel, which contains the 'welcome' message and various links.
 */
public class TopPanel extends Composite implements PresenterViewerLink {

	interface Binder extends UiBinder<Widget, TopPanel> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private Presenter presenter = null;

	private static String s_HelpAboutCaption = "About";
	private static String s_newVwmlProjectCaption = "New VWML project";
	
	private AceEditorTheme storedAceEditorTheme = AceEditorTheme.VWML_IDLE_FINGERS;

	public ComboBox<AceEditorTheme> comboATh;

	// @UiField MenuItem logoutField;
	@UiField MenuItem newVwmlProjField;
	@UiField TextButton bnNewVwmlProjField;
	@UiField TextButton bnSaveSelectedFile;
	@UiField MenuItem openFringeManager;
	@UiField MenuItem miHelpAboutField;
	// @UiField FlowPanel panelEditor;
	@UiField SimpleContainer comboAceEditorPlaceCont;
	@UiField SimpleContainer userNamePlaceCont;
	@UiField SimpleContainer comboPlaceCont;
	// @UiField SimpleContainer outer;
	@UiField Label userName;
	@UiField TextButton userLogout;
	@UiField Menu scrollMenu;

	@UiField (provided = true) BorderLayoutData northDataTP = new BorderLayoutData(21);
	@UiField (provided = true) MarginData centerDataTP = new MarginData();
	@UiField (provided = true) BorderLayoutData eastDataTP = new BorderLayoutData(630);
	@UiField BorderLayoutContainer conTP;

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

		// centerData.setMargins(new Margins(2, 2, 0, 1));
		eastDataTP.setMargins(new Margins(0, 0, 0, 0));
		// southData.setMargins(new Margins(1, 1, 1, 1));
		// centerData.setMargins(new Margins(0, 5, 0, 5));

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
		// comboATh.setEmptyText("Select a theme...");

		

		
		StateManager.get().getProvider().getValue("storedAceEditorTheme", new Callback<String, Throwable>() {
			
			@Override
			public void onSuccess(String result) {
				try {
					if (result != null) {
						storedAceEditorTheme = AceEditorTheme.fromString(result);
						comboATh.setValue(storedAceEditorTheme);						
					}
				} catch (Exception e) {
					comboATh.setValue(AceEditorTheme.VWML_IDLE_FINGERS);
				} 
			}
			
			@Override
			public void onFailure(Throwable reason) {
				comboATh.setValue(storedAceEditorTheme);
			}
		});
		
		
		
		comboATh.addSelectionHandler(new SelectionHandler<AceEditorTheme>() {
			@Override
			public void onSelection(SelectionEvent<AceEditorTheme> event) {
				// editor1.setTheme(event.getSelectedItem());
				StateManager.get().getProvider().setValue("storedAceEditorTheme", event.getSelectedItem().getName());
				presenter.fireEvent(new AceColorThemeChangedEvent(event));

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
		// combo.getElement().getStyle().setFloat(Float.RIGHT);
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

				((DevelopmentBoardPresenter) getAssociatedPresenter()).getProjectPanel().requestDirContent(null);
			}

		});

		comboPlaceCont.add(combo);

		scrollMenu.addBeforeSelectionHandler(new BeforeSelectionHandler<Item>() {

			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Item> event) {
				if (event.getItem().getData("fileId") != null) {
					Long fileId = event.getItem().getData("fileId");
					FileSheet currentTab = (FileSheet) ((DevelopmentBoardPresenter) presenter).getProjectManager().getAssociatedTabWidget(fileId);
					((DevelopmentBoardPresenter) presenter).scrollToTab(currentTab, true);
				}

			}
		});

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

	@UiHandler(value = { "miHelpAboutField", "newVwmlProjField", "openFringeManager","insertFringeInFile" })
	public void onMenuSelection(SelectionEvent<Item> event) {
		MenuItem item = (MenuItem) event.getSelectedItem();
		if (item.getItemId().equalsIgnoreCase("idHelpAbout")) {
			executeHelpAbout();
		} if (item.getItemId().equalsIgnoreCase("idNewVwmlProjField")) {
			presenter.fireEvent(new ProjectMenuEvent("idNewProject"));
		} if (item.getItemId().equalsIgnoreCase("idOpenFringeManager")) {
			presenter.fireEvent(new OpenFringeManagerEvent("idOpenFringeManager"));
		} if (item.getItemId().equalsIgnoreCase("idInsertFringeInFile")) {
			presenter.fireEvent(new OpenDialogForSelectFringeEvent("idInsertFringeInFile"));
		} 
	}

	@UiHandler({ "userLogout", "bnNewVwmlProjField", "bnSaveSelectedFile" })
	public void onToolBarrButtonClick(SelectEvent event) {
		Info.display("Click", ((TextButton) event.getSource()).getText() + " clicked");
		// History.newItem("loginGxt");
		String sItemId = ((TextButton) event.getSource()).getItemId();
		switch (sItemId) {
		case "idUserLogout":
			History.newItem("loginGxt");
			break;
		case "idNewVwmlProjField":
			presenter.fireEvent(new ProjectMenuEvent("idNewProject"));
			break;
		case "idSaveSelectedFile":
			presenter.fireEvent(new SaveFileEvent());
			break;
		default:
			break;
		}
	}

	public void executeHelpAbout() {
		AboutDialogExt d = new AboutDialogExt();
		if (getAssociatedPresenter() != null) {

			String user = getAssociatedPresenter().getLoggedAsUser();
			if (user == null) {
				user = "olmel";
			}
			d.setLoggedAsUser(user);
		} else {
			d.setLoggedAsUser("olmel");
		}
		d.setSize("350", "270");
		d.showCenter(s_HelpAboutCaption, null);
	}

	private void bind() {

	}

	public void addItemToScrollMenu(String sFullFileName, Long fileId) {
		String sFileNameWithRelativePath = FileItemInfo.makeRelPathFromAbsolute(presenter.getLoggedAsUser(), sFullFileName) + 
										   Utils.FILE_SEPARATOR +
										   Utils.extractJustFileName(sFullFileName);
		MenuItem mi = new MenuItem(sFileNameWithRelativePath);
		mi.setData("fileId", fileId);
		scrollMenu.add(mi);
	}

	public void delItemFromScrollMenu(Long fileId) {

		for (int i = 0; i < scrollMenu.getWidgetCount(); i++) {
			if (((MenuItem) scrollMenu.getWidget(i)).getData("fileId") == fileId) {
				scrollMenu.remove(scrollMenu.getWidget(i));
				break;
			}
		}
	}

}
