package com.vw.ide.client.devboardext;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.event.uiflow.AceColorThemeChangedEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.ui.editorpanel.EditorPanel;
import com.vw.ide.client.ui.projectpanel.ProjectPanel;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.client.ui.toppanel.TopPanel;
import com.vw.ide.client.ui.toppanel.TopPanel.Theme;
import com.vw.ide.client.ui.windowspanel.WindowsPanelView;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.userstate.UserStateInfo;

public class DevelopmentBoard extends ResizeComposite implements IsWidget, PresenterViewerLink {

	private static DevelopmentBoardUiBinder uiBinder = GWT.create(DevelopmentBoardUiBinder.class);

	@UiField
	SimpleContainer mainContainer = new SimpleContainer();
	
	@UiField(provided = true)
	MarginData outerData = new MarginData(1);
	@UiField(provided = true)
	BorderLayoutData northData = new BorderLayoutData(83);
	@UiField(provided = true)
	BorderLayoutData westData = new BorderLayoutData(200);
	@UiField(provided = true)
	MarginData centerData = new MarginData();
	@UiField(provided = true)
	BorderLayoutData eastData = new BorderLayoutData(150);
	@UiField(provided = true)
	BorderLayoutData southData = new BorderLayoutData(120);
	@UiField
	BorderLayoutContainer con;
	
	@UiField ContentPanel editorContentPanel;

	@UiField
	TopPanel topPanel;
	@UiField
	ProjectPanel projectPanel;
	@UiField
	EditorPanel editor;
	@UiField
	WindowsPanelView windows;

	private Presenter presenter = null;

	interface DevelopmentBoardUiBinder extends UiBinder<SimpleContainer, DevelopmentBoard> {
	}
	
