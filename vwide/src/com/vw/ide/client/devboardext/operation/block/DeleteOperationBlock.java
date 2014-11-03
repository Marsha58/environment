package com.vw.ide.client.devboardext.operation.block;

import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;

public class DeleteOperationBlock {

	private DevelopmentBoard devBoard;
	
	public DeleteOperationBlock(DevelopmentBoard devBoard) {
		this.devBoard = devBoard;
	}
	
	public void deleteProjectItem(ProjectItemInfo itemInfo) {
		devBoard.getProjectPanel().deleteBranchView(itemInfo);
		devBoard.getTopPanel().delItemFromScrollMenu(itemInfo);
		devBoard.getEditorPanel().getTabPanel().remove(itemInfo.getFileSheet());
		if (devBoard.getEditorPanel().getTabPanel().getWidgetCount() == 0) {
			devBoard.setTextForEditorContentPanel(null);
			devBoard.getProjectPanel().selectParentOf(itemInfo);
		}
	}
}
