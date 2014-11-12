package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.core.client.Callback;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.state.client.StateManager;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.fringemanagment.FringeManager;
import com.vw.ide.client.fringemanagment.FringeManagerPresenter;
import com.vw.ide.client.event.handler.fringes.OpenFringeManagerHandler;
import com.vw.ide.client.event.uiflow.fringes.OpenFringeManagerEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterFactory;

public class OpenFringeManagerEventHandler extends Presenter.PresenterEventHandler implements OpenFringeManagerHandler {
	
	private final String PROPERTY_WIDTH = "fringeManagerDialog_Width";
	private final String PROPERTY_HEIGHT = "fringeManagerDialog_Height";
	String fringeManagerDialog_Width = "700";
	String fringeManagerDialog_Height = "400";

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (OpenFringeManagerEvent)event);
	}

	@Override
	public void onOpenFringeManager(OpenFringeManagerEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
		
	}	
	
	protected void process(DevelopmentBoardPresenter presenter, OpenFringeManagerEvent event) {
		String menuId = event.getItemId();

		if (menuId != null) {
			switch (menuId) {
			case "idOpenFringeManager":
				getStoredDimension();
				doOpenFringeManagerDialog(presenter);
				break;
			default:
				break;
			}
		}
	}

	
	
	private void getStoredDimension(){
		StateManager.get().getProvider().getValue(PROPERTY_WIDTH, new Callback<String, Throwable>() { 
			
			@Override
			public void onSuccess(String result) {
				try {
					if (result != null) {
						fringeManagerDialog_Width = result;
					}
				} catch (Exception e) {
				} 
			}

			@Override
			public void onFailure(Throwable reason) {
				// TODO Auto-generated method stub
				
			}
			
		});
		StateManager.get().getProvider().getValue(PROPERTY_HEIGHT, new Callback<String, Throwable>() { 
			
			@Override
			public void onSuccess(String result) {
				try {
					if (result != null) {
						fringeManagerDialog_Height = result;
					}
				} catch (Exception e) {
				} 
			}

			@Override
			public void onFailure(Throwable reason) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	private void doOpenFringeManagerDialog(DevelopmentBoardPresenter presenter) {
		FringeManager fringeManagerDialog = new FringeManager();
		fringeManagerDialog.setLoggedAsUser(FlowController.getLoggedAsUser());
		FringeManagerPresenter fringeManagerPresenter = (FringeManagerPresenter) PresenterFactory.buildFringeManagerPresenter(presenter.eventBus, fringeManagerDialog);
		fringeManagerDialog.associatePresenter(fringeManagerPresenter);
		fringeManagerDialog.setSize(fringeManagerDialog_Width, fringeManagerDialog_Height);
		fringeManagerDialog.setResizable(true);
		fringeManagerDialog.setModal(true);
		fringeManagerDialog.showCenter("Fringe Manager", null);
		
		
		fringeManagerDialog.addResizeHandler(new ResizeHandler() {
			
			@Override
			public void onResize(ResizeEvent event) {
				StateManager.get().getProvider().setValue(PROPERTY_WIDTH, String.valueOf(event.getWidth()));
				StateManager.get().getProvider().setValue(PROPERTY_HEIGHT, String.valueOf(event.getHeight()));
			}
		});
		
	}



}

