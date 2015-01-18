package com.vw.ide.client.ui.consolespanel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
//import com.google.gwt.sample.mail.client.MailItem;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;
import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.ui.consolespanel.tab.CompilationErrorConsoleTab;
import com.vw.ide.client.ui.consolespanel.tab.CompilationErrorResultTabProperties;
import com.vw.ide.client.ui.consolespanel.tab.InfoConsoleTab;
import com.vw.ide.client.ui.consolespanel.tab.SearchConsoleTab;
import com.vw.ide.client.ui.consolespanel.tab.SearchResultTabProperties;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.shared.servlet.processor.dto.compiler.CompilationErrorResult;
import com.vw.ide.shared.servlet.processor.dto.sandr.SearchAndReplaceResult;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;

public class ConsolesPanelView extends Composite {

	public static enum Tab {
		INFO,
		ERROR,
		SEARCH,
		COMMUNICATION
	};
	
	private static final SearchResultTabProperties searchProps = GWT.create(SearchResultTabProperties.class);
	private static final CompilationErrorResultTabProperties compilatonErrorProps = GWT.create(CompilationErrorResultTabProperties.class);

	private static class GridItemHasPosition {
		
		protected void selectPosition(DevelopmentBoardPresenter owner, FileItemInfo fi, int line, int pos) {
			DevelopmentBoard devBoard = owner.getView();
			ProjectItemInfo pi = devBoard.getProjectPanel().getProjectItemByFileItemInfo(fi);
			if (pi != null) {
				pi.setLastLine(line);
				pi.setLastPos(pos);
				devBoard.getProjectPanel().select(pi);
				if (pi.isAlreadyOpened() && pi.getLastLine() != -1 && pi.getLastPos() != -1) {
					pi.getFileSheet().setCursorPosition(pi.getLastLine(), pi.getLastPos());
					pi.setLastLine(-1);
					pi.setLastPos(-1);
				}
			}
		}
	}
	
	private static class SearchSelectionHandler extends GridItemHasPosition implements SelectionHandler<SearchAndReplaceResult> {

		private ConsolesPanelView owner;
		
		public SearchSelectionHandler(ConsolesPanelView owner) {
			this.owner = owner;
		}
		
		@Override
		public void onSelection(SelectionEvent<SearchAndReplaceResult> event) {
			SearchAndReplaceResult r = event.getSelectedItem();
			selectPosition((DevelopmentBoardPresenter)owner.getAssociatedPresenter(), r.getFileInfo(), r.getLine().intValue() - 1, r.getPosition().intValue());
		}
	}

	private static class CompilationErrorSelectionHandler extends GridItemHasPosition implements SelectionHandler<CompilationErrorResult> {

		private ConsolesPanelView owner;
		
		public CompilationErrorSelectionHandler(ConsolesPanelView owner) {
			this.owner = owner;
		}
		
		@Override
		public void onSelection(SelectionEvent<CompilationErrorResult> event) {
			CompilationErrorResult r = event.getSelectedItem();
			selectPosition((DevelopmentBoardPresenter)owner.getAssociatedPresenter(), r.getFileInfo(), r.getLine().intValue() - 1, r.getPosition().intValue());
		}
	}
	
	private static WindowsPanelViewUiBinder uiBinder = GWT
			.create(WindowsPanelViewUiBinder.class);

	interface WindowsPanelViewUiBinder extends 	UiBinder<Widget, ConsolesPanelView>  {
	}

	@UiField
	TabPanel consolesTab;
	
	// all info messages are sent here
	@UiField
	ContentPanel infoConsole;
	// 'text' out fringe
	@UiField
	SimpleContainer outgoingConsole;
	// 'text' in fringe
	@UiField
	SimpleContainer incomingConsole;

	// 'Search' related data
	@UiField(provided = true)
	ColumnModel<SearchAndReplaceResult> searchColumnModel;
	@UiField(provided = true)
	ListStore<SearchAndReplaceResult> searchStore;
	@UiField
	GridView<SearchAndReplaceResult> searchGridView;
	@UiField
	Grid<SearchAndReplaceResult> searchResult;
	CheckBoxSelectionModel<SearchAndReplaceResult> searchConsoleSelectionMode = new CheckBoxSelectionModel<SearchAndReplaceResult>(new IdentityValueProvider<SearchAndReplaceResult>());
    Menu consoleSearchAndReplaceMenu = new Menu();
    
    // 'Error' related data
	@UiField(provided = true)
	ColumnModel<CompilationErrorResult> compilationErrorColumnModel;
	@UiField(provided = true)
	ListStore<CompilationErrorResult> compilationErrorStore;
	@UiField
	GridView<CompilationErrorResult> compilationErrorGridView;
	@UiField
	Grid<CompilationErrorResult> compilationErrorResult;
    Menu consoleCompilationErrorMenu = new Menu();
    
