package com.vw.ide.client.service.remote;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.vw.ide.client.service.ProcessedResult;

public class Result<R> extends ProcessedResult<R> {

	private ResultCallback<R> callback;
	
	public Result() {
	}
	
	public Result(ResultCallback<R> callback) {
		setCallback(callback);
	}

	public ResultCallback<R> getCallback() {
		return callback;
	}

	public void setCallback(ResultCallback<R> callback) {
		this.callback = callback;
	}

	@Override
	public void onFailure(Throwable caught) {
		AlertMessageBox alertMessageBox = new AlertMessageBox("Error", caught.getMessage());
		alertMessageBox.show();
	}

	@Override
	public void onSuccess(R result) {
		if (callback != null) {
			callback.handle(result);
		}
	}
}
