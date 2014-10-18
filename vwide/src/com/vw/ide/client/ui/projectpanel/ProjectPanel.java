/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vw.ide.client.ui.projectpanel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
//import com.sencha.gxt.examples.resources.client.images.ExampleImages;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.SelectFileEvent;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.images.ExampleImages;
import com.vw.ide.client.model.BaseDto;
import com.vw.ide.client.model.FileDto;
import com.vw.ide.client.model.FolderDto;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.service.ProcessedResult;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserService.ServiceCallbackForCompleteContent;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowserAsync;
import com.vw.ide.shared.servlet.remotebrowser.RequestedDirScanResult;

/**
 * A composite that contains the shortcut stack panel on the left side. The
 * mailbox tree and shortcut lists don't actually do anything, but serve to show
 * how you can construct an interface using
 * {@link com.google.gwt.user.client.ui.StackPanel},
 * {@link com.google.gwt.user.client.ui.Tree}, and other custom widgets.
 */
public class ProjectPanel extends Composite implements IsWidget,
		PresenterViewerLink {

	interface Binder extends UiBinder<Widget, ProjectPanel> {
	}

	private static final Binder uiBinder = GWT.create(Binder.class);

	private Presenter presenter = null;
	private static int autoId = 0;
	private Widget widget;
	private String selectedFile = "";

	@UiField(provided = true)
	TreeStore<BaseDto> store = new TreeStore<BaseDto>(new KeyProvider());
	@UiField
	Tree<BaseDto, String> projectsDirsField;
	@UiField
	ExampleImages images;

	public FolderDto selectedFolder = null;
	public BaseDto treeSelectedItem;
	public FileItemInfo selectedItem4ContextMenu;
	public static final String SELECT_ID = "SELECT_ID";

	public ProPanelContextMenu contextMenu;

	class KeyProvider implements ModelKeyProvider<BaseDto> {
		@Override
		public String getKey(BaseDto item) {
			return (item instanceof FolderDto ? "f-" : "m-")
					+ item.getId().toString();
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

	public void associatePresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	protected Presenter getAssociatedPresenter() {
		return this.presenter;
	}

	public static class DirContentResult extends
			ProcessedResult<RequestedDirScanResult> {

		private ProjectPanel dialog;

		public DirContentResult() {

		}

		public DirContentResult(ProjectPanel dialog) {
			this.dialog = dialog;

		}

		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(RequestedDirScanResult result) {

			dialog.makeTreeData(result);

			((DevelopmentBoardPresenter) dialog.presenter)
					.updateProjects(((DevelopmentBoardPresenter) dialog.presenter)
							.searchProjects());
			// ((DevelopmentBoardPresenter)
			// dialog.presenter).updateProjectsTree(dialog.store);
			((DevelopmentBoardPresenter) dialog.presenter)
					.updateProjectsFiles(dialog.store);
			// ((DevelopmentBoardPresenter)
			// dialog.presenter).checkStoreFiles(dialog.store);

			BaseDto rootItem = ((DevelopmentBoardPresenter) dialog.presenter)
					.getProjectPanel().store.getRootItems().get(0);
			((DevelopmentBoardPresenter) dialog.presenter).getProjectPanel().projectsDirsField
					.getSelectionModel().select(rootItem, true);
			((DevelopmentBoardPresenter) dialog.presenter).getProjectPanel().projectsDirsField
					.setExpanded(rootItem, true);

			ServerLogEvent serverLogEvent = new ServerLogEvent(result);
			dialog.getAssociatedPresenter().fireEvent(serverLogEvent);
		}
	}

	/**
	 * Constructs a new shortcuts widget using the specified images.
	 * 
	 * @param images
	 *            a bundle that provides the images for this widget
	 */
	public ProjectPanel() {
		if (widget == null) {
			widget = constructUi();
		}
		initWidget(widget);
		prepare();
	}

	private Widget constructUi() {
		widget = uiBinder.createAndBindUi(this);
		widget.addStyleName("margin-10");
		// tree.getStyle().setLeafIcon(ExampleImages.INSTANCE.music());

		projectsDirsField.getSelectionModel().setSelectionMode(
				SelectionMode.SINGLE);

		projectsDirsField.getSelectionModel().addSelectionHandler(
				new SelectionHandler<BaseDto>() {

					public void onSelection(SelectionEvent<BaseDto> event) {
						treeSelectedItem = event.getSelectedItem();
						FileItemInfo fileItemInfo = new FileItemInfo();
						fileItemInfo.setName(treeSelectedItem.getName());
						fileItemInfo.setAbsolutePath(treeSelectedItem
								.getAbsolutePath());
						if (treeSelectedItem.getType() == "file") {
							fileItemInfo.setDir(false);
							fileItemInfo
									.setProjectId(((FileDto) treeSelectedItem)
											.getProjectId());
							fileItemInfo.setFileId(((FileDto) treeSelectedItem)
									.getFileId());
							if (presenter != null) {
								presenter.fireEvent(new SelectFileEvent(
										fileItemInfo));
							}
						} else {
							fileItemInfo.setDir(true);
						}
						selectedItem4ContextMenu = fileItemInfo;
					}
				});

		return widget;
	}

	public void prepare() {
		// requestForDirContent(null);
		buildContextMenu();
	}

	public void buildContextMenu() {

		contextMenu = new ProPanelContextMenu();
		contextMenu.setWidth(160);

		contextMenu.addBeforeShowHandler(new BeforeShowHandler() {
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				contextMenu.associatePresenter(getAssociatedPresenter());
				contextMenu.checkEnabling(selectedItem4ContextMenu);
			}
		});

		projectsDirsField.setContextMenu(contextMenu);
	}

	public void requestForDirContent(String parent) {
		RemoteDirectoryBrowserAsync service = RemoteBrowserService.instance()
				.getServiceImpl();
		if (service != null) {
			ServiceCallbackForCompleteContent cbk = RemoteBrowserService
					.instance().buildCallbackForCompleteContent();
			cbk.setProcessedResult(new DirContentResult(this));
			String user = getAssociatedPresenter().getLoggedAsUser();
			service.getDirScan(user, parent, cbk);
		}
	}

	public FolderDto root = null;
	public String basePath = "";

	public FolderDto makeTree(RequestedDirScanResult dirs) {

		String[] arrPath;
		String sOwnerFolderName = "";
		String delims = "[\\\\/]+";

		if (root == null) {
			root = makeFolder("Root", "", "");
			basePath = dirs.getParentPath();
			String user = presenter.getLoggedAsUser();
			FolderDto owner = makeFolder(user, "", "");
			List<BaseDto> children = new ArrayList<BaseDto>();
			children.add(owner);
			root.setChildren(children);
		}

		arrPath = dirs.getParentPath().split(delims);
		sOwnerFolderName = arrPath[arrPath.length - 1];


		FolderDto owner = findOwnerElement(root, sOwnerFolderName,	dirs.getParentPath(), false);
		if (owner != null) {
			FolderDto folder = null;
			String sNewPath = "";

			for (FileItemInfo fi : dirs.getFiles()) {
				if (fi.isDir()) {
					if (owner.getRelPath().trim().length() > 0) {
						sNewPath = owner.getRelPath() + Utils.FILE_SEPARATOR
								+ fi.getName();
					} else {
						sNewPath = fi.getName();
					}
					System.out.println("sNewPath:" + sNewPath);
					folder = makeFolder(fi.getName(), sNewPath,
							fi.getAbsolutePath());
					owner.addOrReplaceChild(folder);
					requestForDirContent(folder.getRelPath());

				} else {
					owner.addOrReplaceChild(makeFileItem(fi.getName(), owner,
							owner.getRelPath(), fi.getAbsolutePath()));
				}
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
		absolutePath = absolutePath.replaceAll("[\\\\]", "/"); 
		for (BaseDto el : ownerFolder.getChildren()) {
			if (el.getType() == "dir") {
				String sRelPathFromAbsPath = absolutePath.substring(basePath
						.length());
				if (sRelPathFromAbsPath.length() > 2) {
					if ((sRelPathFromAbsPath.substring(0, 1).equalsIgnoreCase("\\"))|| (sRelPathFromAbsPath.substring(0, 1)	.equalsIgnoreCase("/"))) {
						sRelPathFromAbsPath = sRelPathFromAbsPath.substring(1);
					}
				}

				if (el.getRelPath().trim().equalsIgnoreCase(sRelPathFromAbsPath.trim())) {
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

	public TreeStore<BaseDto> getStore() {
		return store;
	}

}
