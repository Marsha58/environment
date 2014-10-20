package com.vw.ide.client.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.presenters.Presenter;

/**
 * Default implementation of 'Controller2PresenterEventDelegator'
 * @author Oleg
 *
 */
public class DefaultController2PresenterEventDelegator {

	public void handle(Presenter presenter, GwtEvent<?> event) {
		presenter.handleEvent(event);
	}
}
