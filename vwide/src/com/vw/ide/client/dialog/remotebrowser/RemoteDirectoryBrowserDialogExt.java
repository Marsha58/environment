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
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.browser.DirBrowserServiceBroker;
import com.vw.ide.client.utils.Utils.YesNoMsgBoxSelctionCallback;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestedDirScanResult;
//import com.google.gwt.user.client.ui.Tree;

/**
 * Selects file/directory on remote side
 * 
 * @author Oleg
 * 
 */
public class RemoteDirectoryBrowserDialogExt extends VwmlDialogExt {

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

	/**
	 * Called upon response from remote service
	 * 
	 * @author Oleg
	 * 
	 */

	public static class DirContentResult extends ResultCallback<RequestedDirScanResult> {

		private RemoteDirectoryBrowserDialogExt dialog;

		public DirContentResult() {

		}

		public DirContentResult(RemoteDirectoryBrowserDialogExt dialog) {
			this.dialog = dialog;

		}

		@Override
		public void handle(RequestedDirScanResult result) {
			dialog.makeTreeData(result);
		}
	}

	public static class DirOperationCreateResult extends ResultCallback<RequestDirOperationResult> {

		private RemoteDirectoryBrowserDialogExt dialog;

		public DirOperationCreateResult() {

		}

		public DirOperationCreateResult(RemoteDirectoryBrowserDialogExt dialog) {
			this.dialog = dialog;
		}

		@Override
		public void handle(RequestDirOperationResult result) {
			if (result.getRetCode().intValue() != 0) {
				String messageAlert = "The operation '" + result.getOperation()
						+ "' failed.\r\nResult'" + result.getResult() + "'";
				AlertMessageBox alertMessageBox = new AlertMessageBox(
						"Warning", messageAlert);
				alertMessageBox.show();
			} else {
				dialog.refreshTreeStartingFromSelected();
			}
		}
	}

	public static class DirOperationRemoveResult extends ResultCallback<RequestDirOperationResult> {

		private RemoteDirectoryBrowserDialogExt dialog;

		public DirOperationRemoveResult() {

		}

		public DirOperationRemoveResult(RemoteDirectoryBrowserDialogExt dialog) {
			this.dialog = dialog;
		}

