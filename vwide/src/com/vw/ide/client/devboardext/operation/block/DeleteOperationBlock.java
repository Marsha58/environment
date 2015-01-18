package com.vw.ide.client.devboardext.operation.block;

import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;

public class DeleteOperationBlock {

	private DevelopmentBoard devBoard;
	
	public DeleteOperationBlock(DevelopmentBoard devBoard) {
		this.devBoard = devBoard;
	}
	
	public void deleteProjectItem(ProjectItemInfo itemInfo) {
		if (!itemInfo.isMarkAsProject()) {
			devBoard.getTopPanel().delItemFromScrollMenu(itemInfo);
			devBoard.getEditorPanel().getTabPanel().remove(itemInfo.getFileSheet());
			devBoard.getConsoles().getSearchConsoleTab().removeByFileItemInfo(itemInfo.getAssociatedData());
		}
		else {
			devBoard.getConsoles().getSearchConsoleTab().removeByProjectName(itemInfo.getProjectDescription().getProjectName());
		}
		if (devBoard.getEditorPanel().getTabPanel().getWidgetCount() == 0) {
			devBoard.setTextForEditorContentPanel(null);
			devBoard.getProjectPanel().selectParentOf(itemInfo);
		}
		devBoard.getProjectPanel().deleteItemOnTreeBranchView(itemInfo);
	}
	
	public void deleteProjectItemFromViewOnly(ProjectItemInfo itemInfo) {
		devBoard.getProjectPanel().deleteItemOnTreeBranchView(itemInfo);
	}
}
