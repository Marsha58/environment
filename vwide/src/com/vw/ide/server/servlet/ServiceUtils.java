package com.vw.ide.server.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
	
	
	 /** Read the given binary file, and return its contents as a byte array.*/ 
	public static byte[] readFile(InputStream is) throws Exception {
		byte[] result = new byte[1024];
    	InputStream input = null;
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
		input = new BufferedInputStream(is);
		int bytesRead = 0;
		int writtenBytes = 0;
		while(true) {
          bytesRead = input.read(result); 
          if (bytesRead > 0){
        	  bos.write(result, 0, bytesRead);
        	  writtenBytes += bytesRead;
          }
          else {
        	  break;
          }
		}
		result = null;
		if (writtenBytes != 0) {
			bos.flush();
			result = bos.toByteArray();
		}
		return result;
	}	
}
