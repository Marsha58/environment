package com.vw.ide.client.devboardext.operation.block;

import com.vw.ide.client.devboardext.DevelopmentBoard;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.operation.block.RenameOperationBlock.RenameProjectItemCallback;
import com.vw.ide.client.devboardext.service.projectmanager.callbacks.ProjectMoveItemResultCallback;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerServiceBroker;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;

public class MoveOperationBlock {

	private DevelopmentBoard devBoard;

	public MoveOperationBlock(DevelopmentBoard devBoard) {
		this.devBoard = devBoard;
	}
	
	public void move(ProjectItemInfo itemToMove, ProjectItemInfo neighbour) {
		if (neighbour.getAssociatedData().isDir()) {
			devBoard.getProjectPanel().moveItemOnTreeBranchView(itemToMove,
																neighbour,
																new RenameProjectItemCallback(devBoard.getRenameOperationBlock()));
			itemToMove.getProjectDescription().setProjectFiles((devBoard.getUpdatedListOfFiles(itemToMove.getProjectDescription())));
			ProjectManagerServiceBroker.requestForMovingItemOnProject(itemToMove.getProjectDescription(),
																	itemToMove.getAssociatedData(),
																	neighbour.getAssociatedData(),
																	new ProjectMoveItemResultCallback((DevelopmentBoardPresenter)devBoard.getAssociatedPresenter(),
																									itemToMove.getProjectDescription()));
		}
	}
}
