package com.vw.ide.client.ui.consolespanel;

import com.google.gwt.core.client.GWT;
//import com.google.gwt.sample.mail.client.MailItem;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.form.ListField;
import com.vw.ide.client.presenters.Presenter;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;

public class ConsolesPanelView extends Composite {

	private static class SearchResult {
		
	}
	
	protected class SearchKeyProvider implements ModelKeyProvider<SearchResult> {
		@Override
		public String getKey(SearchResult item) {
			return "";
		}
	}
	
	private static WindowsPanelViewUiBinder uiBinder = GWT
			.create(WindowsPanelViewUiBinder.class);

	interface WindowsPanelViewUiBinder extends 	UiBinder<Widget, ConsolesPanelView>  {
	}

	private AceEditor logAceEditor;
	// all info messages are sent here
	@UiField HTML infoConsole;
	// 'text' out fringe
	@UiField
	SimpleContainer outgoingConsole;
	// 'text' in fringe
	@UiField
	SimpleContainer incomingConsole;

	// 'Search' related data
	private SearchKeyProvider searchKeyProvider = new SearchKeyProvider();
	@UiField(provided = true)
	ListStore<SearchResult> searchStore = new ListStore<SearchResult>(searchKeyProvider);
	@UiField(provided = true)
	ListField<SearchResult, String> searchResult;
	
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

	protected Presenter getAssociatedPresenter() {
		return this.presenter;
	}

	@UiFactory
	protected ValueProvider<SearchResult, String> searchCreateValueProvider() {
		return new ValueProvider<SearchResult, String>() {

			@Override
			public String getValue(SearchResult object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setValue(SearchResult object, String value) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getPath() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
	
	private Widget constructUi() {
		ListView<SearchResult, String> lw = new ListView<SearchResult, String>(searchStore, searchCreateValueProvider());
		searchResult = new ListField<SearchResult, String>(lw);
	    widget = uiBinder.createAndBindUi(this);
	    widget.addStyleName("margin-10");
	    constructEditor();
	    return widget;
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
   
   public void appendLog(String logContent) {
   }
}
