package com.vw.ide.client.devboardext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.vw.ide.client.dialog.fileopen.FileOpenDialog;
import com.vw.ide.client.dialog.newvwmlproj.NewVwmlProjectDialogExt;
import com.vw.ide.client.event.uiflow.AceColorThemeChangedEvent;
import com.vw.ide.client.event.uiflow.EditorTabClosedEvent;
import com.vw.ide.client.event.uiflow.FileEditedEvent;
import com.vw.ide.client.event.uiflow.GetDirContentEvent;
import com.vw.ide.client.event.uiflow.LogoutEvent;
import com.vw.ide.client.event.uiflow.ProjectMenuEvent;
import com.vw.ide.client.event.uiflow.SaveFileEvent;
import com.vw.ide.client.event.uiflow.SelectFileEvent;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.model.BaseDto;
import com.vw.ide.client.model.FileDto;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.projects.ProjectItem;
import com.vw.ide.client.projects.ProjectItemImpl;
import com.vw.ide.client.projects.ProjectManager;
import com.vw.ide.client.projects.ProjectManagerImpl;
import com.vw.ide.client.service.ProcessedResult;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForAnyOperation;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForDirOperation;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForFileOperation;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForUserState;
import com.vw.ide.client.ui.projectpanel.ProjectPanel;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.client.ui.toppanel.TopPanel;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.shared.OperationTypes;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowserAsync;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestFileOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestProjectCreationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestUserStateResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestedDirScanResult;

/**
 * Development board screen
 * 
 * @author Oleg
 * 
 */
public class DevelopmentBoardPresenter extends Presenter {

	public final HandlerManager eventBus;
	private final PresenterViewerLink view;
	public ProjectManager projectManager;

	private static String s_HelpAboutCaption = "About";
	private static String s_newVwmlProjectCaption = "New VWML project";

	public DevelopmentBoardPresenter(HandlerManager eventBus,
			PresenterViewerLink view) {
		this.eventBus = eventBus;
		this.view = view;
		if (view != null) {
			view.associatePresenter(this);
		}
		projectManager = new ProjectManagerImpl();
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		requestForGettingUserState(getLoggedAsUser());
	}

	public void fireEvent(GwtEvent<?> event) {
		// eventBus.fireEvent(event);

		if (event instanceof SelectFileEvent) {
			requestForReadingFile(((SelectFileEvent) event).getFileItemInfo()
					.getAbsolutePath(), ((SelectFileEvent) event)
					.getFileItemInfo().getProjectId(),
					((SelectFileEvent) event).getFileItemInfo().getFileId());
		} else if (event instanceof SaveFileEvent) {
			doSaveCurrentFile();
		} else if (event instanceof FileEditedEvent) {
			doChangeFileStateToEdited((FileEditedEvent) event);
		} else if (event instanceof AceColorThemeChangedEvent) {
			doAceColorThemeChange((AceColorThemeChangedEvent) event);
		} else if (event instanceof EditorTabClosedEvent) {
			doCloseFile((EditorTabClosedEvent) event);
		} else if (event instanceof GetDirContentEvent) {
			((DevelopmentBoard) view).projectPanel.requestForDirContent(null);
		} else if (event instanceof LogoutEvent) {
			History.newItem("loginGxt");
		}
		if (event instanceof ProjectMenuEvent) {
			doMenuItemClickProceed((ProjectMenuEvent) event);
		} else if (event instanceof ServerLogEvent) {
			doLog((ServerLogEvent) event);
		}
	}

	@Override
	public void handleEvent(GwtEvent<?> event) {

	}

	public TopPanel getTopPanel() {
		return ((DevelopmentBoard) view).topPanel;
	}

	public ContentPanel getEditorContentPanel() {
		return ((DevelopmentBoard) view).editorContentPanel;
	}

	public ProjectPanel getProjectPanel() {
		return ((DevelopmentBoard) view).projectPanel;
	}

	private String selectProjectName(String sPath) {
		String sRes = sPath;
		String delims = "[\\\\/]+"; // / parse such string as
									// "\\aaaa\\bbbb\\\\cccc\\\\dddd/eeee//ffff/gggg"
		if (sPath.length() > 0) {
			String[] arrSpl = sPath.split(delims);
			sRes = arrSpl[arrSpl.length - 1];
		}
		return sRes;
	}

