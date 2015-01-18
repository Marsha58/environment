package com.vw.ide.server.servlet.processor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vw.ide.server.servlet.IService;
import com.vw.ide.server.servlet.ServiceUtils;
import com.vw.ide.server.servlet.locator.ServiceLocator;
import com.vw.ide.server.servlet.processor.command.sandr.SearchAndReplaceCommandHandler;
import com.vw.ide.server.servlet.processor.command.vwml.RunVWMLProjectCommandHandler;
import com.vw.ide.server.vwml.log.router.VWMLProcessorLogRouterAppender;
import com.vw.ide.server.vwml.log.router.stream.TracerLogOutStream;
import com.vw.ide.shared.servlet.processor.CommandProcessor;
import com.vw.ide.shared.servlet.processor.CommandProcessorResult;
import com.vw.ide.shared.servlet.processor.dto.sandr.SearchAndReplaceBundle;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.tracer.TracerMessage;

@SuppressWarnings("serial")
public class CommandProcessorServiceImpl extends RemoteServiceServlet implements CommandProcessor, IService {

	public static final int ID = 1052;
	private Logger logger = Logger.getLogger(CommandProcessorServiceImpl.class);
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServiceLocator.instance().register(this);
		if (logger.isInfoEnabled()) {
			logger.info("CommandProcessorServiceImpl started and initialized");
		}
		Logger.getRootLogger().addAppender(new VWMLProcessorLogRouterAppender(new TracerLogOutStream()));
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public String getName() {
		return getClass().getName();
	}

	@Override
	public CommandProcessorResult buildProject(String userName, ProjectDescription projectDescription) {
		CommandProcessorResult r = null;
		if (logger.isInfoEnabled()) {
			logger.info("User '" + userName + "' runs project '" + projectDescription.getProjectName() + "'");
		}
		try {
			r = RunVWMLProjectCommandHandler.instance(projectDescription).handle();
		} catch (Exception e) {
			r = new CommandProcessorResult();
			r.setOperation("run_project");
			r.setResult(e.getMessage());
			r.setRetCode(CommandProcessorResult.GENERAL_FAIL);
		}
		return r;
	}

	@Override
	public CommandProcessorResult runProject(String userName, ProjectDescription projectDescription) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandProcessorResult buildAndExportProjectToExecBin(String userName, ProjectDescription projectDescription) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandProcessorResult performSearchAndReplace(String userName, SearchAndReplaceBundle searchAndReplaceBundle) {
		CommandProcessorResult r = null;
		if (logger.isInfoEnabled()) {
			logger.info("User '" + userName + "' requests search and replace action on '" + searchAndReplaceBundle + "'");
		}
		try {
			r = SearchAndReplaceCommandHandler.instance(searchAndReplaceBundle).handle();
		} catch (Exception e) {
			r = new CommandProcessorResult();
			r.setOperation("search_and_replace");
			r.setResult(e.getMessage());
			r.setRetCode(CommandProcessorResult.GENERAL_FAIL);
		}
		return null;
	}
	
	protected void pushMessageToTracer(String userName, String message) {
		ServiceUtils.pushDataToTracer(userName, new TracerMessage(message));
	}
}
