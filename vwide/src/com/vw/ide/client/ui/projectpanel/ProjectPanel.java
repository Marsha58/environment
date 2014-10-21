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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.vw.ide.client.FlowController;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.SelectFileEvent;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.images.ExampleImages;
import com.vw.ide.client.model.BaseDto;
import com.vw.ide.client.model.FileDto;
import com.vw.ide.client.model.FolderDto;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.projects.ProjectItem;
import com.vw.ide.client.projects.ProjectItemImpl;
import com.vw.ide.client.projects.ProjectManager;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserServiceBroker;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RequestedDirScanResult;

/**
 * A composite that contains the shortcut stack panel on the left side. The
 * mailbox tree and shortcut lists don't actually do anything, but serve to show
 * how you can construct an interface using
 * {@link com.google.gwt.user.client.ui.StackPanel},
 * {@link com.google.gwt.user.client.ui.Tree}, and other custom widgets.
 */
public class ProjectPanel extends Composite implements IsWidget, PresenterViewerLink {

	interface Binder extends UiBinder<Widget, ProjectPanel> {
	}

	private static class DirContentResultCallback extends RemoteBrowserServiceBroker.ResultCallback<RequestedDirScanResult> {

		private ProjectPanel owner;

		public DirContentResultCallback(ProjectPanel owner) {
			this.owner = owner;
		}

		@Override
		public void handle(RequestedDirScanResult result) {
			owner.makeTreeData(result);
			owner.updateProjects(owner.searchProjects());
			owner.updateProjectsFiles(owner.getStore());
			owner.updateProjectsDirField(owner.getStore().getRootItems().get(0));
			owner.getAssociatedPresenter().fireEvent(new ServerLogEvent(result));
		}
	}

	@UiField(provided = true)
	TreeStore<BaseDto> store = new TreeStore<BaseDto>(new KeyProvider());
	@UiField
	Tree<BaseDto, String> projectsDirsField;
	@UiField
	ExampleImages images;

	private Presenter presenter = null;
	private static int autoId = 0;
	private Widget widget;
	private DirContentResultCallback dirContentResultCbk = new DirContentResultCallback(this);
	private ProjectManager projectManager;
	private BaseDto treeSelectedItem;
	private FileItemInfo selectedItem4ContextMenu;
	private ProPanelContextMenu contextMenu;
	private FolderDto root = null;
	private String basePath = "";

	public static final String SELECT_ID = "SELECT_ID";
	private static final Binder uiBinder = GWT.create(Binder.class);

	class KeyProvider implements ModelKeyProvider<BaseDto> {
		@Override
		public String getKey(BaseDto item) {
			return (item instanceof FolderDto ? "f-" : "m-") + item.getId().toString();
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

	public ProjectManager getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}

	protected ArrayList<ProjectItem> searchProjects() {
		ArrayList<ProjectItem> projectList = new ArrayList<ProjectItem>();
		for (BaseDto allBaseDto : getStore().getAll()) {
			String sPath = allBaseDto.getRelPath();
			String sName = allBaseDto.getName();
			String sProjectName = selectProjectName(sPath);
			if ((sProjectName + ".xml").equalsIgnoreCase(sName)) {
				if (sProjectName.length() > 0) {
					ProjectItem newProjectItem = new ProjectItemImpl(sProjectName, sPath);
					projectList.add(newProjectItem);
				}
			}
		}
		return projectList;
	}

	protected void updateProjects(ArrayList<ProjectItem> projectsList) {
		for (ProjectItem curPI : projectsList) {
			if (projectManager == null) {
				projectManager = ((DevelopmentBoardPresenter) presenter).getProjectManager();
			}
			Long projectId = this.projectManager.getProjectIdByProjectInfo(curPI);
			if (projectId == -1L) {
				projectManager.addProject(curPI);
			}

		}
	}

	protected void updateProjectsFiles(TreeStore<BaseDto> store) {
		List<BaseDto> bdto = store.getAll();
		for (BaseDto b : bdto) {
			try {
				if (b instanceof FileDto) {
					FileDto curItemInStore = (FileDto) b;
					Long fileId = -1l;
					if (this.projectManager == null) {
						projectManager = ((DevelopmentBoardPresenter) presenter).getProjectManager();						
					}
					Long projectId = this.projectManager.getProjectIdByRelPath(curItemInStore.getRelPath());
					this.projectManager.getFileIdByFilePath(curItemInStore.getAbsolutePath());
					if (!this.projectManager.checkIfFileExists(curItemInStore.getAbsolutePath())) {
						FileItemInfo newFileItemInfo = new FileItemInfo();
						newFileItemInfo.setAbsolutePath(curItemInStore.getAbsolutePath());
						newFileItemInfo.setRelPath(curItemInStore.getRelPath());
						newFileItemInfo.setName(Utils.extractJustFileName(curItemInStore.getAbsolutePath()));
						newFileItemInfo.setDir(false);
						newFileItemInfo.setProjectId(projectId);
						fileId = this.projectManager.addFile(newFileItemInfo);
					} else {
						fileId = this.projectManager.getFileIdByFilePath(curItemInStore.getAbsolutePath());
					}
					curItemInStore.setProjectId(projectId);
					curItemInStore.setFileId(fileId);
				}
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());

			}
		}
	}

