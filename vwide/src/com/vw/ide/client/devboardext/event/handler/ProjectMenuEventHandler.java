package com.vw.ide.client.devboardext.event.handler;

import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardDialogHandlers;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.dialog.fileopen.FileOpenDialog;
import com.vw.ide.client.dialog.vwmlproj.VwmlProjectDialog;
import com.vw.ide.client.event.handler.ProjectMenuHandler;
import com.vw.ide.client.event.uiflow.ProjectMenuEvent;
import com.vw.ide.client.event.uiflow.StartProjectExecutionEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;

public class ProjectMenuEventHandler extends Presenter.PresenterEventHandler implements ProjectMenuHandler {
	
	private static String s_newVwmlProjectCaption = "New VWML project";
	private static String s_vwmlProjectCaption = "VWML project";

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
		presenter.setSelectedItemInTheProjectTree(presenter.getView().getProjectPanel().getTreeSelectedItem());
		if (menuId != null && presenter.getSelectedItemInTheProjectTree() != null) {
			switch (menuId) {
			case "new_project":
				makeProjectNew(presenter);
				break;
			case "edit_project":
				editProjectProps(presenter);
				break;
			case "delete_project":
				doDelCurrentProject(presenter, presenter.getSelectedItemInTheProjectTree());
				break;
			case "import_project":
				doImportProject(presenter);
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
			case "start_execution_project":
				doStartProjectExecution(presenter, presenter.getSelectedItemInTheProjectTree());
				break;
			default:
				break;
			}
		}
	}

	private void makeProjectNew(DevelopmentBoardPresenter presenter) {
		VwmlProjectDialog d = new VwmlProjectDialog(null, null, VwmlProjectDialog.EditMode.NEW);
		d.setLoggedAsUser(FlowController.getLoggedAsUser());
		d.associatePresenter(presenter);
		d.setSize("480", "350");
		d.showCenter(s_newVwmlProjectCaption, null);
	}

	private void editProjectProps(DevelopmentBoardPresenter presenter) {
		if (presenter.getSelectedItemInTheProjectTree() != null) {
	 		ProjectDescription projectDescription = presenter.getSelectedItemInTheProjectTree().getProjectDescription();
			VwmlProjectDialog d = new VwmlProjectDialog(projectDescription, null, VwmlProjectDialog.EditMode.EDIT_VWML_SETTINGS);
			d.setLoggedAsUser(FlowController.getLoggedAsUser());
			d.associatePresenter(presenter);
			d.setSize("480", "350");
			d.showCenter(s_vwmlProjectCaption, null);
		}
		else {
			AlertMessageBox box = new AlertMessageBox("Warning", "Please do project selection");
			box.show();
		}
	}
	
	private void doDelCurrentProject(DevelopmentBoardPresenter presenter, ProjectItemInfo projectItemInfo) {
		String msg = Format.substitute(
				"Are you sure you want to delete project '{0}'?",
				projectItemInfo.getAssociatedData().getName());
		ConfirmMessageBox box = new ConfirmMessageBox("Confirm", msg);
		box.addDialogHideHandler(new DevelopmentBoardDialogHandlers.DeletedProjectDialogHideHandler(presenter));
		box.show();
	}

	private void doImportProject(DevelopmentBoardPresenter presenter) {
		final FileOpenDialog box = new FileOpenDialog();
		box.setEditLabelText("Select project to import (main VWML project file)");
		box.setParentPath("/");
		box.addDialogHideHandler(new DevelopmentBoardDialogHandlers.ImportProjectDialogHideHandler(box, presenter));
		box.show();
	}
	
	private void doCreateFile(DevelopmentBoardPresenter presenter, ProjectItemInfo projectItemInfo) {
		final PromptMessageBox box = new PromptMessageBox("Name", "Please enter file name:");
		box.addDialogHideHandler(new DevelopmentBoardDialogHandlers.CreateFileDialogHideHandler(box, presenter));
		box.show();
	}

	private void doCreateFolder(DevelopmentBoardPresenter presenter, ProjectItemInfo projectItemInfo) {
		final PromptMessageBox box = new PromptMessageBox("Name", "Please enter folder name:");
		box.addDialogHideHandler(new DevelopmentBoardDialogHandlers.CreateFolderDialogHideHandler(box, presenter));
		box.show();
	}

	private void doImportFile(DevelopmentBoardPresenter presenter, ProjectItemInfo projectItemInfo) {
		String parentPath = Utils.extractJustPath(projectItemInfo.getAssociatedData().getAbsolutePath());
		final FileOpenDialog box = new FileOpenDialog();
		box.setEditLabelText("Select file to import");
		box.setParentPath(parentPath);
		box.addDialogHideHandler(new DevelopmentBoardDialogHandlers.ImportFileDialogHideHandler(box, presenter));
		box.show();
	}

	private void doRenameFile(DevelopmentBoardPresenter presenter, ProjectItemInfo projectItemInfo) {
		PromptMessageBox renameBox = new PromptMessageBox("Name", "Please enter new file name:");
		renameBox.getTextField().setText(projectItemInfo.getAssociatedData().getName());
		renameBox.addDialogHideHandler(new DevelopmentBoardDialogHandlers.RenameFileDialogHideHandler(renameBox, presenter, projectItemInfo));
		renameBox.show();
	}

	private void doDelCurrentFile(DevelopmentBoardPresenter presenter, ProjectItemInfo projectItemInfo) {
		String msg = Format.substitute("Are you sure you want to delete file '{0}'?", projectItemInfo.getAssociatedData().getName());
		ConfirmMessageBox box = new ConfirmMessageBox("Confirm", msg);
		box.addDialogHideHandler(new DevelopmentBoardDialogHandlers.DeleteFileDialogHideHandler(presenter));
		box.show();
	}
	
	private void doStartProjectExecution(DevelopmentBoardPresenter presenter, ProjectItemInfo projectItemInfo) {
		presenter.fireEvent(new StartProjectExecutionEvent(projectItemInfo.getProjectDescription()));
	}

}

