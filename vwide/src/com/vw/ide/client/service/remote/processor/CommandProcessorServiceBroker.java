package com.vw.ide.client.service.remote.processor;

import com.vw.ide.client.service.remote.Result;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.processor.CommandProcessorService.ServiceCallbackForCommandProcessor;
import com.vw.ide.shared.servlet.processor.CommandProcessorAsync;
import com.vw.ide.shared.servlet.processor.CommandProcessorResult;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;

/**
 * Command processor broker implementation
 * @author Oleg
 *
 */
public class CommandProcessorServiceBroker {

	/**
	 * Starts compilation process of VWML project described by projDescription
	 * @param userName
	 * @param projDescription
	 * @param resultCallback
	 */
	public static void buildProject(String userName, ProjectDescription projDescription, ResultCallback<CommandProcessorResult> resultCallback) {
		CommandProcessorAsync service = CommandProcessorService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForCommandProcessor cbk = CommandProcessorService.instance().buildCallbackForCommandProcessor();
			cbk.setProcessedResult(new Result<CommandProcessorResult>(resultCallback));
			service.buildProject(userName, projDescription, cbk);
		}		
	}
	
	/**
	 * Runs compiled project
	 * @param userName
	 * @param projectDescription
	 * @return
	 */
	public static void runProject(String userName, ProjectDescription projectDescription, ResultCallback<CommandProcessorResult> resultCallback) {
		CommandProcessorAsync service = CommandProcessorService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForCommandProcessor cbk = CommandProcessorService.instance().buildCallbackForCommandProcessor();
			cbk.setProcessedResult(new Result<CommandProcessorResult>(resultCallback));
			service.runProject(userName, projectDescription, cbk);
		}		
	}
	
	/**
	 * Compiles, runs static test and export project as executable bin file
	 * @param userName
	 * @param projectDescription
	 * @return
	 */
	public static void buildAndExportProjectToExecBin(String userName, ProjectDescription projectDescription, ResultCallback<CommandProcessorResult> resultCallback) {
		CommandProcessorAsync service = CommandProcessorService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForCommandProcessor cbk = CommandProcessorService.instance().buildCallbackForCommandProcessor();
			cbk.setProcessedResult(new Result<CommandProcessorResult>(resultCallback));
			service.buildAndExportProjectToExecBin(userName, projectDescription, cbk);
		}		
	}
}
