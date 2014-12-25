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
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.TreeStore.TreeNode;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent.DndDragMoveHandler;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent.DndDragStartHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.TreeDragSource;
import com.sencha.gxt.dnd.core.client.TreeDropTarget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.vw.ide.client.Resources;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.devboardext.operation.block.RenameOperationBlock;
import com.vw.ide.client.event.uiflow.MoveFileEvent;
import com.vw.ide.client.event.uiflow.SelectFileEvent;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.images.IdeImages;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerServiceBroker;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.projectmanager.RequestUserAvailableProjectResult;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.userstate.UserStateInfo;

/**
 * A composite that contains the shortcut stack panel on the left side. The
 * mailbox tree and shortcut lists don't actually do anything, but serve to show
 * how you can construct an interface using
 * {@link com.google.gwt.user.client.ui.StackPanel},
 * {@link com.google.gwt.user.client.ui.Tree}, and other custom widgets.
 */
@SuppressWarnings("deprecation")
public class ProjectPanel extends Composite implements IsWidget, PresenterViewerLink {

	interface Binder extends UiBinder<Widget, ProjectPanel> {
	}

	public static class ProjectItemInfo implements TreeStore.TreeNode<ProjectItemInfo>, Comparator<ProjectItemInfo> {

		private ProjectDescription projectDescription;
		private FileItemInfo associatedData = new FileItemInfo();
		private FileSheet fileSheet;
		private boolean markAsProject = false;
		private boolean alreadyOpened = false;
		private boolean isEdited = false;
		private boolean markAsUserRoot = false;
		
		@Override
		public List<? extends com.sencha.gxt.data.shared.TreeStore.TreeNode<ProjectItemInfo>> getChildren() {
			return null;
		}

		public void copyTo(ProjectItemInfo to) {
			to.setAlreadyOpened(alreadyOpened);
			to.setEdited(isEdited);
			to.getAssociatedData().setDir(getAssociatedData().isDir());
			to.getAssociatedData().setEdited(getAssociatedData().isEdited());
			to.getAssociatedData().setContent(getAssociatedData().getContent());
			to.setFileSheet(fileSheet);
			to.setMarkAsProject(markAsProject);
			to.setMarkAsUserRoot(markAsUserRoot);
			to.setProjectDescription(projectDescription);
		}
		
		@Override
		public ProjectItemInfo getData() {
			return this;
		}

		public FileItemInfo getAssociatedData() {
			return associatedData;
		}

		public void setAssociatedData(FileItemInfo associatedData) {
			this.associatedData = associatedData;
		}

		public ProjectDescription getProjectDescription() {
			return projectDescription;
		}

		public void setProjectDescription(ProjectDescription projectDescription) {
			this.projectDescription = projectDescription;
		}

		public boolean isMarkAsProject() {
			return markAsProject;
		}

		public void setMarkAsProject(boolean markAsProject) {
			this.markAsProject = markAsProject;
		}
		
		public boolean isAlreadyOpened() {
			return alreadyOpened;
		}

		public void setAlreadyOpened(boolean alreadyOpened) {
			this.alreadyOpened = alreadyOpened;
		}

		public FileSheet getFileSheet() {
			return fileSheet;
		}

		public void setFileSheet(FileSheet fileSheet) {
			this.fileSheet = fileSheet;
		}

		public boolean isEdited() {
			return isEdited;
		}

		public void setEdited(boolean isEdited) {
			this.isEdited = isEdited;
		}

		public boolean isMarkAsUserRoot() {
			return markAsUserRoot;
		}

		public void setMarkAsUserRoot(boolean markAsUserRoot) {
			this.markAsUserRoot = markAsUserRoot;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((associatedData == null) ? 0 : associatedData.generateKey().hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ProjectItemInfo other = (ProjectItemInfo) obj;
			if (associatedData == null) {
				if (other.associatedData != null)
					return false;
			} else if (!associatedData.generateKey().equals(other.associatedData.generateKey()))
				return false;
			return true;
		}

		@Override
		public int compare(ProjectItemInfo o1, ProjectItemInfo o2) {
			String s1 = o1.getAssociatedData().generateKey();
			String s2 = o2.getAssociatedData().generateKey();
			return s1.compareTo(s2);
		}
	}
	
