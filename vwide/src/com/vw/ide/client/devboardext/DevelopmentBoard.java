package com.vw.ide.client.devboardext;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
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
import com.vw.ide.client.devboardext.operation.block.AddOperationBlock;
import com.vw.ide.client.devboardext.operation.block.DeleteOperationBlock;
import com.vw.ide.client.devboardext.operation.block.MoveOperationBlock;
import com.vw.ide.client.devboardext.operation.block.RenameOperationBlock;
import com.vw.ide.client.event.uiflow.AceColorThemeChangedEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.service.remote.tracer.TracerServiceBroker;
import com.vw.ide.client.ui.editorpanel.EditorPanel;
import com.vw.ide.client.ui.projectpanel.ProjectPanel;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.client.ui.toppanel.TopPanel;
import com.vw.ide.client.ui.toppanel.TopPanel.Theme;
import com.vw.ide.client.ui.windowspanel.WindowsPanelView;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.userstate.UserStateInfo;

public class DevelopmentBoard extends ResizeComposite implements IsWidget, PresenterViewerLink {

	private static class DevelopmentBoardCloseHandler implements ClosingHandler {

		public DevelopmentBoardCloseHandler() {
		}

		@Override
		public void onWindowClosing(ClosingEvent event) {
			TracerServiceBroker.unregisterBackNotification();
		}
	}
	
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
	private RenameOperationBlock renameOperationBlock = new RenameOperationBlock(this);
	private AddOperationBlock addOperationBlock = new AddOperationBlock(this);
	private DeleteOperationBlock deleteOperationBlock = new DeleteOperationBlock(this);
	private MoveOperationBlock moveOperationBlock = new MoveOperationBlock(this);
	
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
		Window.addWindowClosingHandler(new DevelopmentBoardCloseHandler());
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

	public Presenter getAssociatedPresenter() {
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
				topPanel.enableSaveAll(getProjectPanel().isAnyEditedNotSavedFile(itemInfo.getProjectDescription()));
			}
		}
	}
	
	public void scrollToTab(ProjectItemInfo itemInfo) {
		if (itemInfo.getFileSheet() != null) {
			editor.getTabPanel().setActiveWidget(itemInfo.getFileSheet());
			editor.getTabPanel().scrollToTab(itemInfo.getFileSheet(), true);
		}
	}

	public void afterClosingTabName(ProjectItemInfo itemInfo) {
		getTopPanel().delItemFromScrollMenu(itemInfo);
		if (editor.getTabPanel().getWidgetCount() == 0) {
			setTextForEditorContentPanel(null);
			getProjectPanel().selectParentOf(itemInfo);
		}
	}

	public void updateFileItemTabName(ProjectItemInfo itemInfo) {
		FileSheet updatedFileSheet = itemInfo.getFileSheet();
		if (updatedFileSheet != null) {
			TabItemConfig conf = editor.getTabPanel().getConfig(updatedFileSheet);
			conf.setText(itemInfo.getAssociatedData().getName());
			getEditorPanel().getTabPanel().update(updatedFileSheet, conf);
			getTopPanel().delItemFromScrollMenu(itemInfo);
			getTopPanel().addItemToScrollMenu(itemInfo);
		}
	}
	
	public void setTextForEditorContentPanel(String text) {
		editorContentPanel.setHeadingText(text);
	}
	
	public ProjectPanel getProjectPanel() {
		return projectPanel;
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
	
	public TopPanel getTopPanel() {
		return topPanel;
	}
	
	public EditorPanel getEditorPanel() {
		return editor;
	}
	
	public RenameOperationBlock getRenameOperationBlock() {
		return renameOperationBlock;
	}
	
	public AddOperationBlock getAddOperationBlock() {
		return addOperationBlock;
	}

	public DeleteOperationBlock getDeleteOperationBlock() {
		return deleteOperationBlock;
	}
	
	public MoveOperationBlock getMoveOperationBlock() {
		return moveOperationBlock;
	}
	
	public void appendLog(String log) {
		windows.appendLog(log);		
	}

	public List<FileItemInfo> getUpdatedListOfFiles(ProjectDescription projectDescription) {
		return getProjectPanel().getActualListOfFiles(projectDescription);
	}

	protected void requestUserProjects() {
		projectPanel.requestUserProjects(FlowController.getLoggedAsUser());
	}
	
	protected void associatePresenterWithSubpanels(Presenter presenter) {
		topPanel.associatePresenter(presenter);
		editor.associatePresenter(presenter);
		windows.associatePresenter(presenter);
		projectPanel.associatePresenter(presenter);
	}
}
