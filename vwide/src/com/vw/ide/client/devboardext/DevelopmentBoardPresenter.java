package com.vw.ide.client.devboardext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.event.uiflow.AceColorThemeChangedEvent;
import com.vw.ide.client.event.uiflow.EditorTabClosedEvent;
import com.vw.ide.client.event.uiflow.FileEditedEvent;
import com.vw.ide.client.event.uiflow.GetDirContentEvent;
import com.vw.ide.client.event.uiflow.LogoutEvent;
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
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForFileSaving;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.client.ui.toppanel.TopPanel;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowserAsync;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestFileSavingResult;

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
			doRemoveFile((EditorTabClosedEvent) event);
		} else if (event instanceof GetDirContentEvent) {
			((DevelopmentBoard) view).projectPanel.requestForDirContent(null);
		} else if (event instanceof LogoutEvent) {
			History.newItem("loginGxt");
		} else if (event instanceof ServerLogEvent) {
			doLog((ServerLogEvent) event);
		} 
	}

	public TopPanel getTopPanel() {
		return ((DevelopmentBoard) view).topPanel;
	}

	public ContentPanel getEditorContentPanel() {
		return ((DevelopmentBoard) view).editorContentPanel;
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

	// function just for testing purpose
	public void checkStoreFiles(TreeStore<BaseDto> store) {
		List<BaseDto> bdto = store.getAll();
		for (BaseDto b : bdto)
			try {
				if (b instanceof FileDto) {
					FileDto curItemInStore = (FileDto) b;
					System.out.println("|- AbsolutePath:"
							+ curItemInStore.getAbsolutePath()
							+ "; \t\t\tName:" + curItemInStore.getName()
							+ "; \t\t\tprojectId:"
							+ curItemInStore.getProjectId() + "; \t\t\tfileId:"
							+ curItemInStore.getFileId() + "; \t\t\tRelPath:"
							+ curItemInStore.getRelPath() + "; \t\t\tFolder:"
							+ curItemInStore.getFolder());
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
	}

	public static class DirOperationReadingFileResult extends
			ProcessedResult<RequestDirOperationResult> {

		private Presenter presenter;

		public DirOperationReadingFileResult() {
		}

		public DirOperationReadingFileResult(Presenter presenter) {
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

					String fileMD5 = calculateCheckSum(result.getTextFile());
					FileSheet newFileSheet = new FileSheet(presenter,
							result.getProjectId(), result.getFileId(),
							result.getPath());

					newFileSheet.constructEditor(result.getTextFile(),
							FileItemInfo.getFileType(Utils
									.extractJustFileName(result.getPath())));
					((DevelopmentBoardPresenter) presenter).projectManager
							.setAssociatedTabWidget(result.getFileId(),
									newFileSheet);

					TabItemConfig tabItemConfig = new TabItemConfig(
							Utils.extractJustFileName(result.getPath()));
					tabItemConfig.setClosable(true);
					((DevelopmentBoard) ((DevelopmentBoardPresenter) presenter).view).editor
							.getTabPanel().add(newFileSheet, tabItemConfig);
					((DevelopmentBoardPresenter) presenter).scrollToTab(
							result.getFileId(), true);

					((DevelopmentBoard) ((DevelopmentBoardPresenter) presenter).view).topPanel
							.addItemToScrollMenu(result.getPath(),
									result.getFileId());
				}
			}
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

	private void doSelectFile(SelectFileEvent event) {
		History.newItem("selectFile");
	}

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

	private void doRemoveFile(EditorTabClosedEvent event) {
		Long fileId = ((FileSheet) event.getEvent().getItem()).getFileId();
		projectManager.getOpenedFilesContext().remove(fileId);
		FileSheet curFileSheet = (FileSheet) projectManager
				.getAssociatedTabWidgetsContext().remove(fileId);

		if (projectManager.getOpenedFilesContext().isEmpty()) {
			getEditorContentPanel().setHeadingText(
					"files have not been selected");
		}

		((DevelopmentBoard) view).topPanel.delItemFromScrollMenu(fileId);

	}

	private void doChangeFileStateToEdited(FileEditedEvent event) {
		Long fileId = event.getFileItemInfo().getFileId();
		projectManager.setFileState(fileId, true);
		Widget editedWidget = projectManager.getAssociatedTabWidget(fileId);
		((DevelopmentBoard) view).editor.setFileEditedState(editedWidget, true);
	}

	public class FileSavingResult extends
			ProcessedResult<RequestFileSavingResult> {

		public FileSavingResult() {

		}

		@Override
		public void onFailure(Throwable caught) {
			AlertMessageBox alertMessageBox = new AlertMessageBox("Error",
					caught.getMessage());
			alertMessageBox.show();
		}

		@Override
		public void onSuccess(RequestFileSavingResult result) {
			if (result.getRetCode().intValue() != 0) {
				String messageAlert = "The operation '" + result.getOperation()
						+ "' failed.\r\nResult'" + result.getResult() + "'";
				AlertMessageBox alertMessageBox = new AlertMessageBox(
						"Warning", messageAlert);
				alertMessageBox.show();
			} else {
//				fireEvent(new FileSavedEvent());
				AlertMessageBox alertMessageBox = new AlertMessageBox(
						"Info", "file " + result.getFileName() + " saved");
			}
			
			ServerLogEvent serverLogEvent = new ServerLogEvent(result);
			fireEvent(serverLogEvent);
		}

	}
	
	

	
	public void doLog(ServerLogEvent event) {
		 Date curDate = new Date();
		 
	     String nodeRecord = "\n<record time='"+ curDate.toLocaleString()+ "'>";
	     String nodeRecordOperation = "\n\t<operation>"+ event.getRequestResult().getOperation()+ "</operation>";
	     String nodeRecordResCode = "\n\t<res_code>"+ event.getRequestResult().getRetCode()+ "</res_code>";
	     nodeRecord += nodeRecordOperation + nodeRecordResCode; 
		if (event.getRequestResult() instanceof RequestFileSavingResult) {
			RequestFileSavingResult result = (RequestFileSavingResult) event.getRequestResult();
		    String nodeRecordFile = "\n\t\t<file>";
			String nodeRecordFileId = "\n\t\t\t<file_id>"+ result.getFileId()+ "</file_id>";
 		    String nodeRecordFileName = "\n\t\t\t<file_name>"+ result.getFileName()+ "</file_name>";
		    nodeRecordFile += nodeRecordFileId + nodeRecordFileName + "\n\t\t</file>";
		    nodeRecord += nodeRecordFile; 
		} else {
//			System.out.println("Operation : " + event.getRequestResult().getOperation() + "; result code : " + event.getRequestResult().getRetCode()); 
		}
		nodeRecord += "\n</record>";
		((DevelopmentBoard) view).windows.appendLog(nodeRecord);
	}

	public void doSaveCurrentFile() {
		FileSheet currentWidget = (FileSheet) ((DevelopmentBoard) view).editor.getTabPanel().getActiveWidget();
		String sFullName = currentWidget.getFilePath() + "\\" + currentWidget.getFileName();
		requestForFileSaving(sFullName, currentWidget.getProjectId(), currentWidget.getFileId(), currentWidget.getAceEditor().getText());
	}
	
	public void requestForFileSaving(String fileName, Long projectId, Long fileId, String content) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();

		if (service != null) {
			ServiceCallbackForFileSaving cbk = RemoteBrowserService
					.instance().buildCallbackForSavingFile();
			cbk.setProcessedResult(new FileSavingResult());
			service.saveFile(this.getLoggedAsUser(), fileName, projectId, fileId, content,  cbk);
		}
	}
	

}
