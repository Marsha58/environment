package com.vw.ide.client.devboardext;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.event.BeforeCloseEvent;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent;
import com.vw.ide.client.event.handler.AceColorThemeChangedHandler;
import com.vw.ide.client.event.handler.SelectFileHandler;
import com.vw.ide.client.event.uiflow.AceColorThemeChangedEvent;
import com.vw.ide.client.event.uiflow.EditorTabClosedEvent;
import com.vw.ide.client.event.uiflow.GetDirContentEvent;
import com.vw.ide.client.event.uiflow.LogoutEvent;
import com.vw.ide.client.event.uiflow.SelectFileEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.projects.FileManager;
import com.vw.ide.client.projects.FileManagerImpl;
import com.vw.ide.client.projects.ProjectManager;
import com.vw.ide.client.projects.ProjectManagerImpl;
import com.vw.ide.client.service.ProcessedResult;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForAnyOperation;
import com.vw.ide.client.ui.projectpanel.ProjectPanel;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.client.ui.toppanel.TopPanel;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowserAsync;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestProjectCreationResult;

import java.io.UnsupportedEncodingException;
import java.security.*;

/**
 * Development board screen
 * 
 * @author Oleg
 * 
 */
public class DevelopmentBoardPresenter extends Presenter {

	public final HandlerManager eventBus;
	private final PresenterViewerLink view;
//	public FileManager fileManager;
	public ProjectManager projectManager;

	public DevelopmentBoardPresenter(HandlerManager eventBus,
			PresenterViewerLink view) {
		this.eventBus = eventBus;
		this.view = view;
		if (view != null) {
			view.associatePresenter(this);
		}

//		fileManager = new FileManagerImpl();
		projectManager = new ProjectManagerImpl();
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());

	}

	public void fireEvent(GwtEvent<?> event) {
		// eventBus.fireEvent(event);

		if (event instanceof SelectFileEvent) {
			SelectFileEvent evt = (SelectFileEvent) event;
			Long projectId = projectManager.getProjectIdByFilePath(evt.getFileItemInfo().getPath());
			Long fileId    = projectManager.getFileIdByFilePath(evt.getFileItemInfo().getPath());
			
			requestForReadingFile(evt.getFileItemInfo().getPath(), projectId, fileId);
		} else if (event instanceof AceColorThemeChangedEvent) {
			doAceColorThemeChange((AceColorThemeChangedEvent) event);
		} else if (event instanceof EditorTabClosedEvent) {
			doRemoveFile((EditorTabClosedEvent) event);
		} else if (event instanceof GetDirContentEvent) {
			((DevelopmentBoard) view).projectPanel.requestForDirContent(null);
		} else if (event instanceof LogoutEvent) {
			History.newItem("loginGxt");
		}
	}

	public TopPanel getTopPanel() {
		return ((DevelopmentBoard) view).topPanel;
	}

	public ContentPanel getEditorContentPanel() {
		return ((DevelopmentBoard) view).editorContentPanel;
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
				
				((DevelopmentBoardPresenter) presenter).projectManager.checkFile(result.getPath());

				if (((DevelopmentBoardPresenter) presenter).projectManager.checkIsFileOpened(result.getPath())) {
					Long fileId = ((DevelopmentBoardPresenter) presenter).projectManager
							.getFileIdByFilePath(result.getPath());
					((DevelopmentBoardPresenter) presenter).scrollToTab(fileId,
							true);
				} else {

//					Long fileId = ((DevelopmentBoardPresenter) presenter).projectManager.getNewFileId();
					
					((DevelopmentBoardPresenter) presenter).projectManager.addFileToOpenedFilesContext(result.getProjectId(), result.getFileId(), 
								    new FileItemInfo(Utils.extractJustFileName(result.getPath()),
									result.getPath(), false));

					String fileMD5 = calculateCheckSum(result.getTextFile());

/*					((DevelopmentBoardPresenter) presenter).projectManager
							.setFileContent(fileId, result.getTextFile());
*/

					FileSheet newFileSheet = new FileSheet(presenter, result.getFileId(),
							Utils.extractJustFileName(result.getPath()));
					
					
					newFileSheet.constructEditor(result.getTextFile(),FileItemInfo.getFileType(Utils.extractJustFileName(result.getPath())));
					((DevelopmentBoardPresenter) presenter).projectManager.setAssociatedTabWidget(result.getFileId(), newFileSheet);

					TabItemConfig tabItemConfig = new TabItemConfig(
							Utils.extractJustFileName(result.getPath()));
					tabItemConfig.setClosable(true);
					((DevelopmentBoard) ((DevelopmentBoardPresenter) presenter).view).editor
							.getTabPanel().add(newFileSheet, tabItemConfig);
					((DevelopmentBoardPresenter) presenter).scrollToTab(result.getFileId(),
							true);
				}
			}
		}
	}



	protected void requestForReadingFile(String fileName, Long projectId, Long fileId) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();
		if (service != null) {
			ServiceCallbackForAnyOperation cbk = RemoteBrowserService
					.instance().buildCallbackForAnyOperation();
			cbk.setProcessedResult(new DirOperationReadingFileResult(this));
			service.readFile(getLoggedAsUser(), "", fileName, projectId, fileId, cbk);
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
		FileSheet curFileSheet = (FileSheet) projectManager.getAssociatedTabWidgetsContext().get(fileId);
		((DevelopmentBoard) view).editor.getTabPanel().setActiveWidget(
				curFileSheet);
		((DevelopmentBoard) view).editor.getTabPanel().scrollToTab(
				curFileSheet, animate);
	}

	private void doAceColorThemeChange(AceColorThemeChangedEvent event) {
		for (Long lKey : projectManager.getAssociatedTabWidgetsContext().keySet()) {
			FileSheet curFileSheet = (FileSheet) projectManager.getAssociatedTabWidgetsContext().get(lKey);

			// curFileSheet.getAceEditor().setTheme(getTopPanel().comboATh.getCurrentValue());
			curFileSheet.getAceEditor().setTheme(
					event.getEvent().getSelectedItem());
		}

	}

	private void doRemoveFile(EditorTabClosedEvent event) {
		Long fileId = ((FileSheet) event.getEvent().getItem()).getFileId();
		projectManager.getOpenedFilesContext().remove(fileId);
		FileSheet curFileSheet = (FileSheet) projectManager.getAssociatedTabWidgetsContext().remove(fileId);
	}

}
