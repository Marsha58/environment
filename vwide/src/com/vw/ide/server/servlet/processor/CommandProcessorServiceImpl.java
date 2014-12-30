package com.vw.ide.server.servlet.processor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vw.ide.server.servlet.IService;
import com.vw.ide.server.servlet.ServiceUtils;
import com.vw.ide.server.servlet.locator.ServiceLocator;
import com.vw.ide.shared.servlet.processor.CommandProcessor;
import com.vw.ide.shared.servlet.processor.CommandProcessorResult;
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
		pushMessageToTracer(userName, "Compilation of started");
		return null;
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

	protected void pushMessageToTracer(String userName, String message) {
		ServiceUtils.pushDataToTracer(userName, new TracerMessage(message));
	}
}
