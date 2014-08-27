package com.vw.ide.client.devboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.client.dialog.about.AboutDialog;
import com.vw.ide.client.dialog.newvwmlproj.NewVwmlProjectDialog;
import com.vw.ide.client.event.uiflow.LogoutEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.ui.editorpanel.EditorPanel;
import com.vw.ide.client.ui.projectpanel.Shortcuts;
import com.vw.ide.client.ui.toppanel.TopPanel;
import com.vw.ide.client.ui.windowspanel.WindowsPanelView;

public class DevelopmentBoardView extends Composite implements HasText,
		PresenterViewerLink {

	private static DevelopmentBoardViewUiBinder uiBinder = GWT
			.create(DevelopmentBoardViewUiBinder.class);
	private static String s_newVwmlProjectCaption = "New VWML project";
	private static String s_AboutCaption = "About";

//	 @UiField MenuItem logoutField;
//	 @UiField MenuItem newVwmlProjField;
//	 @UiField MenuItem helpAboutField;
//	 @UiField FlowPanel panelEditor;

	@UiField
	TopPanel topPanel;
	@UiField
	EditorPanel editorPanel;
	@UiField
	WindowsPanelView windowsPanelView;
	@UiField
	Shortcuts shortcuts;
	// @UiField FlowPanel mailList;
	// @UiField FlowPanel mailDetail;
	// @UiField FlowPanel shortcuts;

	private Presenter presenter = null;

	interface DevelopmentBoardViewUiBinder extends
			UiBinder<DockLayoutPanel, DevelopmentBoardView> {
	}

	public DevelopmentBoardView() {
		// Create the UI defined in Mail.ui.xml.
		DockLayoutPanel outer = uiBinder.createAndBindUi(this);

		Window.enableScrolling(false);
		Window.setMargin("0px");

		// Special-case stuff to make topPanel overhang a bit.
		Element topElem = outer.getWidgetContainerElement(topPanel);
		topElem.getStyle().setZIndex(2);
		topElem.getStyle().setOverflow(Overflow.VISIBLE);

		// Add the outer panel to the RootLayoutPanel, so that it will be
		// displayed.
		RootLayoutPanel root = RootLayoutPanel.get();
		root.add(outer);

		initWidget(outer);
		// initWidget(uiBinder.createAndBindUi(this));
		bind();

	}

	public DevelopmentBoardView(String firstName) {
		DockLayoutPanel outer = uiBinder.createAndBindUi(this);

		Window.enableScrolling(false);
		Window.setMargin("0px");

		// Special-case stuff to make topPanel overhang a bit.
		Element topElem = outer.getWidgetContainerElement(topPanel);
		topElem.getStyle().setZIndex(2);
		topElem.getStyle().setOverflow(Overflow.VISIBLE);

		// Add the outer panel to the RootLayoutPanel, so that it will be
		// displayed.
		RootLayoutPanel root = RootLayoutPanel.get();
		root.add(outer);

		initWidget(outer);
		// initWidget(uiBinder.createAndBindUi(this));
		bind();
	}

	/**
	 * Fired by menu-item 'Logout' in case if last was selected
	 * 
	 * @author Oleg
	 * 
	 */
	public static class LogoutCommand implements ScheduledCommand {

		private DevelopmentBoardView view = null;

		public LogoutCommand(DevelopmentBoardView view) {
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
	public static class NewVwmlProjectCommand implements ScheduledCommand {

		private DevelopmentBoardView view = null;

		public NewVwmlProjectCommand(DevelopmentBoardView view) {
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

	@Override
	public Widget asWidget() {
		return this;
	}

	public void associatePresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	protected Presenter getAssociatedPresenter() {
		return presenter;
	}

	private void bind() {

		// Special-case stuff to make topPanel overhang a bit.
		// Element topElem = outer.getWidgetContainerElement(topPanel);
		// topElem.getStyle().setZIndex(2);
		// topElem.getStyle().setOverflow(Overflow.VISIBLE);

//		if (logoutField != null) {
//			logoutField.setScheduledCommand(new LogoutCommand(this));
//		}
		
//		if (newVwmlProjField != null) {
//			newVwmlProjField
//					.setScheduledCommand(new NewVwmlProjectCommand(this));
//		}
		
//		if (helpAboutField != null) {
//			helpAboutField.setScheduledCommand(new AboutCommand());
//		}

		// RootLayoutPanel root = RootLayoutPanel.get();
		// root.add(outer);

	}
}
