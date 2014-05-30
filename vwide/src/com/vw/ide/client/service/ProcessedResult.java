package com.vw.ide.client.service;

/**
 * Must be implemented by service user
 * @author Oleg
 *
 * @param <T>
 */
public abstract class ProcessedResult<T> {

	public abstract void onFailure(Throwable caught);

	public abstract void onSuccess(T result);
}
