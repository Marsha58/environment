package com.vw.ide.client.dialog.fringemanagment.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.dialog.fringeload.FringeLoadDialog;
import com.vw.ide.client.dialog.fringemanagment.FringeManagerDialogHandlers;
import com.vw.ide.client.dialog.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.event.handler.FringesContextMenuHandler;
import com.vw.ide.client.event.uiflow.FringesContextMenuEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

public class FringesContextMenuEventHandler  extends Presenter.PresenterEventHandler implements FringesContextMenuHandler{

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((FringeManagerPresenter)presenter, (FringesContextMenuEvent)event);
	}

	@Override
	public void onFringesContextMenuClick(FringesContextMenuEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	protected void process(FringeManagerPresenter presenter, FringesContextMenuEvent event) {
		String menuId = event.getMenuId();
		if (menuId != null && presenter.getView().getSelectedFringe() != null) {
			switch (menuId) {
			case "load_fringe_jar":
				doLoadJar(presenter, presenter.getView().getSelectedCategory(), presenter.getView().getSelectedFringe());
				break;
			default:
				break;
			}
		}
	}
	
	
	private void doLoadJar(FringeManagerPresenter presenter,Category category, Fringe fringe) {
		final FringeLoadDialog box = new FringeLoadDialog(presenter, category, fringe);
//		box.setEditLabelText("Select file");
		// box.setParentPath(parentPath);
		box.setHeadingText("Fringe loading dialog");
		box.addDialogHideHandler(new FringeManagerDialogHandlers.LoadFringeDialogHideHandler(box, presenter));
		box.show();		
	}	



}
