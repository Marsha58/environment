package com.vw.ide.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Root class for service callbacks
 * @author Oleg
 *
 * @param <T>
 */
public class ServiceCallback<T> implements AsyncCallback<T> {
	private ProcessedResult<T> processedResult;

	public ProcessedResult<T> getProcessedResult() {
		return processedResult;
	}

	public void setProcessedResult(ProcessedResult<T> processedResult) {
		this.processedResult = processedResult;
	}

	public void onFailure(Throwable caught) {
		if (getProcessedResult() != null) {
			getProcessedResult().onFailure(caught);
		}
	}

	public void onSuccess(T result) {
		if (getProcessedResult() != null) {
			getProcessedResult().onSuccess(result);
		}
	}
}