		@Override
		public void handle(RequestDirOperationResult result) {
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

	public static class ConfirmDirectoryRemoving implements YesNoMsgBoxSelctionCallback {

		private RemoteDirectoryBrowserDialogExt dialog;

		public ConfirmDirectoryRemoving(RemoteDirectoryBrowserDialogExt remoteDirectoryBrowserDialogExt) {
			this.dialog = remoteDirectoryBrowserDialogExt;
		}

		public void selected(SELECTION selection) {
			if (selection == SELECTION.YES) {
				dialog.onRemoveDirectory();
			}
		}
	}

	protected class KeyProvider implements ModelKeyProvider<BaseDto> {
		@Override
		public String getKey(BaseDto item) {
			return (item instanceof FolderDto ? "f-" : "m-") + item.getId().toString();
		}
	}

	@UiFactory
	protected ValueProvider<BaseDto, String> createValueProvider() {
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
	
	protected static class ItemResource {
		private String fullPath;

		public String getFullPath() {
			return fullPath;
		}

		public void setFullPath(String fullPath) {
			this.fullPath = fullPath;
		}
	}
	
	@UiField
	Label selectedPathField;
	@UiField
	TextButton createDir;
	@UiField
	TextButton removeDir;
	@UiField
	TextButton refreshDir;
	private KeyProvider nodeKeyProvider = new KeyProvider();
	@UiField(provided = true)
	TreeStore<BaseDto> store = new TreeStore<BaseDto>(nodeKeyProvider);
	private BaseDto treeSelectedItem;
	@UiField
	Tree<BaseDto, String> treeDirs;
	TextButton bnSelect = new TextButton("Select");
	TextButton bnCancel = new TextButton("Cancel");

	private static String s_createNewFolderDialog = "Create new folder";
	private static RemoteDirectoryBrowserDialogUiBinderExt uiBinder = GWT.create(RemoteDirectoryBrowserDialogUiBinderExt.class);
	public static final String SELECT_ID = "SELECT_ID";
	private static int autoId = 0;

	interface RemoteDirectoryBrowserDialogUiBinderExt extends UiBinder<Widget, RemoteDirectoryBrowserDialogExt> {
	}
	
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
		treeDirs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		treeDirs.getSelectionModel().addSelectionHandler(
				new SelectionHandler<BaseDto>() {
					public void onSelection(SelectionEvent<BaseDto> event) {
						treeSelectedItem = event.getSelectedItem();
						selectedPathField.setText(treeSelectedItem.getAbsolutePath());
						if (treeSelectedItem.getType() == "dir") {
							requestForDirContent(treeSelectedItem.getAbsolutePath());
							((TextButton) getButtonBar().getItemByItemId(SELECT_ID)).enable();
						} else {
							((TextButton) getButtonBar().getItemByItemId(SELECT_ID)).disable();
						}
						if (isSelectedRoot()) {
							removeDir.disable();
						}
						else {
							removeDir.enable();
						}
					}
				});
		bind();
	}

	public RemoteDirectoryBrowserDialogExt(String firstName) {
		super.setWidget(uiBinder.createAndBindUi(this));
		bind();
	}

	/**
	 * Performs preparation steps
	 */
	public void prepare() {
		requestForDirContent(null);
	}

	protected void requestForDirContent(String parent) {
		DirBrowserServiceBroker.requestForDirContent(getLoggedAsUser(), parent, new DirContentResult(this));
	}

	protected void bind() {
	}

	@Override
	protected void onButtonPressed(TextButton textButton) {
		super.onButtonPressed(textButton);
		if (textButton == getButton(PredefinedButton.CANCEL)) {
			hide();
		}
	}

	protected void refreshTreeStartingFromSelected() {
		requestForDirContent(treeSelectedItem.getAbsolutePath());
	}

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
		if (treeSelectedItem != null) {
			String sMessage = "The folder '" + treeSelectedItem.getAbsolutePath()
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
		if (treeSelectedItem != null) {
			requestForDirContent(treeSelectedItem.getAbsolutePath());
		} else {
			requestForDirContent("");
		}
	}

	protected void onCreateDirectory(String dir) {
		if (treeSelectedItem != null) {
			DirBrowserServiceBroker.requestForFolderCreating(getLoggedAsUser(),
															treeSelectedItem.getAbsolutePath(),
															dir,
															new DirOperationCreateResult(this));
		}
	}

	protected void onRemoveDirectory() {
		if (treeSelectedItem != null && !isSelectedRoot()) {
			DirBrowserServiceBroker.requestForFolderDeletion(getLoggedAsUser(),
															null,
															treeSelectedItem.getAbsolutePath(),
															new DirOperationRemoveResult(this));
		}
	}

	private void makeTreeData(RequestedDirScanResult dirs) {
		if (treeSelectedItem != null) {
			addSubtreeTo(treeSelectedItem, dirs);
		}
		else {
			treeSelectedItem = makeFolder(getLoggedAsUser(), "/", dirs.getParentPath());
			treeSelectedItem.setAbsolutePath(dirs.getParentPath());
			store.add(treeSelectedItem);
			addSubtreeTo(treeSelectedItem, dirs);
			treeDirs.getSelectionModel().select(treeSelectedItem, true);
			treeDirs.setExpanded(treeSelectedItem, true);
		}
	}

	private void addSubtreeTo(BaseDto parent, RequestedDirScanResult dirs) {
		List<BaseDto> existing = store.getChildren(parent);
		for(FileItemInfo fi : dirs.getFiles()) {
			if (fi.isDir()) {
				BaseDto node = makeFolder(fi.getName(), parent.getName(), fi.getAbsolutePath());
				if (existing != null && !existing.contains(node)) {
					store.add(parent, node);
				}
			}
		}
	}
	
	private void removeSelected() {
		if (treeSelectedItem != null) {
			BaseDto parent = store.getParent(treeSelectedItem);
			store.remove(treeSelectedItem);
			treeSelectedItem = parent;
			if (treeSelectedItem != null) {
				treeDirs.getSelectionModel().select(treeSelectedItem, true);
				treeDirs.setExpanded(treeSelectedItem, true);
			}
		}
	}
	
	private boolean isSelectedRoot() {
		return ((treeSelectedItem != null) && (store.getParent(treeSelectedItem) == null)) ? true : false;
	}
	
	private static FolderDto makeFolder(String name, String path, String absolutePath) {
		FolderDto theReturn = new FolderDto(++autoId, name, path, absolutePath);
		theReturn.setChildren((List<BaseDto>) new ArrayList<BaseDto>());
		return theReturn;
	}

	@SuppressWarnings("unused")
	private static FileDto makeFileItem(String fileName, String folder, String relPath, String absolutePath) {
		return new FileDto(++autoId, fileName, folder, relPath, absolutePath);
	}
}
