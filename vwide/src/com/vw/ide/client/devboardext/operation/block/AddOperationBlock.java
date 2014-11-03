package com.vw.ide.client.devboardext.operation.block;

import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

public class AddOperationBlock {

	private DevelopmentBoard devBoard;
	
	public AddOperationBlock(DevelopmentBoard devBoard) {
		this.devBoard = devBoard;
	}

	public void addProjectItemAndSelect(ProjectItemInfo parent, FileItemInfo toAdd) {
		devBoard.getProjectPanel().buildTreeBranchView(parent, toAdd, null);
		devBoard.getProjectPanel().selectByKey(toAdd.generateKey());
	}
	
	public void addNewFileTabItem(ProjectItemInfo itemInfo) {
		TabItemConfig tabItemConfig = new TabItemConfig(itemInfo.getAssociatedData().getName());
		tabItemConfig.setClosable(true);
		FileSheet newFileSheet = new FileSheet(devBoard.getAssociatedPresenter(), itemInfo);
		newFileSheet.constructEditor(itemInfo.getAssociatedData().getContent(),
									FileItemInfo.recognizeFileType(itemInfo.getAssociatedData().getName()));
		devBoard.getEditorPanel().getTabPanel().add(newFileSheet, tabItemConfig);
		itemInfo.setFileSheet(newFileSheet);
		devBoard.scrollToTab(itemInfo);
		devBoard.getTopPanel().addItemToScrollMenu(itemInfo);
		itemInfo.setAlreadyOpened(true);
	}
}
