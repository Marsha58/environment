package com.vw.ide.client.dialog.searchandreplace;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.service.userstate.callbacks.UserStateGettingResultCallback;
import com.vw.ide.client.devboardext.service.userstate.callbacks.UserStateUpdatingResultCallback;
import com.vw.ide.client.devboardext.service.userstate.callbacks.custom.handler.UserStateHandler;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.processor.CommandProcessorServiceBroker;
import com.vw.ide.client.service.remote.userstate.RemoteUserStateServiceBroker;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.shared.servlet.processor.CommandProcessorResult;
import com.vw.ide.shared.servlet.processor.dto.sandr.SearchAndReplaceBundle;
import com.vw.ide.shared.servlet.userstate.UserStateInfo;

/**
 * Allows user to input search text and select operation
 * Taken from http://www.sencha.com/examples/#ExamplePlace:advancedforms
 * @author Oleg
 *
 */
public class SearchAndReplaceDialog extends Dialog {
	
	public static abstract class SearchHandler {
		public abstract void process(SearchAndReplaceBundle bundle);
	}

	private static class UserHistoryHandler implements UserStateHandler {

		private SearchAndReplaceDialog owner;
		
		public UserHistoryHandler(SearchAndReplaceDialog owner) {
			this.owner = owner;
		}
		
		@Override
		public void handle(UserStateInfo userState) {
			owner.updateSearchHistoryStore(userState.getSearchHistory());
			owner.updateReplaceHistoryStore(userState.getReplaceHistory());
			owner.setUserState(userState);
			owner.postInit();
		}
	}

	private static class ReplaceCallback extends ResultCallback<CommandProcessorResult> {

		@Override
		public void handle(CommandProcessorResult result) {
		}
		
	}
	
	private static class ProjectModelKeyProvider implements ModelKeyProvider<ProjectItemInfo> {

		@Override
		public String getKey(ProjectItemInfo item) {
			return item.getAssociatedData().getName();
		}
	}
	
	private static class ProjectLabelProvider implements LabelProvider<ProjectItemInfo> {

		@Override
		public String getLabel(ProjectItemInfo item) {
			return item.getAssociatedData().getName();
		}
	}

	private static class HistoryModelKeyProvider implements ModelKeyProvider<String> {

		@Override
		public String getKey(String item) {
			return item;
		}
	}

	private static class HistoryLabelProvider implements LabelProvider<String> {

		@Override
		public String getLabel(String item) {
			return item;
		}
	}

	private static class HistorySelectionHandler implements SelectionHandler<String>, ValueChangeHandler<String> {

		private TextField selected;
		
		public HistorySelectionHandler(TextField selected) {
			this.selected = selected;
		}
		
