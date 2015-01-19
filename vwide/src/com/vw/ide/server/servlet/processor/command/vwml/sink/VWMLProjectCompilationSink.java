package com.vw.ide.server.servlet.processor.command.vwml.sink;

import java.io.InputStream;

import org.apache.log4j.Logger;

import com.vw.ide.client.utils.Utils;
import com.vw.ide.server.servlet.ServiceUtils;
import com.vw.ide.server.servlet.processor.command.vwml.task.MonitorProcessTask;
import com.vw.ide.server.servlet.processor.command.vwml.task.RunVWMLPRojectTask;
import com.vw.ide.server.servlet.processor.servant.Servant;
import com.vw.ide.shared.servlet.processor.dto.compiler.CompilationErrorResult;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.tracer.TracerVwmlCompilationErrorMessage;
import com.vw.lang.processor.model.sink.CompilationSink;
import com.vw.lang.sink.OperationInfo;

public class VWMLProjectCompilationSink extends CompilationSink {

	private String projectName;
	private String projectCompilationDir;
	private String userName;
	private Logger logger = Logger.getLogger(RunVWMLPRojectTask.class);
	
	public VWMLProjectCompilationSink() {
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setProjectCompilationDir(String projectCompilationDir) {
		this.projectCompilationDir = projectCompilationDir;
	}

	@Override
	public void skippedCode(OperationInfo opInfo) {
	}

	@Override
	public void handleInclude(String fileName) {
	}

	@Override
	public void publishModulePackage(String packageName) {
	}

	@Override
	public void publishFileLocation(String fileLocation) {
	}

	@Override
	public void publishAuthor(String author) {
	}

	@Override
	public void publishProjectName(String projectName) {
	}

	@Override
	public void publishProjectDescription(String projectDescription) {
	}

	@Override
	public void publishModuleName(String moduleName) {
	}

	@Override
	public void delegateErrorCompilationMessage(OperationInfo opInfo) {
		CompilationErrorResult err = new CompilationErrorResult();
		err.setCause(opInfo.getDescription());
		FileItemInfo fi = new FileItemInfo();
		fi.setName(Utils.extractJustFileName(opInfo.getFileName()));
		fi.setAbsolutePath(Utils.extractJustPath(opInfo.getFileName()));
		err.setFileInfo(fi);
		ServiceUtils.correctFileItem(projectCompilationDir, fi);
		err.setProjectName(projectName);
		err.setLine(opInfo.getLine());
		err.setPosition(opInfo.getPosition());
		err.setUserName(userName);
		ServiceUtils.pushDataToTracer(userName, new TracerVwmlCompilationErrorMessage(err));
	}

	@Override
	public void delegateStartProcessExecution(String processName) {
		if (logger.isInfoEnabled()) {
			logger.info("Started execution of back process '" + processName + "'");
		}
	}

	@Override
	public void delegateRuntimeStreams(String processName, InputStream is, InputStream es) {
		Servant.instance().scheduleTask(userName + "::" + processName,
										new MonitorProcessTask(userName, is, es));
	}

	@Override
	public void delegateFinishProcessExecution(String processName) {
		Servant.instance().stop(userName + "::" + processName);
		if (logger.isInfoEnabled()) {
			logger.info("Finished execution of back process '" + processName + "'");
		}
	}
}
