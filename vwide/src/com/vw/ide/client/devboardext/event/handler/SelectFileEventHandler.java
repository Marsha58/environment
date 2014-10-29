package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.service.browser.callbacks.DirOperationFileReadingResultCallback;
import com.vw.ide.client.devboardext.service.userstate.callbacks.GettingUserStateResultCallback;
import com.vw.ide.client.devboardext.service.userstate.callbacks.UpdatingUserStateResultCallback;
import com.vw.ide.client.devboardext.service.userstate.callbacks.UserStateHandler;
import com.vw.ide.client.event.handler.SelectFileHandler;
import com.vw.ide.client.event.uiflow.SelectFileEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.remote.browser.DirBrowserServiceBroker;
import com.vw.ide.client.service.remote.userstate.RemoteUserStateServiceBroker;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.shared.servlet.userstate.UserStateInfo;

public class SelectFileEventHandler extends Presenter.PresenterEventHandler implements SelectFileHandler {
	
	private static class UpdateUserStateOnSelection implements UserStateHandler {

		private ProjectItemInfo projectItemInfo;
		
		public UpdateUserStateOnSelection(ProjectItemInfo projectItemInfo) {
			this.projectItemInfo = projectItemInfo;
		}
		
		@Override
		public void handle(UserStateInfo userState) {
			userState.setFileIdSelected(projectItemInfo.getAssociatedData());
			userState.setProjectIdSelected(projectItemInfo.getProjectDescription());
			RemoteUserStateServiceBroker.requestForUpdateUserState(
										FlowController.getLoggedAsUser(),
										userState,
										new UpdatingUserStateResultCallback(null));
		}
	}
	
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		ProjectItemInfo projectItemInfo = ((SelectFileEvent)event).getFileItemInfo();
		RemoteUserStateServiceBroker.requestForGettingUserState(FlowController.getLoggedAsUser(),
										new GettingUserStateResultCallback((DevelopmentBoardPresenter)presenter, new UpdateUserStateOnSelection(projectItemInfo)));
		DirBrowserServiceBroker.requestForReadingFile(
										FlowController.getLoggedAsUser(),
										projectItemInfo.getAssociatedData().getAbsolutePath(),
										null,
										null,
										new DirOperationFileReadingResultCallback((DevelopmentBoardPresenter)presenter, projectItemInfo));
	}

	@Override
	public void onSelectFile(SelectFileEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
}
