package com.vw.ide.server.servlet.projectmanager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vw.ide.client.projects.FilesTypesEnum;
import com.vw.ide.server.servlet.IService;
import com.vw.ide.server.servlet.locator.ServiceLocator;
import com.vw.ide.server.servlet.remotebrowser.DirBrowserImpl;
import com.vw.ide.server.servlet.userstate.UserStateServiceImpl;
import com.vw.ide.shared.servlet.RequestResult;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.projectmanager.RemoteProjectManagerService;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectAddFileResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectCreationResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectDeletionResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectRemoveFileResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectRenameFileResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectUpdateResult;
import com.vw.ide.shared.servlet.projectmanager.RequestUserAvailableProjectResult;
import com.vw.ide.shared.servlet.projectmanager.specific.InterpreterDescription;
import com.vw.ide.shared.servlet.projectmanager.specific.ParallelInterpreterDescription;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.remotebrowser.RequestDirOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestSerializationOperationResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestUserStateResult;
import com.vw.ide.shared.servlet.remotebrowser.RequestedDirScanResult;
import com.vw.ide.shared.servlet.userstate.UserStateInfo;

/**
 * Remote management of VWML project
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class ProjectManagerServiceImpl extends RemoteServiceServlet implements RemoteProjectManagerService, IService {

	private Logger logger = Logger.getLogger(ProjectManagerServiceImpl.class);
	private static final String INTERPRETER_CONF_FILE = "interpreter.properties";
	private static final String ADDONS_CONF_FILE = "pom.xml";
	private static final String FRINGES_AS_ADDONS = "" +
	"<!-- external dependency --> \n" + 
	"<dependency>\n" +
	"\t<groupId>com.vw.model.lang</groupId>\n" +
	"\t<artifactId>AdvancedIntegrationFringes</artifactId>\n" +
	"\t<version>1.0-SNAPSHOT</version>\n" +
	"</dependency> ";
	
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
		res.setRetCode(RequestProjectCreationResult.GENERAL_OK);
		UserStateInfo usi = getUserStateInfo(description.getUserName(), res);
		if (usi == null) {
			return res;
		}
		DirBrowserImpl browser = (DirBrowserImpl)ServiceLocator.instance().locate(DirBrowserImpl.ID);
		if (browser == null) {
			res.setRetCode(RequestProjectCreationResult.GENERAL_FAIL);
			res.setResult("Remote browser service wasn't found");
			return res;
		}
		try {
			createProjectLayout(browser, description);
			generateMainProjectFile(browser, description);
			generateProjectDescriptionFile(browser, description);
			generateInterpreterConfigFiles(browser, description);
		}
		catch(Exception ex) {
			try {
				removeProjectLayout(browser, description);
			} catch (Exception e) {
				logger.error("User '" + description.getUserName() + "' project '" + description.getProjectName() + "'; error '" + ex.getMessage() + "'");
			}
			logger.error("User '" + description.getUserName() + "' project '" + description.getProjectName() + "'; error '" + ex.getMessage() + "'");
		}
		res.setProjectDescription(description);
		return res;
	}
	
	@Override
	public RequestProjectDeletionResult deleteProject(ProjectDescription description) {
		RequestProjectDeletionResult res = new RequestProjectDeletionResult();
		res.setOperation("deleting project");
		res.setRetCode(RequestProjectDeletionResult.GENERAL_OK);
		UserStateInfo usi = getUserStateInfo(description.getUserName(), res);
		if (usi == null) {
			return res;
		}
		try {
			DirBrowserImpl browser = (DirBrowserImpl)ServiceLocator.instance().locate(DirBrowserImpl.ID);
			if (browser == null) {
				res.setRetCode(RequestProjectDeletionResult.GENERAL_FAIL);
				res.setResult("Remote browser service wasn't found");
				return res;
			}
			removeProjectLayout(browser, description);
		}
		catch(Exception ex) {
			res.setResult(ex.getMessage());
			res.setRetCode(-1);
		}
		return res;
	}

	@Override
	public RequestProjectAddFileResult addFileToProject(ProjectDescription description, FileItemInfo toAdd) {
		RequestProjectAddFileResult res = new RequestProjectAddFileResult();
		res.setOperation("adding file to project");
		res.setRetCode(RequestProjectDeletionResult.GENERAL_OK);
		UserStateInfo usi = getUserStateInfo(description.getUserName(), res);
		if (usi == null) {
			return res;
		}
		DirBrowserImpl browser = (DirBrowserImpl)ServiceLocator.instance().locate(DirBrowserImpl.ID);
		if (browser == null) {
			res.setRetCode(RequestProjectDeletionResult.GENERAL_FAIL);
			res.setResult("Remote browser service wasn't found");
			return res;
		}
		RequestResult r = browser.createFile(description.getUserName(), toAdd.getAbsolutePath(), toAdd.getName(), toAdd.getContent());
		if (r.getRetCode() != RequestDirOperationResult.GENERAL_OK) {
			res.setResult(r.getResult());
			res.setRetCode(r.getRetCode());
			return res;
		}
		description.getProjectFiles().add(toAdd);
		r = updateProject(description);
		if (r.getRetCode() != RequestProjectUpdateResult.GENERAL_OK) {
			res.setResult(r.getResult());
			res.setRetCode(r.getRetCode());
			return res;
		}
		if (!toAdd.isDir()) {
			usi.setProjectIdSelected(description);
			usi.addFileToOpenedFiles(toAdd);
		}
		return res;
	}
	
	@Override
	public RequestProjectUpdateResult updateProject(ProjectDescription description) {
		RequestProjectUpdateResult res = new RequestProjectUpdateResult();
		UserStateInfo usi = getUserStateInfo(description.getUserName(), res);
		if (usi == null) {
			return res;
		}
		DirBrowserImpl browser = (DirBrowserImpl)ServiceLocator.instance().locate(DirBrowserImpl.ID);
		if (browser == null) {
			res.setRetCode(RequestProjectCreationResult.GENERAL_FAIL);
			res.setResult("Remote browser service wasn't found");
			return res;
		}
		String projectDescriptionFile = makeProjectConfigFileName(description.getMainModuleName());
		if (logger.isInfoEnabled()) {
			logger.info("User '" + description.getUserName() + "'" + "; project '" + projectDescriptionFile + "';" + " updating");
		}
		description.getMainProjectFile().setContent(null);
		for(FileItemInfo fi : description.getProjectFiles()) {
			fi.setContent(null);
		}
		RequestSerializationOperationResult r = browser.serializeObjectToJson(description.getUserName(),
																			  projectDescriptionFile,
																			  getProjectOperationalDir(description),
																			  description);
		if (r.getRetCode() != RequestSerializationOperationResult.GENERAL_OK) {
			res.setRetCode(r.getRetCode());
			res.setResult(r.getResult());
		}
		return res;
	}

	@Override
	public RequestUserAvailableProjectResult getUserAvailableProjects(String userName) {
		RequestUserAvailableProjectResult res = new RequestUserAvailableProjectResult();
		UserStateInfo usi = getUserStateInfo(userName, res);
		if (usi == null) {
			return res;
		}
		DirBrowserImpl browser = (DirBrowserImpl)ServiceLocator.instance().locate(DirBrowserImpl.ID);
		if (browser == null) {
			res.setRetCode(RequestProjectCreationResult.GENERAL_FAIL);
			res.setResult("Remote browser service wasn't found");
			return res;
		}
		RequestedDirScanResult dirScanRes = browser.getDirScan(userName, null);
		if (dirScanRes.getRetCode() != RequestedDirScanResult.GENERAL_OK) {
			res.setRetCode(dirScanRes.getRetCode());
			res.setResult(dirScanRes.getResult());
			return res;
		}
		RequestSerializationOperationResult r = null;
		for(FileItemInfo fi : dirScanRes.getFiles()) {
			if (FileItemInfo.recognizeFileType(fi.getName()) == FilesTypesEnum.VWML_PROJ) {
				if (logger.isInfoEnabled()) {
					logger.info("User '" + userName + "'; reading project file '" + fi.getName() + "'");
				}
				r = browser.deserializeObjectFromJson(userName, null, fi.getAbsolutePath(), ProjectDescription.class);
				if (r.getRetCode() != RequestedDirScanResult.GENERAL_OK) {
					logger.error("User '" + userName + "'; corrupted project file '" + fi.getName() + "' found; error '" + r.getResult() + "'"); 
					continue;
				}
				ProjectDescription d = (ProjectDescription)r.getSerializiedObject();
				if (d.getProjectFiles().size() == 0) {
					d.getProjectFiles().add(d.getMainProjectFile());
				}
				res.getAvailableProjects().add(d);
			}
		}
		return res;
	}

	@Override
	public RequestProjectRemoveFileResult removeFileFromProject(ProjectDescription description, FileItemInfo toRemove) {
		RequestProjectRemoveFileResult res = new RequestProjectRemoveFileResult();
		res.setOperation("removing file from project");
		res.setRetCode(RequestProjectDeletionResult.GENERAL_OK);
		UserStateInfo usi = getUserStateInfo(description.getUserName(), res);
		if (usi == null) {
			return res;
		}
		DirBrowserImpl browser = (DirBrowserImpl)ServiceLocator.instance().locate(DirBrowserImpl.ID);
		if (browser == null) {
			res.setRetCode(RequestProjectDeletionResult.GENERAL_FAIL);
			res.setResult("Remote browser service wasn't found");
			return res;
		}
		RequestResult r = browser.deleteFile(description.getUserName(), toRemove.getAbsolutePath(), null);
		if (r.getRetCode() != RequestResult.GENERAL_OK) {
			res.setResult(r.getResult());
			res.setRetCode(r.getRetCode());
			return res;
		}
		r = updateProject(description);
		if (r.getRetCode() != RequestResult.GENERAL_OK) {
			res.setResult(r.getResult());
			res.setRetCode(r.getRetCode());
			return res;
		}
		usi.setProjectIdSelected(description);
		usi.removeFileFromOpenedFiles(toRemove);
		return res;
	}

	@Override
	public RequestProjectRenameFileResult renameFileFromProject(ProjectDescription description, FileItemInfo toRename, FileItemInfo newName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String makeMainProjectFileName(String mainModuleName) {
		mainModuleName = mainModuleName + FilesTypesEnum.VWML;
		return mainModuleName.toString().toLowerCase();
	}
	
	private String makeProjectConfigFileName(String mainModuleName) {
		mainModuleName = mainModuleName + "." + FilesTypesEnum.VWML_PROJ;
		return mainModuleName.toString().toLowerCase();
	}
	
	
	private void createProjectLayout(DirBrowserImpl browser, ProjectDescription description) throws Exception {
		String dirs[] = {
				description.getJavaSrcPath(),
				getProjectOperationalDir(description),
				getProjectOperationalDir(description) + "/interpreter",
				getProjectOperationalDir(description) + "/addons"
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
				getProjectOperationalDir(description),
				getProjectOperationalDir(description) + "/interpreter",
				getProjectOperationalDir(description) + "/addons"
		};
		for(String dir : dirs) {
			if (logger.isInfoEnabled()) {
				logger.info("User '" + description.getUserName() + "'" + "; project '" + description.getProjectName() + "';" + " removes directory '" + dir + "'");
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
		
		description.setMainProjectFile(new FileItemInfo(sProjectMainFileName, getProjectOperationalDir(description), false));
		description.getProjectFiles().add(description.getMainProjectFile());
		RequestProjectUpdateResult updateResult = updateProject(description);
		if (updateResult.getRetCode() != RequestDirOperationResult.GENERAL_OK) {
			throw new Exception(updateResult.getResult());
		}
	}
	
	private void generateProjectDescriptionFile(DirBrowserImpl browser, ProjectDescription description) throws Exception {
		String projectDescriptionFile = makeProjectConfigFileName(description.getMainModuleName());
		if (logger.isInfoEnabled()) {
			logger.info("User '" + description.getUserName() + "'" + "; project '" + description.getProjectName() + "';" + " description file '" + projectDescriptionFile + "'");
		}
		description.setProjectDescriptionFile(new FileItemInfo(projectDescriptionFile, getProjectOperationalDir(description), false));
		RequestSerializationOperationResult res = browser.serializeObjectToJson(description.getUserName(),
																				projectDescriptionFile,
																				getProjectOperationalDir(description),
																				description);
		if (res.getRetCode() != RequestDirOperationResult.GENERAL_OK) {
			throw new Exception(res.getResult());
		}
	}
	
	private void generateInterpreterConfigFiles(DirBrowserImpl browser, ProjectDescription description) throws Exception {
		String content = "interpreter.execution.step.delay=500\r\n";
		content += "interpreter.execution.strategy=" + description.getInterpreterDescription().getName() + "\r\n";
		if (description.getInterpreterDescription().getName().equals(InterpreterDescription.PARALLEL)) {
			content += "interpreter.ring.nodes=" + ((ParallelInterpreterDescription)(description.getInterpreterDescription())).getNodesPerRing() + "\r\n";
		}
		content += "interpreter.ring.visitor=com.vw.lang.conflictring.visitor.VWMLConflictRingVisitor\r\n";
		RequestDirOperationResult res = browser.createFile(description.getUserName(),
															description.getProjectPath() + "/" + description.getMainModuleName() + "/interpreter",
															INTERPRETER_CONF_FILE,
															content);
		if (res.getRetCode() != RequestDirOperationResult.GENERAL_OK) {
			throw new Exception(res.getResult());
		}
		res = browser.createFile(description.getUserName(),
								description.getProjectPath() + "/" + description.getMainModuleName() + "/addons",
								ADDONS_CONF_FILE,
								FRINGES_AS_ADDONS);
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
											description.getDescr(),
											description.getMainModuleName());
	}
	
	private String makeProjectFileHeaderPattern(String projectName,
												String packageName,
												String javaSrcPath,
												String author,
												String descr,
												String moduleName) {
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
				packageName, javaSrcPath, author, projectName, descr, moduleName);		
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
	
	private UserStateInfo getUserStateInfo(String userName, RequestResult res) {
		UserStateServiceImpl userStateService = (UserStateServiceImpl)ServiceLocator.instance().locate(UserStateServiceImpl.ID);
		if (userStateService == null) {
			res.setRetCode(RequestResult.GENERAL_FAIL);
			res.setResult("User state service service wasn't found");
			return null;
		}
		RequestUserStateResult userStateResult = userStateService.getUserState(userName);
		if (userStateResult.getRetCode() != RequestUserStateResult.GENERAL_OK) {
			res.setResult(userStateResult.getResult());
			res.setRetCode(userStateResult.getRetCode());
			return null;
		}
		if (userStateResult.getUserStateInfo() == null) {
			res.setResult("User '" + userName + "' wasn't logeed in");
			res.setRetCode(RequestUserStateResult.GENERAL_FAIL);
			return null;
		}
		return userStateResult.getUserStateInfo();
	}
}
