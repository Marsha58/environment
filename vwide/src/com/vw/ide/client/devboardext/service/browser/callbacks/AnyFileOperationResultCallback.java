package com.vw.ide.client.devboardext.service.browser.callbacks;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.FileStateChangedEvent;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.projects.ProjectManager;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserServiceBroker;
import com.vw.ide.client.ui.editorpanel.FileSheet;
import com.vw.ide.shared.OperationTypes;
import com.vw.ide.shared.servlet.remotebrowser.RequestFileOperationResult;

public class AnyFileOperationResultCallback extends RemoteBrowserServiceBroker.ResultCallback<RequestFileOperationResult> {

	private DevelopmentBoardPresenter owner = null;
	private boolean requestForUpdateDirContent = true;

	public AnyFileOperationResultCallback(DevelopmentBoardPresenter owner, boolean requestForUpdateDirContent) {
		this.owner = owner;
		this.requestForUpdateDirContent = requestForUpdateDirContent;
	}
	
	@Override
	public void handle(RequestFileOperationResult result) {
		if (result.getRetCode().intValue() != 0) {
			String messageAlert = "The operation '" + result.getOperation()
					+ "' failed.\r\nResult'" + result.getResult() + "'";
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Warning", messageAlert);
			alertMessageBox.show();
		}
		else {
			if (result.getOperationType() == OperationTypes.RENAME_FILE) {
				updateFileName(owner, result.getFileId(),result.getFileName(),result.getFileNewName()); 
			}
			else
			if (result.getOperationType() == OperationTypes.SAVE_FILE) {
				FileSheet savedFileSheet = (FileSheet)owner.getProjectManager().getAssociatedTabWidget(result.getFileId());
				savedFileSheet.setIsFileEdited(false);
				FileStateChangedEvent evt = new FileStateChangedEvent(result.getFileId(),false);
				owner.fireEvent(evt);
			}
		}
		if (requestForUpdateDirContent) {
			owner.getView().getProjectPanel().requestDirContent(null);
		}
		owner.fireEvent(new ServerLogEvent(result));
	}
	
	private void updateFileName(DevelopmentBoardPresenter presenter, Long fileId, String oldFileName, String newFileName) {
		ProjectManager projectManager = presenter.getProjectManager();
		projectManager.changeFileName(fileId, newFileName);
		FileSheet updatedFileSheet = (FileSheet) projectManager.getAssociatedTabWidget(fileId);
		presenter.getView().updateEditorFileSheetName(updatedFileSheet, newFileName);
	}
}