	class KeyProvider implements ModelKeyProvider<ProjectItemInfo> {
		@Override
		public String getKey(ProjectItemInfo item) {
			return item.getAssociatedData().generateKey();
		}
	}

	private static class AvailableProjectsResultCallback extends ResultCallback<RequestUserAvailableProjectResult> {

		private ProjectPanel owner;
		
		public AvailableProjectsResultCallback(ProjectPanel owner) {
			this.owner = owner;
		}
		
		@Override
		public void handle(RequestUserAvailableProjectResult result) {
			owner.makeTree(result);
			owner.getAssociatedPresenter().fireEvent(new ServerLogEvent(result));
		}
	}

	@UiField(provided = true)
	TreeStore<ProjectItemInfo> store = new TreeStore<ProjectItemInfo>(new KeyProvider());
	@UiField
	Tree<ProjectItemInfo, String> projectsField;
	@UiField
	IdeImages images;
	TreeDragSource<ProjectItemInfo> projectsFieldDragSource;
	TreeDropTarget<ProjectItemInfo> projectsFieldDragTarget;	

	private Presenter presenter = null;
	private Widget widget;
	private ProjectItemInfo treeSelectedItem;
	private ProjectPanelContextMenu contextMenu;

	public static final String SELECT_ID = "SELECT_ID";

	private static final Binder uiBinder = GWT.create(Binder.class);
	
	@UiFactory
	public ValueProvider<ProjectItemInfo, String> createValueProvider() {
		return new ValueProvider<ProjectItemInfo, String>() {

			@Override
			public String getValue(ProjectItemInfo object) {
				return object.getAssociatedData().getName();
			}

			@Override
			public void setValue(ProjectItemInfo object, String value) {
			}

			@Override
			public String getPath() {
				return "associatedData";
			}
		};
	}

