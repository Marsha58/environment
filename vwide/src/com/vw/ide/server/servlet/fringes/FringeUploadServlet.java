package com.vw.ide.server.servlet.fringes;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.shared.UConsts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import com.vw.ide.client.utils.Utils;
import com.vw.ide.server.servlet.fringes.JarClassLoader;;

/**
 * Servlet implementation class FringeUploadServlet
 */
public class FringeUploadServlet extends UploadAction {
	private Logger logger = Logger.getLogger(FringeUploadServlet.class);
	private static final long serialVersionUID = 1L;
	private static String s_defFringesDir = "D://var/fringes";
	private static String fringeInterfaceName = "";

	Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
	/**
	 * Maintain a list with received files and their content types.
	 */
	Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

	private void loadProperties(ServletContext context) {

		Properties prop = new Properties();
		try {
			InputStream isPropertiesFile = context.getResourceAsStream("/WEB-INF/classes/config.properties");
			if (isPropertiesFile != null) {
				prop.load(isPropertiesFile);
				if (prop.getProperty("fringes_dir") != null) {
					s_defFringesDir = prop.getProperty("fringes_dir");
				}
				if (prop.getProperty("fringe_interface") != null) {
					fringeInterfaceName = prop.getProperty("fringe_interface");
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		loadProperties(config.getServletContext());
		logger.info("FringeUploadServlet started and initialized");
	}	
	
	/**
	 * Override executeAction to save the received files in a custom place and
	 * delete this items from session.
	 */
	@Override
	public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
		String response = "";
		String parentPath = s_defFringesDir + Utils.FILE_SEPARATOR + removeQuates(request.getParameter("folder"));
		File folder = new File(parentPath);
		if (!folder.exists()) {
			
			try {
				folder.mkdirs();
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());
			}
		}
		JarClassLoader jarClassLoader = null; 
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {
					String fringeFullFileName = parentPath + Utils.FILE_SEPARATOR + item.getName();
					logger.info(fringeFullFileName);
					File file = new File(fringeFullFileName);
					item.write(file);
					
					
					ObjectNode object=JsonNodeFactory.instance.objectNode();
					object.put("id", Integer.parseInt(request.getParameter("id")));
					object.put("path", parentPath);
					object.put("filename", item.getName());
					
					jarClassLoader = new JarClassLoader(fringeFullFileName,"fringes",fringeInterfaceName);
					ArrayNode arrayClassNames = JsonNodeFactory.instance.arrayNode();
					for (String curName : jarClassLoader.getCacheClassNames()) {
						arrayClassNames.add(curName); 
					}
					
					object.put("classes", arrayClassNames);

					if(arrayClassNames.size() == 0) {
						doDeleteFile(fringeFullFileName);
					}
					
					response += object.toString();
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage());
					throw new UploadActionException(e);
				}
			}
		}
		// / Remove files from session because we have a copy of them
		removeSessionFileItems(request);
		// / Send your customized message to the client.
		return response;
	}
	
	private String removeQuates(String in) {
		String out = in.substring(1, in.length()-1);
		return out;
	}

	/**
	 * Get the content of an uploaded file.
	 */
	@Override
	public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fieldName = request.getParameter(UConsts.PARAM_SHOW);
		File f = receivedFiles.get(fieldName);
		if (f != null) {
			response.setContentType(receivedContentTypes.get(fieldName));
			FileInputStream is = new FileInputStream(f);
			copyFromInputStreamToOutputStream(is, response.getOutputStream());
		} else {
			renderXmlResponse(request, response, XML_ERROR_ITEM_NOT_FOUND);
		}
	}

	/**
	 * Remove a file when the user sends a delete request.
	 */
	@Override
	public void removeItem(HttpServletRequest request, String fieldName) throws UploadActionException {
		doDeleteFile(fieldName);
	}

	private void doDeleteFile(String fieldName){
		File file = receivedFiles.get(fieldName);
		receivedFiles.remove(fieldName);
		receivedContentTypes.remove(fieldName);
		if (file != null) {
			file.delete();
		}
	}

}