	private AceEditor incomingPanel = constructEditor();
	private AceEditor infoPanel = constructEditor();

	// consoles
	private SearchConsoleTab searchConsoleTab = new SearchConsoleTab();
	private InfoConsoleTab infoConsoleTab = new InfoConsoleTab();
	private CompilationErrorConsoleTab compilationErrorConsoleTab = new CompilationErrorConsoleTab();
	
	private Presenter presenter = null;
	
	private Widget widget;	
	
	public ConsolesPanelView() {
		if (widget == null) {
	        widget = constructUi();
		}		
		initWidget(widget);
	}

	public void associatePresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public SearchConsoleTab getSearchConsoleTab() {
		return searchConsoleTab;
	}

	public InfoConsoleTab getInfoConsoleTab() {
		return infoConsoleTab;
	}
	
	public CompilationErrorConsoleTab getCompilationErrorConsoleTab() {
		return compilationErrorConsoleTab;
	}

	public Menu getConsoleSearchAndReplaceMenu() {
		return consoleSearchAndReplaceMenu;
	}

	public void setConsoleSearchAndReplaceMenu(Menu consoleSearchAndReplaceMenu) {
		this.consoleSearchAndReplaceMenu = consoleSearchAndReplaceMenu;
	}

	public void scrollToTab(Tab tab) {
		Widget w = consolesTab.getWidget(tab.ordinal());
		consolesTab.setActiveWidget(w);
		consolesTab.scrollToTab(w, true);
	}

	protected Presenter getAssociatedPresenter() {
		return this.presenter;
	}

	private Widget constructUi() {
		initCompilationErrorTabControl();
		initSearchTabControl();
		widget = uiBinder.createAndBindUi(this);
	    widget.addStyleName("margin-10");
		postInitInfoTabControl();
	    postInitSearchTabControl();
		postInitCommunicationTabControl();
		postInitCompilationErrorTabControl();
		return widget;
	}	
	
	private void postInitInfoTabControl() {
		infoConsoleTab.setOwner(this);
		infoConsoleTab.setControl(infoPanel);
		infoPanel.setReadOnly(true);
		infoConsole.add(infoPanel, new MarginData(1, 1, 1, 1));
	}
	
	private void postInitCommunicationTabControl() {
		MarginData layoutData = new MarginData(1, 1, 1, 1);
		incomingConsole.add(incomingPanel, layoutData);
	}
	
	private void initCompilationErrorTabControl() {
		compilationErrorConsoleTab.setOwner(this);
		ColumnConfig<CompilationErrorResult, String> projectCol = new ColumnConfig<CompilationErrorResult, String>(compilatonErrorProps.projectName(), 150, "Project");
		ColumnConfig<CompilationErrorResult, String> fileCol = new ColumnConfig<CompilationErrorResult, String>(compilatonErrorProps.file(), 450, "File");
		ColumnConfig<CompilationErrorResult, String> placeCol = new ColumnConfig<CompilationErrorResult, String>(compilatonErrorProps.place(), 50, "Place");
		ColumnConfig<CompilationErrorResult, String> causeCol = new ColumnConfig<CompilationErrorResult, String>(compilatonErrorProps.cause(), 250, "Cause");
		List<ColumnConfig<CompilationErrorResult, ?>> columns = new ArrayList<ColumnConfig<CompilationErrorResult, ?>>();
		columns.add(projectCol);
		columns.add(fileCol);
		columns.add(placeCol);
		columns.add(causeCol);
		compilationErrorColumnModel = new ColumnModel<CompilationErrorResult>(columns);
		compilationErrorStore = new ListStore<CompilationErrorResult>(compilatonErrorProps.key());
		compilationErrorConsoleTab.setCompilationErrorResult(compilationErrorStore);
	}

	private void postInitCompilationErrorTabControl() {
		compilationErrorResult.getSelectionModel().addSelectionHandler(new CompilationErrorSelectionHandler(this));
	}
	
