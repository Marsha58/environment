package com.vw.ide.server.servlet.processor.command.vwml.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.vw.ide.server.servlet.ServiceUtils;
import com.vw.ide.server.servlet.processor.command.vwml.sink.VWMLProjectCompilationSink;
import com.vw.ide.server.servlet.processor.servant.Servant.Task;
import com.vw.ide.shared.servlet.processor.CommandProcessorResult;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.tracer.TracerBuildVwmlProjectFinishedMessage;
import com.vw.lang.processor.model.main.VWML;
import com.vw.lang.processor.model.sink.CompilationSink.Mode;

/**
 * Compiles given VWML project
 * @author Oleg
 *
 */
public class RunVWMLPRojectTask extends Task {

	
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
		String defCompilerSettings = "-m compile -entity ue_im3 -p verbose=true,release=true,standalone=true,unittest=true -debuginfo true -interpreter /var/projects/olmel/msg/interpreter/interpreter.properties -addons integration_pom=/var/projects/olmel/msg/addons/pom.xml /var/projects/olmel/msg/msg.vwml";
		super.process();
		if (logger.isInfoEnabled()) {
			logger.info("Started to build process '" + project.getProjectName() + "'");
		}
		TracerBuildVwmlProjectFinishedMessage notificationMsg = new TracerBuildVwmlProjectFinishedMessage();
		notificationMsg.setProject(project);
		CommandProcessorResult r = new CommandProcessorResult();
		notificationMsg.setData(r);
		VWML vwml = new VWML();
		try {
			vwml.init();
			vwml.setOperatedFromIDE(true);
			VWMLProjectCompilationSink sink = new VWMLProjectCompilationSink();
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
			logger.info("Finished to build process '" + project.getProjectName() + "'");
		}
	}
}