	public ArrayList<ProjectItem> searchProjects() {
		String sPath;
		String sProjectName;
		String sName;
		ArrayList<ProjectItem> projectList = new ArrayList<ProjectItem>();

		for (BaseDto allBaseDto : ((DevelopmentBoard) view).projectPanel
				.getStore().getAll()) {
			sPath = allBaseDto.getRelPath();
			sName = allBaseDto.getName();
			sProjectName = selectProjectName(sPath);
			if ((sProjectName + ".xml").equalsIgnoreCase(sName)) {
				if (sProjectName.length() > 0) {

					ProjectItem newProjectItem = new ProjectItemImpl(
							sProjectName, sPath);

					projectList.add(newProjectItem);
				}
			}
		}
		return projectList;

	}

	public void updateProjects(ArrayList<ProjectItem> projectsList) {
		for (ProjectItem curPI : projectsList) {
			Long projectId = projectManager.getProjectIdByProjectInfo(curPI);
			if (projectId == -1L) {
				projectManager.addProject(curPI);
			}
		}
	}

	public void updateProjectsFiles(TreeStore<BaseDto> store) {
		List<BaseDto> bdto = store.getAll();
		for (BaseDto b : bdto)
			try {
				if (b instanceof FileDto) {
					FileDto curItemInStore = (FileDto) b;
					Long projectId = projectManager
							.getProjectIdByRelPath(curItemInStore.getRelPath());
					projectManager.getFileIdByFilePath(curItemInStore
							.getAbsolutePath());

					Long fileId = -1l;
					if (!projectManager.checkIfFileExists(curItemInStore
							.getAbsolutePath())) {
						FileItemInfo newFileItemInfo = new FileItemInfo();
						newFileItemInfo.setAbsolutePath(curItemInStore
								.getAbsolutePath());
						newFileItemInfo.setRelPath(curItemInStore.getRelPath());
						newFileItemInfo.setName(Utils
								.extractJustFileName(curItemInStore
										.getAbsolutePath()));
						newFileItemInfo.setDir(false);
						newFileItemInfo.setProjectId(projectId);
						fileId = projectManager.addFile(newFileItemInfo);
					} else {
						fileId = projectManager
								.getFileIdByFilePath(curItemInStore
										.getAbsolutePath());
					}

					curItemInStore.setProjectId(projectId);
					curItemInStore.setFileId(fileId);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
	}

	public static class DirOperationReadingFileResult extends
			ProcessedResult<RequestDirOperationResult> {

		private DevelopmentBoardPresenter presenter;

		public DirOperationReadingFileResult() {
		}

		public DirOperationReadingFileResult(DevelopmentBoardPresenter presenter) {
			this.presenter = presenter;
		}

		@Override
		public void onFailure(Throwable caught) {
			AlertMessageBox alertMessageBox = new AlertMessageBox("Error",
					caught.getMessage());
			alertMessageBox.show();
		}

		@Override
		public void onSuccess(RequestDirOperationResult result) {
			if (result.getRetCode().intValue() != 0) {
				String messageAlert = "The operation '" + result.getOperation()
						+ "' failed.\r\nResult'" + result.getResult() + "'";
				AlertMessageBox alertMessageBox = new AlertMessageBox(
						"Warning", messageAlert);
				alertMessageBox.show();
			} else {

				((DevelopmentBoardPresenter) presenter).projectManager
						.checkFile(result.getPath());

				if (((DevelopmentBoardPresenter) presenter).projectManager
						.checkIsFileOpened(result.getPath())) {
					Long fileId = ((DevelopmentBoardPresenter) presenter).projectManager
							.getFileIdByFilePath(result.getPath());
					((DevelopmentBoardPresenter) presenter).scrollToTab(fileId,
							true);
				} else {

					((DevelopmentBoardPresenter) presenter).projectManager
							.addFileToOpenedFilesContext(
									result.getProjectId(),
									result.getFileId(),
									new FileItemInfo(Utils
											.extractJustFileName(result
													.getPath()), result
											.getPath(), false));

//					String fileMD5 = calculateCheckSum(result.getTextFile());
					FileSheet newFileSheet = new FileSheet(presenter,
							result.getProjectId(), result.getFileId(),
							result.getPath());

					newFileSheet.constructEditor(result.getTextFile(),FileItemInfo.getFileType(Utils
									.extractJustFileName(result.getPath())));
					presenter.projectManager.setAssociatedTabWidget(result.getFileId(), newFileSheet);

					TabItemConfig tabItemConfig = new TabItemConfig(Utils.extractJustFileName(result.getPath()));
					tabItemConfig.setClosable(true);
					
					((DevelopmentBoard) ((DevelopmentBoardPresenter) presenter).view).editor.getTabPanel().add(newFileSheet, tabItemConfig);
					((DevelopmentBoardPresenter) presenter).scrollToTab(result.getFileId(), true);

					((DevelopmentBoard) ((DevelopmentBoardPresenter) presenter).view).topPanel
							.addItemToScrollMenu(result.getPath(),
									result.getFileId());
				}
			}

			ServerLogEvent serverLogEvent = new ServerLogEvent(result);
			this.presenter.fireEvent(serverLogEvent);
		}
	}

	protected void requestForReadingFile(String fileName, Long projectId,
			Long fileId) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();
		if (service != null) {
			ServiceCallbackForAnyOperation cbk = RemoteBrowserService
					.instance().buildCallbackForAnyOperation();
			cbk.setProcessedResult(new DirOperationReadingFileResult(this));
			service.readFile(getLoggedAsUser(), "", fileName, projectId,
					fileId, cbk);
		}
	}

	public static String calculateCheckSum(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

/*	private void doSelectFile(SelectFileEvent event) {
		History.newItem("selectFile");
	}*/

	public void scrollToTab(Long fileId, boolean animate) {
		FileSheet curFileSheet = (FileSheet) projectManager
				.getAssociatedTabWidgetsContext().get(fileId);
		((DevelopmentBoard) view).editor.getTabPanel().setActiveWidget(
				curFileSheet);
		((DevelopmentBoard) view).editor.getTabPanel().scrollToTab(
				curFileSheet, animate);
	}

	private void doAceColorThemeChange(AceColorThemeChangedEvent event) {
		for (Long lKey : projectManager.getAssociatedTabWidgetsContext()
				.keySet()) {
			FileSheet curFileSheet = (FileSheet) projectManager
					.getAssociatedTabWidgetsContext().get(lKey);
			// curFileSheet.getAceEditor().setTheme(getTopPanel().comboATh.getCurrentValue());
			curFileSheet.getAceEditor().setTheme(
					event.getEvent().getSelectedItem());
		}

	}

	private void doCloseFile(EditorTabClosedEvent event) {
		Long fileId = ((FileSheet) event.getEvent().getItem()).getFileId();
		projectManager.getOpenedFilesContext().remove(fileId);
		projectManager.getAssociatedTabWidgetsContext().remove(fileId);

		if (projectManager.getOpenedFilesContext().isEmpty()) {
			getEditorContentPanel().setHeadingText(
					"files have not been selected");
		}
		((DevelopmentBoard) view).topPanel.delItemFromScrollMenu(fileId);

		String fileFullName = ((FileSheet) event.getEvent().getItem())
				.getFilePath()
				+ "\\"
				+ ((FileSheet) event.getEvent().getItem()).getFileName();
		requestForFileClosing(fileFullName, fileId);
	}

	public void requestForFileClosing(String fileName, Long fileId) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();

		if (service != null) {
			ServiceCallbackForFileOperation cbk = RemoteBrowserService
					.instance().buildCallbackForFileOperation();
			cbk.setProcessedResult(new FileOperationResult());
			service.closeFile(this.getLoggedAsUser(), fileName, fileId, cbk);
		}
	}

	private void doChangeFileStateToEdited(FileEditedEvent event) {
		Long fileId = event.getFileItemInfo().getFileId();
		projectManager.setFileState(fileId, true);
		Widget editedWidget = projectManager.getAssociatedTabWidget(fileId);
		((DevelopmentBoard) view).editor.setFileEditedState(editedWidget, true);
	}

	public class DirOperationResult extends
			ProcessedResult<RequestDirOperationResult> {

		public DirOperationResult() {

		}

		@Override
		public void onFailure(Throwable caught) {
			AlertMessageBox alertMessageBox = new AlertMessageBox("Error",
					caught.getMessage());
			alertMessageBox.show();
		}

		@Override
		public void onSuccess(RequestDirOperationResult result) {
			if (result.getRetCode().intValue() != 0) {
				String messageAlert = "The operation '" + result.getOperation()
						+ "' failed.\r\nResult'" + result.getResult() + "'";
				AlertMessageBox alertMessageBox = new AlertMessageBox(
						"Warning", messageAlert);
				alertMessageBox.show();
			} else {
				// fireEvent(new FileSavedEvent());
				AlertMessageBox alertMessageBox = new AlertMessageBox("Info",
						"file " + result.getPath() + " saved");
			}

			ServerLogEvent serverLogEvent = new ServerLogEvent(result);
			((DevelopmentBoard) view).projectPanel.requestForDirContent(null);
			fireEvent(serverLogEvent);
		}

	}

	public class FileOperationResult extends
			ProcessedResult<RequestFileOperationResult> {

		public FileOperationResult() {

		}

		@Override
		public void onFailure(Throwable caught) {
			AlertMessageBox alertMessageBox = new AlertMessageBox("Error",
					caught.getMessage());
			alertMessageBox.show();
		}

		@Override
		public void onSuccess(RequestFileOperationResult result) {
			if (result.getRetCode().intValue() != 0) {
				String messageAlert = "The operation '" + result.getOperation()
						+ "' failed.\r\nResult'" + result.getResult() + "'";
				AlertMessageBox alertMessageBox = new AlertMessageBox(
						"Warning", messageAlert);
				alertMessageBox.show();
			} else {
				if (result.getOperationType() == OperationTypes.RENAME_FILE) {
					updateFileName(result.getFileId(),result.getFileName(),result.getFileNewName()); 
				} else if (result.getOperationType() == OperationTypes.SAVE_FILE) {
					FileSheet savedFileSheet = (FileSheet) projectManager.getAssociatedTabWidget(result.getFileId());
					savedFileSheet.setIsFileEdited(false);
				}

			}

			ServerLogEvent serverLogEvent = new ServerLogEvent(result);
			((DevelopmentBoard) view).projectPanel.requestForDirContent(null);
			fireEvent(serverLogEvent);
		}

	}

	public void doLog(ServerLogEvent event) {
		Date curDate = new Date();

		String nodeRecord = "\n{\"time\":\"" + curDate.toLocaleString() + "\",";
		String nodeRecordOperation = "\"operation\":"
				+ event.getRequestResult().getOperation() + ",";
		String nodeRecordResCode = "\"res_code\":"
				+ event.getRequestResult().getRetCode() + ",";
		String nodeRecordResult = "\"result\":"
				+ event.getRequestResult().getResult() + ",";
		nodeRecord += nodeRecordOperation + nodeRecordResCode
				+ nodeRecordResult;
		if (event.getRequestResult() instanceof RequestProjectCreationResult) {
			RequestProjectCreationResult result = (RequestProjectCreationResult) event
					.getRequestResult();
			String nodeRecordProject = "\"project\":{";
			String nodeRecordProjectName = "\"name\":"
					+ result.getProjectName() + ",";
			String nodeRecordProjectPath = "\"<path\":"
					+ result.getProjectPath();
			nodeRecordProject += nodeRecordProjectName + nodeRecordProjectPath
					+ "}";
			nodeRecord += nodeRecordProject;
		} else if (event.getRequestResult() instanceof RequestFileOperationResult) {
			RequestFileOperationResult result = (RequestFileOperationResult) event
					.getRequestResult();
			String nodeRecordFile = "\"file\":{";
			String nodeRecordFileId = "\"id\":" + result.getFileId() + ",";
			String nodeRecordFileName = "\"name\":" + result.getFileName();
			nodeRecordFile += nodeRecordFileId + nodeRecordFileName + "}";
			nodeRecord += nodeRecordFile;
		} else if (event.getRequestResult() instanceof RequestDirOperationResult) {
			RequestDirOperationResult result = (RequestDirOperationResult) event
					.getRequestResult();
			nodeRecord += "\"project_id\":" + result.getProjectId()
					+ ",\"file\":{\"id\":" + result.getFileId() + ",\"path\":"
					+ result.getPath();
		} else if (event.getRequestResult() instanceof RequestedDirScanResult) {
			RequestedDirScanResult result = (RequestedDirScanResult) event
					.getRequestResult();
			String nodeRecordParentPath = "\"parent_path\":"
					+ result.getParentPath() + ",";
			String nodeRecordFiles = "\"files\":[";
			boolean bIsFirst = true;
			for (FileItemInfo curFile : result.getFiles()) {
				if (bIsFirst == false) {
					nodeRecordFiles += ",";
				}
				bIsFirst = false;

				nodeRecordFiles += "\"name\":" + curFile.getName();
				/*
				 * nodeRecordFiles += "{\"name\":"+ curFile.getName() +
				 * "\"rel_path\":"+ curFile.getRelPath() + "\"project_id\":"+
				 * curFile.getProjectId() + "\"file_id\":"+ curFile.getFileId()
				 * + "}";
				 */
			}
			;
			nodeRecord += nodeRecordParentPath + nodeRecordFiles + "]";
		} else {
			// System.out.println("Operation : " +
			// event.getRequestResult().getOperation() + "; result code : " +
			// event.getRequestResult().getRetCode());
		}
		nodeRecord += "}";
		((DevelopmentBoard) view).windows.appendLog(nodeRecord);
	}

	public void doSaveCurrentFile() {
		FileSheet currentWidget = (FileSheet) ((DevelopmentBoard) view).editor
				.getTabPanel().getActiveWidget();
		String sFullName = currentWidget.getFilePath() + "\\"
				+ currentWidget.getFileName();
		requestForFileSaving(sFullName, currentWidget.getProjectId(),
				currentWidget.getFileId(), currentWidget.getAceEditor()
						.getText());
	}

	public void requestForFileSaving(String fileName, Long projectId,
			Long fileId, String content) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();

		if (service != null) {
			ServiceCallbackForFileOperation cbk = RemoteBrowserService
					.instance().buildCallbackForFileOperation();
			cbk.setProcessedResult(new FileOperationResult());
			service.saveFile(this.getLoggedAsUser(), fileName, projectId,
					fileId, content, cbk);
		}
	}

