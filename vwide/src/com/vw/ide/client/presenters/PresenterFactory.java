package com.vw.ide.client.presenters;

import com.google.gwt.event.shared.HandlerManager;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.login.LoginGxtPresenter;
import com.vw.ide.client.fringemanagment.FringeManagerPresenter;;

/**
 * Instantiates available presenters
 * @author Oleg
 *
 */
public class PresenterFactory {
	/**
	 * Instantiates login presenter
	 * @param eventBus
	 * @param view
	 * @return
	 */
	public static Presenter buildLoginPresenter(HandlerManager eventBus, PresenterViewerLink view) {
		return new LoginGxtPresenter(eventBus, view);
	}
	
	public static Presenter buildDevBoardPresenter(HandlerManager eventBus, PresenterViewerLink view) {
		return new DevelopmentBoardPresenter(eventBus, view);
	}
	
	public static Presenter buildFringeManagerPresenter(HandlerManager eventBus, PresenterViewerLink view) {
		return new FringeManagerPresenter(eventBus, view);
	}	
}
