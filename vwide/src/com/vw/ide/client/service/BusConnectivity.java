package com.vw.ide.client.service;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;

/**
 * Each service which wishes to send events should implement this interface
 * @author Oleg
 *
 */
public interface BusConnectivity {
	public void setBusRef(HandlerManager busRef);
	public void fireEvent(GwtEvent<?> event);
}
