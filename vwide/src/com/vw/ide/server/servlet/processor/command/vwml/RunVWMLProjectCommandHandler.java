package com.vw.ide.server.servlet.processor.command.vwml;

import com.vw.ide.server.servlet.processor.command.vwml.task.RunVWMLPRojectTask;
import com.vw.ide.server.servlet.processor.servant.Servant;
import com.vw.ide.shared.servlet.processor.CommandProcessorResult;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;

public class RunVWMLProjectCommandHandler {
	
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
