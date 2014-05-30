package com.vw.ide.client.presenters;

import com.google.gwt.user.client.ui.Widget;

/**
 * Establishes link association between presenter and view
 * @author Oleg
 *
 */
public interface PresenterViewerLink {
	  public Widget asWidget();
	  public void associatePresenter(Presenter presenter);
}
