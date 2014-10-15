package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.SelectFileHandler;
import com.vw.ide.client.event.handler.ServerLogHandler;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RequestResult;

/**
 * Fired when user pressed 'SelectFile'
 * @author OMelnyk
 *
 */
public class ServerLogEvent extends GwtEvent<ServerLogHandler> {

	
	public static Type<ServerLogHandler> TYPE = new Type<ServerLogHandler>();
    private RequestResult requestResult;
	
	public ServerLogEvent() {
		super();
	}

	public ServerLogEvent(RequestResult requestResult) {
		super();
		this.requestResult = requestResult;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ServerLogHandler> getAssociatedType() {
		return TYPE;
	}

	public RequestResult getRequestResult() {
		return requestResult;
	}

	public void setRequestResult(RequestResult requestResult) {
		this.requestResult = requestResult;
	}


	@Override
	protected void dispatch(ServerLogHandler handler) {
		handler.onServerLog(this);
	}
}
