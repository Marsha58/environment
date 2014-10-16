package com.vw.ide.client.dialog.remotebrowser;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.vw.ide.client.dialog.VwmlDialogExt;
import com.vw.ide.client.dialog.simple.SimpleSinglelineEditDialogExt;
import com.vw.ide.client.model.BaseDto;
import com.vw.ide.client.model.FileDto;
import com.vw.ide.client.model.FolderDto;
import com.vw.ide.client.service.ProcessedResult;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForAnyOperation;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForCompleteContent;
import com.vw.ide.client.utils.Utils.YesNoMsgBoxSelctionCallback;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowserAsync;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestProjectCreationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestedDirScanResult;

/**
 * Selects file/directory on remote side
 * 
 * @author Oleg
 * 
 */
public class RemoteDirectoryBrowserDialogExt extends VwmlDialogExt {

	class KeyProvider implements ModelKeyProvider<BaseDto> {
		@Override
		public String getKey(BaseDto item) {
			return (item instanceof FolderDto ? "f-" : "m-")
					+ item.getId().toString();
		}
	}

	private static int autoId = 0;

	public static class NewFolderDialogResult implements
			SimpleSinglelineEditDialogExt.ResultCallback {

		private RemoteDirectoryBrowserDialogExt owner;

		public NewFolderDialogResult(RemoteDirectoryBrowserDialogExt owner) {
			this.owner = owner;

		}

		public void setResult(String result) {

			owner.onCreateDirectory(result);
		}
	}

	@UiFactory
	public ValueProvider<BaseDto, String> createValueProvider() {
		return new ValueProvider<BaseDto, String>() {

			@Override
			public String getValue(BaseDto object) {
				return object.getName();
			}

			@Override
			public void setValue(BaseDto object, String value) {
			}

			@Override
			public String getPath() {
				return "name";
			}
		};
	}

	/**
	 * Called upon response from remote service
	 * 
	 * @author Oleg
	 * 
	 */

	public static class DirContentResult extends
			ProcessedResult<RequestedDirScanResult> {

		private RemoteDirectoryBrowserDialogExt dialog;

		public DirContentResult() {

		}

		public DirContentResult(RemoteDirectoryBrowserDialogExt dialog) {
			this.dialog = dialog;

		}

		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(RequestedDirScanResult result) {
			dialog.makeTreeData(result);
		}
	}

