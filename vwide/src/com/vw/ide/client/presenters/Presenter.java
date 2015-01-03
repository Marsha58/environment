package com.vw.ide.client.presenters;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.vw.ide.client.event.handler.DefaultController2PresenterEventDelegator;
import com.vw.ide.client.presenters.automata.EventProcessingAutomata;

/**
 * Root class for all presenters (per logged user)
 * @author Oleg
 *
 */
public abstract class Presenter {

	public static abstract class PresenterEventHandler {
		
		private Presenter presenter;
		private EventProcessingAutomata embeddedAutomata;

		public Presenter getPresenter() {
			return presenter;
		}

		public void setPresenter(Presenter presenter) {
			this.presenter = presenter;
		}

		public EventProcessingAutomata getEmbeddedAutomata() {
			return embeddedAutomata;
		}

		public void setEmbeddedAutomata(EventProcessingAutomata embeddedAutomata) {
			this.embeddedAutomata = embeddedAutomata;
		}

		public abstract void handler(Presenter presenter, GwtEvent<?> event);
	}
	
	private String loggedAsUser;
	private DefaultController2PresenterEventDelegator eventDelegator = new DefaultController2PresenterEventDelegator();
	
	/**
	 * Runs presenter's lifecycle
	 * @param container
	 */
	public abstract void go(final HasWidgets container);
	
	/**
	 * Fires event on bus
	 * @param event
	 */
	public abstract void fireEvent(GwtEvent<?> event);
	
	/**
	 * Handles incoming events
	 * @param event
	 */
	public abstract void handleEvent(GwtEvent<?> event);
	
	/**
	 * Registers events on bus
	 * @param eventBus
	 */
	public abstract void registerOnEventBus(HandlerManager eventBus);

	/**
	 * Unregisters events on bus
	 * @param eventBus
	 */
	public abstract void unregisterOnEventBus(HandlerManager eventBus);

	/**
	 * Returns event handler by event's type
	 * @param type
	 * @return
	 */
	public abstract PresenterEventHandler getEventHandlerByType(com.google.gwt.event.shared.GwtEvent.Type<?> type);
	
	public DefaultController2PresenterEventDelegator getEventDelegator() {
		return eventDelegator;
	}

	public String getLoggedAsUser() {
		return loggedAsUser;
	}

	public void setLoggedAsUser(String loggedAsUser) {
		this.loggedAsUser = loggedAsUser;
	}
	
	public void delegate(GwtEvent<?> event) {
		getEventDelegator().handle(this, event);
	}
}