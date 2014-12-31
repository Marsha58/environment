package com.vw.ide.client.devboardext;

import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.service.projectmanager.callbacks.ProjectAddFileResultCallback;
import com.vw.ide.client.devboardext.service.projectmanager.callbacks.ProjectDeletionResultCallback;
import com.vw.ide.client.devboardext.service.projectmanager.callbacks.ProjectRemoveFileResultCallback;
import com.vw.ide.client.devboardext.service.projectmanager.callbacks.ProjectRenameFileResultCallback;
import com.vw.ide.client.dialog.fileopen.FileOpenDialog;
import com.vw.ide.client.projects.FilesTypesEnum;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerServiceBroker;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.projectmanager.RemoteProjectManagerService;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectImportResult;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

/**
 * Set of dialog handlers
 * @author Oleg
 *
 */
public class DevelopmentBoardDialogHandlers {
	
	public static class DeletedProjectDialogHideHandler implements DialogHideHandler {

		private DevelopmentBoardPresenter owner;
		
		public DeletedProjectDialogHideHandler(DevelopmentBoardPresenter owner) {
			this.owner = owner;
		}
		
		@Override
		public void onDialogHide(DialogHideEvent event) {
			String s = event.getHideButton().name();

			if (s.equalsIgnoreCase("YES")) {
				ProjectManagerServiceBroker.requestForDeletingProject(
										  owner.getSelectedItemInTheProjectTree().getProjectDescription(),
										  new ProjectDeletionResultCallback(owner, owner.getSelectedItemInTheProjectTree()));
			}
		}
	};

	public static class DeleteFileDialogHideHandler implements DialogHideHandler {
		private DevelopmentBoardPresenter owner;
		
		public DeleteFileDialogHideHandler(DevelopmentBoardPresenter owner) {
			this.owner = owner;
		}
		
		@Override
		public void onDialogHide(DialogHideEvent event) {
			String s = event.getHideButton().name();

			if (s.equalsIgnoreCase("YES") && owner.getSelectedItemInTheProjectTree() != null) {
				ProjectManagerServiceBroker.requestForRemovingFileFromProject(owner.getSelectedItemInTheProjectTree().getProjectDescription(),
																			  owner.getSelectedItemInTheProjectTree().getAssociatedData(),
																			  new ProjectRemoveFileResultCallback(owner, owner.getSelectedItemInTheProjectTree()));
			}
		}
	}

	public static class CreateFileDialogHideHandler implements DialogHideHandler {
		private PromptMessageBox box;
		private DevelopmentBoardPresenter owner;
		
		public CreateFileDialogHideHandler(PromptMessageBox box, DevelopmentBoardPresenter owner) {
			this.owner = owner;
			this.box = box;
		}
		
		@Override
		public void onDialogHide(DialogHideEvent event) {
			if (box.getValue() != null) {
				if (owner.isValidFileName(box.getValue())) {
					createFileAndAddToProject(box.getValue());
				}
			}
		}
		
		private void createFileAndAddToProject(String fileName) {
			if (!fileName.contains(".")) {
				fileName += "." + FilesTypesEnum.VWML;
			}
			ProjectItemInfo projectItemInfo = owner.getSelectedItemInTheProjectTree();
			String path = deducePathByProjectItem(projectItemInfo);
			FileItemInfo fileInfo = new FileItemInfo(fileName, path, false);
			ProjectManagerServiceBroker.requestForAddingFileToProject(FlowController.getLoggedAsUser(),
					projectItemInfo.getProjectDescription(),
					fileInfo,
					new ProjectAddFileResultCallback(owner, projectItemInfo, fileInfo));
		}
	}

	public static class CreateFolderDialogHideHandler implements DialogHideHandler {
		private PromptMessageBox box;
		private DevelopmentBoardPresenter owner;
		
		public CreateFolderDialogHideHandler(PromptMessageBox box, DevelopmentBoardPresenter owner) {
			this.owner = owner;
			this.box = box;
		}
		
		@Override
		public void onDialogHide(DialogHideEvent event) {
			if (box.getValue() != null) {
				if (owner.isValidFileName(box.getValue())) {
					createFolderAndAddToProject(box.getValue());
				}
			}
		}
		
		private void createFolderAndAddToProject(String fileName) {
			ProjectItemInfo projectItemInfo = owner.getSelectedItemInTheProjectTree();
			String path = deducePathByProjectItem(projectItemInfo);
			FileItemInfo fileInfo = new FileItemInfo(fileName, path + "/" + fileName, true);
			ProjectManagerServiceBroker.requestForAddingFileToProject(FlowController.getLoggedAsUser(),
					projectItemInfo.getProjectDescription(),
					fileInfo,
					new ProjectAddFileResultCallback(owner, projectItemInfo, fileInfo));
		}		
	}

	public static class ImportFileDialogHideHandler implements DialogHideHandler {
		private FileOpenDialog box;
		private DevelopmentBoardPresenter owner;
		
		public ImportFileDialogHideHandler(FileOpenDialog box, DevelopmentBoardPresenter owner) {
			this.owner = owner;
			this.box = box;
		}
		
