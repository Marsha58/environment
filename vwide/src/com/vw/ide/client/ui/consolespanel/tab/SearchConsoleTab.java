package com.vw.ide.client.ui.consolespanel.tab;

import java.util.ArrayList;
import java.util.List;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.dialog.searchandreplace.SearchAndReplaceDialog;
import com.vw.ide.client.ui.consolespanel.ConsolesPanelView;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.shared.servlet.processor.dto.sandr.SearchAndReplaceBundle;
import com.vw.ide.shared.servlet.processor.dto.sandr.SearchAndReplaceResult;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

public class SearchConsoleTab extends ConsoleTab {
	// context menu
	public static final String selectAll = "selectAll";
	public static final String unselectAll = "unselectAll";
	public static final String applyReplace = "applyReplace";
	public static final String clearAll = "clearAll";
	
	private ConsolesPanelView owner = null;
	private ListStore<SearchAndReplaceResult> searchResult = null;
	private CheckBoxSelectionModel<SearchAndReplaceResult> searchConsoleSelectionMode;
	
	public SearchConsoleTab() {
		
	}

	public ConsolesPanelView getOwner() {
		return owner;
	}

	public void setOwner(ConsolesPanelView owner) {
		this.owner = owner;
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
	
	public void removeByFileItemInfo(FileItemInfo fi) {
		if (searchResult == null || searchResult.size() == 0 || fi == null) {
			return;
		}
		int i = 0;
		while(i != searchResult.size()) {
			for(i = 0; i < searchResult.size(); i++) {
				SearchAndReplaceResult r = searchResult.get(i);
				if (r.getFileInfo() != null && r.getFileInfo().equals(fi)) {
					searchResult.remove(r);
					break;
				}
			}
		}
	}
	
	public void removeByProjectName(String projectName) {
		if (searchResult == null || searchResult.size() == 0 || projectName == null) {
			return;
		}
		int i = 0;
		while(i != searchResult.size()) {
			for(i = 0; i < searchResult.size(); i++) {
				SearchAndReplaceResult r = searchResult.get(i);
				if (r.getProjectName() != null && r.getProjectName().equals(projectName)) {
					searchResult.remove(r);
					break;
				}
			}
		}
	}

	public void renameByFileItemInfo(FileItemInfo fi, FileItemInfo newFi) {
		if (searchResult == null || searchResult.size() == 0 || fi == null) {
			return;
		}
		for(int i = 0; i < searchResult.size(); i++) {
			SearchAndReplaceResult r = searchResult.get(i);
			if (r.getFileInfo() != null && r.getFileInfo().equals(fi)) {
				searchResult.remove(i);
				r.setFileInfo(newFi);
				searchResult.add(r);
			}
		}
	}
	
	public void clear() {
		if (searchResult != null) {
			searchResult.clear();
		}
	}
	
	public void selectAll() {
		searchConsoleSelectionMode.selectAll();
	}

	public void deselectAll() {
		searchConsoleSelectionMode.deselectAll();
	}
	
	public void applyReplace(DevelopmentBoardPresenter presenter) {
		if (searchResult.size() == 0) {
			return;
		}
		List<SearchAndReplaceResult> sr = searchConsoleSelectionMode.getSelectedItems();
		if (sr != null && sr.size() != 0) {
			List<FileItemInfo> replaceInFiles = new ArrayList<FileItemInfo>();
			String search = null;
			String replace = null;
			ProjectDescription proj = null;
			for(SearchAndReplaceResult r : sr) {
				if (replaceInFiles.contains(r.getFileInfo())) {
					continue;
				}
				if (search == null) {
					search = r.getSearch();
				}
				if (replace == null) {
					replace = r.getReplace();
				}
				ProjectItemInfo pi = presenter.getView().getProjectPanel().getProjectItemByFileItemInfo(r.getFileInfo());
				if (pi.isEdited()) {
					searchConsoleSelectionMode.deselect(r);
					continue;
				}
				if (proj == null && pi != null) {
					proj = pi.getProjectDescription();
				}
				replaceInFiles.add(r.getFileInfo());
			}
			SearchAndReplaceBundle bundle = new SearchAndReplaceBundle(SearchAndReplaceBundle.PHASE_REPLACE,
																		search,
																		replace,
																		proj,
																		replaceInFiles
																		);
			SearchAndReplaceDialog.sendReplaceRequest(presenter, bundle);
		}
	}
	
	public CheckBoxSelectionModel<SearchAndReplaceResult> getSearchConsoleSelectionMode() {
		return searchConsoleSelectionMode;
	}

	public void setSearchConsoleSelectionMode(CheckBoxSelectionModel<SearchAndReplaceResult> searchConsoleSelectionMode) {
		this.searchConsoleSelectionMode = searchConsoleSelectionMode;
	}
	
	public void enableSelectMenuItem(String itemId, boolean enable) {
		MenuItem item = (MenuItem)owner.getConsoleSearchAndReplaceMenu().getItemByItemId(itemId);
		if (item != null) {
			item.setEnabled(enable);
		}
	}
	
	public boolean isReplaceAllowed(DevelopmentBoardPresenter presenter) {
		if (searchResult.size() == 0) {
			return false;
		}
		int edited = 0;
		for(int i = 0; i < searchResult.size(); i++) {
			ProjectItemInfo pi = presenter.getView().getProjectPanel().getProjectItemByFileItemInfo(searchResult.get(i).getFileInfo());
			if (pi != null && pi.getAssociatedData() != null && pi.getAssociatedData().isEdited()) {
				edited++;
			}
		}
		boolean c1 = (searchResult.get(0).getReplace() != null && searchResult.get(0).getReplace().length() != 0);
		boolean c2 = searchConsoleSelectionMode.getSelectedItems().size() != edited;
		return (c1 & c2);
	}
}
