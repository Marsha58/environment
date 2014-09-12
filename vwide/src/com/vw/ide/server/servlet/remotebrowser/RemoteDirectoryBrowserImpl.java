package com.vw.ide.server.servlet.remotebrowser;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowser;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestedDirScanResult;


/**
 * Implementation of remote directory browser
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class RemoteDirectoryBrowserImpl extends RemoteServiceServlet implements RemoteDirectoryBrowser {

	private Logger logger = Logger.getLogger(RemoteDirectoryBrowserImpl.class);
	private static String s_defRootDir = "/var/projects";
	
	public RemoteDirectoryBrowserImpl() {
		super();
		if (logger.isInfoEnabled()) {
			logger.info("RemoteDirectoryBrowserImpl constructed");
		}
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if (logger.isInfoEnabled()) {
			logger.info("RemoteDirectoryBrowserImpl started and initialized");
		}
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
		String path = constructUserHomePath(user) + ((dir != null) ? ("/" + dir) : "");
		RequestedDirScanResult r = new RequestedDirScanResult();
		r.setParentPath(new File(path).getAbsolutePath());
		if (user != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Asking for dirs list for user '" + user + "'; dir '" + dir + "'; full path '" + path + "'");
			}
			r.setFiles(getListOfFileItems(path));
		}
		else {
			r.setFiles(new ArrayList<FileItemInfo>());
		}
		return r;
	}

	public RequestDirOperationResult createDir(String user, String parent, String dir) {
		RequestDirOperationResult res = new RequestDirOperationResult();
		String fullPath = parent + "/" + dir;
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

	public RequestDirOperationResult removeDir(String user, String parent, String dir) {
		RequestDirOperationResult res = new RequestDirOperationResult();
		res.setPath(dir);
		res.setRetCode(0);
		res.setOperation("remove directory");
		try {
			purgeDirectory(new File(dir));
		}
		catch(Exception ex) {
			res.setResult(ex.getMessage());
			res.setRetCode(-1);
		}
		return res;
	}
	
	private List<FileItemInfo> getListOfFileItems(String dir) {
		List<FileItemInfo> filesInfo = new ArrayList<FileItemInfo>();
		File directory = new File(dir);
	    // get all the files from a directory
	    File[] fList = directory.listFiles();
	    if (fList != null) {
		    for (File file : fList) {
		    	FileItemInfo fi = new FileItemInfo();
		    	fi.setName(file.getName());
		    	fi.setPath(file.getAbsolutePath());
		        fi.setDir(true);
		    	if (file.isFile()) {
		        	fi.setDir(false);
		        }
		    	filesInfo.add(fi);
		    }
	    }
	    return filesInfo;
	}
	
	private String constructUserHomePath(String user) {
		return s_defRootDir + "/" + user;
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

	@Override
	public RequestDirOperationResult readFile(String user, String parent,
			String fileName) {
		RequestDirOperationResult res = new RequestDirOperationResult();
		res.setOperation("read file");
		res.setRetCode(0);
		try {
			res.setTextFile(openFile(fileName));
		}
		catch(Exception ex) {
			res.setResult(ex.getMessage());
			res.setRetCode(-1);
		}
		return res;
	}	
	
}