	private Widget constructUi() {
		widget = uiBinder.createAndBindUi(this);
		widget.addStyleName("margin-10");
		projectsDirsField.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		projectsDirsField.getSelectionModel().addSelectionHandler(new SelectionHandler<BaseDto>() {
			public void onSelection(SelectionEvent<BaseDto> event) {
				treeSelectedItem = event.getSelectedItem();
				FileItemInfo fileItemInfo = new FileItemInfo();
				fileItemInfo.setName(treeSelectedItem.getName());
				fileItemInfo.setAbsolutePath(treeSelectedItem.getAbsolutePath());
				if (treeSelectedItem.getType() == "file") {
					fileItemInfo.setDir(false);
					fileItemInfo.setProjectId(((FileDto) treeSelectedItem).getProjectId());
					fileItemInfo.setFileId(((FileDto) treeSelectedItem).getFileId());
					if (presenter != null) {
						presenter.fireEvent(new SelectFileEvent(fileItemInfo));
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
		buildContextMenu();
	}

	public void buildContextMenu() {
		contextMenu = new ProPanelContextMenu();
		contextMenu.setWidth(130);
		contextMenu.addBeforeShowHandler(new BeforeShowHandler() {
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				contextMenu.associatePresenter(getAssociatedPresenter());
				contextMenu.checkEnabling(selectedItem4ContextMenu);
			}
		});
		projectsDirsField.setContextMenu(contextMenu);
	}

	public void requestDirContent(String root) {
		RemoteBrowserServiceBroker.requestForDirContent(FlowController.getLoggedAsUser(), root, dirContentResultCbk);
	}

	public FileItemInfo getSelectedItem4ContextMenu() {
		return selectedItem4ContextMenu;
	}

	protected FolderDto makeTree(RequestedDirScanResult dirs) {
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
		try {
			arrPath = dirs.getParentPath().split(delims);
			sOwnerFolderName = arrPath[arrPath.length - 1];
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		FolderDto owner = findOwnerElement(root, sOwnerFolderName, dirs.getParentPath(), false);
		FolderDto folder = null;
		String sNewPath = "";
		for (FileItemInfo fi : dirs.getFiles()) {
			if (fi.isDir()) {
				if (owner.getRelPath().trim().length() > 0) {
					sNewPath = owner.getRelPath() + Utils.FILE_SEPARATOR + fi.getName();
				} else {
					sNewPath = fi.getName();
				}
				folder = makeFolder(fi.getName(), sNewPath, fi.getAbsolutePath());
				
				if(owner != null) {
					owner.addOrReplaceChild(folder);
				} 			
				requestDirContent(folder.getRelPath());
			} else {
				if(owner != null) {
					owner.addOrReplaceChild(makeFileItem(fi.getName(), owner, owner.getRelPath(), fi.getAbsolutePath()));
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

	private FolderDto findOwnerElement(FolderDto ownerFolder, String sFolderName, String absolutePath, boolean IsCatched) {
		FolderDto res = null;
		absolutePath = absolutePath.replaceAll("[\\\\]", "/");
		for (BaseDto el : ownerFolder.getChildren()) {
			if (el.getType() == "dir") {
				String sRelPathFromAbsPath = absolutePath.substring(basePath.length());
				if (sRelPathFromAbsPath.length() > 2) {
					if ((sRelPathFromAbsPath.substring(0, 1).equalsIgnoreCase("\\")) || (sRelPathFromAbsPath.substring(0, 1).equalsIgnoreCase("/"))) {
						sRelPathFromAbsPath = sRelPathFromAbsPath.substring(1);
					}
				}
				if (el.getRelPath().trim().equalsIgnoreCase(sRelPathFromAbsPath.trim())) {
					IsCatched = true;
					res = (FolderDto) el;
					break;
				} else {
					if (!IsCatched) {
						res = findOwnerElement((FolderDto) el, sFolderName, absolutePath, IsCatched);
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

	private static FolderDto makeFolder(String name, String path, String absolutePath) {
		FolderDto theReturn = new FolderDto(++autoId, name, path, absolutePath);
		theReturn.setChildren((List<BaseDto>) new ArrayList<BaseDto>());
		return theReturn;
	}

	private static FileDto makeFileItem(String fileName, FolderDto folder, String relPath, String absolutePath) {
		return makeFileItem(fileName, folder.getName(), relPath, absolutePath);
	}

	private static FileDto makeFileItem(String fileName, String folder, String relPath, String absolutePath) {
		if((fileName == null) || (folder == null)|| (relPath == null)){
			System.out.println("fileName : "+fileName + ",folder : "+folder+ ",relPath :" + relPath);
		}
		return new FileDto(++autoId, fileName, folder, relPath, absolutePath);
	}

	private TreeStore<BaseDto> getStore() {
		return store;
	}

	private String selectProjectName(String sPath) {
		String sRes = sPath;
		String delims = "[\\\\/]+";
		if (sPath.length() > 0) {
			String[] arrSpl = sPath.split(delims);
			sRes = arrSpl[arrSpl.length - 1];
		}
		return sRes;
	}

	private void updateProjectsDirField(BaseDto rootItem) {
		projectsDirsField.getSelectionModel().select(rootItem, true);
		projectsDirsField.setExpanded(rootItem, true);
	}
}