		@Override
		public void onDialogHide(DialogHideEvent event) {
			if (box.getLoadedFiles() > 0) {
				importFileAndAddToProject(box.getFileName(0), box.getContent(0));
			}
		}
	
		private void importFileAndAddToProject(String fileName, String content) {
			ProjectItemInfo projectItemInfo = owner.getSelectedItemInTheProjectTree();
			String path = deducePathByProjectItem(projectItemInfo);
			FileItemInfo fileInfo = new FileItemInfo(fileName, path, false);
			fileInfo.setContent(content);
			ProjectManagerServiceBroker.requestForAddingFileToProject(FlowController.getLoggedAsUser(),
					projectItemInfo.getProjectDescription(),
					fileInfo,
					new ProjectAddFileResultCallback(owner, projectItemInfo, fileInfo));
		}		
	}

	public static class ImportProjectDialogHideHandler implements DialogHideHandler {
		
		protected static class ImportProjectCallback extends ResultCallback<RequestProjectImportResult> {

			public ImportProjectCallback() {
				
			}
			
			@Override
			public void handle(RequestProjectImportResult result) {
			}
		}
		
		private FileOpenDialog box;
		private DevelopmentBoardPresenter owner;
		
		public ImportProjectDialogHideHandler(FileOpenDialog box, DevelopmentBoardPresenter owner) {
			this.owner = owner;
			this.box = box;
		}
		
		@Override
		public void onDialogHide(DialogHideEvent event) {
			if (box.getLoadedFiles() > 0) {
				importProject(box.getFileName(0), box.getContent(0));
			}
		}

		private void importProject(String mainProjectFileName, String content) {
			FileItemInfo fileInfo = new FileItemInfo(mainProjectFileName, "", false);
			content = btoa(content);
			fileInfo.setContent(content);
			ProjectManagerServiceBroker.requestForImportProject(FlowController.getLoggedAsUser(),
																fileInfo,
																new ImportProjectCallback());
		}
		
		native String btoa(String content) /*-{
			return btoa(content);
		}-*/;
	}
	
	public static class RenameFileDialogHideHandler implements DialogHideHandler {

		private static class SaveFileDialogHideHandler implements DialogHideHandler {
			private DevelopmentBoardPresenter owner;
			private String fullNewFileName;
			private String content;
			
			protected SaveFileDialogHideHandler(DevelopmentBoardPresenter owner, String fullNewFileName, String content) {
				this.owner = owner;
				this.content = content;
				this.fullNewFileName = fullNewFileName;
			}
			
			@Override
			public void onDialogHide(DialogHideEvent event) {
				if (event.getHideButton().name().equalsIgnoreCase("YES")) {
					RenameFileDialogHideHandler.renameFile(owner, fullNewFileName, content);
				}
			}
		}

		private PromptMessageBox box;
		private DevelopmentBoardPresenter owner;
		private ProjectItemInfo projectItemInfo;
		
		public RenameFileDialogHideHandler(PromptMessageBox box, DevelopmentBoardPresenter owner, ProjectItemInfo projectItemInfo) {
			this.owner = owner;
			this.box = box;
			this.projectItemInfo = projectItemInfo;
		}
		
		@Override
		public void onDialogHide(DialogHideEvent event) {
			if (event.getHideButton().name().equalsIgnoreCase("ok")) {
				if (box.getValue() != null && owner.getSelectedItemInTheProjectTree() != null) {
					String newFileName = box.getValue();
					if (projectItemInfo.isEdited()) {
						final ConfirmMessageBox saveConfirmBox = new ConfirmMessageBox("Confirm", "Save changes ?");
						saveConfirmBox.addDialogHideHandler(new RenameFileDialogHideHandler.SaveFileDialogHideHandler(owner, newFileName, projectItemInfo.getFileSheet().getAceEditor().getText()));
						saveConfirmBox.show();
					}
					else {
						renameFile(owner, newFileName, null);
					}
				}	
			}
		}
		
		private static void renameFile(DevelopmentBoardPresenter owner, String newFileName, String content) {
			ProjectItemInfo projectItemInfo = owner.getSelectedItemInTheProjectTree();
			FileItemInfo newFileInfo = new FileItemInfo(newFileName,
										projectItemInfo.getAssociatedData().getAbsolutePath(),
										projectItemInfo.getAssociatedData().isDir());
			newFileInfo.setContent(content);
			newFileInfo.setRelPath(projectItemInfo.getAssociatedData().getRelPath());
			owner.getView().getRenameOperationBlock().renameItemAndUpdateProject(projectItemInfo, newFileInfo);
			ProjectManagerServiceBroker.requestForRenamingFileOnProject(
							projectItemInfo.getProjectDescription(),
							projectItemInfo.getAssociatedData(),
							newFileInfo,
							new ProjectRenameFileResultCallback(owner, projectItemInfo.getProjectDescription()));
		}
	}
	
	protected static String deducePathByProjectItem(ProjectItemInfo projectItemInfo) {
		String path = null;
		if (projectItemInfo.isMarkAsProject()) {
			path = Utils.createFullProjectPath(projectItemInfo.getProjectDescription());
		}
		else {
			path = projectItemInfo.getAssociatedData().getAbsolutePath();
		}
		return path;
	}
}
