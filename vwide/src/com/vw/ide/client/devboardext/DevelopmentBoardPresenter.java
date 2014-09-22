package com.vw.ide.client.devboardext;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.event.handler.SelectFileHandler;
import com.vw.ide.client.event.uiflow.LogoutEvent;
import com.vw.ide.client.event.uiflow.SelectFileEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.projects.FileManager;
import com.vw.ide.client.projects.FileManagerImpl;
import com.vw.ide.client.service.ProcessedResult;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForAnyOperation;
import com.vw.ide.client.ui.projectpanel.ProjectPanel;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.client.ui.toppanel.TopPanel;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowserAsync;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;

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
	public FileManager fileManager;

	public DevelopmentBoardPresenter(HandlerManager eventBus,
			PresenterViewerLink view) {
		this.eventBus = eventBus;
		this.view = view;
		if (view != null) {
			view.associatePresenter(this);
		}

		fileManager = new FileManagerImpl();

	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
	}

	public void fireEvent(GwtEvent<?> event) {
		// eventBus.fireEvent(event);

		if (event instanceof SelectFileEvent) {
			SelectFileEvent evt = (SelectFileEvent) event;
//			fileManager.addFile(evt.getFileItemInfo());
			requestForReadingFile(evt.getFileItemInfo().getPath());
		} else if (event instanceof LogoutEvent) {
			History.newItem("loginGxt");
		}  
	}

	public TopPanel getTopPanel() {
		return ((DevelopmentBoard) view).topPanel;
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

		public String extractJustPath(String input) {
			String output = "";
			String[] arrPath = input.split("\\\\");
			String sLastItemName = arrPath[arrPath.length - 1];
			if (sLastItemName.indexOf(".") == -1) {
				return input;
			} else {
				for (int i = 0; i < arrPath.length - 2; i++) {
					output += arrPath[i] + "\\";
				}
				output += arrPath[arrPath.length - 2];
			}
			return output;
		}
		
		public String extractJustFileName(String input) {
			String output = "";
			String[] arrPath = input.split("\\\\");
			String sLastItemName = arrPath[arrPath.length - 1];
			if (sLastItemName.indexOf(".") != -1) {
				output = arrPath[arrPath.length -1];
			} 
			return output;
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
//				String path = extractJustPath(result.getPath());
//				String name = extractJustFileName(result.getPath());
				
				if(((DevelopmentBoardPresenter)presenter).fileManager.checkIsFileOpened(result.getPath())) {
//					((DevelopmentBoard) ((DevelopmentBoardPresenter) presenter).view).editor.getTabPanel().scrollToTab(item, true);
				} else {
					
					Long fileId = ((DevelopmentBoardPresenter)presenter).fileManager.addFile(new FileItemInfo(extractJustFileName(result.getPath()),result.getPath(),false));
					
//					Long fileId = ((DevelopmentBoardPresenter)presenter).fileManager.getFileIdByFilePath(result.getPath()); 
					byte[] fileMD5 = calculateCheckSum(result.getTextFile());
					
					
					((DevelopmentBoardPresenter)presenter).fileManager.getFileItemInfo(fileId).setCheckSumOnOpen(fileMD5);
					((DevelopmentBoardPresenter)presenter).fileManager.setFileContent(fileId, result.getTextFile());
					
					FileSheet newFileSheet = new FileSheet(presenter,fileId,extractJustFileName(result.getPath()));
					newFileSheet.constructEditor(result.getTextFile());
					
					((DevelopmentBoard) ((DevelopmentBoardPresenter) presenter).view).editor.getTabPanel().add(newFileSheet,extractJustFileName(result.getPath()));
				}
//				editor.tabPanel.add(newFileSheet,"test");
			}
		}
	}

	protected void requestForReadingFile(String fileName) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();
		if (service != null) {
			ServiceCallbackForAnyOperation cbk = RemoteBrowserService
					.instance().buildCallbackForAnyOperation();
			cbk.setProcessedResult(new DirOperationReadingFileResult(this));
			service.readFile(getLoggedAsUser(), "", fileName, cbk);
		}
	}
	
	
	public static byte[] calculateCheckSum(String input) {
		byte[] bytesOfMessage = null;
		try {
			bytesOfMessage = input.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] thedigest = md.digest(bytesOfMessage);		
		return thedigest;
	}

}
