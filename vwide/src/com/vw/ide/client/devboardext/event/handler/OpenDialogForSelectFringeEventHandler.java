package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.core.client.Callback;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Timer;
import com.sencha.gxt.state.client.StateManager;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.fringes.OpenDialogForSelectFringeHandler;
import com.vw.ide.client.event.uiflow.GetCategoriesEvent;
import com.vw.ide.client.event.uiflow.GetFringesInCategoriesEvent;
import com.vw.ide.client.event.uiflow.fringes.OpenDialogForSelectFringeEvent;
import com.vw.ide.client.fringemanagment.fringeinsert.FringeInsertDialog;
import com.vw.ide.client.presenters.Presenter;

public class OpenDialogForSelectFringeEventHandler extends Presenter.PresenterEventHandler implements OpenDialogForSelectFringeHandler {
	
	private final String PROPERTY_WIDTH = "insertFringeDialog_Width";
	private final String PROPERTY_HEIGHT = "insertFringeDialog_Height";
	String fringeDialog_Width = "500";
	String fringeDialog_Height = "400";
	DevelopmentBoardPresenter presenter;

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		this.presenter = (DevelopmentBoardPresenter)presenter;
		process((DevelopmentBoardPresenter)presenter, (OpenDialogForSelectFringeEvent)event);
	}

	@Override
	public void onOpenDialogForSelectFringe(OpenDialogForSelectFringeEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
		
	}	
	
	protected void process(DevelopmentBoardPresenter presenter, OpenDialogForSelectFringeEvent event) {
		String menuId = event.getItemId();

		if (menuId != null) {
			switch (menuId) {
			case "idInsertFringeInFile":
				getStoredDimension();
				presenter.fireEvent(new GetFringesInCategoriesEvent());
				callerOpenDialog();
				break;
			default:
				break;
			}
		}
	}

	public void callerOpenDialog() {
		Timer t = new Timer() {
			@Override
			public void run() {
				doOpenDialog();
			}
		};
		t.schedule(500);
	}
	
	
	private void getStoredDimension(){
		StateManager.get().getProvider().getValue(PROPERTY_WIDTH, new Callback<String, Throwable>() { 
			
			@Override
			public void onSuccess(String result) {
				try {
					if (result != null) {
						fringeDialog_Width = result;
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
						fringeDialog_Height = result;
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
	
	private void doOpenDialog() {
		final FringeInsertDialog box = new FringeInsertDialog(presenter);
		box.setLoggedAsUser(FlowController.getLoggedAsUser());
		box.setSize(fringeDialog_Width, fringeDialog_Height);
		box.showCenter("Fringe insert dialog", null);
		box.setResizable(true);
		box.setModal(true);
		
		box.addResizeHandler(new ResizeHandler() {
			
			@Override
			public void onResize(ResizeEvent event) {
				StateManager.get().getProvider().setValue(PROPERTY_WIDTH, String.valueOf(event.getWidth()));
				StateManager.get().getProvider().setValue(PROPERTY_HEIGHT, String.valueOf(event.getHeight()));
			}
		});
		
	}




}


