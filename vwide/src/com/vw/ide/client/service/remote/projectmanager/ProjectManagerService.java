package com.vw.ide.client.service.remote.projectmanager;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.vw.ide.client.service.BusConnectivity;
import com.vw.ide.client.service.ServiceCallback;
import com.vw.ide.client.service.VwIdeClientService;
import com.vw.ide.client.service.factory.ServicesStubFactory;
import com.vw.ide.shared.servlet.projectmanager.RemoteProjectManagerServiceAsync;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectAddFileResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectCreationResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectDeletionResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectRemoveFileResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectRenameFileResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectUpdateResult;
import com.vw.ide.shared.servlet.projectmanager.RequestUserAvailableProjectResult;

/**
 * Project manager service proxy
 * @author Oleg
 *
 */
public class ProjectManagerService implements BusConnectivity, VwIdeClientService {

	public static class ServiceCallbackForProjectCreation extends ServiceCallback<RequestProjectCreationResult> {
	}

	public static class ServiceCallbackForProjectDeletion extends ServiceCallback<RequestProjectDeletionResult> {
	}

	public static class ServiceCallbackForUpdatingProject extends ServiceCallback<RequestProjectUpdateResult> {
	}

	public static class ServiceCallbackForAvailableProjects extends ServiceCallback<RequestUserAvailableProjectResult> {
	}

	public static class ServiceCallbackForAddFileToProject extends ServiceCallback<RequestProjectAddFileResult> {
	}

	public static class ServiceCallbackForRemoveFileFromProject extends ServiceCallback<RequestProjectRemoveFileResult> {
	}

	public static class ServiceCallbackForRenameFileOnProject extends ServiceCallback<RequestProjectRenameFileResult> {
	}
	
	private RemoteProjectManagerServiceAsync serviceImpl = ServicesStubFactory.createRemoteProjectManagerServiceAsync();
	private HandlerManager bus;
	private static ProjectManagerService s_instance = null;

	/**
	 * Simple singleton implementation
	 * @return
	 */
	public static synchronized ProjectManagerService instance() {
		if (s_instance != null) {
			return s_instance;
		}
		s_instance = new ProjectManagerService();
		return s_instance;
	}

	
	@Override
	public void setBusRef(HandlerManager busRef) {
		bus = busRef;
	}

	public RemoteProjectManagerServiceAsync getServiceImpl() {
		return serviceImpl;
	}
	
	@Override
	public void fireEvent(GwtEvent<?> event) {
		try {
			bus.fireEvent(event);
		}
		catch(Exception e) {
		}
	}
	
	public ServiceCallbackForProjectCreation buildCallbackForProjectCreation() {
		return new ServiceCallbackForProjectCreation();
	}		

	public ServiceCallbackForProjectDeletion buildCallbackForProjectDeletion() {
		return new ServiceCallbackForProjectDeletion();
	}		

	public ServiceCallbackForUpdatingProject buildCallbackForUpdatingProject() {
		return new ServiceCallbackForUpdatingProject();
	}		

	public ServiceCallbackForAvailableProjects buildCallbackForAvailableProjects() {
		return new ServiceCallbackForAvailableProjects();
	}

	public ServiceCallbackForAddFileToProject buildCallbackForAddFileToProject() {
		return new ServiceCallbackForAddFileToProject();
	}

	public ServiceCallbackForRemoveFileFromProject buildCallbackForRemoveFileFromProject() {
		return new ServiceCallbackForRemoveFileFromProject();
	}
	
	public ServiceCallbackForRenameFileOnProject buildCallbackForRenameFileOnProject() {
		return new ServiceCallbackForRenameFileOnProject();
	}
}
