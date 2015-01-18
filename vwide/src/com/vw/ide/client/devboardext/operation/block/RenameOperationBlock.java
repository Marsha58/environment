package com.vw.ide.client.devboardext.operation.block;

import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

public class RenameOperationBlock {

	private DevelopmentBoard devBoard;
	
	public static class RenameProjectItemCallback {
		
		private RenameOperationBlock renameBlock;
		
		public RenameProjectItemCallback(RenameOperationBlock renameBlock) {
			this.renameBlock = renameBlock;
		}
		
		public void rename(ProjectItemInfo oldItemInfo, ProjectItemInfo newItemInfo) {
			renameBlock.renameOpenedProjectItem(oldItemInfo, newItemInfo);
		}
	}
	
	public RenameOperationBlock(DevelopmentBoard devBoard) {
		this.devBoard = devBoard;
	}

	public void renameItemAndUpdateProject(ProjectItemInfo itemInfo, FileItemInfo toRename) {
		renameItemOnTreeBranchView(itemInfo, toRename, new RenameProjectItemCallback(this));
		itemInfo.getProjectDescription().setProjectFiles((devBoard.getUpdatedListOfFiles(itemInfo.getProjectDescription())));
	}

	protected void renameItemOnTreeBranchView(ProjectItemInfo itemInfo, FileItemInfo toRename, RenameProjectItemCallback renameCbk) {
		devBoard.getProjectPanel().renameItemOnTreeBranchView(itemInfo, toRename, renameCbk);
	}

	protected void renameOpenedProjectItem(ProjectItemInfo origItemInfo, ProjectItemInfo newItemInfo) {
		for(int i = 0; i < devBoard.getEditorPanel().getTabPanel().getWidgetCount(); i++) {
			FileSheet fs = (FileSheet)devBoard.getEditorPanel().getTabPanel().getWidget(i);
			if (fs.getItemInfo() == origItemInfo) {
				fs.setItemInfo(newItemInfo);
				newItemInfo.setFileSheet(fs);
				devBoard.getTopPanel().delItemFromScrollMenu(origItemInfo);
				devBoard.updateFileItemTabName(newItemInfo);
				devBoard.setTextForEditorContentPanel(newItemInfo.getAssociatedData().getAbsolutePath() + "/" + newItemInfo.getAssociatedData().getName());
			}
		}
		// last search should be updated also
		devBoard.getConsoles().getSearchConsoleTab().renameByFileItemInfo(origItemInfo.getAssociatedData(), newItemInfo.getAssociatedData());
	}
}
