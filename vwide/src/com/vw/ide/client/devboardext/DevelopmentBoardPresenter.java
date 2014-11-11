package com.vw.ide.client.devboardext;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.vw.ide.client.devboardext.event.handler.AceColorThemeChangedEventHandler;
import com.vw.ide.client.devboardext.event.handler.EditorTabClosedEventHandler;
import com.vw.ide.client.devboardext.event.handler.FileStateChangedEventHandler;
import com.vw.ide.client.devboardext.event.handler.GetDirContentEventHandler;
import com.vw.ide.client.devboardext.event.handler.LogoutEventHandler;
import com.vw.ide.client.devboardext.event.handler.OpenFringeManagerEventHandler;
import com.vw.ide.client.devboardext.event.handler.ProjectMenuEventHandler;
import com.vw.ide.client.devboardext.event.handler.SaveFileEventHandler;
import com.vw.ide.client.devboardext.event.handler.SelectFileEventHandler;
import com.vw.ide.client.devboardext.event.handler.ServerLogEventHandler;
import com.vw.ide.client.devboardext.service.browser.callbacks.GettingUserStateResultCallback;
import com.vw.ide.client.dialog.fringemanagment.event.handler.GetCategoriesEventHandler;
import com.vw.ide.client.dialog.fringemanagment.event.handler.GetFringesEventHandler;
import com.vw.ide.client.event.handler.AceColorThemeChangedHandler;
import com.vw.ide.client.event.handler.EditorTabClosedHandler;
import com.vw.ide.client.event.handler.FileStateChangedHandler;
import com.vw.ide.client.event.handler.GetCategoriesHandler;
import com.vw.ide.client.event.handler.GetDirContentHandler;
import com.vw.ide.client.event.handler.GetFringesHandler;
import com.vw.ide.client.event.handler.LogoutHandler;
import com.vw.ide.client.event.handler.ProjectMenuHandler;
import com.vw.ide.client.event.handler.SaveFileHandler;
import com.vw.ide.client.event.handler.SelectFileHandler;
import com.vw.ide.client.event.handler.ServerLogHandler;
import com.vw.ide.client.event.handler.fringes.OpenFringeManagerHandler;
import com.vw.ide.client.event.uiflow.AceColorThemeChangedEvent;
import com.vw.ide.client.event.uiflow.EditorTabClosedEvent;
import com.vw.ide.client.event.uiflow.FileStateChangedEvent;
import com.vw.ide.client.event.uiflow.GetCategoriesEvent;
import com.vw.ide.client.event.uiflow.GetDirContentEvent;
import com.vw.ide.client.event.uiflow.GetFringesEvent;
import com.vw.ide.client.event.uiflow.LogoutEvent;
import com.vw.ide.client.event.uiflow.ProjectMenuEvent;
import com.vw.ide.client.event.uiflow.SaveFileEvent;
import com.vw.ide.client.event.uiflow.SelectFileEvent;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.event.uiflow.fringes.OpenFringeManagerEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.projects.ProjectManager;
import com.vw.ide.client.projects.ProjectManagerImpl;
import com.vw.ide.client.service.remotebrowser.RemoteBrowserServiceBroker;
import com.vw.ide.client.ui.editorpanel.EditorPanel;
import com.vw.ide.client.ui.projectpanel.ProjectPanel;
import com.vw.ide.client.ui.editorpanel.FileSheet;
import com.vw.ide.client.ui.toppanel.TopPanel;
import com.vw.ide.server.servlet.fringes.persistance.dao.CategoriesDAO;
import com.vw.ide.server.servlet.fringes.persistance.dao.FringesDAO;
import com.vw.ide.server.servlet.fringes.persistance.dao.FringesDAOFactory;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

/**
 * Development board screen
 * 
 * @author Oleg
 * 
 */
public class DevelopmentBoardPresenter extends Presenter {

	public final HandlerManager eventBus;
	private final PresenterViewerLink view;
	private ProjectManager projectManager;
	private FileItemInfo selectedItemInTheProjectTree;
	private Long selectedProjectInTheProjectTree;
	
	
	

	
	
	@SuppressWarnings("serial")
	private static Map<Type<?>, Presenter.PresenterEventHandler> dispatcher = new HashMap<Type<?>, Presenter.PresenterEventHandler>() {
		{
			put(SelectFileEvent.TYPE, new SelectFileEventHandler());
			put(SaveFileEvent.TYPE, new SaveFileEventHandler());
			put(FileStateChangedEvent.TYPE, new FileStateChangedEventHandler());
			put(AceColorThemeChangedEvent.TYPE, new AceColorThemeChangedEventHandler());
			put(EditorTabClosedEvent.TYPE, new EditorTabClosedEventHandler());
			put(GetDirContentEvent.TYPE, new GetDirContentEventHandler());
			put(LogoutEvent.TYPE, new LogoutEventHandler());
			put(ProjectMenuEvent.TYPE, new ProjectMenuEventHandler());
			put(ServerLogEvent.TYPE, new ServerLogEventHandler());
			put(OpenFringeManagerEvent.TYPE, new OpenFringeManagerEventHandler());
		}
	};
	
	
	public DevelopmentBoardPresenter(HandlerManager eventBus, PresenterViewerLink view) {
		this.eventBus = eventBus;
		this.view = view;
		if (view != null) {
			view.associatePresenter(this);
		}
		projectManager = new ProjectManagerImpl();
		((DevelopmentBoard)view).setProjectManager(projectManager);
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		RemoteBrowserServiceBroker.requestForGettingUserState(getLoggedAsUser(), new GettingUserStateResultCallback(this));
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}

