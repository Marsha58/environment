package com.vw.ide.server.servlet.projectmanager;

import java.io.File;
import java.io.FileWriter;

import javax.servlet.ServletConfig;
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
import com.vw.ide.server.servlet.IService;
import com.vw.ide.server.servlet.locator.ServiceLocator;
import com.vw.ide.server.servlet.remotebrowser.DirBrowserImpl;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.projectmanager.RemoteProjectManagerService;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectCreationResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectDeletionResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;

/**
 * Remote management of VWML project
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class ProjectManagerServiceImpl extends RemoteServiceServlet implements RemoteProjectManagerService, IService {

	private Logger logger = Logger.getLogger(ProjectManagerServiceImpl.class);
	public static final int ID = 1027;
	
	public ProjectManagerServiceImpl() {
		super();
		if (logger.isInfoEnabled()) {
			logger.info("ProjectManagerServiceImpl constructed");
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
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServiceLocator.instance().register(this);
		if (logger.isInfoEnabled()) {
			logger.info("ProjectManagerServiceImpl started and initialized");
		}
	}

	@Override
	public RequestProjectCreationResult createProject(ProjectDescription description) {
		RequestProjectCreationResult res = new RequestProjectCreationResult();
		res.setOperation("project creating");
		res.setProjectName(description.getProjectName());
		res.setRetCode(RequestProjectCreationResult.GENERAL_OK);
		
		DirBrowserImpl browser = (DirBrowserImpl)ServiceLocator.instance().locate(DirBrowserImpl.ID);
		if (browser == null) {
			res.setRetCode(RequestProjectCreationResult.GENERAL_FAIL);
			res.setResult("Remote browser service wasn't found");
			return res;
		}
		try {
			createProjectLayout(browser, description);
			generateMainProjectFile(browser, description);
		}
		catch(Exception ex) {
			try {
				removeProjectLayout(browser, description);
			} catch (Exception e) {
				logger.error("User '" + description.getUserName() + "' project '" + description.getProjectName() + "'; error '" + ex.getMessage() + "'");
			}
			logger.error("User '" + description.getUserName() + "' project '" + description.getProjectName() + "'; error '" + ex.getMessage() + "'");
		}		
		return res;
	}
	
	@Override
	public RequestProjectDeletionResult deleteProject(String userName, String projectName, Long projectId) {
		RequestProjectDeletionResult res = new RequestProjectDeletionResult();
		res.setOperation("deleting project");
		res.setRetCode(RequestProjectDeletionResult.GENERAL_OK);
		try {
			File fileTemp = new File(projectName);
			DirBrowserImpl browser = (DirBrowserImpl)ServiceLocator.instance().locate(DirBrowserImpl.ID);
			if (browser == null) {
				res.setRetCode(RequestProjectDeletionResult.GENERAL_FAIL);
				res.setResult("Remote browser service wasn't found");
				return res;
			}
			browser.deleteFolder(fileTemp);
		}
		catch(Exception ex) {
			res.setResult(ex.getMessage());
			res.setRetCode(-1);
		}
		return res;
	}
	
	private String makeMainProjectFileName(String mainModuleName) {
		mainModuleName = mainModuleName + ".vwml";
		return mainModuleName.toString().toLowerCase();
	}
	
	private String makeProjectConfigFileName(String projectMainFileName) {
		StringBuffer sProjectConfigFileName = new StringBuffer();
		if (projectMainFileName.indexOf(".") != -1) {
			sProjectConfigFileName.append(projectMainFileName.substring(0,projectMainFileName.indexOf(".")));
			sProjectConfigFileName.append(".xml");		
		}
		return sProjectConfigFileName.toString().toLowerCase();
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
	
	private void createProjectLayout(DirBrowserImpl browser, ProjectDescription description) throws Exception {
		String dirs[] = {
				description.getJavaSrcPath(),
				getProjectOperationalDir(description)
		};
		for(String dir : dirs) {
			if (logger.isInfoEnabled()) {
				logger.info("User '" + description.getUserName() + "'" + "; project '" + description.getProjectName() + "';" + " create directory '" + dir + "'");
			}
			RequestDirOperationResult r = browser.createAbsoluteDir(description.getUserName(), dir);
			if (r.getRetCode() != RequestDirOperationResult.GENERAL_OK) {
				throw new Exception(r.getResult());
			}
		}
	}

	private void removeProjectLayout(DirBrowserImpl browser, ProjectDescription description) throws Exception {
		String dirs[] = {
				description.getJavaSrcPath(),
				getProjectOperationalDir(description)
		};
		for(String dir : dirs) {
			if (logger.isInfoEnabled()) {
				logger.info("User '" + description.getUserName() + "'" + "; project '" + description.getProjectName() + "';" + " create directory '" + dir + "'");
			}
			RequestDirOperationResult r = browser.removeDir(description.getUserName(), null, dir);
			if (r.getRetCode() != RequestDirOperationResult.GENERAL_OK) {
				throw new Exception(r.getResult());
			}
		}
	}
	
	private void generateMainProjectFile(DirBrowserImpl browser, ProjectDescription description) throws Exception {
		String sProjectMainFileName = makeMainProjectFileName(description.getMainModuleName());
		if (logger.isInfoEnabled()) {
			logger.info("User '" + description.getUserName() + "'" + "; project '" + description.getProjectName() + "';" + " main file '" + sProjectMainFileName + "'");
		}
		String content = generateContentOfMainProjectFile(description);
		RequestDirOperationResult res = browser.createFile(	description.getUserName(),
															getProjectOperationalDir(description),
															sProjectMainFileName,
															content);
		if (res.getRetCode() != RequestDirOperationResult.GENERAL_OK) {
			throw new Exception(res.getResult());
		}
	}
	
	private String getProjectOperationalDir(ProjectDescription description) {
		return description.getProjectPath() + "/" + description.getMainModuleName();
	}
	
	private String generateContentOfMainProjectFile(ProjectDescription description) {
		return makeProjectFileHeaderPattern(description.getProjectName(),
											description.getPackageName(),
											description.getJavaSrcPath(),
											description.getAuthor(),
											description.getDescr());
	}
	
	private String makeProjectFileHeaderPattern(String projectName,
												String packageName,
												String javaSrcPath,
												String author,
												String descr) {
		String pattern = format("options {\n" + 
				"\tlanguage=__java__ {\n" +
				"\t\tpackage = \"%s\"\n" + 
				"\t\tpath = \"%s\"\n" +
				"\t\tauthor = \"%s\"\n" + 
				"\t\tproject_name = \"%s\"\n" +
				"\t\tdescription = \"%s\"\n" + 
				"\t\tbeyond {\n" + 
				"\t\t\tfringe communication ias (\n" +
				"\t\t\t)\n" +
				"\t\t\tfringe services ias (\n" +
				"\t\t\t\tmath ias \"com.vw.lang.beyond.java.fringe.gate.math.Math\"\n" +
				"\t\t\t)\n" +
				"\t\t}\n" + 
				"\t}\n" +
				"\tconflictring {\n" + 
				"\t}\n" + 
				"}\n" +
				"module %s {\n" +
				"}\n",
				packageName, javaSrcPath, author, projectName, descr, packageName.toLowerCase());		
		return pattern;
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
}
