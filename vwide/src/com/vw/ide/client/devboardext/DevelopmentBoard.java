package com.vw.ide.client.devboardext;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent;
import com.sencha.gxt.widget.core.client.event.ResizeStartEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.vw.ide.client.dialog.about.AboutDialog;
import com.vw.ide.client.event.uiflow.LogoutEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.ui.editorpanel.EditorPanel;
import com.vw.ide.client.ui.projectpanel.ProjectPanel;
import com.vw.ide.client.ui.toppanel.TopPanel;
import com.vw.ide.client.ui.toppanel.TopPanel.Theme;
import com.vw.ide.client.ui.windowspanel.WindowsPanelView;

public class DevelopmentBoard extends ResizeComposite implements IsWidget,
		PresenterViewerLink {

	private static DevelopmentBoardUiBinder uiBinder = GWT
			.create(DevelopmentBoardUiBinder.class);

	private static String s_newVwmlProjectCaption = "New VWML project";
	private static String s_AboutCaption = "About";
	
	@UiField
	SimpleContainer mainContainer = new SimpleContainer();
	
	@UiField(provided = true)
	MarginData outerData = new MarginData(1);
	@UiField(provided = true)
	BorderLayoutData northData = new BorderLayoutData(81);
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

	interface DevelopmentBoardUiBinder extends
			UiBinder<SimpleContainer, DevelopmentBoard> {
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
		combo.setValue(Theme.GRAY.isActive() ? Theme.GRAY : Theme.BLUE
				.isActive() ? Theme.BLUE : Theme.NEPTUNE);
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
					northData.setSize(89);
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
			northData.setSize(89);
		}		
		
		initWidget(uiBinder.createAndBindUi(this));

		
	}

	public DevelopmentBoard(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public DevelopmentBoard(Integer size) {
		initWidget(uiBinder.createAndBindUi(this));
	}
	



	

	/**
	 * Fired by menu-item 'Logout' in case if last was selected
	 * 
	 * @author Oleg
	 * 
	 */
	public static class LogoutCommand implements ScheduledCommand {

		private DevelopmentBoard view = null;

		public LogoutCommand(DevelopmentBoard view) {
			this.view = view;
		}

		public void execute() {
			if (view.getAssociatedPresenter() != null) {
				view.getAssociatedPresenter().fireEvent(new LogoutEvent());
			}
		}
	}

	/**
	 * Fired when 'Project -> New' menu item selected
	 * 
	 * @author Oleg
	 * 
	 */
/*	
	public static class NewVwmlProjectCommand implements ScheduledCommand {

		private DevelopmentBoard view = null;

		public NewVwmlProjectCommand(DevelopmentBoard view) {
			this.view = view;
		}

		public void execute() {
			if (view.getAssociatedPresenter() != null) {
				NewVwmlProjectDialog d = new NewVwmlProjectDialog();
				d.setLoggedAsUser(view.getAssociatedPresenter()
						.getLoggedAsUser());
				d.show(s_newVwmlProjectCaption, null, 0, 0);
			}
		}
	}
*/
	/**
	 * Fired when 'Project -> New' menu item selected
	 * 
	 * @author Oleg
	 * 
	 */
	public static class AboutCommand implements ScheduledCommand {

		public AboutCommand() {
		}

		public void execute() {
			AboutDialog d = new AboutDialog();
			d.show(s_AboutCaption, null, 0, 0);
		}
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
		return widget;
	}

	public void associatePresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	protected Presenter getAssociatedPresenter() {
		return presenter;
	}
	
	public void associatePresenterWithSubpanels(Presenter presenter) {
		topPanel.associatePresenter(presenter);
		editor.associatePresenter(presenter);
		windows.associatePresenter(presenter);
		projectPanel.associatePresenter(presenter);
		projectPanel.requestForDirContent(null);
	}	

	
	

}