	public DevelopmentBoard() {
		ListStore<Theme> colors = new ListStore<Theme>(
			new ModelKeyProvider<Theme>() {
				@Override
				public String getKey(Theme item) {
					return item.name();
				}
		});
		
		colors.addAll(Arrays.asList(Theme.values()));

		final SimpleContainer con = new SimpleContainer();
		con.getElement().getStyle().setMargin(3, Unit.PX);
		con.setResize(false);

		ComboBox<Theme> combo = new ComboBox<Theme>(colors,
				new StringLabelProvider<Theme>());
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
					Window.Location.assign(GWT.getHostPageBaseURL()
							+ "Vwide.html" + Window.Location.getHash());
					northData.setSize(74);
					break;
				case GRAY:
					Window.Location.assign(GWT.getHostPageBaseURL()
							+ "Vwide-gray.html" + Window.Location.getHash());
					northData.setSize(76);
					break;
				case NEPTUNE:
					Window.Location.assign(GWT.getHostPageBaseURL()
							+ "Vwide-neptune.html" + Window.Location.getHash());
					northData.setSize(94);
					break;
				default:
					assert false : "Unsupported theme enum";
				}
			}
		});
		con.add(combo);
		if (Theme.BLUE.isActive()) {
			northData.setSize(74);
		} else if (Theme.GRAY.isActive()) {
			northData.setSize(76);
		} else {
			northData.setSize(94);
		}		
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DevelopmentBoard(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DevelopmentBoard(Integer size) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setText(String text) {
	}

	public String getText() {
		return null;
	}

	private Widget widget;

	@Override
	public Widget asWidget() {
		if (widget == null) {
			northData.setMargins(new Margins(5));
			westData.setMargins(new Margins(0, 5, 0, 5));
			westData.setCollapsible(true);
			westData.setSplit(true);
			eastData.setMargins(new Margins(0, 5, 0, 5));
			eastData.setCollapsible(true);
			eastData.setSplit(true);
			southData.setMargins(new Margins(5));
			southData.setCollapsible(true);
			southData.setSplit(true);
			widget = uiBinder.createAndBindUi(this);
		}
		associatePresenterWithSubpanels(presenter);
		requestUserProjects();
		return widget;
	}

	public void associatePresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	protected Presenter getAssociatedPresenter() {
		return presenter;
	}
	
	public FileSheet getActiveFileSheetWidget() {
		return (FileSheet)editor.getTabPanel().getActiveWidget();
	}
	
	public void markFileAsEdited(ProjectItemInfo itemInfo, boolean edited) {
		if (!itemInfo.isEdited()) {
			itemInfo.setEdited(edited);
			editor.setFileEditedState(itemInfo.getFileSheet(), edited);
			topPanel.enableSaveFile(edited);
			if (edited) {
				topPanel.enableSaveAll(edited);
			}
			else {
				boolean saveAllEnabled = false;
				for(FileItemInfo fi : itemInfo.getProjectDescription().getProjectFiles()) {
					if (getProjectPanel().isEdited(fi)) {
						saveAllEnabled = true;
						break;
					}
				}
				topPanel.enableSaveAll(saveAllEnabled);
			}
		}
	}
	
	public void deleteFileItemFromScrollMenu(ProjectItemInfo itemInfo) {
		topPanel.delItemFromScrollMenu(itemInfo);	
	}

	public void addNewFileTabItem(ProjectItemInfo itemInfo) {
		TabItemConfig tabItemConfig = new TabItemConfig(itemInfo.getAssociatedData().getName());
		tabItemConfig.setClosable(true);
		FileSheet newFileSheet = new FileSheet(presenter, itemInfo);
		newFileSheet.constructEditor(itemInfo.getAssociatedData().getContent(),
									FileItemInfo.recognizeFileType(itemInfo.getAssociatedData().getName()));
		editor.getTabPanel().add(newFileSheet, tabItemConfig);
		itemInfo.setFileSheet(newFileSheet);
		scrollToTab(itemInfo);
		addFileItemToScrollMenu(itemInfo);
		itemInfo.setAlreadyOpened(true);
	}

	public void scrollToTab(ProjectItemInfo itemInfo) {
		if (itemInfo.getFileSheet() != null) {
			editor.getTabPanel().setActiveWidget(itemInfo.getFileSheet());
			editor.getTabPanel().scrollToTab(itemInfo.getFileSheet(), true);
		}
	}

	public void renameFileTabItem(ProjectItemInfo itemInfo, FileItemInfo toRename) {
		getProjectPanel().renameTreeBranchView(itemInfo, toRename);
		updateTabName(itemInfo);
	}
	
	public void updateTabName(ProjectItemInfo itemInfo) {
		FileSheet updatedFileSheet = itemInfo.getFileSheet();
		if (updatedFileSheet != null) {
			TabItemConfig conf = editor.getTabPanel().getConfig(updatedFileSheet);
			conf.setText(itemInfo.getAssociatedData().getName());
			editor.getTabPanel().update(updatedFileSheet, conf);
			deleteFileItemFromScrollMenu(itemInfo);
			addFileItemToScrollMenu(itemInfo);
		}
	}
	
	public void afterClosingTabName(ProjectItemInfo itemInfo) {
		deleteFileItemFromScrollMenu(itemInfo);
		if (editor.getTabPanel().getWidgetCount() == 0) {
			setTextForEditorContentPanel(null);
			getProjectPanel().selectParentOf(itemInfo);
		}
	}
	
	public void addFileItemToScrollMenu(ProjectItemInfo itemInfo) {
		topPanel.addItemToScrollMenu(itemInfo);
	}
	
	public void setTextForEditorContentPanel(String text) {
		editorContentPanel.setHeadingText(text);
	}
	
	public ProjectPanel getProjectPanel() {
		return projectPanel;
	}
	
	public void requestUserProjects() {
		projectPanel.requestUserProjects(FlowController.getLoggedAsUser());
	}

	public void updateEditorPanelTheme(AceColorThemeChangedEvent event) {
		for (int i = 0; i < editor.getTabPanel().getWidgetCount(); i++) {
			FileSheet curFileSheet = (FileSheet)editor.getTabPanel().getWidget(i);
			curFileSheet.getAceEditor().setTheme(event.getEvent().getSelectedItem());
		}
	}
	
	public void restoreView(UserStateInfo userState) {
		getProjectPanel().restoreView(userState);
	}
	
	public void addProjectItemAndSelect(ProjectItemInfo parent, FileItemInfo toAdd) {
		getProjectPanel().buildTreeBranchView(parent, toAdd);
		getProjectPanel().selectByKey(toAdd.generateKey());
	}

	public void deleteProjectItem(ProjectItemInfo itemInfo) {
		getProjectPanel().deleteBranchView(itemInfo);
		deleteFileItemFromScrollMenu(itemInfo);
		editor.getTabPanel().remove(itemInfo.getFileSheet());
		if (editor.getTabPanel().getWidgetCount() == 0) {
			setTextForEditorContentPanel(null);
			getProjectPanel().selectParentOf(itemInfo);
		}
	}
	
	public void appendLog(String log) {
		windows.appendLog(log);		
	}
	
	protected void associatePresenterWithSubpanels(Presenter presenter) {
		topPanel.associatePresenter(presenter);
		editor.associatePresenter(presenter);
		windows.associatePresenter(presenter);
		projectPanel.associatePresenter(presenter);
	}
}
