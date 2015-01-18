package com.vw.ide.server.vwml.log.router.stream;

import java.io.StringWriter;

import com.vw.ide.server.servlet.ServiceUtils;
import com.vw.ide.shared.servlet.tracer.TracerMessage;

public class TracerLogOutStream extends StringWriter {

	private static ThreadLocal<String> userNameStg = new ThreadLocal<String>();
	
	public TracerLogOutStream() {
		super();
	}

	public TracerLogOutStream(String userName) {
		super();
		userNameStg.set(userName);
	}

	public String getUserName() {
		return userNameStg.get();
	}

	public void setUserName(String userName) {
		userNameStg.set(userName);
	}

	@Override
	public void write(char[] cbuf, int off, int len) {
		write(String.valueOf(cbuf, off, len));
	}
	
	@Override
	public void write(String str, int off, int len) {
		write(str.substring(off, off + len));
	}

	@Override
	public void write(String str) {
		if (userNameStg.get() != null) {
			ServiceUtils.pushDataToTracer(userNameStg.get(), new TracerMessage(str));
		}
	}
}
