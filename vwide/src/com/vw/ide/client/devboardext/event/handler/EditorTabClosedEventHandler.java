package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.service.userstate.callbacks.UserStateGettingResultCallback;
import com.vw.ide.client.devboardext.service.userstate.callbacks.UserStateUpdatingResultCallback;
import com.vw.ide.client.devboardext.service.userstate.callbacks.custom.handler.UserStateHandler;
import com.vw.ide.client.event.handler.EditorTabClosedHandler;
import com.vw.ide.client.event.uiflow.EditorTabClosedEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.remote.userstate.RemoteUserStateServiceBroker;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.shared.servlet.userstate.UserStateInfo;

public class EditorTabClosedEventHandler extends Presenter.PresenterEventHandler implements EditorTabClosedHandler {
	
	private static class HandleUserStateOnClosingEditorTab implements UserStateHandler {

		private ProjectItemInfo itemInfo;
		private DevelopmentBoardPresenter presenter;
		
		public HandleUserStateOnClosingEditorTab(DevelopmentBoardPresenter presenter, ProjectItemInfo itemInfo) {
			this.itemInfo = itemInfo;
			this.presenter = presenter;
		}
		
		@Override
		public void handle(UserStateInfo userState) {
			userState.setFileIdSelected(null);
			userState.removeFileFromOpenedFiles(itemInfo.getAssociatedData());
			itemInfo.setAlreadyOpened(false);
			presenter.getView().afterClosingTabName(itemInfo);
			RemoteUserStateServiceBroker.requestForUpdateUserState(FlowController.getLoggedAsUser(),
																	userState,
																	new UserStateUpdatingResultCallback(presenter));
			// runs automata upon finishing of processing EditorTabClosedEvent
			// installed when project is deleted (waits for closing all opened tabs)
			Presenter.PresenterEventHandler handler = presenter.getEventHandlerByType(EditorTabClosedEvent.TYPE);
			if (handler != null && handler.getEmbeddedAutomata() != null) {
				try {
					// automata is installed before 'project deletion' operation is activated
					handler.getEmbeddedAutomata().handle(EditorTabClosedEvent.TYPE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (EditorTabClosedEvent)event);
	}

	@Override
	public void onEditorTabClosed(EditorTabClosedEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	private void process(DevelopmentBoardPresenter presenter, EditorTabClosedEvent event) {
		FileSheet fileSheet = (FileSheet)event.getEvent().getItem();
		presenter.getView().getTopPanel().delItemFromScrollMenu(fileSheet.getItemInfo());
		RemoteUserStateServiceBroker.requestForGettingUserState(FlowController.getLoggedAsUser(),
																new UserStateGettingResultCallback(presenter, new HandleUserStateOnClosingEditorTab(presenter, fileSheet.getItemInfo())));
	}
}