	@Override
	public void handleEvent(GwtEvent<?> event) {
		Presenter.PresenterEventHandler handler = dispatcher.get(event.getAssociatedType());
		if (handler != null) {
			handler.handler(this, event);
		}
	}

	@Override
	public void registerOnEventBus(HandlerManager eventBus) {
		for(Presenter.PresenterEventHandler handler : dispatcher.values()) {
			handler.setPresenter(this);
		}
		eventBus.addHandler(SelectFileEvent.TYPE, (SelectFileHandler)dispatcher.get(SelectFileEvent.TYPE));
		eventBus.addHandler(SaveFileEvent.TYPE, (SaveFileHandler)dispatcher.get(SaveFileEvent.TYPE));
		eventBus.addHandler(FileStateChangedEvent.TYPE, (FileStateChangedHandler)dispatcher.get(FileStateChangedEvent.TYPE));
		eventBus.addHandler(AceColorThemeChangedEvent.TYPE, (AceColorThemeChangedHandler)dispatcher.get(AceColorThemeChangedEvent.TYPE));
		eventBus.addHandler(EditorTabClosedEvent.TYPE, (EditorTabClosedHandler)dispatcher.get(EditorTabClosedEvent.TYPE));
		eventBus.addHandler(GetDirContentEvent.TYPE, (GetDirContentHandler)dispatcher.get(GetDirContentEvent.TYPE));
		eventBus.addHandler(LogoutEvent.TYPE, (LogoutHandler)dispatcher.get(LogoutEvent.TYPE));
		eventBus.addHandler(ProjectMenuEvent.TYPE, (ProjectMenuHandler)dispatcher.get(ProjectMenuEvent.TYPE));
		eventBus.addHandler(ServerLogEvent.TYPE, (ServerLogHandler)dispatcher.get(ServerLogEvent.TYPE));
		eventBus.addHandler(OpenFringeManagerEvent.TYPE, (OpenFringeManagerHandler)dispatcher.get(OpenFringeManagerEvent.TYPE));
	}
	
	@Override
	public void unregisterOnEventBus(HandlerManager eventBus) {
		eventBus.removeHandler(SelectFileEvent.TYPE, (SelectFileHandler)dispatcher.get(SelectFileEvent.TYPE));
		eventBus.removeHandler(SaveFileEvent.TYPE, (SaveFileHandler)dispatcher.get(SaveFileEvent.TYPE));
		eventBus.removeHandler(FileStateChangedEvent.TYPE, (FileStateChangedHandler)dispatcher.get(FileStateChangedEvent.TYPE));
		eventBus.removeHandler(AceColorThemeChangedEvent.TYPE, (AceColorThemeChangedHandler)dispatcher.get(AceColorThemeChangedEvent.TYPE));
		eventBus.removeHandler(EditorTabClosedEvent.TYPE, (EditorTabClosedHandler)dispatcher.get(EditorTabClosedEvent.TYPE));
		eventBus.removeHandler(GetDirContentEvent.TYPE, (GetDirContentHandler)dispatcher.get(GetDirContentEvent.TYPE));
		eventBus.removeHandler(LogoutEvent.TYPE, (LogoutHandler)dispatcher.get(LogoutEvent.TYPE));
		eventBus.removeHandler(ProjectMenuEvent.TYPE, (ProjectMenuHandler)dispatcher.get(ProjectMenuEvent.TYPE));
		eventBus.removeHandler(ServerLogEvent.TYPE, (ServerLogHandler)dispatcher.get(ServerLogEvent.TYPE));
		eventBus.removeHandler(OpenFringeManagerEvent.TYPE, (OpenFringeManagerHandler)dispatcher.get(OpenFringeManagerEvent.TYPE));
	}

	
	public TopPanel getTopPanel() {
		return ((DevelopmentBoard) view).topPanel;
	}

	public DevelopmentBoard getView() {
		return (DevelopmentBoard)view;
	}

	public ProjectManager getProjectManager() {
		return projectManager;
	}
	
	public static String calculateCheckSum(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

	public FileItemInfo getSelectedItemInTheProjectTree() {
		return selectedItemInTheProjectTree;
	}

	public Long getSelectedProjectInTheProjectTree() {
		return selectedProjectInTheProjectTree;
	}

	public void setSelectedItemInTheProjectTree(FileItemInfo selectedItemInTheProjectTree) {
		this.selectedItemInTheProjectTree = selectedItemInTheProjectTree;
	}

	public void setSelectedProjectInTheProjectTree(Long selectedProjectInTheProjectTree) {
		this.selectedProjectInTheProjectTree = selectedProjectInTheProjectTree;
	}

	protected boolean isValidFileName(String fileName) {
		return true;
	}
	
	public ProjectPanel getProjectPanel() {
		return ((DevelopmentBoard) view).getProjectPanel();
	}

	public EditorPanel getEditorPanel() {
		return ((DevelopmentBoard) view).getEditorPanel();
	}
	
	public ContentPanel getEditorContentPanel() {
		return ((DevelopmentBoard) view).editorContentPanel;
	}	
	
	public void scrollToTab(FileSheet curFileSheet, boolean animate) {
		((DevelopmentBoard) view).scrollToTab(curFileSheet, animate);
	}	
	
	
}
