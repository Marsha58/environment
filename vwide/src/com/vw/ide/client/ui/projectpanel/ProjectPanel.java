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

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.vw.ide.client.Resources;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.uiflow.SelectFileEvent;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.images.IdeImages;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.projectmanager.ProjectManagerServiceBroker;
import com.vw.ide.client.ui.toppanel.FileSheet;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.projectmanager.RequestUserAvailableProjectResult;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

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

	public static class ProjectItemInfo implements TreeStore.TreeNode<ProjectItemInfo> {

		private ProjectDescription projectDescription;
		private FileItemInfo associatedData;
		private FileSheet fileSheet;
		private boolean markAsProject = false;
		private boolean alreadyOpened = false;
		private boolean isEdited = false;
		
		@Override
		public List<? extends com.sencha.gxt.data.shared.TreeStore.TreeNode<ProjectItemInfo>> getChildren() {
			return null;
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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((associatedData == null) ? 0 : associatedData.getRelPath().hashCode());
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
			} else if (!associatedData.getRelPath().equals(other.associatedData.getRelPath()))
				return false;
			return true;
		}
	}
	
	class KeyProvider implements ModelKeyProvider<ProjectItemInfo> {
		@Override
		public String getKey(ProjectItemInfo item) {
			return item.getAssociatedData().getRelPath();
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

	public void prepare() {
		buildContextMenu();
	}

	public void buildContextMenu() {
		contextMenu = new ProjectPanelContextMenu(); 
		contextMenu.setWidth(160);
		contextMenu.addBeforeShowHandler(new BeforeShowHandler(){
			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				contextMenu.associatePresenter(getAssociatedPresenter());
				if (treeSelectedItem != null) {
					contextMenu.checkEnabling(treeSelectedItem.getAssociatedData());
				}
			}
		});
		projectsField.setContextMenu(contextMenu);
	}

	public void requestUserProjects(String user) {
		ProjectManagerServiceBroker.requestForAvailableProjects(user, new AvailableProjectsResultCallback(this));
	}

	protected void makeTree(RequestUserAvailableProjectResult projects) {
		if (store.getRootCount() == 0) {
			store.add(makeUserRoot(presenter.getLoggedAsUser()));
		}
		ProjectItemInfo root = store.findModelWithKey(presenter.getLoggedAsUser());
		for(ProjectDescription description : projects.getAvailableProjects()) {
			ProjectItemInfo projectRoot = makeProjectRoot(description, description.getMainModuleName());
			projectRoot.setMarkAsProject(true);
			store.add(root, projectRoot);
			buildProjectTreeBranchView(projectRoot);
		}
	}
	
	private void buildProjectTreeBranchView(ProjectItemInfo projectRoot) {
		for(FileItemInfo fi : projectRoot.getProjectDescription().getProjectFiles()) {
			ProjectItemInfo p = buildTreeBranch(projectRoot.getProjectDescription(), projectRoot, fi);
			if (p != null) {
				ProjectItemInfo pi = new ProjectItemInfo();
				pi.setProjectDescription(projectRoot.getProjectDescription());
				pi.setAssociatedData(fi);
				store.add(p, pi);
			}
		}
	}
	
	private ProjectItemInfo buildTreeBranch(ProjectDescription projectDescription, ProjectItemInfo projectItem, FileItemInfo fileInfo) {
		ProjectItemInfo p = null;
		String projectFullPath = projectDescription.getProjectPath() + "/" + projectDescription.getMainModuleName();
		if (fileInfo.getAbsolutePath().equals(projectFullPath)) {
			p = projectItem;
		}
		else
		if (fileInfo.getAbsolutePath().startsWith(projectFullPath)) {
			String[] splittedPath = fileInfo.getAbsolutePath().substring(projectFullPath.length() + 1).split("[\\\\/]+");
			if (splittedPath.length > 0) {
				int depth = 0;
				return buildTreeBranchImpl(projectDescription, projectItem, splittedPath, fileInfo, depth);
			}
			else {
				p = projectItem;
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
		if (items != null && items.contains(splittedPath[depth])) {
			r = buildTreeBranchImpl(projectDescription,
								items.get(items.indexOf(splittedPath[depth])),
								splittedPath,
								fileInfo,
								++depth);
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
	
	private static ProjectItemInfo makeUserRoot(String name) {
		ProjectItemInfo pi = new ProjectItemInfo();
		FileItemInfo fi = new FileItemInfo();
		fi.setName(name);
		fi.setDir(true);
		fi.setRelPath(name);
		pi.setAssociatedData(fi);
		return pi;
	}

	private static ProjectItemInfo makeProjectRoot(ProjectDescription projectDescription, String name) {
		ProjectItemInfo pi = new ProjectItemInfo();
		FileItemInfo fi = new FileItemInfo();
		fi.setName(name);
		fi.setDir(true);
		fi.setRelPath(name);
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
					public void onSelection(SelectionEvent<ProjectItemInfo> event) {
						treeSelectedItem = event.getSelectedItem();
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
					}
				});
		projectsField.setIconProvider(new IconProvider<ProjectItemInfo>() {
			@Override
			public ImageResource getIcon(ProjectItemInfo model) {
				if (model.isMarkAsProject()) {
					return Resources.IMAGES.new_wiz_en();
				}
				return null;
			}
		});
		return widget;
	}
} 