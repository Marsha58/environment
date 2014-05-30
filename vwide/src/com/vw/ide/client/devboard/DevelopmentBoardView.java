package com.vw.ide.client.devboard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.client.dialog.newvwmlproj.NewVwmlProjectDialog;
import com.vw.ide.client.event.uiflow.LogoutEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;

public class DevelopmentBoardView extends Composite implements HasText, PresenterViewerLink {

	private static DevelopmentBoardViewUiBinder uiBinder = GWT.create(DevelopmentBoardViewUiBinder.class);
	private static String s_newVwmlProjectCaption = "New VWML project";
	
	@UiField MenuItem logoutField;
	@UiField MenuItem newVwmlProjField;

	private Presenter presenter = null;
	
	interface DevelopmentBoardViewUiBinder extends UiBinder<Widget, DevelopmentBoardView> {
	}

	/**
	 * Fired by menu-item 'Logout' in case if last was selected
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
				d.setLoggedAsUser(view.getAssociatedPresenter().getLoggedAsUser());
				d.show(s_newVwmlProjectCaption, null, 0, 0);
			}
		}
	}
	
	public DevelopmentBoardView() {
		initWidget(uiBinder.createAndBindUi(this));
		bind();
	}

	public DevelopmentBoardView(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		bind();
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
		if (logoutField != null) {
			logoutField.setScheduledCommand(new LogoutCommand(this));
		}
		if (newVwmlProjField != null) {
			newVwmlProjField.setScheduledCommand(new NewVwmlProjectCommand(this));
		}
	}
}
