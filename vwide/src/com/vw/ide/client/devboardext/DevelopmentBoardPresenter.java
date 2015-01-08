package com.vw.ide.client.devboardext;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.vw.ide.client.devboardext.event.handler.AceColorThemeChangedEventHandler;
import com.vw.ide.client.devboardext.event.handler.EditorTabClosedEventHandler;
import com.vw.ide.client.devboardext.event.handler.FileEditedEventHandler;
import com.vw.ide.client.devboardext.event.handler.GetDirContentEventHandler;
import com.vw.ide.client.devboardext.event.handler.LogoutEventHandler;
import com.vw.ide.client.devboardext.event.handler.MoveFileEventHandler;
import com.vw.ide.client.devboardext.event.handler.ProjectMenuEventHandler;
import com.vw.ide.client.devboardext.event.handler.SaveAllFilesEventHandler;
import com.vw.ide.client.devboardext.event.handler.SaveFileEventHandler;
import com.vw.ide.client.devboardext.event.handler.SearchTextEventHandler;
import com.vw.ide.client.devboardext.event.handler.SelectFileEventHandler;
import com.vw.ide.client.devboardext.event.handler.ServerLogEventHandler;
import com.vw.ide.client.devboardext.event.handler.StartProjectExecutionEventHandler;
import com.vw.ide.client.devboardext.service.userstate.callbacks.UserStateGettingResultCallback;
import com.vw.ide.client.devboardext.service.userstate.callbacks.custom.handler.UserStateHandler;
import com.vw.ide.client.event.handler.AceColorThemeChangedHandler;
import com.vw.ide.client.event.handler.EditorTabClosedHandler;
import com.vw.ide.client.event.handler.FileEditedHandler;
import com.vw.ide.client.event.handler.GetDirContentHandler;
import com.vw.ide.client.event.handler.LogoutHandler;
import com.vw.ide.client.event.handler.ProjectMenuHandler;
import com.vw.ide.client.event.handler.SaveFileHandler;
import com.vw.ide.client.event.handler.SelectFileHandler;
import com.vw.ide.client.event.handler.ServerLogHandler;
import com.vw.ide.client.event.uiflow.AceColorThemeChangedEvent;
import com.vw.ide.client.event.uiflow.EditorTabClosedEvent;
import com.vw.ide.client.event.uiflow.FileEditedEvent;
import com.vw.ide.client.event.uiflow.GetDirContentEvent;
import com.vw.ide.client.event.uiflow.LogoutEvent;
import com.vw.ide.client.event.uiflow.MoveFileEvent;
import com.vw.ide.client.event.uiflow.ProjectMenuEvent;
import com.vw.ide.client.event.uiflow.SaveAllFilesEvent;
import com.vw.ide.client.event.uiflow.SaveFileEvent;
import com.vw.ide.client.event.uiflow.SearchTextEvent;
import com.vw.ide.client.event.uiflow.SelectFileEvent;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.event.uiflow.StartProjectExecutionEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.client.presenters.PresenterViewerLink;
import com.vw.ide.client.service.remote.tracer.TracerServiceBroker;
import com.vw.ide.client.service.remote.userstate.RemoteUserStateServiceBroker;
import com.vw.ide.client.ui.projectpanel.ProjectPanel.ProjectItemInfo;
import com.vw.ide.client.ui.toppanel.TopPanel;
import com.vw.ide.shared.servlet.userstate.UserStateInfo;

/**
 * Development board screen
 * 
 * @author Oleg
 * 
 */
public class DevelopmentBoardPresenter extends Presenter {

	private static class RestoreUserDevelopmentEnvironment implements UserStateHandler {

		private DevelopmentBoardPresenter owner;
		
		public RestoreUserDevelopmentEnvironment(DevelopmentBoardPresenter owner) {
			this.owner = owner;
		}
		
		@Override
		public void handle(UserStateInfo userState) {
			owner.getView().restoreView(userState);
		}
	}
	
	public final HandlerManager eventBus;
	private final PresenterViewerLink view;
	private ProjectItemInfo selectedItemInTheProjectTree;
	
	@SuppressWarnings("serial")
	private static Map<Type<?>, Presenter.PresenterEventHandler> dispatcher = new HashMap<Type<?>, Presenter.PresenterEventHandler>() {
		{
			put(SelectFileEvent.TYPE, new SelectFileEventHandler());
			put(SaveFileEvent.TYPE, new SaveFileEventHandler());
			put(FileEditedEvent.TYPE, new FileEditedEventHandler());
			put(MoveFileEvent.TYPE, new MoveFileEventHandler());
			put(AceColorThemeChangedEvent.TYPE, new AceColorThemeChangedEventHandler());
			put(EditorTabClosedEvent.TYPE, new EditorTabClosedEventHandler());
			put(GetDirContentEvent.TYPE, new GetDirContentEventHandler());
			put(LogoutEvent.TYPE, new LogoutEventHandler());
			put(ProjectMenuEvent.TYPE, new ProjectMenuEventHandler());
			put(ServerLogEvent.TYPE, new ServerLogEventHandler());
			put(SaveAllFilesEvent.TYPE, new SaveAllFilesEventHandler());
			put(StartProjectExecutionEvent.TYPE, new StartProjectExecutionEventHandler());
			put(SearchTextEvent.TYPE, new SearchTextEventHandler());
		}
	};
	
