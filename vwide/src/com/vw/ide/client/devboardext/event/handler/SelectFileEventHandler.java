package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.service.browser.callbacks.BrowserDirOperationFileReadingResultCallback;
import com.vw.ide.client.devboardext.service.browser.callbacks.custom.handler.HandlerOnDirOperation;
import com.vw.ide.client.devboardext.service.userstate.callbacks.UserStateGettingResultCallback;
import com.vw.ide.client.devboardext.service.userstate.callbacks.UserStateUpdatingResultCallback;
import com.vw.ide.client.devboardext.service.userstate.callbacks.custom.handler.UserStateHandler;
import com.vw.ide.client.event.handler.SelectFileHandler;
import com.vw.ide.client.event.uiflow.SelectFileEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.service.remote.browser.DirBrowserServiceBroker;
import com.vw.ide.client.service.remote.userstate.RemoteUserStateServiceBroker;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.userstate.UserStateInfo;

public class SelectFileEventHandler extends Presenter.PresenterEventHandler implements SelectFileHandler {

	private static class UpdateUserStateOnReadOperation implements HandlerOnDirOperation {

		private DevelopmentBoardPresenter presenter;
		private ProjectItemInfo projectItemInfo;
		private UserStateInfo userState;
		
		public UpdateUserStateOnReadOperation(DevelopmentBoardPresenter presenter, ProjectItemInfo projectItemInfo, UserStateInfo userState) {
			this.presenter = presenter;
			this.projectItemInfo = projectItemInfo;
			this.userState = userState;
		}
		
		@Override
		public void handle(RequestDirOperationResult result) {
			if (result.getRetCode().intValue() != 0) {
				String messageAlert = "The operation '" + result.getOperation()
									+ "' failed.\r\nResult'" + result.getResult() + "'";
				AlertMessageBox alertMessageBox = new AlertMessageBox("Warning", messageAlert);
				alertMessageBox.show();
			} 
			else {
				if (!projectItemInfo.isAlreadyOpened()) {
					projectItemInfo.getAssociatedData().setContent(result.getTextFile());
					presenter.getView().getAddOperationBlock().addNewFileTabItem(projectItemInfo);
					userState.addFileToOpenedFiles(projectItemInfo.getAssociatedData());
					RemoteUserStateServiceBroker.requestForUpdateUserState(
													FlowController.getLoggedAsUser(),
													userState,
													new UserStateUpdatingResultCallback(presenter));
					if (projectItemInfo.getLastLine() != -1 && projectItemInfo.getLastPos() != -1) {
						projectItemInfo.getFileSheet().setCursorPosition(projectItemInfo.getLastLine(),
																		 projectItemInfo.getLastPos());
						projectItemInfo.setLastLine(-1);
						projectItemInfo.setLastPos(-1);
					}
				}
			}
		}
		
	}
	
	private static class UpdateUserStateOnSelection implements UserStateHandler {

		private ProjectItemInfo projectItemInfo;
		private DevelopmentBoardPresenter presenter;
		
		public UpdateUserStateOnSelection(DevelopmentBoardPresenter presenter, ProjectItemInfo projectItemInfo) {
			this.projectItemInfo = projectItemInfo;
			this.presenter = presenter;
		}
		
		@Override
		public void handle(UserStateInfo userState) {
			userState.setFileIdSelected(projectItemInfo.getAssociatedData());
			userState.setProjectIdSelected(projectItemInfo.getProjectDescription());
			DirBrowserServiceBroker.requestForReadingFile(
					FlowController.getLoggedAsUser(),
					projectItemInfo.getAssociatedData().getAbsolutePath(),
					projectItemInfo.getAssociatedData().getName(),
					null,
					null,
					new BrowserDirOperationFileReadingResultCallback((DevelopmentBoardPresenter)presenter,
																	new UpdateUserStateOnReadOperation(presenter, projectItemInfo, userState)));
		}
	}
	
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		ProjectItemInfo projectItemInfo = ((SelectFileEvent)event).getFileItemInfo();
		RemoteUserStateServiceBroker.requestForGettingUserState(FlowController.getLoggedAsUser(),
										new UserStateGettingResultCallback((DevelopmentBoardPresenter)presenter,
																			new UpdateUserStateOnSelection((DevelopmentBoardPresenter)presenter, projectItemInfo)));
	}

	@Override
	public void onSelectFile(SelectFileEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
}
