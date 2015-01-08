package com.vw.ide.client.ui.consolespanel;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;

/**
 * Development board screen
 * @author omelnyk
 *
 */
public class ConsolesPanelPresenter extends Presenter {

	private final HandlerManager eventBus;
	private final PresenterViewerLink view;

	public ConsolesPanelPresenter(HandlerManager eventBus, PresenterViewerLink view) {
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

	@Override
	public void handleEvent(GwtEvent<?> event) {
	}

	@Override
	public void registerOnEventBus(HandlerManager eventBus) {
	}

	@Override
	public void unregisterOnEventBus(HandlerManager eventBus) {
	}

	@Override
	public PresenterEventHandler getEventHandlerByType(Type<?> type) {
		return null;
	}
}