	public ProjectItemInfo getTreeSelectedItem() {
		return treeSelectedItem;
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

	public void requestUserProjects(String user) {
		ProjectManagerServiceBroker.requestForAvailableProjects(user, new AvailableProjectsResultCallback(this));
	}

	public void restoreView(UserStateInfo state) {
		for(FileItemInfo f : state.getOpenedFiles()) {
			selectByKey(f.generateKey());
		}
		if (state.getFileIdSelected() != null && state.getFileIdSelected().getRelPath() != null) {
			selectByKey(state.getFileIdSelected().generateKey());
		}
	}

	public void buildTreeBranchView(ProjectItemInfo parent, FileItemInfo fi, ProjectItemInfo readyItem) {
		ProjectItemInfo p = buildTreeBranch(parent.getProjectDescription(), parent, fi);
		if (p != null && !fi.isDir()) {
			if (readyItem == null) {
				readyItem = new ProjectItemInfo();
			}
			readyItem.setProjectDescription(parent.getProjectDescription());
			readyItem.setAssociatedData(fi);
			String projFullPath = Utils.createFullProjectPath(parent.getProjectDescription());
			String relPath = fi.getAbsolutePath().substring(projFullPath.length());
			fi.setRelPath(relPath);
			store.add(p, readyItem);
		}
	}

	public List<FileItemInfo> getActualListOfFiles(ProjectDescription projectDescription) {
		List<FileItemInfo> fileItems = null;
		ProjectItemInfo pi = store.findModelWithKey(makeProjectRoot(projectDescription, projectDescription.getUserName()).getAssociatedData().generateKey());
		if (pi != null) {
			List<ProjectItemInfo> projectItems = store.getAllChildren(pi);
			fileItems = new ArrayList<FileItemInfo>();
			for(ProjectItemInfo p : projectItems) {
				fileItems.add(p.getAssociatedData());
			}
		}
		return fileItems;
	}
	
	public void deleteItemOnTreeBranchView(ProjectItemInfo parent) {
		store.remove(parent);
	}

	public void renameItemOnTreeBranchView(ProjectItemInfo toRename, FileItemInfo fi, RenameOperationBlock.RenameProjectItemCallback renameCbk) {
		if (fi.isDir()) {
			ProjectItemInfo parent = store.getParent(toRename);
			ProjectItemInfo proxyItem = new ProjectItemInfo();
			toRename.copyTo(proxyItem);
			fi.setAbsolutePath(parent.getAssociatedData().getAbsolutePath() + "/" + fi.getName());
			fi.setRelPath(parent.getAssociatedData().getRelPath() + "/" + fi.getName());
			proxyItem.setAssociatedData(fi);
			TreeStore<ProjectItemInfo> renamed = createEphemerialSubTree(proxyItem,
																		store.getChildren(toRename),
																		renameCbk);
			deleteItemOnTreeBranchView(toRename);
			List<ProjectItemInfo> renamedItems = renamed.getAllChildren(renamed.getRootItems().get(0));
			for(ProjectItemInfo pi : renamedItems) {
				buildTreeBranchView(parent, pi.getAssociatedData(), pi);
			}
		}
		else {
			ProjectItemInfo parent = store.getParent(toRename);
			ProjectItemInfo proxyItem = new ProjectItemInfo();
			toRename.copyTo(proxyItem);
			proxyItem.setAssociatedData(fi);
			if (renameCbk != null) {
				renameCbk.rename(toRename, proxyItem);
			}
			store.remove(toRename);
			store.add(parent, proxyItem);
		}
	}

	public void moveItemOnTreeBranchView(ProjectItemInfo itemToMove, ProjectItemInfo neighbour, RenameOperationBlock.RenameProjectItemCallback renameCbk) {
		if (neighbour.getAssociatedData().isDir()) {
			if (itemToMove.getAssociatedData().isDir()) {
				TreeStore<ProjectItemInfo> moved = createEphemerialSubTree(neighbour,
																			itemToMove,
																			renameCbk);
				deleteItemOnTreeBranchView(itemToMove);
				List<ProjectItemInfo> movedItems = moved.getAll();
				for(ProjectItemInfo pi : movedItems) {
					buildTreeBranchView(neighbour, pi.getAssociatedData(), pi);
				}
			}
			else {
				ProjectItemInfo proxyItem = new ProjectItemInfo();
				itemToMove.copyTo(proxyItem);
				itemToMove.getAssociatedData().copyTo(proxyItem.getAssociatedData());
				proxyItem.getAssociatedData().setAbsolutePath(neighbour.getAssociatedData().getAbsolutePath());
				proxyItem.getAssociatedData().setRelPath(neighbour.getAssociatedData().getRelPath());
				if (renameCbk != null) {
					renameCbk.rename(itemToMove, proxyItem);
				}
				deleteItemOnTreeBranchView(itemToMove);
				store.add(neighbour, proxyItem);
			}
		}
	}
	
	public void selectParentOf(ProjectItemInfo itemInfo) {
		ProjectItemInfo p = store.getParent(itemInfo);
		if (p != null) {
			projectsField.getSelectionModel().select(p, true);
		}
	}

	public void select(ProjectItemInfo itemInfo) {
		projectsField.getSelectionModel().select(itemInfo, true);
	}

	public void selectByKey(String key) {
		ProjectItemInfo p = store.findModelWithKey(key);
		if (p != null) {
			select(p);
		}
	}

	public boolean isEdited(FileItemInfo fi) {
		boolean r = false;
		ProjectItemInfo p = store.findModelWithKey(fi.generateKey());
		if (p != null && p.isEdited()) {
			r = true;
		}
		return r;
	}
	
	public boolean isAnyEditedNotSavedFile(ProjectDescription description) {
		boolean saveAllEnabled = false;
		for(FileItemInfo fi : description.getProjectFiles()) {
			if (isEdited(fi)) {
				saveAllEnabled = true;
				break;
			}
		}
		return saveAllEnabled;
	}

	protected void prepare() {
		buildContextMenu();
		StoreSortInfo<ProjectItemInfo> ssi = new StoreSortInfo<ProjectItemInfo>(new ProjectItemInfo(), SortDir.DESC);
		store.addSortInfo(ssi);
	}
	
	protected void buildContextMenu() {
		contextMenu = new ProjectPanelContextMenu(); 
		contextMenu.setWidth(170);
		contextMenu.addBeforeShowHandler(new BeforeShowHandler(){
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				contextMenu.associatePresenter(getAssociatedPresenter());
				if (treeSelectedItem != null) {
					contextMenu.checkEnabling(treeSelectedItem);
				}
			}
		});
		projectsField.setContextMenu(contextMenu);
	}
	
