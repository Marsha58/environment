package com.vw.ide.client.dialog.remotebrowser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.vw.ide.client.dialog.VwmlDialog;
import com.vw.ide.client.dialog.simple.SimpleSinglelineEditDialog;
import com.vw.ide.client.service.ProcessedResult;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForAnyOperation;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForCompleteContent;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.client.utils.Utils.YesNoMsgBoxSelctionCallback;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowserAsync;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestedDirScanResult;

/**
 * Selects file/directory on remote side
 * @author Oleg
 *
 */
public class RemoteDirectoryBrowserDialog extends VwmlDialog {
	
	public static class NewFolderDialogResult implements SimpleSinglelineEditDialog.ResultCallback {

		private RemoteDirectoryBrowserDialog owner;
		
		public NewFolderDialogResult(RemoteDirectoryBrowserDialog owner) {
			this.owner = owner;
		}
		
		public void setResult(String result) {
			owner.onCreateDirectory(result);
		}
	}
	
	/**
	 * Called upon response from remote service
	 * @author Oleg
	 *
	 */
	public static class DirContentResult extends ProcessedResult<RequestedDirScanResult> {

		private RemoteDirectoryBrowserDialog dialog;
		
		public DirContentResult() {
			
		}
		
		public DirContentResult(RemoteDirectoryBrowserDialog dialog) {
			this.dialog = dialog;
		}
		
		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(RequestedDirScanResult result) {
			dialog.fillDialog(result);
		}
	}
	
	public static class DirOperationCreateResult extends ProcessedResult<RequestDirOperationResult> {

		private RemoteDirectoryBrowserDialog dialog;
		
		public DirOperationCreateResult() {
			
		}
		
		public DirOperationCreateResult(RemoteDirectoryBrowserDialog dialog) {
			this.dialog = dialog;
			
		}
		
		@Override
		public void onFailure(Throwable caught) {
			Utils.messageBox("Error", caught.getMessage(), null);
		}

		@Override
		public void onSuccess(RequestDirOperationResult result) {
			if (result.getRetCode().intValue() != 0) {
				Utils.messageBox("Warning", "The operation '" + result.getOperation() + "' failed.\r\nResult'" + result.getResult() + "'", null);
			}
			else {
				dialog.removeAllChildrenStartingFromSelected();
				dialog.refreshTreeStartingFromSelected();
			}
		}
	}

	public static class DirOperationRemoveResult extends ProcessedResult<RequestDirOperationResult> {

		private RemoteDirectoryBrowserDialog dialog;
		
		public DirOperationRemoveResult() {
			
		}
		
		public DirOperationRemoveResult(RemoteDirectoryBrowserDialog dialog) {
			this.dialog = dialog;
		}
		
		@Override
		public void onFailure(Throwable caught) {
			Utils.messageBox("Error", caught.getMessage(), null);
		}

		@Override
		public void onSuccess(RequestDirOperationResult result) {
			if (result.getRetCode().intValue() != 0) {
				Utils.messageBox("Warning", "The operation '" + result.getOperation() + "' failed.\r\nResult'" + result.getResult() + "'", null);
			}
			else {
				dialog.removeSelected();
			}
		}
	}
	
	public static class ConfirmDirectoryRemoving implements YesNoMsgBoxSelctionCallback {

		private RemoteDirectoryBrowserDialog dialog;
		
		public ConfirmDirectoryRemoving(RemoteDirectoryBrowserDialog dialog) {
			this.dialog = dialog;
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
	private static RemoteDirectoryBrowserDialogUiBinder uiBinder = GWT.create(RemoteDirectoryBrowserDialogUiBinder.class);
	@UiField ListBox filesField;
	@UiField Tree dirsField;
	@UiField Label selectedPathField;
	@UiField PushButton createDir;
	@UiField PushButton removeDir;
	@UiField PushButton refreshDir;

	interface RemoteDirectoryBrowserDialogUiBinder extends UiBinder<Widget, RemoteDirectoryBrowserDialog> {
	}

	public RemoteDirectoryBrowserDialog() {
		super.setWidget(uiBinder.createAndBindUi(this));
		

		bind();
	}

	public RemoteDirectoryBrowserDialog(String firstName) {
		super.setWidget(uiBinder.createAndBindUi(this));
		bind();
	}

	@UiHandler("cancel")
	void onCancelClick(ClickEvent event) {
		this.hide(true);	
	}
	
	@UiHandler("ok")
	void onOkClick(ClickEvent event) {
	}
	


	/**
	 * Performs preparation steps
	 */
	public void prepare() {
		requestForDirContent(null);
		if (dirsField.getItemCount() != 0) {
			dirsField.setSelectedItem(dirsField.getItem(0));
		}
	}
	
	protected void bind() {
		filesField.setVisibleItemCount(100);
	}
	
	protected void fillDialog(RequestedDirScanResult dirs) {
		TreeItem parent = null;
		if (dirsField.getItemCount() == 0) {
			dirsField.setAnimationEnabled(true);
			parent = new TreeItem();
			parent.setText(getLoggedAsUser());
			ItemResource r = new ItemResource();
			r.setFullPath(dirs.getParentPath());
			parent.setUserObject(r);
			dirsField.addItem(parent);
		}
		else {
			parent = dirsField.getSelectedItem();
		}
		boolean addItems = (parent.getChildCount() == 0);
		filesField.clear();
		for(FileItemInfo fi : dirs.getFiles()) {
			if (fi.isDir()) {
				if (addItems) {
					ItemResource r = new ItemResource();
					r.setFullPath(fi.getPath());
					parent.addTextItem(fi.getName()).setUserObject(r);
				}
			}
			else {
				filesField.addItem(fi.getPath());
			}
		}
	}
	
	protected void requestForDirContent(String parent) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForCompleteContent cbk = RemoteBrowserService.instance().buildCallbackForCompleteContent();
			cbk.setProcessedResult(new DirContentResult(this));
			service.getDirScan(getLoggedAsUser(), parent, cbk);
		}
	}

