package com.vw.ide.client.devboardext.event.handler;


import java.util.Date;

import com.google.gwt.event.shared.GwtEvent;
import com.kfuntak.gwt.json.serialization.client.JsonSerializable;
import com.vw.ide.client.devboardext.DevelopmentBoardPresenter;
import com.vw.ide.client.event.handler.ServerLogHandler;
import com.vw.ide.client.event.uiflow.ServerLogEvent;
import com.vw.ide.client.presenters.Presenter;
import com.vw.ide.shared.servlet.RequestResult;

public class ServerLogEventHandler extends Presenter.PresenterEventHandler implements ServerLogHandler {
	
	protected static class Log implements JsonSerializable {
		private String operation;
		private String result;
		private Integer retCode;
		
		public Log() {
			super();
		}

		public Log(String operation, String result, Integer retCode) {
			super();
			this.operation = operation;
			this.result = result;
			this.retCode = retCode;
		}

		public String getResult() {
			return result;
		}
		
		public void setResult(String result) {
			this.result = result;
		}
		
		public Integer getRetCode() {
			return retCode;
		}
		
		public void setRetCode(Integer retCode) {
			this.retCode = retCode;
		}

		public String getOperation() {
			return operation;
		}

		public void setOperation(String operation) {
			this.operation = operation;
		}
		
		public String toString() {
			Date date = new Date();
			return "Time: " + date.toString() + "; operation: '" + getOperation() + "'; code '" + getRetCode() + "'; result '" + getResult() + "'";
		}
	}
	
	@Override
	public void handler(Presenter presenter, GwtEvent<?> event) {
		process((DevelopmentBoardPresenter)presenter, (ServerLogEvent)event);
	}

	@Override
	public void onServerLog(ServerLogEvent event) {
		if (getPresenter() != null) {
			getPresenter().delegate(event);
		}
	}
	
	protected void process(DevelopmentBoardPresenter presenter, ServerLogEvent event) {
		RequestResult r = event.getRequestResult();
		if (r != null && r.getResult() != null && r.getResult().length() != 0) {
			presenter.getView().getConsoles().getInfoConsoleTab().addInfo(r.getResult());
		}
	}
}