	protected void makeTree(RequestUserAvailableProjectResult projects) {
		if (store.getRootCount() == 0) {
			store.add(makeUserRoot(presenter.getLoggedAsUser()));
		}
		ProjectItemInfo root = store.findModelWithKey(FileItemInfo.generateKeyFrom(presenter.getLoggedAsUser(), presenter.getLoggedAsUser()));
		root.setMarkAsUserRoot(true);
		for(ProjectDescription description : projects.getAvailableProjects()) {
			ProjectItemInfo projectRoot = makeProjectRoot(description, description.getMainModuleName());
			projectRoot.setMarkAsProject(true);
			store.add(root, projectRoot);
			buildProjectTreeBranchView(projectRoot);
		}
	}
	
	private void buildProjectTreeBranchView(ProjectItemInfo projectRoot) {
		for(FileItemInfo fi : projectRoot.getProjectDescription().getProjectFiles()) {
			buildTreeBranchView(projectRoot, fi, null);
		}
	}
	
	private ProjectItemInfo buildTreeBranch(ProjectDescription projectDescription, ProjectItemInfo parentItem, FileItemInfo fileInfo) {
		ProjectItemInfo p = parentItem;
		String projectFullPath = Utils.createFullProjectPath(projectDescription);
		if (fileInfo.getAbsolutePath().equals(projectFullPath)) {
			p = parentItem;
		}
		else
		if (fileInfo.getAbsolutePath().startsWith(projectFullPath)) {
			if (!fileInfo.getAbsolutePath().equals(parentItem.getAssociatedData().getAbsolutePath())) {
				String[] splittedPath = fileInfo.getAbsolutePath().substring(parentItem.getAssociatedData().getAbsolutePath().length() + 1).split("[\\\\/]+");
				if (splittedPath.length > 0) {
					int depth = 0;
					return buildTreeBranchImpl(projectDescription, parentItem, splittedPath, fileInfo, depth);
				}
			}
		}
		return p;
	}

	private ProjectItemInfo buildTreeBranchImpl(ProjectDescription projectDescription,
												ProjectItemInfo parentProjectItem,
												String[] splittedPath,
												FileItemInfo fileInfo,
												int depth) {
		ProjectItemInfo r = parentProjectItem;
		if (depth == splittedPath.length) {
			return r;
		}
		List<ProjectItemInfo> items = store.getChildren(parentProjectItem);
		boolean exists = false;
		ProjectItemInfo existingChild = null;
		for(ProjectItemInfo item : items) {
			if (item.getAssociatedData().getName().equals(splittedPath[depth])) {
				exists = true;
				existingChild = item;
				break;
			}
		}
		if (exists) {
			r = buildTreeBranchImpl(projectDescription, existingChild, splittedPath, fileInfo, ++depth);
		}
		else {
			ProjectItemInfo pi = new ProjectItemInfo();
			FileItemInfo fi = new FileItemInfo(splittedPath[depth], null, true);
			fi.setAbsolutePath(parentProjectItem.getAssociatedData().getAbsolutePath() + "/" + splittedPath[depth]);
			fi.setRelPath(parentProjectItem.getAssociatedData().getRelPath() + "/" + splittedPath[depth]);
			pi.setProjectDescription(projectDescription);
			pi.setAssociatedData(fi);
			store.add(parentProjectItem, pi);
			r = buildTreeBranchImpl(projectDescription, pi, splittedPath, fileInfo, ++depth);
		}
		return r;
	}

	private TreeStore<ProjectItemInfo> createEphemerialSubTree(ProjectItemInfo newRoot, ProjectItemInfo item, RenameOperationBlock.RenameProjectItemCallback renameCbk) {
		ProjectItemInfo proxyItem = new ProjectItemInfo();
		item.copyTo(proxyItem);
		item.getAssociatedData().copyTo(proxyItem.getAssociatedData());
		proxyItem.getAssociatedData().setAbsolutePath(newRoot.getAssociatedData().getAbsolutePath() + "/" + item.getAssociatedData().getName());
		proxyItem.getAssociatedData().setRelPath(newRoot.getAssociatedData().getRelPath() + "/" + item.getAssociatedData().getName());
		return createEphemerialSubTree(proxyItem, store.getAllChildren(item), renameCbk);
	}
	
