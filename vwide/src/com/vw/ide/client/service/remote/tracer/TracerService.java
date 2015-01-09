package com.vw.ide.client.service.remote.tracer;

import java.io.Serializable;
import java.util.List;

import net.zschech.gwt.comet.client.CometClient;
import net.zschech.gwt.comet.client.CometListener;
import net.zschech.gwt.comet.client.CometSerializer;
import net.zschech.gwt.comet.client.SerialTypes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.vw.ide.client.event.uiflow.SearchAndReplaceResultEvent;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.service.BusConnectivity;
import com.vw.ide.client.service.ServiceCallback;
import com.vw.ide.client.service.VwIdeClientService;
import com.vw.ide.client.service.factory.ServicesStubFactory;
import com.vw.ide.shared.OperationTypes;
import com.vw.ide.shared.servlet.RequestResult;
import com.vw.ide.shared.servlet.processor.command.sandr.SearchAndReplaceMessage;
import com.vw.ide.shared.servlet.tracer.RemoteTracerAsync;
import com.vw.ide.shared.servlet.tracer.TracerMessage;
import com.vw.ide.shared.servlet.tracer.TracerRegisterResult;
import com.vw.ide.shared.servlet.tracer.TracerUnregisterResult;

public class TracerService implements BusConnectivity, VwIdeClientService {

	@SerialTypes( { TracerMessage.class, SearchAndReplaceMessage.class })
    public static abstract class TracerServiceCometSerializer extends CometSerializer {
    }
	
	protected static class TracerEventListener implements CometListener {

		private TracerService owner = null;
		
		public TracerEventListener(TracerService owner) {
			this.owner = owner;
		}
		
		@Override
		public void onConnected(int heartbeat) {
			System.out.println("tracer service connected");
		}

		@Override
		public void onDisconnected() {
			System.out.println("tracer service disconnected");
		}

		@Override
		public void onError(Throwable exception, boolean connected) {
			System.out.println("tracer service reports about error '" + exception.getLocalizedMessage() + "'");
		}

		@Override
		public void onHeartbeat() {
			System.out.println("tracer service heartbeat");
		}

		@Override
		public void onRefresh() {
			System.out.println("tracer service refresh");
		}

		@Override
		public void onMessage(List<? extends Serializable> messages) {
			if (messages != null) {
				for(Serializable message : messages) {
					if (message instanceof TracerMessage) {
						TracerMessage data = (TracerMessage)message;
						RequestResult rr = new RequestResult();
						rr.setOperationType(OperationTypes.LOG_OPERATION);
						rr.setRetCode(0);
						rr.setOperation("log");
						rr.setResult(data.getData());
						owner.fireEvent(new ServerLogEvent(rr));
						System.out.println("Got tracer message '" + data.getData() + "'");
					}
					if (message instanceof SearchAndReplaceMessage) {
						SearchAndReplaceMessage data = (SearchAndReplaceMessage)message;
						owner.fireEvent(new SearchAndReplaceResultEvent(data.getData()));
						System.out.println("Got search&replace message '" + data.getData() + "'");
					}
				}
			}
		}
	}
	
	public static class ServiceCallbackForTracerRegister extends ServiceCallback<TracerRegisterResult> {
		
	}

	public static class ServiceCallbackForTracerUnregister extends ServiceCallback<TracerUnregisterResult> {
		
	}
	
	private RemoteTracerAsync serviceImpl = ServicesStubFactory.createRemoteTracerServiceAsync();
	private HandlerManager bus;
	private CometClient backNotificationClient = null;
	private static TracerService s_instance = null;
	
	/**
	 * Simple singleton implementation
	 * @return
	 */
	public static synchronized TracerService instance() {
		if (s_instance != null) {
			return s_instance;
		}
		s_instance = new TracerService();
		s_instance.init();
		return s_instance;
	}
	
	@Override
	public void setBusRef(HandlerManager busRef) {
		bus = busRef;
	}

	public RemoteTracerAsync getServiceImpl() {
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
	
	public ServiceCallbackForTracerRegister buildCallbackForTracerRegister() {
		return new ServiceCallbackForTracerRegister();
	}
	
	public ServiceCallbackForTracerUnregister buildCallbackForTracerUnregister() {
		return new ServiceCallbackForTracerUnregister();
	}
	
	public void registerBackNotification() {
		if (backNotificationClient == null) {
			CometSerializer serializer = GWT.create(TracerServiceCometSerializer.class);
			String tracerServiceUrl = GWT.getModuleBaseURL() + "comet?gwt.codesvr=127.0.0.1:9997";
			backNotificationClient = new CometClient(tracerServiceUrl, serializer, new TracerEventListener(this));
			backNotificationClient.start();
		}
	}

	public void unregisterBackNotification() {
		if (backNotificationClient != null) {
			backNotificationClient.stop();
			backNotificationClient = null;
		}
	}
	
	protected void init() {
	}
}
