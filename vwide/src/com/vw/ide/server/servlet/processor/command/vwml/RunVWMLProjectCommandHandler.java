package com.vw.ide.server.servlet.processor.command.vwml;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.vw.ide.client.utils.Utils;
import com.vw.ide.server.servlet.ServiceUtils;
import com.vw.ide.server.servlet.processor.servant.Servant;
import com.vw.ide.server.servlet.processor.servant.Servant.Task;
import com.vw.ide.shared.servlet.processor.CommandProcessorResult;
import com.vw.ide.shared.servlet.processor.dto.compiler.CompilationErrorResult;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.tracer.TracerBuildVwmlProjectFinishedMessage;
import com.vw.ide.shared.servlet.tracer.TracerVwmlCompilationErrorMessage;
import com.vw.lang.processor.model.main.VWML;
import com.vw.lang.processor.model.sink.CompilationSink;
import com.vw.lang.processor.model.sink.CompilationSink.Mode;
import com.vw.lang.sink.OperationInfo;

public class RunVWMLProjectCommandHandler {
	
	/**
	 * Compiles given VWML project
	 * @author Oleg
	 *
	 */
	public static class RunVWMLPRojectTask extends Task {

		private static class ErrorCompilationSink extends CompilationSink {

			private String projectName;
			private String projectCompilationDir;
			private String userName;
			
			public ErrorCompilationSink() {
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
		}
		
		private ProjectDescription project;
		private Logger logger = Logger.getLogger(RunVWMLPRojectTask.class);
		
		public RunVWMLPRojectTask(ProjectDescription project) {
			this.project = project;
		}
		
		public ProjectDescription getProject() {
			return project;
		}

		public void setProject(ProjectDescription project) {
			this.project = project;
		}

		@Override
		public void process() {
			String defCompilerSettings = "-m source -entity ue_im3 -p verbose=true,release=true,standalone=true,unittest=true -debuginfo true -interpreter /var/projects/olmel/msg/interpreter/interpreter.properties -addons integration_pom=/var/projects/olmel/msg/addons/pom.xml /var/projects/olmel/msg/msg.vwml";
			super.process();
			if (logger.isInfoEnabled()) {
				logger.info("Starting build process of '" + project.getProjectName() + "'");
			}
			TracerBuildVwmlProjectFinishedMessage notificationMsg = new TracerBuildVwmlProjectFinishedMessage();
			notificationMsg.setProject(project);
			CommandProcessorResult r = new CommandProcessorResult();
			notificationMsg.setData(r);
			VWML vwml = new VWML();
			try {
				ErrorCompilationSink sink = new ErrorCompilationSink();
				sink.setMode(Mode.FULL_BUILD);
				sink.setProjectName(project.getProjectName());
				sink.setUserName(project.getUserName());
				sink.setProjectCompilationDir(ServiceUtils.getProjectOperationalDir(project));
				vwml.setCompilationSink(sink);
				vwml.handleArgs(defCompilerSettings.split(" "));
				r.setRetCode(CommandProcessorResult.GENERAL_OK);
				r.setResult("Compilation of '" + project.getProjectName() + "' finished - Ok");
				
			} catch (Exception e) {
				r.setRetCode(CommandProcessorResult.GENERAL_FAIL);
				r.setResult("Compilation of '" + project.getProjectName() + "' finished - Failed");
				List<StackTraceElement> stack = new ArrayList<StackTraceElement>();
				if (e.getStackTrace() != null) {
					for(StackTraceElement se : e.getStackTrace()) {
						stack.add(se);
					}
				}
				notificationMsg.setStack(stack);
			}
			ServiceUtils.pushDataToTracer(project.getUserName(), notificationMsg);
			if (logger.isInfoEnabled()) {
				logger.info("Finishing build process of '" + project.getProjectName() + "'");
			}
		}
	}
	
	private ProjectDescription project;
	
	private RunVWMLProjectCommandHandler(ProjectDescription description) {
		this.project = description;
	}

	public static RunVWMLProjectCommandHandler instance(ProjectDescription description) {
		return new RunVWMLProjectCommandHandler(description);
	}
	
	public CommandProcessorResult handle() throws Exception {
		CommandProcessorResult r = new CommandProcessorResult();
		r.setRetCode(CommandProcessorResult.GENERAL_OK);
		r.setResult("compilation of '" + project.getProjectName() + "' started");
		Servant.instance().scheduleTask(project.getUserName(),
										new RunVWMLPRojectTask(project));
		return r;
	}
}
