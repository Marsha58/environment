package com.vw.ide.server.servlet.remotebrowser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vw.ide.server.servlet.IService;
import com.vw.ide.server.servlet.locator.ServiceLocator;
import com.vw.ide.server.servlet.userstate.UserStateServiceImpl;
import com.vw.ide.shared.OperationTypes;
import com.vw.ide.shared.servlet.RequestResult;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowser;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestFileOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestSerializationOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestedDirScanResult;
import com.vw.ide.shared.servlet.userstate.UserStateInfo;


/**
 * Implementation of remote directory browser
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class DirBrowserImpl extends RemoteServiceServlet implements RemoteDirectoryBrowser, IService {

	private Logger logger = Logger.getLogger(DirBrowserImpl.class);
	private static String s_defRootDir = "/var/projects";

	public static final int ID = 1024;
	
	public DirBrowserImpl() {
		super();
		if (logger.isInfoEnabled()) {
			logger.info("RemoteDirectoryBrowserImpl constructed");
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		loadProperties(config.getServletContext());
		ServiceLocator.instance().register(this);
		if (logger.isInfoEnabled()) {
			logger.info("RemoteDirectoryBrowserImpl started and initialized");
		}
	}
	
	@Override
	public int getId() {
		return ID;
	}

	@Override
	public String getName() {
		return getClass().getName();
	}

	public String constructUserHomePath(String user) {
		return s_defRootDir + "/" + user;
	}
	
	/**
	 * Returns list of subdirs inside the given dir
	 */
	public List<String> getListOfDirectories(String user, String dir) {
		List<String> dirsInfo = new ArrayList<String>();
		if (user != null) {
			RequestedDirScanResult r = getDirScan(user, dir);
			for(FileItemInfo fi : r.getFiles()) {
				if (fi.isDir()) {
					dirsInfo.add(fi.getName());
				}
			}
		}
		return dirsInfo;
	}

	/**
	 * Returns full directory content
	 */
	public RequestedDirScanResult getDirScan(String user, String dir) {
		String path = (dir == null) ? constructUserHomePath(user) : dir;
		RequestedDirScanResult r = new RequestedDirScanResult();
		r.setParentPath(new File(path).getAbsolutePath());
		if (user != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Asking for dirs list for user '" + user + "'; dir '" + dir + "'; full path '" + path + "'");
			}
			r.setFiles(getListOfFileItems(path, null));
		}
		else {
			r.setFiles(new ArrayList<FileItemInfo>());
		}
		return r;
	}

	public RequestDirOperationResult createAbsoluteDir(String user, String dir) {
		RequestDirOperationResult res = new RequestDirOperationResult();
		String fullPath = dir;
		res.setPath(fullPath);
		res.setRetCode(0);
		res.setOperation("create directory");
		if (logger.isDebugEnabled()) {
			logger.debug("User '" + user + "' creates dir '" + fullPath + "'");
		}
		try {
			File f = new File(fullPath);
			if (!f.exists()) {
				f.mkdirs();
			}
		}
		catch(Exception ex) {
			res.setResult(ex.getMessage());
			res.setRetCode(-1);
		}
		return res;
	}
	
	public RequestDirOperationResult createDir(String user, String parent, String dir) {
		return createAbsoluteDir(user, parent + "/" + dir);
	}

	public RequestDirOperationResult removeDir(String user, String parent, String dir) {
		RequestDirOperationResult res = new RequestDirOperationResult();
		String fullPath = parent + "/" + dir;
		res.setPath(fullPath);
		res.setRetCode(0);
		res.setOperation("remove directory");
		try {
			purgeDirectory(new File(fullPath));
		}
		catch(Exception ex) {
			res.setResult(ex.getMessage());
			res.setRetCode(-1);
		}
		return res;
	}
	
	public void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}

	public RequestDirOperationResult createFile(String user, String path, String fileName, String content) {
		RequestDirOperationResult res = new RequestDirOperationResult();
		res.setOperation("file creating");
		res.setRetCode(RequestDirOperationResult.GENERAL_OK);
		String sFullPath = path + "/" + fileName; 
		try {
			
			File parentPath = new File(path);
			if (!parentPath.exists()) {
				parentPath.mkdirs();
			}
			File fNewFile = new File(sFullPath);
			if (!fNewFile.exists()) {
				fNewFile.createNewFile();
				FileWriter writer = new FileWriter(fNewFile);
				if (content != null) {
					writer.write(content);
				} else {
					writer.write("");
				}
				writer.flush();
				writer.close();
			}
		}
		catch(Exception ex) {
			res.setResult(ex.getMessage());
			res.setRetCode(RequestDirOperationResult.GENERAL_FAIL);
		}		
		return res;
	}

	public RequestSerializationOperationResult serializeObjectToJson(String user, String fileName, String path, Object content) {
		RequestSerializationOperationResult res = new RequestSerializationOperationResult();
		res.setOperation("file serialization json");
		res.setRetCode(RequestSerializationOperationResult.GENERAL_OK);
		String fullPath = path + "/" + fileName;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(fullPath), content);
		} catch (IOException ex) {
			res.setResult(ex.getMessage());
			res.setRetCode(RequestSerializationOperationResult.GENERAL_FAIL);
		}
		return res;
	}
	
	public RequestSerializationOperationResult deserializeObjectFromJson(String user, String fileName, String path, Class<?> desirialization) {
		RequestSerializationOperationResult res = new RequestSerializationOperationResult();
		res.setOperation("file serialization json");
		res.setRetCode(RequestSerializationOperationResult.GENERAL_OK);
		String fullPath = (fileName != null) ? path + "/" + fileName : path;
		ObjectMapper mapper = new ObjectMapper();
		try {
			res.setSerializiedObject(mapper.readValue(new File(fullPath), desirialization));
		} catch (IOException ex) {
			res.setResult(ex.getMessage());
			res.setRetCode(RequestSerializationOperationResult.GENERAL_FAIL);
		}
		return res;
	}

	@Override
	public RequestDirOperationResult addFile(String user, String parent, String fileName, Long projectId, Long fileId, String content) {
		RequestDirOperationResult res = createFile(user, parent, fileName, content);
		res.setProjectId(projectId);
		return res;
	}

	@Override
	public RequestFileOperationResult deleteFile(String user, String path, String fileName, Long fileId) {
		RequestFileOperationResult res = new RequestFileOperationResult();
		String fullPathName = path + "/" + fileName;
		res.setFileId(fileId);
		res.setOperation("deleting file");
		res.setFileName(fullPathName);
		res.setRetCode(0);
		try{
	        File fileTemp = new File(fullPathName);
	          if (fileTemp.exists()){
	             fileTemp.delete();
	          }   
	      }catch(Exception ex){
				res.setResult(ex.getMessage());
				res.setRetCode(-1);
	      }
		return res;
	}

	@Override
	public RequestDirOperationResult readFile(String user, String parent, String fileName, Long projectId, Long fileId) {
		UserStateInfo userStateInfo;
		RequestDirOperationResult res = new RequestDirOperationResult();
		userStateInfo = locateUserStateService().getUserStateInfo(user);
		if (userStateInfo == null) {
			userNotFoundReport(res, user, "read_file");
			return res;
		}
		String fullPathName = parent + "/" + fileName;
		FileItemInfo fileItemInfo = new FileItemInfo(fileName, parent, false);
		fileItemInfo.setFileId(fileId);
		fileItemInfo.setProjectId(projectId);
		res.setProjectId(projectId);
		res.setFileId(fileId);
		res.setOperation("reading file");
		res.setPath(fullPathName);
		res.setRetCode(0);
		try {
			res.setTextFile(openFile(fullPathName));
		}
		catch(Exception ex) {
			res.setResult(ex.getMessage());
			res.setRetCode(-1);
		}
		return res;
	}

	@Override
	public RequestFileOperationResult saveFile(String user, String path, String fileName, Long projectId, Long fileId, String content) {
		RequestFileOperationResult res = new RequestFileOperationResult();
		String fullPathName = path + "/" + fileName;
		res.setFileName(fullPathName);
		res.setFileId(fileId);
		res.setOperation("saving file");
		res.setRetCode(0);
		
		Writer writer = null;
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fullPathName), "utf-8"));
			writer.write(content);
		}
		catch(IOException ex) {
			res.setResult(ex.getMessage());
			res.setRetCode(-1);
		} finally {
		   try {writer.close();}
		   catch (Exception ex) {
				res.setResult(ex.getMessage());
				res.setRetCode(-1);
		   }
		}
		return res;
	}

	@Override
	public RequestFileOperationResult closeFile(String user, String fileName, Long fileId) {
		RequestFileOperationResult res = new RequestFileOperationResult();
		UserStateInfo userStateInfo = locateUserStateService().getUserStateInfo(user);
		if (userStateInfo == null) {
			userNotFoundReport(res, user, "close_file");
			return res;
		}
		res.setFileId(fileId);
		res.setOperation("closing file");
		res.setFileName(fileName);
		res.setRetCode(0);
		return res;
	}

	@Override
	public RequestFileOperationResult renameFile(String user, String fileName, 	Long fileId, String fileNewName) {
		RequestFileOperationResult res = new RequestFileOperationResult();
		UserStateInfo userStateInfo = locateUserStateService().getUserStateInfo(user);
		if (userStateInfo == null) {
			userNotFoundReport(res, user, "rename_file");
			return res;
		}
		res.setFileId(fileId);
		res.setOperationType(OperationTypes.RENAME_FILE);
		res.setFileName(fileName);
		res.setFileNewName(fileNewName);
	    File oldFile = new File(fileName);
	    File newFile = new File(fileNewName);
	    if(!newFile.exists()) {
	    	if (oldFile.renameTo(newFile)) {
				res.setResult("File has been renamed");
				res.setRetCode(0);
	    	} else {
				res.setResult("Error while renaming");
				res.setRetCode(-1);
	    	}
		} else {
			res.setResult("File with such name exists");
			res.setRetCode(-2);
		}
		return res;
	}

	public RequestDirOperationResult renameDir(String user, String parentDir, String oldName, String newName) {
		RequestDirOperationResult res = new RequestDirOperationResult();
		UserStateInfo userStateInfo = locateUserStateService().getUserStateInfo(user);
		if (userStateInfo == null) {
			userNotFoundReport(res, user, "rename_dir");
			return res;
		}
		res.setOperationType(OperationTypes.RENAME_DIR);
	    File oldFile = new File(parentDir + "/" + oldName);
	    File newFile = new File(parentDir + "/" + newName);
	    if(!newFile.exists()) {
	    	if (oldFile.renameTo(newFile)) {
				res.setResult("File has been renamed");
				res.setRetCode(0);
	    	} else {
				res.setResult("Error while renaming");
				res.setRetCode(-1);
	    	}
		} else {
			res.setResult("File with such name exists");
			res.setRetCode(-2);
		}
		return res;
	}
	
	private UserStateServiceImpl locateUserStateService() {
		return (UserStateServiceImpl)ServiceLocator.instance().locate(UserStateServiceImpl.ID);
	}
	
	private void userNotFoundReport(RequestResult res, String user, String operation) {
		res.setOperation(operation);
		res.setResult("user '" + user + "' not found or wasn't registered");
		res.setRetCode(-2);
	}
	
	private List<FileItemInfo> getListOfFileItems(String dir, List<FileItemInfo> filesInfo) {
		if (filesInfo == null) {
			filesInfo = new ArrayList<FileItemInfo>();
		}
		File directory = new File(dir);
	    // get all the files from a directory
	    File[] fList = directory.listFiles();
	    if (fList != null) {
		    for (File file : fList) {
		    	FileItemInfo fi = new FileItemInfo();
		    	fi.setName(file.getName());
		    	fi.setAbsolutePath(file.getAbsolutePath());
		        fi.setDir(true);
		    	if (file.isFile()) {
		        	fi.setDir(false);
		        }
		    	else {
		    		filesInfo = getListOfFileItems(file.getAbsolutePath(), filesInfo);
		    	}
		    	if (logger.isDebugEnabled()) {
		    		logger.debug("Scanned " + fi.getAbsolutePath());
		    	}
		    	filesInfo.add(fi);
		    }
	    }
	    return filesInfo;
	}

	private void loadProperties(ServletContext context) {
		Properties prop = new Properties();
		try {
		    //load a properties file from class path, inside static method
			InputStream isPropertiesFile = context.getResourceAsStream("/WEB-INF/classes/config.properties");
			if(isPropertiesFile != null) {
				prop.load(isPropertiesFile);
				if (prop.getProperty("root_dir") != null) {
					s_defRootDir = prop.getProperty("root_dir");
				}
			}
		} 
		catch (IOException ex) {
		    ex.printStackTrace();
		}		
	}
	
	private void purgeDirectory(File dir) {
	    for (File file: dir.listFiles()) {
	        if (file.isDirectory()) purgeDirectory(file);
	        file.delete();
	    }
	    dir.delete();
	}
	
	private String openFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
}