	public static class DirOperationCreateResult extends
			ProcessedResult<RequestDirOperationResult> {

		private RemoteDirectoryBrowserDialogExt dialog;

		public DirOperationCreateResult() {

		}

		public DirOperationCreateResult(RemoteDirectoryBrowserDialogExt dialog) {
			this.dialog = dialog;
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
				dialog.removeAllChildrenStartingFromSelected();
				dialog.refreshTreeStartingFromSelected();
			}
		}
	}

	public static class DirOperationRemoveResult extends
			ProcessedResult<RequestDirOperationResult> {

		private RemoteDirectoryBrowserDialogExt dialog;

		public DirOperationRemoveResult() {

		}

		public DirOperationRemoveResult(RemoteDirectoryBrowserDialogExt dialog) {
			this.dialog = dialog;
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
				dialog.removeSelected();
			}
		}
	}

	

	public static class ConfirmDirectoryRemoving implements
			YesNoMsgBoxSelctionCallback {

		private RemoteDirectoryBrowserDialogExt dialog;

		public ConfirmDirectoryRemoving(
				RemoteDirectoryBrowserDialogExt remoteDirectoryBrowserDialogExt) {
			this.dialog = remoteDirectoryBrowserDialogExt;
		}

		public void selected(SELECTION selection) {
			if (selection == SELECTION.YES) {
				dialog.onRemoveDirectory();
			}
		}
	}

	protected static class ItemResource {
		private String fullPath;

		public String getFullPath() {
			return fullPath;
		}

		public void setFullPath(String fullPath) {
			this.fullPath = fullPath;
		}
	}

	private static String s_createNewFolderDialog = "Create new folder";
	private static RemoteDirectoryBrowserDialogUiBinderExt uiBinder = GWT
			.create(RemoteDirectoryBrowserDialogUiBinderExt.class);
	// @UiField ListBox filesField;
	// @UiField Tree dirsField;
	@UiField
	Label selectedPathField;
	@UiField
	TextButton createDir;
	@UiField
	TextButton removeDir;
	@UiField
	TextButton refreshDir;

	@UiField(provided = true)
	TreeStore<BaseDto> store = new TreeStore<BaseDto>(new KeyProvider());

	interface RemoteDirectoryBrowserDialogUiBinderExt extends
			UiBinder<Widget, RemoteDirectoryBrowserDialogExt> {
	}

	public FolderDto selectedFolder = null;
	public BaseDto treeSelectedItem;
	public static final String SELECT_ID = "SELECT_ID";

	@UiField
	Tree<BaseDto, String> treeDirs;

	TextButton bnSelect = new TextButton("Select");
	TextButton bnCancel = new TextButton("Cancel");

	public RemoteDirectoryBrowserDialogExt() {
		super.setWidget(uiBinder.createAndBindUi(this));
		setPredefinedButtons(PredefinedButton.CANCEL);
		
		bnSelect.setId(SELECT_ID);
		bnSelect.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				treeSelectedItem.getName();
				hide();
			}
		});
		getButtonBar().add(bnSelect);
		// bnSelect.setId(SELECT_ID);
		// getButtonBar().add(bnSelect);
		// if(getButton(PredefinedButton.OK) != null) {
		// getButtonBar().remove(getButton(PredefinedButton.OK));
		// }

		treeDirs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		treeDirs.getSelectionModel().addSelectionHandler(
				new SelectionHandler<BaseDto>() {

					public void onSelection(SelectionEvent<BaseDto> event) {
						treeSelectedItem = event.getSelectedItem();

						selectedPathField.setText(treeSelectedItem
								.getAbsolutePath());
						if (treeSelectedItem.getType() == "dir") {
							selectedFolder = (FolderDto) treeSelectedItem;
							String s = selectedFolder.getName();
							// requestForDirContent(selectedFolder.getRelPath());
							((TextButton) getButtonBar().getItemByItemId(
									SELECT_ID)).enable();
						} else {
							((TextButton) getButtonBar().getItemByItemId(
									SELECT_ID)).disable();
						}
					}
				});

		bind();

	}

	@Override
	protected void onButtonPressed(TextButton textButton) {
		super.onButtonPressed(textButton);
		if (textButton == getButton(PredefinedButton.CANCEL)) {
			hide();
		}
	}

	public RemoteDirectoryBrowserDialogExt(String firstName) {
		super.setWidget(uiBinder.createAndBindUi(this));
		bind();
	}

	// @UiHandler("cancel")
	// void onCancelClick(ClickEvent event) {
	// this.hide(true);
	// }

	// @UiHandler("ok")
	// void onOkClick(ClickEvent event) {
	// }

	/**
	 * Performs preparation steps
	 */
	public void prepare() {
		requestForDirContent(null);
	}

	protected void requestForDirContent(String parent) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();
		if (service != null) {
			ServiceCallbackForCompleteContent cbk = RemoteBrowserService
					.instance().buildCallbackForCompleteContent();
			cbk.setProcessedResult(new DirContentResult(this));
			service.getDirScan(getLoggedAsUser(), parent, cbk);
		}
	}

	protected void bind() {
	}

	public FolderDto root = null;
	public String basePath = "";

	public FolderDto makeTree(RequestedDirScanResult dirs) {

		String[] arrPath;
		String sOwnerFolderName = "";

		if (root == null) {
			root = makeFolder("Root", "", "");
			basePath = dirs.getParentPath();
			String user = getLoggedAsUser();
			FolderDto owner = makeFolder(user, "", "");
			List<BaseDto> children = new ArrayList<BaseDto>();
			children.add(owner);
			root.setChildren(children);
		}

		try {
			String delims = "[\\\\/]+";
			arrPath = dirs.getParentPath().split(delims);
			sOwnerFolderName = arrPath[arrPath.length - 1];
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		FolderDto owner = findOwnerElement(root, sOwnerFolderName,
				dirs.getParentPath(), false);

		FolderDto folder = null;
		String sNewPath = "";

		for (FileItemInfo fi : dirs.getFiles()) {
			if (fi.isDir()) {
				if (owner.getRelPath().trim().length() > 0) {
					sNewPath = owner.getRelPath() + "\\" + fi.getName();
				} else {
					sNewPath = fi.getName();
				}
				folder = makeFolder(fi.getName(), sNewPath, fi.getAbsolutePath());
				owner.addOrReplaceChild(folder);
				requestForDirContent(folder.getRelPath());
			} else {
				owner.addOrReplaceChild(makeFileItem(fi.getName(), owner,
						owner.getRelPath(), fi.getAbsolutePath()));
			}
		}

		return root;
	}

	private void makeTreeData(RequestedDirScanResult dirs) {

		FolderDto root = makeTree(dirs);
		for (BaseDto base : root.getChildren()) {
			store.remove(base);
			store.add(base);
			if (base instanceof FolderDto) {
				processFolder(store, (FolderDto) base);
			}
		}
	}

	private FolderDto findOwnerElement(FolderDto ownerFolder,
			String sFolderName, String absolutePath, boolean IsCatched) {
		FolderDto res = null;
		for (BaseDto el : ownerFolder.getChildren()) {
			if (el.getType() == "dir") {
				String sRelPathFromAbsPath = absolutePath.substring(basePath
						.length());
				if (sRelPathFromAbsPath.length() > 2) {
					if (sRelPathFromAbsPath.substring(0, 1).equalsIgnoreCase("\\")) {
						sRelPathFromAbsPath = sRelPathFromAbsPath.substring(1);
					}
				}

/*				System.out.println("el.getName(): " + el.getName()
						+ "; sFolderName: " + sFolderName);*/
				if (el.getRelPath().trim()
						.equalsIgnoreCase(sRelPathFromAbsPath.trim())) {
					IsCatched = true;
					res = (FolderDto) el;
					break;
				} else {
					if (!IsCatched) {
						res = findOwnerElement((FolderDto) el, sFolderName,
								absolutePath, IsCatched);
					}
					if (res != null) {
						return res;
					}
				}
			}
		}
		return res;
	}

	private void processFolder(TreeStore<BaseDto> store, FolderDto folder) {
		for (BaseDto child : folder.getChildren()) {
			store.remove(child);
			store.add(folder, child);
			if (child instanceof FolderDto) {
				processFolder(store, (FolderDto) child);
			}
		}
	}

	private static FolderDto makeFolder(String name, String path,
			String absolutePath) {
		FolderDto theReturn = new FolderDto(++autoId, name, path, absolutePath);
		theReturn.setChildren((List<BaseDto>) new ArrayList<BaseDto>());
		return theReturn;
	}

	private static FileDto makeFileItem(String fileName, FolderDto folder,
			String relPath, String absolutePath) {
		return makeFileItem(fileName, folder.getName(), relPath, absolutePath);
	}

	private static FileDto makeFileItem(String fileName, String folder,
			String relPath, String absolutePath) {
		return new FileDto(++autoId, fileName, folder, relPath, absolutePath);
	}

	protected void refreshTreeStartingFromSelected() {
		requestForDirContent(treeSelectedItem.getRelPath());
	}

	protected void removeAllChildrenStartingFromSelected() {
		/*
		 * TreeItem selected = dirsField.getSelectedItem(); if (selected !=
		 * null) { int children = selected.getChildCount(); for(int i = 0; i <
		 * children; i++) { selected.getChild(0).remove(); } }
		 */
	}

	protected void removeSelected() {
		// TreeItem selected = dirsField.getSelectedItem();
		// if (selected != null) {
		// selected.remove();
		// }
	}

	// @UiHandler("dirsField")
	// protected void onDirsFieldSelection(SelectionEvent<TreeItem> event) {
	// refreshTreeStartingFromSelected();
	// }

	@UiHandler("createDir")
	protected void onCreateDirClick(SelectEvent event) {
		if (treeSelectedItem != null) {
			SimpleSinglelineEditDialogExt d = new SimpleSinglelineEditDialogExt();
			d.setResult(new NewFolderDialogResult(this));
			d.setEditLabelText("Folder name");
			d.setSize("300", "70");
			d.showCenter(s_createNewFolderDialog, null);
		} else {
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Select folder", "Select folder to be parent for new one");
			alertMessageBox.show();
		}
	}

	@UiHandler("removeDir")
	protected void onRemoveDirClick(SelectEvent event) {
		if (selectedFolder != null) {
			// Utils.messageBoxYesNo("Confirmation required", "The folder '"
			// + selectedFolder.getAbsolutePath()
			// + "' is going to be removed", null,
			// new ConfirmDirectoryRemoving(this));

			String sMessage = "The folder '" + selectedFolder.getAbsolutePath()
					+ "' is going to be removed";

			MessageBox box = new MessageBox("Confirmation required", "");
			box.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
			box.setIcon(MessageBox.ICONS.question());
			box.setMessage(sMessage);
			box.addDialogHideHandler(new DialogHideHandler() {
				@Override
				public void onDialogHide(DialogHideEvent event) {
					// String msg =
					// Format.substitute("The '{0}' button was pressed",
					// event.getHideButton());
					if (event.getHideButton().name().equalsIgnoreCase("YES")) {
						onRemoveDirectory();
					}
				}
			});
			box.show();

		} else {
			AlertMessageBox alertMessageBox = new AlertMessageBox(
					"Select folder", "Select folder to remove");
			alertMessageBox.show();
		}
	}

	@UiHandler("refreshDir")
	protected void onRefreshDirClick(SelectEvent event) {
		if (selectedFolder != null) {
			requestForDirContent(selectedFolder.getRelPath());
		} else {
			requestForDirContent("");
		}
	}

	public String extractJustPath(String input) {
		String output = "";
		String delims = "[\\\\/]+";
		String[] arrPath = input.split(delims);
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
		String delims = "[\\\\/]+";
		String[] arrPath = input.split(delims);
		String sLastItemName = arrPath[arrPath.length - 1];
		if (sLastItemName.indexOf(".") != -1) {
			output = arrPath[arrPath.length - 1];
		}
		return output;
	}

	protected void onCreateDirectory(String dir) {

		if (treeSelectedItem != null) {
			RemoteDirectoryBrowserAsync service = RemoteBrowserService
					.instance().getServiceImpl();
			if (service != null) {
				ServiceCallbackForAnyOperation cbk = RemoteBrowserService
						.instance().buildCallbackForAnyOperation();
				cbk.setProcessedResult(new DirOperationCreateResult(this));
				// ItemResource resource = (ItemResource)
				// treeSelectedItem.getAbsolutePath();
				// if (resource != null) {
				service.createDir(getLoggedAsUser(),
						extractJustPath(treeSelectedItem.getAbsolutePath()),
						dir, cbk);
				// }
			}
		}

	}

	protected void onRemoveDirectory() {
		if (treeSelectedItem != null) {
			RemoteDirectoryBrowserAsync service = RemoteBrowserService
					.instance().getServiceImpl();
			if (service != null) {
				ServiceCallbackForAnyOperation cbk = RemoteBrowserService
						.instance().buildCallbackForAnyOperation();
				cbk.setProcessedResult(new DirOperationRemoveResult(this));
				service.removeDir(getLoggedAsUser(), null,
						selectedFolder.getAbsolutePath(), cbk);
			}
		}
	}

	private String buildRelPath(TreeItem item) {
		if (item.getText() != null && !item.getText().equals(getLoggedAsUser())) {
			String p = null;
			while (item != null && !item.getText().equals(getLoggedAsUser())) {
				if (p != null) {
					p = item.getText() + "\\" + p;
				} else {
					p = item.getText();
				}
				item = item.getParentItem();
			}
			return p;
		}
		return null;
	}

}