	public FileItemInfo selectedItemInTheProjectTree;
	public String newFileName;

	public void doMenuItemClickProceed(ProjectMenuEvent event) {
		String menuId = event.getMenuId();
		selectedItemInTheProjectTree = ((DevelopmentBoard) view).projectPanel.selectedItem4ContextMenu;

		if (menuId != null) {
			switch (menuId) {
			case "new_project":
				String sPath;
				if (selectedItemInTheProjectTree == null) {
					sPath = "";
				} else {
					sPath = Utils.extractJustPath(selectedItemInTheProjectTree
							.getAbsolutePath());
				}
				makeProjectNew(sPath);
				break;
			case "delete_project":
				doDelCurrentProject(selectedItemInTheProjectTree);
				break;
			case "import_file":
				doImportFile(selectedItemInTheProjectTree);
				break;
			case "new_file":
				doCreateFile(selectedItemInTheProjectTree);
				break;
			case "rename_file":
				doRenameFile(selectedItemInTheProjectTree);
				break;
			case "delete_file":
				doDelCurrentFile(selectedItemInTheProjectTree);
				break;
			case "new_folder":
				doCreateFolder(selectedItemInTheProjectTree);
				break;
			default:
				break;
			}
		}

	}

	public void makeProjectNew(String path4project) {
		NewVwmlProjectDialogExt d = new NewVwmlProjectDialogExt();
		d.setLoggedAsUser(getLoggedAsUser());
		d.setPath4project(path4project);
		d.associatePresenter(this);
		d.setSize("480", "380");
		d.showCenter(s_newVwmlProjectCaption, null);
	}

