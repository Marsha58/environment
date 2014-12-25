package com.vw.ide.shared.servlet.tracer;

@SuppressWarnings("serial")
public class TracerMessage extends TracerData<String> {
	public TracerMessage() {
		
	}
	
	public TracerMessage(String message) {
		super(message);
	}
}
