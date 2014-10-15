package com.vw.ide.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.vw.ide.client.service.factory.ServicesBrokerFactory;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Vwide implements EntryPoint {

    private static final HandlerManager eventBus = new HandlerManager(null);
    private static final FlowController mainController = new FlowController(eventBus);
	
    public Vwide() {
    	init();
    }
    
	interface GlobalResources extends ClientBundle {
	    @NotStrict
	    @Source("global.css")
	    CssResource css();
	  }	
	  
	private Viewport viewport = null;  
	
	/**
	 * This is the entry point method.
	 */

    public Viewport getViewport() {
    	return viewport;
    }
  
	public void onModuleLoad() {
	    GWT.<GlobalResources>create(GlobalResources.class).css().ensureInjected();
	    viewport = new Viewport();
	    RootPanel.get().add(viewport);
	    viewport.forceLayout();
	    mainController.go(viewport);
	}
	
	protected void init() {
	    ServicesBrokerFactory.instantiateAllServices(eventBus);
	}
}