		@Override
		public void onSelection(SelectionEvent<String> event) {
			selected.setText(event.getSelectedItem());
		}

		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			selected.setText(event.getValue());
		}
	}
	
	private Widget form = null;
	private List<String> searchHistoryStore = new ArrayList<String>();
	private List<String> replaceHistoryStore = new ArrayList<String>();
	
	private ComboBox<ProjectItemInfo> projectSelector = null;
	private ComboBox<String> searchSelector = null;
	private ComboBox<String> replaceSelector = null;
	private TextField searchText = new TextField();
	private TextField replaceText = new TextField();
	private HorizontalPanel searchPanel = new HorizontalPanel();
	private HorizontalPanel replacePanel = new HorizontalPanel();
	
	private DevelopmentBoardPresenter devBoardPresenter = null;
	private SearchAndReplaceBundle bundle = null;
	private SearchHandler handler = null;
	
	private UserStateInfo userState = null;

	public static void sendReplaceRequest(DevelopmentBoardPresenter presenter, SearchAndReplaceBundle bundle) {
		CommandProcessorServiceBroker.performSearchAndReplace(presenter.getLoggedAsUser(),
															bundle,
															new ReplaceCallback());
	}
	
	public SearchAndReplaceDialog(DevelopmentBoardPresenter devBoardPresenter, SearchHandler handler) {
		this.devBoardPresenter = devBoardPresenter;
		setHandler(handler);
	}

	public boolean init() {
		requestForHistory();
		return true;
	}

	public Widget getForm() {
		return form;
	}
	
	public SearchAndReplaceBundle getBundle() {
		return bundle;
	}
	
	public SearchHandler getHandler() {
		return handler;
	}

	public void setHandler(SearchHandler handler) {
		this.handler = handler;
	}

	public UserStateInfo getUserState() {
		return userState;
	}

	public void setUserState(UserStateInfo userState) {
		this.userState = userState;
	}

	protected void postInit() {
		form = bind();
		if (form != null) {
			setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
			com.sencha.gxt.widget.core.client.button.TextButton b = getButton(PredefinedButton.OK);
			b.setText("Search");
			setWidget(form);
			setModal(true);
			show();
		}
	}
	
	protected Widget bind() {
		VerticalPanel vp = null;
		List<ProjectItemInfo> availableProjects = devBoardPresenter.getView().getProjectPanel().getProjects();
		if (availableProjects.size() != 0) {
			initSearchHistoryField(searchHistoryStore);
			initReplaceHistoryField(replaceHistoryStore);
			initProjectSelectorField(availableProjects);
			setHeadingText("Search and Replace");
			vp = new VerticalPanel();
			vp.setSpacing(10);
			vp.setWidth("480");
		    vp.add(new FieldLabel(searchPanel, "Search"));
		    vp.add(new FieldLabel(replacePanel, "Replace"));
		    vp.add(new FieldLabel(projectSelector, "Project"));
		}
	    return vp;
	}

	@Override
	protected void onButtonPressed(TextButton textButton) {
		if (textButton.equals(getButton(PredefinedButton.OK))) {
			if (handler != null) {
				devBoardPresenter.getView().getConsoles().getSearchConsoleTab().clear();
				bundle = new SearchAndReplaceBundle(SearchAndReplaceBundle.PHASE_SEARCH,
													searchText.getText(),
													replaceText.getText(),
													projectSelector.getValue().getProjectDescription(),
													null);
				handler.process(bundle);
				if (userState != null) {
					if (bundle.getSearch() != null) {
						userState.addToSearchHistory(bundle.getSearch());
					}
					if (bundle.getReplace() != null) {
						userState.addToReplaceHistory(bundle.getReplace());
					}
					storeHistory();
				}
			}
		}
		super.onButtonPressed(textButton);
		hide();
	}

	protected void updateSearchHistoryStore(Set<String> from) {
		searchHistoryStore.addAll(from);
	}

	protected void updateReplaceHistoryStore(Set<String> from) {
		replaceHistoryStore.addAll(from);
	}
	
	private void initProjectSelectorField(List<ProjectItemInfo> availableProjects) {
		ListStore<ProjectItemInfo> projects = new ListStore<ProjectItemInfo>(new ProjectModelKeyProvider());
		projects.addAll(availableProjects);
		projectSelector = new ComboBox<ProjectItemInfo>(projects, new ProjectLabelProvider());
		projectSelector.setTypeAhead(true);
		projectSelector.setTriggerAction(TriggerAction.ALL);
		projectSelector.setValue(projects.get(0));
		projectSelector.setEditable(false);
	}
	
	private void initSearchHistoryField(List<String> searchHistory) {
		ListStore<String> searchHistoryStore = new ListStore<String>(new HistoryModelKeyProvider());
		searchHistoryStore.addAll(searchHistory);
		searchSelector = new ComboBox<String>(searchHistoryStore, new HistoryLabelProvider());
		searchSelector.setTypeAhead(true);
		searchSelector.setTriggerAction(TriggerAction.ALL);
		searchSelector.addSelectionHandler(new HistorySelectionHandler(searchText));
		searchSelector.addValueChangeHandler(new HistorySelectionHandler(searchText));
		if (searchHistoryStore.size() != 0) {
			searchSelector.setValue(searchHistoryStore.get(0), true);
		}
		searchSelector.setEditable(true);
		searchPanel.add(searchText);
		searchPanel.add(searchSelector);
	}

	private void initReplaceHistoryField(List<String> replaceHistory) {
		ListStore<String> replaceHistoryStore = new ListStore<String>(new HistoryModelKeyProvider());
		replaceHistoryStore.addAll(replaceHistory);
		replaceSelector = new ComboBox<String>(replaceHistoryStore, new HistoryLabelProvider());
		replaceSelector.setTypeAhead(true);
		replaceSelector.setTriggerAction(TriggerAction.ALL);
		replaceSelector.addSelectionHandler(new HistorySelectionHandler(replaceText));
		replaceSelector.addValueChangeHandler(new HistorySelectionHandler(replaceText));
		if (replaceHistoryStore.size() != 0) {
			replaceSelector.setValue(replaceHistoryStore.get(0), true);
		}
		replaceSelector.setEditable(true);
		replacePanel.add(replaceText);
		replacePanel.add(replaceSelector);
	}

	private void requestForHistory() {
		RemoteUserStateServiceBroker.requestForGettingUserState(FlowController.getLoggedAsUser(),
																new UserStateGettingResultCallback(devBoardPresenter,
																new UserHistoryHandler(this)));
	}
	
	private void storeHistory() {
		RemoteUserStateServiceBroker.requestForUpdateUserState(FlowController.getLoggedAsUser(),
																userState,
																new UserStateUpdatingResultCallback(devBoardPresenter));
	}
}