	private void initSearchTabControl() {
	    searchConsoleTab.setOwner(this);
		ColumnConfig<SearchAndReplaceResult, String> projectCol = new ColumnConfig<SearchAndReplaceResult, String>(searchProps.projectName(), 150, "Project");
		ColumnConfig<SearchAndReplaceResult, String> fileCol = new ColumnConfig<SearchAndReplaceResult, String>(searchProps.file(), 450, "File");
		ColumnConfig<SearchAndReplaceResult, String> placeCol = new ColumnConfig<SearchAndReplaceResult, String>(searchProps.place(), 50, "Place");
		ColumnConfig<SearchAndReplaceResult, String> searchCol = new ColumnConfig<SearchAndReplaceResult, String>(searchProps.search(), 250, "Search");
		ColumnConfig<SearchAndReplaceResult, String> replaceCol = new ColumnConfig<SearchAndReplaceResult, String>(searchProps.replace(), 250, "Replace");
		List<ColumnConfig<SearchAndReplaceResult, ?>> columns = new ArrayList<ColumnConfig<SearchAndReplaceResult, ?>>();
		columns.add(searchConsoleSelectionMode.getColumn());
		columns.add(projectCol);
		columns.add(fileCol);
		columns.add(placeCol);
		columns.add(searchCol);
		columns.add(replaceCol);
		searchColumnModel = new ColumnModel<SearchAndReplaceResult>(columns);
		searchStore	= new ListStore<SearchAndReplaceResult>(searchProps.key());
		searchConsoleTab.setSearchResult(searchStore);
		searchConsoleTab.setSearchConsoleSelectionMode(searchConsoleSelectionMode);
	}

	private void postInitSearchTabControl() {
		searchConsoleSelectionMode.setShowSelectAll(true);
		searchResult.setSelectionModel(searchConsoleSelectionMode);
	    searchResult.getSelectionModel().addSelectionHandler(new SearchSelectionHandler(this));
	    MenuItem selectAllItem = new MenuItem();
	    selectAllItem.setItemId(SearchConsoleTab.selectAll);
	    selectAllItem.setText("Select all");
	    MenuItem unselectAllItem = new MenuItem();
	    unselectAllItem.setItemId(SearchConsoleTab.unselectAll);
	    unselectAllItem.setText("Deselect all");
	    MenuItem applyReplaceItem = new MenuItem();
	    applyReplaceItem.setItemId(SearchConsoleTab.applyReplace);
	    applyReplaceItem.setText("Replace");
	    MenuItem clearAllItems = new MenuItem();
	    clearAllItems.setItemId(SearchConsoleTab.clearAll);
	    clearAllItems.setText("Clear");
	    MenuItem[] items = {selectAllItem, unselectAllItem, null, applyReplaceItem, null, clearAllItems};
	    for(MenuItem item : items) {
	    	if (item == null) {
	    		consoleSearchAndReplaceMenu.add(new SeparatorMenuItem());
	    		continue;
	    	}
	    	item.setEnabled(false);
	    	consoleSearchAndReplaceMenu.add(item);
	    }
	    consoleSearchAndReplaceMenu.addBeforeShowHandler(new BeforeShowHandler() {
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				getSearchConsoleTab().enableSelectMenuItem(SearchConsoleTab.clearAll, false);
				getSearchConsoleTab().enableSelectMenuItem(SearchConsoleTab.selectAll, false);
				getSearchConsoleTab().enableSelectMenuItem(SearchConsoleTab.unselectAll, false);
				getSearchConsoleTab().enableSelectMenuItem(SearchConsoleTab.applyReplace, false);
				if (searchStore.size() != 0) {
					getSearchConsoleTab().enableSelectMenuItem(SearchConsoleTab.clearAll, true);
					getSearchConsoleTab().enableSelectMenuItem(SearchConsoleTab.selectAll, true);
					if (searchConsoleSelectionMode.getSelectedItems().size() > 0) {
						getSearchConsoleTab().enableSelectMenuItem(SearchConsoleTab.unselectAll, true);
						if (getSearchConsoleTab().isReplaceAllowed((DevelopmentBoardPresenter)presenter)) {
							getSearchConsoleTab().enableSelectMenuItem(SearchConsoleTab.applyReplace, true);
						}
					}
				}
			}
	    });
	    consoleSearchAndReplaceMenu.addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				String itemId = event.getSelectedItem().getItemId();
				switch(itemId) {
					case SearchConsoleTab.clearAll:
						searchConsoleTab.clear();
					break;
					case SearchConsoleTab.selectAll:
						searchConsoleTab.selectAll();
					break;
					case SearchConsoleTab.unselectAll:
						searchConsoleTab.deselectAll();
					break;
					case SearchConsoleTab.applyReplace:
						searchConsoleTab.applyReplace((DevelopmentBoardPresenter)presenter);
					break;
				}
			}
	    }
	    );
	    searchResult.setContextMenu(consoleSearchAndReplaceMenu);
	}
	
	private AceEditor constructEditor() {
		AceEditor editor = new AceEditor();
		editor.setWidth("100%");
		editor.setHeight("100%");
		editor.startEditor(); // must be called before calling
								// setTheme/setMode/etc.
		editor.setTheme(AceEditorTheme.CHROME);
		editor.setText("");
		editor.setMode(AceEditorMode.TEXT);
		return editor;
	}   
}
