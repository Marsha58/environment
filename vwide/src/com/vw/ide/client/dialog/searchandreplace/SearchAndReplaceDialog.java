package com.vw.ide.client.dialog.searchandreplace;

import java.util.List;

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
import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.shared.servlet.processor.dto.sandr.SearchAndReplaceBundle;

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

	private Widget form = null;
	private TextField searchText = null;
	private TextField replaceText = null;
	private ComboBox<ProjectItemInfo> projectSelector = null;
	
	private DevelopmentBoard devBoard = null;
	private SearchAndReplaceBundle bundle = null;
	private SearchHandler handler = null;

	public SearchAndReplaceDialog(DevelopmentBoard devBoard, SearchHandler handler) {
		setDevBoard(devBoard);
		setHandler(handler);
	}

	public boolean init() {
		form = bind();
		if (form != null) {
			setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
			com.sencha.gxt.widget.core.client.button.TextButton b = getButton(PredefinedButton.OK);
			b.setText("Search");
			setWidget(form);
		}
		return (form == null) ? false : true;
	}
	
	public Widget getForm() {
		return form;
	}
	
	public DevelopmentBoard getDevBoard() {
		return devBoard;
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

	protected Widget bind() {
		VerticalPanel vp = null;
		List<ProjectItemInfo> availableProjects = devBoard.getProjectPanel().getProjects();
		if (availableProjects.size() != 0) {
			ListStore<ProjectItemInfo> projects = new ListStore<ProjectItemInfo>(new ProjectModelKeyProvider());
			projects.addAll(availableProjects);
			projectSelector = new ComboBox<ProjectItemInfo>(projects, new ProjectLabelProvider());
			projectSelector.setTypeAhead(true);
			projectSelector.setTriggerAction(TriggerAction.ALL);
			projectSelector.setValue(projects.get(0));
			projectSelector.setEditable(false);
			setHeadingText("Search and Replace");
			vp = new VerticalPanel();
			vp.setSpacing(10);
			vp.setWidth("480");
		    searchText = new TextField();
		    vp.add(new FieldLabel(searchText, "Search"));
		    replaceText = new TextField();
		    vp.add(new FieldLabel(replaceText, "Replace"));
		    vp.add(new FieldLabel(projectSelector, "Project"));
		}
	    return vp;
	}

	protected void setDevBoard(DevelopmentBoard devBoard) {
		this.devBoard = devBoard;
	}

	@Override
	protected void onButtonPressed(TextButton textButton) {
		if (textButton.equals(getButton(PredefinedButton.OK))) {
			if (handler != null) {
				bundle = new SearchAndReplaceBundle(searchText.getValue(), replaceText.getValue(), projectSelector.getValue().getProjectDescription());
				handler.process(bundle);
			}
		}
		super.onButtonPressed(textButton);
		hide();
	}
}