	final DialogHideHandler hideHandler = new DialogHideHandler() {

		@Override
		public void onDialogHide(DialogHideEvent event) {
			String s = event.getHideButton().name();

			if (s.equalsIgnoreCase("YES")) {
				projectManager.removeFile(selectedItemInTheProjectTree
						.getFileId());
				Widget widget2delete = projectManager
						.getAssociatedTabWidget(selectedItemInTheProjectTree
								.getFileId());
				((DevelopmentBoard) view).editor.getTabPanel().remove(
						widget2delete);

				requestForFileDeleting(
						selectedItemInTheProjectTree.getAbsolutePath(),
						selectedItemInTheProjectTree.getFileId());

			}
		}
	};

	public void doDelCurrentFile(FileItemInfo fileItemInfo) {
		String msg = Format.substitute(
				"Are you sure you want to delete file '{0}'?",
				fileItemInfo.getName());
		ConfirmMessageBox box = new ConfirmMessageBox("Confirm", msg);
		box.addDialogHideHandler(hideHandler);
		box.show();

	}

	public void requestForFileDeleting(String fileName, Long fileId) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();

		if (service != null) {
			ServiceCallbackForFileOperation cbk = RemoteBrowserService
					.instance().buildCallbackForFileOperation();
			cbk.setProcessedResult(new FileOperationResult());
			service.deleteFile(this.getLoggedAsUser(), fileName, fileId, cbk);
		}
	}

	final DialogHideHandler hideHandlerProject = new DialogHideHandler() {

		@Override
		public void onDialogHide(DialogHideEvent event) {
			String s = event.getHideButton().name();

			if (s.equalsIgnoreCase("YES")) {

				projectManager.removeProject(selectedProjectInTheProjectTree);
				requestForProjectDeleting(
						selectedItemInTheProjectTree.getAbsolutePath(),
						selectedItemInTheProjectTree.getProjectId());

			}
		}
	};

	public Long selectedProjectInTheProjectTree;

	public void doDelCurrentProject(FileItemInfo fileItemInfo) {

		selectedProjectInTheProjectTree = projectManager
				.getProjectIdByProjectPath(getLoggedAsUser(),
						fileItemInfo.getAbsolutePath());
		String msg = Format.substitute(
				"Are you sure you want to delete project '{0}'?",
				fileItemInfo.getName());
		ConfirmMessageBox box = new ConfirmMessageBox("Confirm", msg);
		box.addDialogHideHandler(hideHandlerProject);
		box.show();

	}

	public void requestForProjectDeleting(String projectPath, Long projectId) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();

		if (service != null) {
			ServiceCallbackForDirOperation cbk = RemoteBrowserService
					.instance().buildCallbackForDirOperation();
			cbk.setProcessedResult(new DirOperationResult());
			service.deleteProject(this.getLoggedAsUser(), projectPath,
					projectId, cbk);
		}
	}

	public boolean isValidFileName(String fileName) {
		// TODO: make this function
		return true;
	}

	public void doCreateFile(FileItemInfo fileItemInfo) {

		selectedProjectInTheProjectTree = projectManager
				.getProjectIdByProjectPath(getLoggedAsUser(),
						fileItemInfo.getAbsolutePath());

		final PromptMessageBox box = new PromptMessageBox("Name",
				"Please enter file name:");
		box.addDialogHideHandler(new DialogHideHandler() {

			@Override
			public void onDialogHide(DialogHideEvent event) {
				if (box.getValue() != null) {
					if (isValidFileName(box.getValue())) {
						String fileName = box.getValue();
						Long fileId = 1000L;
						String content = "";
						requestForFileCreating(Utils
								.extractJustPath(selectedItemInTheProjectTree
										.getAbsolutePath()), fileName,
								selectedProjectInTheProjectTree, fileId,
								content);
					}
				}
			}
		});

		box.show();
	}

	public void requestForFileCreating(String parent, String fileName,
			Long projectId, Long fileId, String content) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();

		if (service != null) {
			ServiceCallbackForDirOperation cbk = RemoteBrowserService
					.instance().buildCallbackForDirOperation();
			cbk.setProcessedResult(new DirOperationResult());
			service.addFile(this.getLoggedAsUser(), parent, fileName,
					projectId, fileId, content, cbk);
		}
	}

	public void doCreateFolder(FileItemInfo fileItemInfo) {

		final PromptMessageBox box = new PromptMessageBox("Name",
				"Please enter folder name:");
		box.addDialogHideHandler(new DialogHideHandler() {

			@Override
			public void onDialogHide(DialogHideEvent event) {
				if (box.getValue() != null) {
					if (isValidFileName(box.getValue())) {
						String folderName = box.getValue();
						requestForFolderCreating(Utils
								.extractJustPath(selectedItemInTheProjectTree
										.getAbsolutePath()), folderName);
					}
				}
			}
		});

		box.show();
	}

	public void requestForFolderCreating(String folderParentPath,
			String folderName) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();

		if (service != null) {
			ServiceCallbackForDirOperation cbk = RemoteBrowserService
					.instance().buildCallbackForDirOperation();
			cbk.setProcessedResult(new DirOperationResult());
			service.createDir(this.getLoggedAsUser(), folderParentPath,
					folderName, cbk);
		}
	}

	protected void requestForGettingUserState(String user) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();
		if (service != null) {
			ServiceCallbackForUserState cbk = RemoteBrowserService.instance()
					.buildCallbackForUserState();
			cbk.setProcessedResult(new GettingUserStateResult(this));
			service.getUserState(getLoggedAsUser(), cbk);
		}
	}

	public static class GettingUserStateResult extends
			ProcessedResult<RequestUserStateResult> {

		private DevelopmentBoardPresenter presenter;

		public GettingUserStateResult() {
		}

		public GettingUserStateResult(DevelopmentBoardPresenter presenter) {
			this.presenter = presenter;
		}

		@Override
		public void onFailure(Throwable caught) {
			AlertMessageBox alertMessageBox = new AlertMessageBox("Error",
					caught.getMessage());
			alertMessageBox.show();
		}

		@Override
		public void onSuccess(RequestUserStateResult result) {
			if (result.getRetCode().intValue() != 0) {
				String messageAlert = "The operation '" + result.getOperation()
						+ "' failed.\r\nResult'" + result.getResult() + "'";
				AlertMessageBox alertMessageBox = new AlertMessageBox(
						"Warning", messageAlert);
				// alertMessageBox.show();
			} else {
				Long fileIdSelected = result.getUserStateInfo()
						.getFileIdSelected();
				Long projectIdSelected = result.getUserStateInfo()
						.getProjectIdSelected();
				FileItemInfo value = null;
				for (Object key : result.getUserStateInfo().getOpenedFiles()
						.keySet()) {
					value = result.getUserStateInfo().getOpenedFiles().get(key);
					if (fileIdSelected != key) {
						presenter.requestForReadingFile(value.getAbsolutePath()
								+ "\\" + value.getName(), value.getProjectId(),
								value.getFileId());
					}
				}
				value = result.getUserStateInfo().getOpenedFiles()
						.get(fileIdSelected);
				presenter.requestForReadingFile(value.getAbsolutePath() + "\\"
						+ value.getName(), value.getProjectId(),
						value.getFileId());

			}

			ServerLogEvent serverLogEvent = new ServerLogEvent(result);
			this.presenter.fireEvent(serverLogEvent);
		}
	}

	public void doImportFile(FileItemInfo fileItemInfo) {

		Long projectId = projectManager.getProjectIdByProjectPath(
				getLoggedAsUser(), fileItemInfo.getAbsolutePath());
		String parentPath = Utils.extractJustPath(fileItemInfo
				.getAbsolutePath());

		final FileOpenDialog box = new FileOpenDialog();
		box.setEditLabelText("Select file to import");
		box.setParentPath(parentPath);
		box.setProjectId(projectId);

		box.addDialogHideHandler(new DialogHideHandler() {
			@Override
			public void onDialogHide(DialogHideEvent event) {
				if (box.getLoadedFiles() > 0) {
					requestForFileCreating(box.getParentPath(),
							box.getFileName(0), box.getProjectId(), 0L,
							box.getContent(0));
				}
			}
		});

		box.show();
	}

	public void requestForFileImporting(String parent, String fileName,
			Long projectId, Long fileId, String content) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();

		if (service != null) {
			ServiceCallbackForDirOperation cbk = RemoteBrowserService
					.instance().buildCallbackForDirOperation();
			cbk.setProcessedResult(new DirOperationResult());
			service.addFile(this.getLoggedAsUser(), parent, fileName,
					projectId, fileId, content, cbk);
		}
	}

	public void requestForFileSavingBeforeRenaming(String fileName,	Long projectId, Long fileId, String content) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();

		if (service != null) {
			ServiceCallbackForFileOperation cbk = RemoteBrowserService.instance().buildCallbackForFileOperation();
			cbk.setProcessedResult(new FileSaveResult());
			service.saveFile(this.getLoggedAsUser(), fileName, projectId,fileId, content, cbk);
		}
	}

	public class FileSaveResult extends
			ProcessedResult<RequestFileOperationResult> {

		public FileSaveResult() {

		}

		@Override
		public void onFailure(Throwable caught) {
			AlertMessageBox alertMessageBox = new AlertMessageBox("Error",
					caught.getMessage());
			alertMessageBox.show();
		}

		@Override
		public void onSuccess(RequestFileOperationResult result) {
			if (result.getRetCode().intValue() != 0) {
				String messageAlert = "The operation '" + result.getOperation()
						+ "' failed.\r\nResult'" + result.getResult() + "'";
				AlertMessageBox alertMessageBox = new AlertMessageBox(
						"Warning", messageAlert);
				alertMessageBox.show();
			} else {
				requestForFileRenaming(	result.getFileName(), result.getFileId(), newFileName);
			}

			ServerLogEvent serverLogEvent = new ServerLogEvent(result);
			((DevelopmentBoard) view).projectPanel.requestForDirContent(null);
			fireEvent(serverLogEvent);
		}

	}

	
	public void doRenameFile(FileItemInfo fileItemInfo) {

		final ConfirmMessageBox saveConfirmBox = new ConfirmMessageBox("Confirm",
				"Save changes?");
		saveConfirmBox.addDialogHideHandler(new DialogHideHandler() {

			@Override
			public void onDialogHide(DialogHideEvent event) {
				if (event.getHideButton().name().equalsIgnoreCase("YES")) {
					requestForFileSavingBeforeRenaming((String) saveConfirmBox.getData("fullFileName"), 
									(Long) saveConfirmBox.getData("projectId"), 
									(Long) saveConfirmBox.getData("fileId"), 
									(String) saveConfirmBox.getData("content"));
				}
			}
		});

		final PromptMessageBox saveBox = new PromptMessageBox("Name", 	"Please enter new file name:");
			
			saveBox.getTextField().setText(selectedItemInTheProjectTree.getName());
			saveBox.addDialogHideHandler(new DialogHideHandler() {
				
				@Override
				public void onDialogHide(DialogHideEvent event) {
					if (event.getHideButton().name().equalsIgnoreCase("ok")) {
						if (saveBox.getValue() != null) {
							String justFilePath = Utils.extractJustPath(selectedItemInTheProjectTree.getAbsolutePath());
							String fullNewFileName = justFilePath + "\\"+ saveBox.getValue();
							newFileName = fullNewFileName;
							Long fileId = selectedItemInTheProjectTree.getFileId();
							
							FileSheet currFileSheet = (FileSheet) projectManager.getAssociatedTabWidget(fileId);
							
							if (currFileSheet.getIsFileEdited()) {
								
								saveConfirmBox.setData("fullFileName", selectedItemInTheProjectTree.getAbsolutePath());
								saveConfirmBox.setData("fullNewFileName", fullNewFileName);
								saveConfirmBox.setData("projectId", selectedItemInTheProjectTree.getProjectId());
								saveConfirmBox.setData("fileId", fileId);
								saveConfirmBox.setData("content", currFileSheet.getAceEditor().getText());
								saveConfirmBox.show();
							} else {
								if (!selectedItemInTheProjectTree.getAbsolutePath().equalsIgnoreCase(fullNewFileName)) {
									requestForFileRenaming(
											selectedItemInTheProjectTree.getAbsolutePath(),
											selectedItemInTheProjectTree.getFileId(),
											fullNewFileName);
								}
							}
						}	
					}
				}		
		});

		saveBox.show();

	}

	public void requestForFileRenaming(String fileName, Long fileId, String fileNewName) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForFileOperation cbk = RemoteBrowserService.instance().buildCallbackForFileOperation();
			cbk.setProcessedResult(new FileOperationResult());
			service.renameFile(this.getLoggedAsUser(), fileName, fileId,fileNewName, cbk);
		}
	}
	
	public void updateFileName(Long fileId, String oldFileName, String newFileName) {
		projectManager.changeFileName(fileId, newFileName);
		
		FileSheet updatedFileSheet = (FileSheet) projectManager.getAssociatedTabWidget(fileId);
		TabItemConfig conf = ((DevelopmentBoard) view).editor.getTabPanel().getConfig(updatedFileSheet);
		conf.setText(Utils.extractJustFileName(newFileName));
		((DevelopmentBoard) view).editor.getTabPanel().update(updatedFileSheet, conf);
	};	
	

}
