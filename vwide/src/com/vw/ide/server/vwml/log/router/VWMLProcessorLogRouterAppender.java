package com.vw.ide.server.vwml.log.router;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;

import com.vw.ide.server.vwml.log.router.stream.TracerLogOutStream;

public class VWMLProcessorLogRouterAppender extends WriterAppender {

	public static String logRouterName = VWMLProcessorLogRouterAppender.class.getSimpleName();
	
	private TracerLogOutStream logOutStream;
	
	public VWMLProcessorLogRouterAppender() {
	}
	
	public VWMLProcessorLogRouterAppender(TracerLogOutStream stream) {
		setName(logRouterName);
		setLayout(new PatternLayout("%d{dd MMM yyyy HH:mm:ss} %5p %c{1} - %m%n"));
		setLogOutStream(stream);
		setThreshold(Logger.getRootLogger().getEffectiveLevel());
		activateOptions();
	}
	
	public TracerLogOutStream getLogOutStream() {
		return logOutStream;
	}

	public void setLogOutStream(TracerLogOutStream stream) {
		logOutStream = stream;
	}
	
	@Override
	public void activateOptions() {
		setWriter(getLogOutStream());
	}
}
