package com.vw.ide.shared.servlet.tracer;

import java.io.Serializable;

/**
 * Incapsulates user's data
 * @author Oleg
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public class TracerData<T extends Serializable> implements Serializable {
	T data;

	public TracerData() {
		
	}
	
	public TracerData(T data) {
		setData(data);
	}
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
