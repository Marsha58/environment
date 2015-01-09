package com.vw.ide.client.ui.consolespanel.tab;

import com.sencha.gxt.data.shared.ListStore;
import com.vw.ide.shared.servlet.processor.command.sandr.SearchAndReplaceResult;

public class SearchConsoleTab extends ConsoleTab {
	
	private ListStore<SearchAndReplaceResult> searchResult = null;
	
	public SearchConsoleTab() {
		
	}

	public ListStore<SearchAndReplaceResult> getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(ListStore<SearchAndReplaceResult> searchResult) {
		this.searchResult = searchResult;
	}

	public void add(SearchAndReplaceResult r) {
		if (searchResult != null) {
			searchResult.add(r);
		}
	}
}
