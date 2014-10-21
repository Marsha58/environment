package com.vw.ide.client.service.remote;

public abstract class ResultCallback<R> {
	public abstract void handle(R result);
}
