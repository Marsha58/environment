package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.BeforeCloseEvent;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.service.userstate.callbacks.GettingUserStateResultCallback;
import com.vw.ide.client.devboardext.service.userstate.callbacks.UpdatingUserStateResultCallback;
import com.vw.ide.client.devboardext.service.userstate.callbacks.UserStateHandler;
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
		
		public HandleUserStateOnClosingEditorTab(ProjectItemInfo itemInfo) {
			this.itemInfo = itemInfo;
		}
		
		@Override
		public void handle(UserStateInfo userState) {
			userState.setFileIdSelected(null);
			userState.removeFileFromOpenedFiles(itemInfo.getAssociatedData());
			itemInfo.setAlreadyOpened(false);
			RemoteUserStateServiceBroker.requestForUpdateUserState(FlowController.getLoggedAsUser(),
																	userState,
																	new UpdatingUserStateResultCallback(null));
		}
	}
	
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (EditorTabClosedEvent)event);
	}

	@Override
	public void onEditorTabClosed(BeforeCloseEvent<Widget> event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	private void process(DevelopmentBoardPresenter presenter, EditorTabClosedEvent event) {
		FileSheet fileSheet = (FileSheet)event.getEvent().getItem();
		presenter.getView().deleteFileItemId(fileSheet.getItemInfo());
		RemoteUserStateServiceBroker.requestForGettingUserState(FlowController.getLoggedAsUser(),
																new GettingUserStateResultCallback(presenter, new HandleUserStateOnClosingEditorTab(fileSheet.getItemInfo())));
	}
}

