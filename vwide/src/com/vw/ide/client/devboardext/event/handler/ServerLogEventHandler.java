package com.vw.ide.client.devboardext.event.handler;

import java.util.Date;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.ServerLogHandler;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestFileOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestProjectCreationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestedDirScanResult;

public class ServerLogEventHandler extends Presenter.PresenterEventHandler implements ServerLogHandler {
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (ServerLogEvent)event);
	}

	@Override
	public void onServerLog(ServerLogEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	protected void process(DevelopmentBoardPresenter presenter, ServerLogEvent event) {
		Date curDate = new Date();

		@SuppressWarnings("deprecation")
		String nodeRecord = "\n{\"time\":\"" + curDate.toLocaleString() + "\",";
		String nodeRecordOperation = "\"operation\":"
				+ event.getRequestResult().getOperation() + ",";
		String nodeRecordResCode = "\"res_code\":"
				+ event.getRequestResult().getRetCode() + ",";
		String nodeRecordResult = "\"result\":"
				+ event.getRequestResult().getResult() + ",";
		nodeRecord += nodeRecordOperation + nodeRecordResCode
				+ nodeRecordResult;
		if (event.getRequestResult() instanceof RequestProjectCreationResult) {
			RequestProjectCreationResult result = (RequestProjectCreationResult) event
					.getRequestResult();
			String nodeRecordProject = "\"project\":{";
			String nodeRecordProjectName = "\"name\":"
					+ result.getProjectName() + ",";
			String nodeRecordProjectPath = "\"<path\":"
					+ result.getProjectPath();
			nodeRecordProject += nodeRecordProjectName + nodeRecordProjectPath
					+ "}";
			nodeRecord += nodeRecordProject;
		} else if (event.getRequestResult() instanceof RequestFileOperationResult) {
			RequestFileOperationResult result = (RequestFileOperationResult) event
					.getRequestResult();
			String nodeRecordFile = "\"file\":{";
			String nodeRecordFileId = "\"id\":" + result.getFileId() + ",";
			String nodeRecordFileName = "\"name\":" + result.getFileName();
			nodeRecordFile += nodeRecordFileId + nodeRecordFileName + "}";
			nodeRecord += nodeRecordFile;
		} else if (event.getRequestResult() instanceof RequestDirOperationResult) {
			RequestDirOperationResult result = (RequestDirOperationResult) event
					.getRequestResult();
			nodeRecord += "\"project_id\":" + result.getProjectId()
					+ ",\"file\":{\"id\":" + result.getFileId() + ",\"path\":"
					+ result.getPath();
		} else if (event.getRequestResult() instanceof RequestedDirScanResult) {
			RequestedDirScanResult result = (RequestedDirScanResult) event
					.getRequestResult();
			String nodeRecordParentPath = "\"parent_path\":"
					+ result.getParentPath() + ",";
			String nodeRecordFiles = "\"files\":[";
			boolean bIsFirst = true;
			for (FileItemInfo curFile : result.getFiles()) {
				if (bIsFirst == false) {
					nodeRecordFiles += ",";
				}
				bIsFirst = false;

				nodeRecordFiles += "\"name\":" + curFile.getName();
				/*
				 * nodeRecordFiles += "{\"name\":"+ curFile.getName() +
				 * "\"rel_path\":"+ curFile.getRelPath() + "\"project_id\":"+
				 * curFile.getProjectId() + "\"file_id\":"+ curFile.getFileId()
				 * + "}";
				 */
			}
			;
			nodeRecord += nodeRecordParentPath + nodeRecordFiles + "]";
		} else {
			// System.out.println("Operation : " +
			// event.getRequestResult().getOperation() + "; result code : " +
			// event.getRequestResult().getRetCode());
		}
		nodeRecord += "}";
		presenter.getView().appendLog(nodeRecord);
	}
}
