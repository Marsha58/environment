package com.vw.ide.client.service.remote.tracer;

import com.vw.ide.client.service.remote.Result;
import com.vw.ide.client.service.remote.ResultCallback;
import com.vw.ide.client.service.remote.tracer.TracerService.ServiceCallbackForTracerRegister;
import com.vw.ide.client.service.remote.tracer.TracerService.ServiceCallbackForTracerUnregister;
import com.vw.ide.shared.servlet.tracer.RemoteTracerAsync;
import com.vw.ide.shared.servlet.tracer.TracerRegisterResult;
import com.vw.ide.shared.servlet.tracer.TracerUnregisterResult;

public class TracerServiceBroker {
	
	public static void registerClientOnTracer(String userName, ResultCallback<TracerRegisterResult> resultCallback) {
		RemoteTracerAsync service = TracerService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForTracerRegister cbk = TracerService.instance().buildCallbackForTracerRegister();
			cbk.setProcessedResult(new Result<TracerRegisterResult>(resultCallback));
			service.register(userName, cbk);
		}		
	}
	
	public static void unregisterClientOnTracer(String userName, ResultCallback<TracerUnregisterResult> resultCallback) {
		RemoteTracerAsync service = TracerService.instance().getServiceImpl();
		if (service != null) {
			ServiceCallbackForTracerUnregister cbk = TracerService.instance().buildCallbackForTracerUnregister();
			cbk.setProcessedResult(new Result<TracerUnregisterResult>(resultCallback));
			service.unregister(userName, cbk);
		}		
	}
}
