package com.vw.ide.client.devboardext.service.browser.callbacks;

import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserServiceBroker;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;

public class DirOperationFileReadingResultCallback extends RemoteBrowserServiceBroker.ResultCallback<RequestDirOperationResult> {

	private DevelopmentBoardPresenter owner = null;
	
	public DirOperationFileReadingResultCallback(DevelopmentBoardPresenter owner) {
		this.owner = owner;
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
			owner.getProjectManager().checkFile(result.getPath());
			if (owner.getProjectManager().checkIsFileOpened(result.getPath())) {
				Long fileId = owner.getProjectManager().getFileIdByFilePath(result.getPath());
				FileSheet curFileSheet = (FileSheet)owner.getProjectManager().getAssociatedTabWidgetsContext().get(fileId);
				owner.getView().scrollToTab(curFileSheet, true);
			}
			else {
				owner.getProjectManager().addFileToOpenedFilesContext(
								result.getProjectId(),
								result.getFileId(),
								new FileItemInfo(Utils.extractJustFileName(result.getPath()), result.getPath(), false));
				FileSheet newFileSheet = new FileSheet(owner, result.getProjectId(), result.getFileId(), result.getPath());
				newFileSheet.constructEditor(result.getTextFile(),FileItemInfo.getFileType(Utils.extractJustFileName(result.getPath())));
				owner.getProjectManager().setAssociatedTabWidget(result.getFileId(), newFileSheet);
				TabItemConfig tabItemConfig = new TabItemConfig(Utils.extractJustFileName(result.getPath()));
				tabItemConfig.setClosable(true);
				owner.getView().addNewFileTabItem(newFileSheet, tabItemConfig);
				FileSheet curFileSheet = (FileSheet)owner.getProjectManager().getAssociatedTabWidgetsContext().get(result.getFileId());
				owner.getView().scrollToTab(curFileSheet, true);
				owner.getView().addFileItemToScrollMenu(result.getPath(), result.getFileId());
			}
		}
		owner.fireEvent(new ServerLogEvent(result));
	}
}