	private TreeStore<ProjectItemInfo> createEphemerialSubTree(ProjectItemInfo from, List<ProjectItemInfo> children, RenameOperationBlock.RenameProjectItemCallback renameCbk) {
		TreeStore<ProjectItemInfo> tStore = new TreeStore<ProjectItemInfo>(new KeyProvider());
		tStore.add(from);
		createEphemerialSubTree(tStore, from, from, children, renameCbk);
		return tStore;
	}
	
	private void createEphemerialSubTree(TreeStore<ProjectItemInfo> eTree, ProjectItemInfo eParent, ProjectItemInfo parent, List<ProjectItemInfo> children, RenameOperationBlock.RenameProjectItemCallback renameCbk) {
		if (children == null) {
			children = store.getChildren(parent);
		}
		if (children != null) {
			for(ProjectItemInfo child : children) {
				ProjectItemInfo pi = new ProjectItemInfo();
				child.copyTo(pi);
				if (child.getAssociatedData().isDir()) {
					pi.getAssociatedData().setAbsolutePath(eParent.getAssociatedData().getAbsolutePath() + "/" + child.getAssociatedData().getName());
					pi.getAssociatedData().setRelPath(eParent.getAssociatedData().getRelPath() + "/" + child.getAssociatedData().getName());
				}
				else {
					pi.getAssociatedData().setAbsolutePath(eParent.getAssociatedData().getAbsolutePath());
					pi.getAssociatedData().setRelPath(eParent.getAssociatedData().getRelPath());
				}
				pi.getAssociatedData().setName(child.getAssociatedData().getName());
				eTree.add(eParent, pi);
				if (renameCbk != null) {
					renameCbk.rename(child, pi);
				}
				createEphemerialSubTree(eTree, pi, child, null, renameCbk);
			}
		}
	}
	
	private static ProjectItemInfo makeUserRoot(String name) {
		ProjectItemInfo pi = new ProjectItemInfo();
		FileItemInfo fi = new FileItemInfo();
		fi.setName(name);
		fi.setDir(true);
		fi.setRelPath(name);
		fi.setAbsolutePath("");
		pi.setAssociatedData(fi);
		return pi;
	}

	private static ProjectItemInfo makeProjectRoot(ProjectDescription projectDescription, String name) {
		ProjectItemInfo pi = new ProjectItemInfo();
		FileItemInfo fi = new FileItemInfo();
		fi.setName(name);
		fi.setDir(true);
		fi.setRelPath(name);
		fi.setAbsolutePath(projectDescription.getProjectPath() + "/" + name);
		pi.setAssociatedData(fi);
		pi.setProjectDescription(projectDescription);
		return pi;
	}
	
