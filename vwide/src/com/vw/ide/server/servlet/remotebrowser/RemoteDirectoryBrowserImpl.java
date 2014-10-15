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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RemoteDirectoryBrowser;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestFileOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestProjectCreationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestUserStateResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestedDirScanResult;
import com.vw.ide.shared.servlet.remotebrowser.UserStateInfo;


/**
 * Implementation of remote directory browser
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class RemoteDirectoryBrowserImpl extends RemoteServiceServlet implements RemoteDirectoryBrowser {

	private Logger logger = Logger.getLogger(RemoteDirectoryBrowserImpl.class);
	private static String s_defRootDir = "/var/projects";
	private  Map<String, UserStateInfo> usersStates = new HashMap <String, UserStateInfo>(); 
	
	public RemoteDirectoryBrowserImpl() {
		super();
		if (logger.isInfoEnabled()) {
			logger.info("RemoteDirectoryBrowserImpl constructed");
		}
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
	
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		loadProperties(config.getServletContext());
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
		    	fi.setAbsolutePath(file.getAbsolutePath());
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


	private String makeMainProjectFileName(String projectName) {
		StringBuffer sPackageFileName = new StringBuffer();
		String[] arrProjectFileName = projectName.split(" ");
		for (String curWord : arrProjectFileName) {
			sPackageFileName.append(curWord);
		}
		sPackageFileName.append(".vwml");		
		return sPackageFileName.toString().toLowerCase();
	}
	
	private String makeProjectConfigFileName(String projectMainFileName) {
		StringBuffer sProjectConfigFileName = new StringBuffer();
		if (projectMainFileName.indexOf(".") != -1) {
			sProjectConfigFileName.append(projectMainFileName.substring(0,projectMainFileName.indexOf(".")));
			sProjectConfigFileName.append(".xml");		
		}
		return sProjectConfigFileName.toString().toLowerCase();
	}	
	
	
	private static String format(final String format, final String... args) {
		String[] split = format.split("%s");
		final StringBuffer msg = new StringBuffer();
		for (int pos = 0; pos < split.length - 1; pos += 1) {
			msg.append(split[pos]);
			msg.append(args[pos]);
		}
		msg.append(split[split.length - 1]);
		return msg.toString();
	}	
	
	private String makeProjectFileHeaderPattern(String projectName, String packageName, String javaSrcPath,
			String author, String descr) {
		String pattern = format("options {\n" + 
				"\tlanguage=_java_ {\n" +
				"\t\tpackage = \"%s\"\n" + 
				"\t\tpath = \"%s\"\n" +
				"\t\tauthor = \"%s\"\n" + 
				"\t\tproject_name = \"%s\"\n" +
				"\t\tdescription = \"%s\"\n" + 
				"\t\tbeyond {\n" + 
				"\t\t}\n" + 
				"\t}\n" +
				"\tconflictring {\n" + 
				"\t}\n" + 
				"}\n" +
				"module %s {\n" +
				"}",
				packageName, javaSrcPath, author, projectName,	descr, packageName.toLowerCase());		
		return pattern;
	}
	
	private void makeProjectConfigFile(String projectConfFileFullName, String projectName, String packageName, String javaSrcPath,
			String author, String descr) {
		
		 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	     DocumentBuilder dBuilder;
		
	     try {
	            dBuilder = dbFactory.newDocumentBuilder();
	            Document doc = dBuilder.newDocument();
	            Element rootElement = doc.createElementNS("http://www.win-interactive.com/" + packageName.toLowerCase(), "project");
	            doc.appendChild(rootElement);

	            Element nodePackage = doc.createElement("package");
	    		nodePackage.appendChild(doc.createTextNode(packageName));
	    		rootElement.appendChild(nodePackage);

	    		Element nodeJavaSrcPath = doc.createElement("path");
	    		nodeJavaSrcPath.appendChild(doc.createTextNode(javaSrcPath));
	    		rootElement.appendChild(nodeJavaSrcPath);
	    		 
	    		Element nodeAuthor = doc.createElement("author");
	    		nodeAuthor.appendChild(doc.createTextNode(author));
	    		rootElement.appendChild(nodeAuthor);

	    		Element nodeProjectName = doc.createElement("projectName");
	    		nodeProjectName.appendChild(doc.createTextNode(projectName));
	    		rootElement.appendChild(nodeProjectName);	    		 

	    		Element nodeDescr = doc.createElement("descr");
	    		nodeDescr.appendChild(doc.createTextNode(descr));
	    		rootElement.appendChild(nodeDescr);	
	    		 
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer();
	            //for pretty print
	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            DOMSource source = new DOMSource(doc);
	 
	            //write to console or file
//	            StreamResult console = new StreamResult(System.out);
	            StreamResult file = new StreamResult(new File(projectConfFileFullName));
	 
	            //write data
//	            transformer.transform(source, console);
	            transformer.transform(source, file);
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}	
	
	@Override
	public RequestProjectCreationResult createProject(String userName,
			String projectName, String projectPath, String packageName, String javaSrcPath,
			String author, String descr) {
		RequestProjectCreationResult res = new RequestProjectCreationResult();
		res.setOperation("project creating");
//		res.setProjectPath(projectPath);
		res.setProjectName(projectName);
		res.setRetCode(0);
		
		String sUserBasePath = constructUserHomePath(userName);
		String sFullProjectPath = "";
		if((projectPath.length()>0)&&(!projectPath.startsWith("\\"))) {
			sFullProjectPath = projectPath; 
		} else {
			sFullProjectPath = sUserBasePath + "\\" + projectName; 
		}
		try {
			
			File dir = new File(sFullProjectPath);
			if (!dir.exists()) {
				dir.mkdirs();
			};

			File dirJavaSrc = new File(sFullProjectPath + "\\" + javaSrcPath);
			if (!dirJavaSrc.exists()) {
				dirJavaSrc.mkdirs();
			};			
			
			String sProjectMainFileName = makeMainProjectFileName(projectName);
			
			File fMainVWML = new File(sFullProjectPath + "\\" + sProjectMainFileName );
			if (!fMainVWML.exists()) {
				fMainVWML.createNewFile();
				FileWriter writer = new FileWriter(fMainVWML); 
				writer.write(makeProjectFileHeaderPattern(projectName, packageName, javaSrcPath, author, descr));
				writer.flush();
				writer.close();
			}

			
			String projectConfFileFullName = sFullProjectPath + "\\" + makeProjectConfigFileName(sProjectMainFileName);
			
			makeProjectConfigFile(projectConfFileFullName, projectName, packageName, javaSrcPath, author, descr);
/*			
			File fProjectConf = new File(sFullProjectPath + "\\" + makeProjectConfigFileName(sProjectMainFileName));
			if (!fProjectConf.exists()) {
				fProjectConf.createNewFile();
				FileWriter writerPC = new FileWriter(fProjectConf); 
				writerPC.write(makeProjectConfigFile(projectName, packageName, javaSrcPath, author, descr));
				writerPC.flush();
				writerPC.close();
			}
*/			
			
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
	
	
	@Override
	public RequestDirOperationResult deleteProject(String userName,
			String projectName, Long projectId) {
		RequestDirOperationResult res = new RequestDirOperationResult();
		res.setProjectId(projectId);
		res.setOperation("deleting project");
		res.setPath(projectName);
		res.setRetCode(0);
		try {
			File fileTemp = new File(projectName);
			deleteFolder(fileTemp);
  
		}
		catch(Exception ex) {
			res.setResult(ex.getMessage());
			res.setRetCode(-1);
		}
		return res;
	}


	@Override
	public RequestDirOperationResult addFile(String user, String parent,
			String fileName, Long projectId, Long fileId) {
		RequestDirOperationResult res = new RequestDirOperationResult();
		res.setOperation("file creating");
//		res.setProjectPath(projectPath);
		res.setProjectId(projectId);
		res.setRetCode(0);
		String sFullProjectPath = parent + "\\" + fileName; 
		try {
			
			File parentPath = new File(Utils.extractJustPath(sFullProjectPath));
			if (!parentPath.exists()) {
				parentPath.mkdirs();
			}
			
			File fNewFile = new File(sFullProjectPath);
			if (!fNewFile.exists()) {
				fNewFile.createNewFile();
				FileWriter writer = new FileWriter(fNewFile); 
				writer.write("");
				writer.flush();
				writer.close();
			}
		}
		catch(Exception ex) {
			res.setResult(ex.getMessage());
			res.setRetCode(-1);
		}		
		
		return res;
	}


	@Override
	public RequestFileOperationResult deleteFile(String user, String fileName,
			Long fileId) {
		RequestFileOperationResult res = new RequestFileOperationResult();
		res.setFileId(fileId);
		res.setOperation("deleting file");
		res.setFileName(fileName);
		res.setRetCode(0);
		try{
	        File fileTemp = new File(fileName);
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
	public RequestDirOperationResult readFile(String user, String parent,
			String fileName, Long projectId, Long fileId) {
		UserStateInfo userStateInfo;
		
		if (usersStates.get(user) == null) {
			userStateInfo = new UserStateInfo();
		} else {
			userStateInfo = usersStates.get(user);
		}
		FileItemInfo fileItemInfo = new FileItemInfo(Utils.extractJustFileName(fileName),Utils.extractJustPath(fileName),false);
		fileItemInfo.setFileId(fileId);
		fileItemInfo.setProjectId(projectId);
		
		userStateInfo.addFile2OpenedFiles(fileId, fileItemInfo);
		userStateInfo.setProjectIdSelected(projectId);
		userStateInfo.setFileIdSelected(fileId);
		usersStates.put(user,userStateInfo);
		
		RequestDirOperationResult res = new RequestDirOperationResult();
		res.setProjectId(projectId);
		res.setFileId(fileId);
		res.setOperation("reading file");
		res.setPath(fileName);
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

	@Override
	public RequestFileOperationResult saveFile(String user, 
			String fileName, Long projectId, Long fileId, String content) {
		RequestFileOperationResult res = new RequestFileOperationResult();
		res.setFileName(fileName);
		res.setFileId(fileId);
		res.setOperation("saving file");
		res.setRetCode(0);
		
		Writer writer = null;
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
			          new FileOutputStream(fileName), "utf-8"));
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
	public RequestUserStateResult getUserState(String user) {
		RequestUserStateResult res = new RequestUserStateResult();
		res.setOperation("getting user state info");
		res.setRetCode(0);
		if (usersStates.get(user) != null) {
			UserStateInfo userStateInfo = usersStates.get(user);
			res.setUserStateInfo(userStateInfo);
		} else {
			res.setResult("user not found");
			res.setRetCode(-1);
		}
		return res;
	}

	
	
}
