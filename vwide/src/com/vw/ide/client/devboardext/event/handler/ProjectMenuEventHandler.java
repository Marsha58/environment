package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardDialogHandlers;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.dialog.fileopen.FileOpenDialog;
import com.vw.ide.client.dialog.newvwmlproj.NewVwmlProjectDialogExt;
import com.vw.ide.client.event.handler.ProjectMenuHandler;
import com.vw.ide.client.event.uiflow.ProjectMenuEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.projects.ProjectManager;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

public class ProjectMenuEventHandler extends Presenter.PresenterEventHandler implements ProjectMenuHandler {
	
	private static String s_newVwmlProjectCaption = "New VWML project";

	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (ProjectMenuEvent)event);
	}

	@Override
	public void onProjectMenuClick(ProjectMenuEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	protected void process(DevelopmentBoardPresenter presenter, ProjectMenuEvent event) {
		String menuId = event.getMenuId();
		presenter.setSelectedItemInTheProjectTree(presenter.getView().getProjectPanel().getSelectedItem4ContextMenu());
		if (menuId != null && presenter.getSelectedItemInTheProjectTree() != null) {
			switch (menuId) {
			case "new_project":
				String sPath;
				if (presenter.getSelectedItemInTheProjectTree() == null) {
					sPath = "";
				} else {
					sPath = Utils.extractJustPath(presenter.getSelectedItemInTheProjectTree().getAbsolutePath());
				}
				makeProjectNew(presenter, sPath);
				break;
			case "delete_project":
				doDelCurrentProject(presenter, presenter.getSelectedItemInTheProjectTree());
				break;
			case "import_file":
				doImportFile(presenter, presenter.getSelectedItemInTheProjectTree());
				break;
			case "new_file":
				doCreateFile(presenter, presenter.getSelectedItemInTheProjectTree());
				break;
			case "rename_file":
				doRenameFile(presenter, presenter.getSelectedItemInTheProjectTree());
				break;
			case "delete_file":
				doDelCurrentFile(presenter, presenter.getSelectedItemInTheProjectTree());
				break;
			case "new_folder":
				doCreateFolder(presenter, presenter.getSelectedItemInTheProjectTree());
				break;
			default:
				break;
			}
		}
	}

	private void makeProjectNew(DevelopmentBoardPresenter presenter, String path4project) {
		NewVwmlProjectDialogExt d = new NewVwmlProjectDialogExt();
		d.setLoggedAsUser(FlowController.getLoggedAsUser());
		d.setPath4project(path4project);
		d.associatePresenter(presenter);
		d.setSize("480", "380");
		d.showCenter(s_newVwmlProjectCaption, null);
	}

	private void doDelCurrentProject(DevelopmentBoardPresenter presenter, FileItemInfo fileItemInfo) {
		ProjectManager projectManager = presenter.getProjectManager();
		presenter.setSelectedProjectInTheProjectTree(projectManager.getProjectIdByProjectPath(FlowController.getLoggedAsUser(), fileItemInfo.getAbsolutePath()));
		String msg = Format.substitute(
				"Are you sure you want to delete project '{0}'?",
				fileItemInfo.getName());
		ConfirmMessageBox box = new ConfirmMessageBox("Confirm", msg);
		box.addDialogHideHandler(new DevelopmentBoardDialogHandlers.DeletedProjectDialogHideHandler(presenter));
		box.show();
	}

	private void doCreateFile(DevelopmentBoardPresenter presenter, FileItemInfo fileItemInfo) {
		ProjectManager projectManager = presenter.getProjectManager();
		presenter.setSelectedProjectInTheProjectTree(projectManager.getProjectIdByProjectPath(FlowController.getLoggedAsUser(), fileItemInfo.getAbsolutePath()));
		final PromptMessageBox box = new PromptMessageBox("Name", "Please enter file name:");
		box.addDialogHideHandler(new DevelopmentBoardDialogHandlers.CreateFileDialogHideHandler(box, presenter));
		box.show();
	}

	private void doCreateFolder(DevelopmentBoardPresenter presenter, FileItemInfo fileItemInfo) {
		final PromptMessageBox box = new PromptMessageBox("Name", "Please enter folder name:");
		box.addDialogHideHandler(new DevelopmentBoardDialogHandlers.CreateFolderDialogHideHandler(box, presenter));
		box.show();
	}

	private void doImportFile(DevelopmentBoardPresenter presenter, FileItemInfo fileItemInfo) {
		ProjectManager projectManager = presenter.getProjectManager();
		Long projectId = projectManager.getProjectIdByProjectPath(FlowController.getLoggedAsUser(), fileItemInfo.getAbsolutePath());
		String parentPath = Utils.extractJustPath(fileItemInfo.getAbsolutePath());
		final FileOpenDialog box = new FileOpenDialog();
		box.setEditLabelText("Select file");
		box.setParentPath(parentPath);
		box.setProjectId(projectId);
		box.addDialogHideHandler(new DevelopmentBoardDialogHandlers.ImportFileDialogHideHandler(box, presenter));
		box.show();
	}

	private void doRenameFile(DevelopmentBoardPresenter presenter, FileItemInfo fileItemInfo) {
		PromptMessageBox renameBox = new PromptMessageBox("Name", "Please enter new file name:");
		renameBox.getTextField().setText(presenter.getSelectedItemInTheProjectTree().getName());
		renameBox.addDialogHideHandler(new DevelopmentBoardDialogHandlers.RenameFileDialogHideHandler(renameBox, presenter));
		renameBox.show();
	}

	private void doDelCurrentFile(DevelopmentBoardPresenter presenter, FileItemInfo fileItemInfo) {
		String msg = Format.substitute("Are you sure you want to delete file '{0}'?", fileItemInfo.getName());
		ConfirmMessageBox box = new ConfirmMessageBox("Confirm", msg);
		box.addDialogHideHandler(new DevelopmentBoardDialogHandlers.DeleteFileDialogHideHandler(presenter));
		box.show();
	}
}