	public DevelopmentBoardPresenter(HandlerManager eventBus, PresenterViewerLink view) {
		this.eventBus = eventBus;
		this.view = view;
		if (view != null) {
			view.associatePresenter(this);
		}
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(view.asWidget());
		RemoteUserStateServiceBroker.requestForGettingUserState(getLoggedAsUser(),
																new UserStateGettingResultCallback(this, new RestoreUserDevelopmentEnvironment(this)));
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
		eventBus.addHandler(FileEditedEvent.TYPE, (FileEditedHandler)dispatcher.get(FileEditedEvent.TYPE));
		eventBus.addHandler(AceColorThemeChangedEvent.TYPE, (AceColorThemeChangedHandler)dispatcher.get(AceColorThemeChangedEvent.TYPE));
		eventBus.addHandler(EditorTabClosedEvent.TYPE, (EditorTabClosedHandler)dispatcher.get(EditorTabClosedEvent.TYPE));
		eventBus.addHandler(GetDirContentEvent.TYPE, (GetDirContentHandler)dispatcher.get(GetDirContentEvent.TYPE));
		eventBus.addHandler(LogoutEvent.TYPE, (LogoutHandler)dispatcher.get(LogoutEvent.TYPE));
		eventBus.addHandler(ProjectMenuEvent.TYPE, (ProjectMenuHandler)dispatcher.get(ProjectMenuEvent.TYPE));
		eventBus.addHandler(ServerLogEvent.TYPE, (ServerLogHandler)dispatcher.get(ServerLogEvent.TYPE));
		eventBus.addHandler(MoveFileEvent.TYPE, (MoveFileEventHandler)dispatcher.get(MoveFileEvent.TYPE));
		eventBus.addHandler(SaveAllFilesEvent.TYPE, (SaveAllFilesEventHandler)dispatcher.get(SaveAllFilesEvent.TYPE));
		eventBus.addHandler(StartProjectExecutionEvent.TYPE, (StartProjectExecutionEventHandler)dispatcher.get(StartProjectExecutionEvent.TYPE));
		eventBus.addHandler(SearchTextEvent.TYPE, (SearchTextEventHandler)dispatcher.get(SearchTextEvent.TYPE));
		TracerServiceBroker.registerBackNotification();
	}
	
	@Override
	public void unregisterOnEventBus(HandlerManager eventBus) {
		eventBus.removeHandler(SelectFileEvent.TYPE, (SelectFileHandler)dispatcher.get(SelectFileEvent.TYPE));
		eventBus.removeHandler(SaveFileEvent.TYPE, (SaveFileHandler)dispatcher.get(SaveFileEvent.TYPE));
		eventBus.removeHandler(FileEditedEvent.TYPE, (FileEditedHandler)dispatcher.get(FileEditedEvent.TYPE));
		eventBus.removeHandler(AceColorThemeChangedEvent.TYPE, (AceColorThemeChangedHandler)dispatcher.get(AceColorThemeChangedEvent.TYPE));
		eventBus.removeHandler(EditorTabClosedEvent.TYPE, (EditorTabClosedHandler)dispatcher.get(EditorTabClosedEvent.TYPE));
		eventBus.removeHandler(GetDirContentEvent.TYPE, (GetDirContentHandler)dispatcher.get(GetDirContentEvent.TYPE));
		eventBus.removeHandler(LogoutEvent.TYPE, (LogoutHandler)dispatcher.get(LogoutEvent.TYPE));
		eventBus.removeHandler(ProjectMenuEvent.TYPE, (ProjectMenuHandler)dispatcher.get(ProjectMenuEvent.TYPE));
		eventBus.removeHandler(ServerLogEvent.TYPE, (ServerLogHandler)dispatcher.get(ServerLogEvent.TYPE));
		eventBus.removeHandler(MoveFileEvent.TYPE, (MoveFileEventHandler)dispatcher.get(MoveFileEvent.TYPE));
		eventBus.removeHandler(SaveAllFilesEvent.TYPE, (SaveAllFilesEventHandler)dispatcher.get(SaveAllFilesEvent.TYPE));
		eventBus.removeHandler(StartProjectExecutionEvent.TYPE, (StartProjectExecutionEventHandler)dispatcher.get(StartProjectExecutionEvent.TYPE));
		eventBus.removeHandler(SearchTextEvent.TYPE, (SearchTextEventHandler)dispatcher.get(SearchTextEvent.TYPE));
		TracerServiceBroker.unregisterBackNotification();
	}

	@Override
	public PresenterEventHandler getEventHandlerByType(com.google.gwt.event.shared.GwtEvent.Type<?> type) {
		return dispatcher.get(type);
	}
	
	public TopPanel getTopPanel() {
		return ((DevelopmentBoard) view).topPanel;
	}

	public DevelopmentBoard getView() {
		return (DevelopmentBoard)view;
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

	public ProjectItemInfo getSelectedItemInTheProjectTree() {
		return selectedItemInTheProjectTree;
	}

	public void setSelectedItemInTheProjectTree(ProjectItemInfo selectedItemInTheProjectTree) {
		this.selectedItemInTheProjectTree = selectedItemInTheProjectTree;
	}

	protected boolean isValidFileName(String fileName) {
		return true;
	}
}
