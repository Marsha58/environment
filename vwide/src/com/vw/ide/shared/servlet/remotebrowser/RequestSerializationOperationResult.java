package com.vw.ide.shared.servlet.remotebrowser;

@SuppressWarnings("serial")
public class RequestSerializationOperationResult extends RequestDirOperationResult {
	private Object serializiedObject;

	public Object getSerializiedObject() {
		return serializiedObject;
	}

	public void setSerializiedObject(Object serializiedObject) {
		this.serializiedObject = serializiedObject;
	}
}
