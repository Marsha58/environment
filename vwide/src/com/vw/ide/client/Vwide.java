package com.vw.ide.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;
import com.vw.ide.client.service.factory.ServicesBrokerFactory;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Vwide implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	    HandlerManager eventBus = new HandlerManager(null);
	    FlowController mainController = new FlowController(eventBus);
	    ServicesBrokerFactory.instantiateAllServices(eventBus);
	    mainController.go(RootPanel.get());
	}
}
