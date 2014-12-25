package com.vw.ide.server.servlet;

import java.io.Serializable;

import com.vw.ide.server.servlet.locator.ServiceLocator;
import com.vw.ide.server.servlet.tracer.TracerServiceImpl;
import com.vw.ide.shared.servlet.tracer.TracerData;

/**
 * Set of aux methods which can be used by services on server side
 * @author Oleg
 *
 */
public class ServiceUtils {
	/**
	 * Pushes data to tracer for sending them to client side which is identified by userName
	 * @param userName
	 * @param data
	 */
	public static <T extends Serializable> void pushDataToTracer(String userName, TracerData<T> data) {
		TracerServiceImpl tsi = (TracerServiceImpl)ServiceLocator.instance().locate(TracerServiceImpl.ID);
		if (tsi != null) {
			tsi.pushData(userName, data);
		}
	}
}
