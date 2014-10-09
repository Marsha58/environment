package com.vw.ide.client.ui.windowspanel;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;

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
