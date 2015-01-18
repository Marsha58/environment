package com.vw.ide.client.event.uiflow;

import com.google.gwt.event.shared.GwtEvent;
import com.vw.ide.client.event.handler.FinishProjectExecutionHandler;
import com.vw.ide.shared.servlet.tracer.TracerBuildVwmlProjectFinishedMessage;

/**
 * Fired when build process has finished
 * @author Oleg
 *
 */
public class FinishProjectExecutionEvent extends GwtEvent<FinishProjectExecutionHandler>  {

	private TracerBuildVwmlProjectFinishedMessage data = null;
	
	public static Type<FinishProjectExecutionHandler> TYPE = new Type<FinishProjectExecutionHandler>();

	public FinishProjectExecutionEvent() {
		super();
	}

	public TracerBuildVwmlProjectFinishedMessage getData() {
		return data;
	}

	public void setData(TracerBuildVwmlProjectFinishedMessage data) {
		this.data = data;
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<FinishProjectExecutionHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FinishProjectExecutionHandler handler) {
		handler.onFinishProjectExecution(this);
	}
}
