package com.vw.ide.client.devboardext;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.service.browser.callbacks.AnyDirOperationResultCallback;
import com.vw.ide.client.devboardext.service.browser.callbacks.AnyFileOperationResultCallback;
import com.vw.ide.client.dialog.fileopen.FileOpenDialog;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserServiceBroker;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.client.utils.Utils;

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
				owner.getProjectManager().removeProject(owner.getSelectedProjectInTheProjectTree());
				RemoteBrowserServiceBroker.requestForDeletingProject(FlowController.getLoggedAsUser(),
										  owner.getSelectedItemInTheProjectTree().getAbsolutePath(),
										  owner.getSelectedItemInTheProjectTree().getProjectId(),
										  new AnyDirOperationResultCallback(owner));

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
				owner.getProjectManager().removeFile(owner.getSelectedItemInTheProjectTree().getFileId());
				Widget widget2delete = owner.getProjectManager().getAssociatedTabWidget(owner.getSelectedItemInTheProjectTree().getFileId());
				owner.getView().removeWidget(widget2delete);
				RemoteBrowserServiceBroker.requestForFileDeleting(FlowController.getLoggedAsUser(),
																  owner.getSelectedItemInTheProjectTree().getAbsolutePath(),
																  owner.getSelectedItemInTheProjectTree().getFileId(),
																  new AnyFileOperationResultCallback(owner, true));
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
					RemoteBrowserServiceBroker.requestForFileCreating(
											FlowController.getLoggedAsUser(),
											Utils.extractJustPath(owner.getSelectedItemInTheProjectTree().getAbsolutePath()),
											box.getValue(),
											owner.getSelectedItemInTheProjectTree().getProjectId(),
											1000L,
											"",
											new AnyDirOperationResultCallback(owner));
				}
			}
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
					RemoteBrowserServiceBroker.requestForFolderCreating(
											FlowController.getLoggedAsUser(),
											Utils.extractJustPath(owner.getSelectedItemInTheProjectTree().getAbsolutePath()),
											box.getValue(),
											new AnyDirOperationResultCallback(owner));
				}
			}
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
				RemoteBrowserServiceBroker.requestForFileCreating(
						FlowController.getLoggedAsUser(),
						box.getParentPath(),
						box.getFileName(0),
						box.getProjectId(),
						0L,
						box.getContent(0),
						new AnyDirOperationResultCallback(owner));
			}
		}
	}

	public static class RenameFileDialogHideHandler implements DialogHideHandler {

		private static class SaveFileDialogHideHandler implements DialogHideHandler {
			private ConfirmMessageBox box;
			private DevelopmentBoardPresenter owner;
			private String fullNewFileName;
			
			protected SaveFileDialogHideHandler(ConfirmMessageBox box, DevelopmentBoardPresenter owner, String fullNewFileName) {
				this.owner = owner;
				this.box = box;
				this.fullNewFileName = fullNewFileName;
			}
			
			@Override
			public void onDialogHide(DialogHideEvent event) {
				if (event.getHideButton().name().equalsIgnoreCase("YES")) {
					RemoteBrowserServiceBroker.requestForFileSaving(FlowController.getLoggedAsUser(),
							(String) box.getData("fullFileName"),
							(Long) box.getData("projectId"),
							(Long) box.getData("fileId"),
							(String) box.getData("content"),
							new AnyFileOperationResultCallback(owner, true));
					RemoteBrowserServiceBroker.requestForFileRenaming(FlowController.getLoggedAsUser(),
							owner.getSelectedItemInTheProjectTree().getAbsolutePath(),
							owner.getSelectedItemInTheProjectTree().getFileId(),
							fullNewFileName,
							new AnyFileOperationResultCallback(owner, false));
				}
			}
		}

		private PromptMessageBox box;
		private DevelopmentBoardPresenter owner;
		
		public RenameFileDialogHideHandler(PromptMessageBox box, DevelopmentBoardPresenter owner) {
			this.owner = owner;
			this.box = box;
		}
		
		@Override
		public void onDialogHide(DialogHideEvent event) {
			if (event.getHideButton().name().equalsIgnoreCase("ok")) {
				if (box.getValue() != null && owner.getSelectedItemInTheProjectTree() != null) {
					String justFilePath = Utils.extractJustPath(owner.getSelectedItemInTheProjectTree().getAbsolutePath());
					String fullNewFileName = justFilePath + "/" + box.getValue();
					Long fileId = owner.getSelectedItemInTheProjectTree().getFileId();
					FileSheet currFileSheet = (FileSheet)owner.getProjectManager().getAssociatedTabWidget(fileId);
					if (currFileSheet.getIsFileEdited()) {
						final ConfirmMessageBox saveConfirmBox = new ConfirmMessageBox("Confirm", "Save changes ?");
						saveConfirmBox.addDialogHideHandler(new RenameFileDialogHideHandler.SaveFileDialogHideHandler(saveConfirmBox, owner, fullNewFileName));
						saveConfirmBox.setData("fullFileName", owner.getSelectedItemInTheProjectTree().getAbsolutePath());
						saveConfirmBox.setData("fullNewFileName", fullNewFileName);
						saveConfirmBox.setData("projectId", owner.getSelectedItemInTheProjectTree().getProjectId());
						saveConfirmBox.setData("fileId", fileId);
						saveConfirmBox.setData("content", currFileSheet.getAceEditor().getText());
						saveConfirmBox.show();
					}
					else {
						RemoteBrowserServiceBroker.requestForFileRenaming(FlowController.getLoggedAsUser(),
								owner.getSelectedItemInTheProjectTree().getAbsolutePath(),
								owner.getSelectedItemInTheProjectTree().getFileId(),
								fullNewFileName,
								new AnyFileOperationResultCallback(owner, false));
					}
				}	
			}
		}
	}
}
