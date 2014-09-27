package com.vw.ide.client.ui.windowspanel;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.projects.FileManager;
import com.vw.ide.client.projects.FileManagerImpl;

/**
 * Development board screen
 * @author omelnyk
 *
 */
public class WindowsPanelPresenter extends Presenter {

	private final HandlerManager eventBus;
	private final PresenterViewerLink view;


	public WindowsPanelPresenter(HandlerManager eventBus, PresenterViewerLink view) {
		this.eventBus = eventBus;
		this.view = view;
		if (view != null) {
			view.associatePresenter(this);
		}
		
		
	

	}
	
	public void go(HasWidgets container) {
	    container.clear();
	    container.add(view.asWidget());
	}
	
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}
}
