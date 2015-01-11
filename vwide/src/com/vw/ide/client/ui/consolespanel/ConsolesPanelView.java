package com.vw.ide.client.ui.consolespanel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
//import com.google.gwt.sample.mail.client.MailItem;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.ui.consolespanel.tab.SearchConsoleTab;
import com.vw.ide.client.ui.consolespanel.tab.SearchResultTabProperties;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.shared.servlet.processor.command.sandr.SearchAndReplaceResult;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;

public class ConsolesPanelView extends Composite {

	public static enum Tab {
		INFO,
		SEARCH,
		COMMUNICATION
	};
	
	private static final SearchResultTabProperties searchProps = GWT.create(SearchResultTabProperties.class);
	
	private static class SearchSelectionHandler implements SelectionHandler<SearchAndReplaceResult> {

		private ConsolesPanelView owner;
		
		public SearchSelectionHandler(ConsolesPanelView owner) {
			this.owner = owner;
		}
		
		@Override
		public void onSelection(SelectionEvent<SearchAndReplaceResult> event) {
			SearchAndReplaceResult r = event.getSelectedItem();
			DevelopmentBoard devBoard = ((DevelopmentBoardPresenter)owner.getAssociatedPresenter()).getView();
			ProjectItemInfo pi = devBoard.getProjectPanel().getProjectItemByFileItemInfo(r.getFileInfo());
			if (pi != null) {
				pi.setLastLine(r.getLine().intValue() - 1);
				pi.setLastPos(r.getPosition().intValue());
				devBoard.getProjectPanel().select(pi);
				if (pi.isAlreadyOpened() && pi.getLastLine() != -1 && pi.getLastPos() != -1) {
					pi.getFileSheet().setCursorPosition(pi.getLastLine(), pi.getLastPos());
					pi.setLastLine(-1);
					pi.setLastPos(-1);
				}
			}
		}
	}
	
	private static WindowsPanelViewUiBinder uiBinder = GWT
			.create(WindowsPanelViewUiBinder.class);

	interface WindowsPanelViewUiBinder extends 	UiBinder<Widget, ConsolesPanelView>  {
	}

	private AceEditor logAceEditor;
	
	@UiField
	TabPanel consolesTab;
	
	// all info messages are sent here
	@UiField HTML infoConsole;
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
	
	// consoles
	private SearchConsoleTab searchConsoleTab = new SearchConsoleTab();
	
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

	public void setSearchConsoleTab(SearchConsoleTab searchConsoleTab) {
		this.searchConsoleTab = searchConsoleTab;
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
		initSearchTabControl();
	    widget = uiBinder.createAndBindUi(this);
	    widget.addStyleName("margin-10");
	    constructEditor();
		searchResult.getSelectionModel().addSelectionHandler(new SearchSelectionHandler(this));
	    return widget;
	}	
	
	private void initSearchTabControl() {
		ColumnConfig<SearchAndReplaceResult, String> projectCol = new ColumnConfig<SearchAndReplaceResult, String>(searchProps.projectName(), 150, "Project");
		ColumnConfig<SearchAndReplaceResult, String> fileCol = new ColumnConfig<SearchAndReplaceResult, String>(searchProps.file(), 450, "File");
		ColumnConfig<SearchAndReplaceResult, String> placeCol = new ColumnConfig<SearchAndReplaceResult, String>(searchProps.place(), 50, "Place");
		ColumnConfig<SearchAndReplaceResult, String> searchCol = new ColumnConfig<SearchAndReplaceResult, String>(searchProps.search(), 250, "Search");
		ColumnConfig<SearchAndReplaceResult, String> replaceCol = new ColumnConfig<SearchAndReplaceResult, String>(searchProps.replace(), 250, "Replace");
		List<ColumnConfig<SearchAndReplaceResult, ?>> columns = new ArrayList<ColumnConfig<SearchAndReplaceResult, ?>>();
		columns.add(projectCol);
		columns.add(fileCol);
		columns.add(placeCol);
		columns.add(searchCol);
		columns.add(replaceCol);
		searchColumnModel = new ColumnModel<SearchAndReplaceResult>(columns);
		searchStore	= new ListStore<SearchAndReplaceResult>(searchProps.key());
		searchConsoleTab.setSearchResult(searchStore);
	}
	
	private void constructEditor() {
	   logAceEditor = new AceEditor();
	   logAceEditor.setWidth("100%");
	   logAceEditor.setHeight("100%");
	   logAceEditor.startEditor(); // must be called before calling
									// setTheme/setMode/etc.
	   logAceEditor.setTheme(AceEditorTheme.CHROME);
	   logAceEditor.setText("");
	   logAceEditor.setMode(AceEditorMode.TEXT);	
	   MarginData layoutData = new MarginData(1, 1, 1, 1);
	   incomingConsole.add(logAceEditor, layoutData); 
   }   
}