	private Widget constructUi() {
		widget = uiBinder.createAndBindUi(this);
		widget.addStyleName("margin-10");
		projectsField.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		projectsField.getSelectionModel().addSelectionHandler(
				new SelectionHandler<ProjectItemInfo>() {
					@Override
					public void onSelection(SelectionEvent<ProjectItemInfo> event) {
						treeSelectedItem = event.getSelectedItem();
						if (treeSelectedItem.isMarkAsProject()) {
							((DevelopmentBoardPresenter)presenter).getView().getTopPanel().enableStarExecution(true);
							return;
						}
						((DevelopmentBoardPresenter)presenter).getView().getTopPanel().enableStarExecution(false);
						if (treeSelectedItem.isMarkAsUserRoot()) {
							return;
						}
						FileItemInfo fileItemInfo = treeSelectedItem.getAssociatedData();
						if (!fileItemInfo.isDir() && !treeSelectedItem.isAlreadyOpened()) {
							if (presenter != null) {
								presenter.fireEvent(new SelectFileEvent(treeSelectedItem));
							}
						}
						else {
							if (treeSelectedItem.isAlreadyOpened()) {
								((DevelopmentBoardPresenter)presenter).getView().scrollToTab(treeSelectedItem);
							}
						}
						if (treeSelectedItem.isEdited()) {
							((DevelopmentBoardPresenter)presenter).getView().getTopPanel().enableSaveFile(true);
							((DevelopmentBoardPresenter)presenter).getView().getTopPanel().enableSaveAll(true);
						}
						else {
							((DevelopmentBoardPresenter)presenter).getView().getTopPanel().enableSaveFile(false);
							((DevelopmentBoardPresenter)presenter).getView().getTopPanel().enableSaveAll(((DevelopmentBoardPresenter)presenter).getView().getProjectPanel().isAnyEditedNotSavedFile(treeSelectedItem.getProjectDescription()));
						}
					}
				});
		projectsField.setIconProvider(new IconProvider<ProjectItemInfo>() {
			@Override
			public ImageResource getIcon(ProjectItemInfo model) {
				if (model.isMarkAsProject()) {
					return Resources.IMAGES.new_wiz_en();
				}
				else
				if (!model.getAssociatedData().isDir()) {
					return Resources.IMAGES.vwml();
				}
				return null;
			}
		});
		projectsFieldDragSource = new TreeDragSource<ProjectItemInfo>(projectsField);
		projectsFieldDragTarget = new TreeDropTarget<ProjectItemInfo>(projectsField);	
		projectsFieldDragTarget.setAllowSelfAsSource(true);
		projectsFieldDragTarget.setFeedback(Feedback.BOTH);
		projectsFieldDragSource.addDragStartHandler(new DndDragStartHandler() {
	        @Override
	        public void onDragStart(DndDragStartEvent event) {
	            @SuppressWarnings("unchecked")
	            List<TreeNode<ProjectItemInfo>> draggingSelection = (List<TreeNode<ProjectItemInfo>>)event.getData();
	            if (draggingSelection != null) {
	            	for (TreeNode<ProjectItemInfo> node : draggingSelection) {
	            		if (node.getData().isMarkAsUserRoot() || node.getData().isMarkAsProject()) {
	            			event.setCancelled(true);
	            			event.getStatusProxy().setStatus(false);
	            			Info.display("Warning", "The node '" + node.getData().getAssociatedData().getName() + "' can't be moved");
	            			return;
	            		}
	            		if (node.getData().isEdited()) {
	            			event.setCancelled(true);
	            			event.getStatusProxy().setStatus(false);
	            			Info.display("Warning", "The node '" + node.getData().getAssociatedData().getName() + "' must be saved before");
	            		}
	            	}
	            }
	        }
		});
		projectsFieldDragTarget.addDropHandler(new DndDropHandler() {
			@Override
			public void onDrop(DndDropEvent event) {
	            @SuppressWarnings("unchecked")
	            List<TreeNode<ProjectItemInfo>> draggingSelection = (List<TreeNode<ProjectItemInfo>>)event.getData();
	            if (draggingSelection != null) {
	            	com.sencha.gxt.widget.core.client.tree.Tree.TreeNode<ProjectItemInfo> item = projectsFieldDragSource.getWidget().findNode(event.getDragEndEvent().getNativeEvent().getEventTarget().<Element> cast());
		            if (item != null) {
		            	ProjectItemInfo placeForDraggedItem = item.getModel();
						if (presenter != null) {
							for (TreeNode<ProjectItemInfo> node : draggingSelection) {
								presenter.fireEvent(new MoveFileEvent(node.getData(), placeForDraggedItem));
							}
						}
		            }
		            else {
            			event.getStatusProxy().setStatus(false);
            			Info.display("Warning", "The selected item(s) can't be moved to this location");
		            }
	            }
			}
		});
		projectsFieldDragTarget.addDragMoveHandler(new DndDragMoveHandler() {
			@Override
			public void onDragMove(DndDragMoveEvent event) {
            	com.sencha.gxt.widget.core.client.tree.Tree.TreeNode<ProjectItemInfo> item = projectsFieldDragSource.getWidget().findNode(event.getDragMoveEvent().getNativeEvent().getEventTarget().<Element> cast());
            	if (item != null) {
            		if (!item.getModel().getAssociatedData().isDir() || item.getModel().isMarkAsProject() || item.getModel().isMarkAsUserRoot()) {
            			event.setCancelled(true);
            			event.getStatusProxy().setStatus(false);
            		}
            	}
			}
		});
		return widget;
	}
} 