	protected void refreshTreeStartingFromSelected() {
		TreeItem selected = dirsField.getSelectedItem();
		if (selected != null) {
			String path = buildRelPath(selected);
			requestForDirContent(path);
			ItemResource r = (ItemResource)selected.getUserObject();
			if (r != null) {
				selectedPathField.setText(r.getFullPath());
			}
		}
	}

	protected void removeAllChildrenStartingFromSelected() {
		TreeItem selected = dirsField.getSelectedItem();
		if (selected != null) {
			int children = selected.getChildCount();
			for(int i = 0; i < children; i++) {
				selected.getChild(0).remove();
			}
		}
	}
	
	protected void removeSelected() {
		TreeItem selected = dirsField.getSelectedItem();
		if (selected != null) {
			selected.remove();
		}
	}
	
	@UiHandler("dirsField")
	protected void onDirsFieldSelection(SelectionEvent<TreeItem> event) {
		refreshTreeStartingFromSelected();
	}
	
	@UiHandler("createDir")
	protected void onCreateDirClick(ClickEvent event) {
		if (dirsField.getSelectedItem() != null) {
			SimpleSinglelineEditDialog d = new SimpleSinglelineEditDialog();
			d.setResult(new NewFolderDialogResult(this));
			d.setEditLabelText("Folder name");
			d.show(s_createNewFolderDialog, null, getPopupLeft() - 30, getPopupTop() + 30);
		}
		else {
			Utils.messageBox("Select folder", "Select folder to be parent for new one", null);
		}
	}

	@UiHandler("removeDir")
	protected void onRemoveDirClick(ClickEvent event) {
		if (dirsField.getSelectedItem() != null) {
			ItemResource resource = (ItemResource)dirsField.getSelectedItem().getUserObject();
			if (resource != null) {
				Utils.messageBoxYesNo("Confirmation required", "The folder '" + resource.getFullPath() +"' is going to be removed",
						              null,
						              new ConfirmDirectoryRemoving(this));
			}
			else {
				Utils.messageBox("Error", "Inconsistency found; selection doesn't contain resources", null);
			}
		}
		else {
			Utils.messageBox("Select folder", "Select folder to remove", null);
		}
	}

	@UiHandler("refreshDir")
	protected void onRefreshDirClick(ClickEvent event) {
		if (dirsField.getSelectedItem() != null) {
			removeAllChildrenStartingFromSelected();
			refreshTreeStartingFromSelected();
		}
		else {
			Utils.messageBox("Select folder", "Select folder to refresh", null);
		}
	}
	
	protected void onCreateDirectory(String dir) {
		if (dirsField.getSelectedItem() != null) {
			RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance().getServiceImpl();
			if (service != null) {
				ServiceCallbackForAnyOperation cbk = RemoteBrowserService.instance().buildCallbackForAnyOperation();
				cbk.setProcessedResult(new DirOperationCreateResult(this));
				ItemResource resource = (ItemResource)dirsField.getSelectedItem().getUserObject();
				if (resource != null) {
					service.createDir(getLoggedAsUser(), resource.getFullPath(), dir, cbk);
				}
			}
		}
	}
	
	protected void onRemoveDirectory() {
		if (dirsField.getSelectedItem() != null) {
			RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance().getServiceImpl();
			if (service != null) {
				ServiceCallbackForAnyOperation cbk = RemoteBrowserService.instance().buildCallbackForAnyOperation();
				cbk.setProcessedResult(new DirOperationRemoveResult(this));
				ItemResource resource = (ItemResource)dirsField.getSelectedItem().getUserObject();
				if (resource != null) {
					service.removeDir(getLoggedAsUser(), null, resource.getFullPath(), cbk);
				}
			}
		}
	}
	
	private String buildRelPath(TreeItem item) {
		if (item.getText() != null && !item.getText().equals(getLoggedAsUser())) {
			String p = null;
			while(item != null && !item.getText().equals(getLoggedAsUser())) {
				if (p != null) {
					p = item.getText() + "/" + p;
				}
				else {
					p = item.getText();
				}
				item = item.getParentItem();
			}
			return p;
		}
		return null;
	}
}
