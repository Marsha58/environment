package com.vw.ide.server.servlet.projectmanager;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vw.ide.client.projects.FilesTypesEnum;
import com.vw.ide.client.utils.Utils;
import com.vw.ide.server.servlet.IService;
import com.vw.ide.server.servlet.ServiceUtils;
import com.vw.ide.server.servlet.locator.ServiceLocator;
import com.vw.ide.server.servlet.projectmanager.sink.ImportProjectCompilationSink;
import com.vw.ide.server.servlet.remotebrowser.DirBrowserImpl;
import com.vw.ide.server.servlet.remotebrowser.DirBrowserUtils;
import com.vw.ide.server.servlet.userstate.UserStateServiceImpl;
import com.vw.ide.shared.servlet.RequestResult;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.projectmanager.RemoteProjectManagerService;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectAddFileResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectCreationResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectDeletionResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectImportResult;
import com.vw.ide.shared.servlet.projectmanager.RequestProjectMoveItemResult;
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
import com.vw.lang.processor.model.main.VWML;

/**
 * Remote management of VWML project
 * @author Oleg
 *
 */
@SuppressWarnings("serial")
public class ProjectManagerServiceImpl extends RemoteServiceServlet implements RemoteProjectManagerService, IService {
	private Logger logger = Logger.getLogger(ProjectManagerServiceImpl.class);
	private static String[] s_vwmlCompilerDefaultSettings = null;
	private static final String VWMLINTERPRETER_DEF_SETTINGS = "-m scan -t static -entity ue_im3 -p verbose=true -debuginfo true";
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
		if (s_vwmlCompilerDefaultSettings == null) {
			s_vwmlCompilerDefaultSettings = VWMLINTERPRETER_DEF_SETTINGS.split(" ");
		}
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
		res.setDescription(description);
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
		res.setDescription(description);
		return res;
	}

	@Override
	public RequestProjectImportResult importProject(String userName, FileItemInfo vwmlProjArchive) {
		RequestProjectImportResult res = new RequestProjectImportResult();
		res.setOperation("importing project");
		res.setRetCode(RequestProjectDeletionResult.GENERAL_OK);
		DirBrowserImpl browser = (DirBrowserImpl)ServiceLocator.instance().locate(DirBrowserImpl.ID);
		if (browser == null) {
			res.setRetCode(RequestProjectDeletionResult.GENERAL_FAIL);
			res.setResult("Remote browser service wasn't found");
			return res;
		}
		try {
			res = importProjectImpl(userName, res, Base64.decodeBase64(vwmlProjArchive.getContent()), browser.constructUserHomePath(userName));
		} catch (Exception e) {
			res.setRetCode(RequestProjectImportResult.GENERAL_FAIL);
			res.setResult(e.getLocalizedMessage());
			if (res.getDescription() != null) {
				try {
					removeProjectLayout(browser, res.getDescription());
				} catch (Exception ex) {
					res.setResult(ex.getLocalizedMessage());
				}
			}
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
		if (description.getProjectFiles().contains(toAdd)) {
			res.setRetCode(RequestProjectDeletionResult.GENERAL_FAIL);
			res.setResult("File '" + toAdd.getAbsolutePath() + "/" + toAdd.getName() + "' has already been added to project '" + description.getProjectName() + "'");
			return res;
		}
		DirBrowserImpl browser = (DirBrowserImpl)ServiceLocator.instance().locate(DirBrowserImpl.ID);
		if (browser == null) {
			res.setRetCode(RequestProjectDeletionResult.GENERAL_FAIL);
			res.setResult("Remote browser service wasn't found");
			return res;
		}
		RequestResult r = null;
		if (!toAdd.isDir()) {
			r = browser.createFile(description.getUserName(), toAdd.getAbsolutePath(), toAdd.getName(), toAdd.getContent());
		}
		else {
			String[] compoundPath = toAdd.getName().split("[\\\\/]+");
			if (compoundPath != null) {
				toAdd.setName(compoundPath[compoundPath.length - 1]);
			}
			r = browser.createAbsoluteDir(description.getUserName(), toAdd.getAbsolutePath());
		}
		if (r.getRetCode() != RequestDirOperationResult.GENERAL_OK) {
			res.setResult(r.getResult());
			res.setRetCode(r.getRetCode());
			return res;
		}
		String projDir = getProjectOperationalDir(description);
		correctFileItem(projDir, toAdd);
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
		res.setDescription(description);
		if (logger.isInfoEnabled()) {
			logger.debug("User '" + description.getUserName() + "' added item '" + toAdd + "' to project '" + description.getProjectName() + "'");
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
		if (logger.isInfoEnabled()) {
			logger.debug("User '" + description.getUserName() + "' updated project '" + description.getProjectName() + "'");
		}
		res.setDescription(description);
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
				String projDir = getProjectOperationalDir(d);
				for(FileItemInfo f : d.getProjectFiles()) {
					correctFileItem(projDir, f);
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
		RequestResult r = browser.deleteFile(description.getUserName(), toRemove.getAbsolutePath(), toRemove.getName(), null);
		if (r.getRetCode() != RequestResult.GENERAL_OK) {
			res.setResult(r.getResult());
			res.setRetCode(r.getRetCode());
			return res;
		}
		description.getProjectFiles().remove(toRemove);
		r = updateProject(description);
		if (r.getRetCode() != RequestResult.GENERAL_OK) {
			res.setResult(r.getResult());
			res.setRetCode(r.getRetCode());
			return res;
		}
		usi.setProjectIdSelected(description);
		usi.removeFileFromOpenedFiles(toRemove);
		if (logger.isInfoEnabled()) {
			logger.debug("User '" + description.getUserName() + "' removed item '" + toRemove + "' from project '" + description.getProjectName() + "'");
		}
		res.setDescription(description);
		return res;
	}

	@Override
	public RequestProjectRenameFileResult renameFileFromProject(ProjectDescription description, FileItemInfo toRename, FileItemInfo newName) {
		RequestProjectRenameFileResult res = new RequestProjectRenameFileResult();
		res.setOperation("renaming file/directory");
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
		RequestResult r = null;
		if (!toRename.isDir()) {
			r = browser.renameFile(description.getUserName(),
									toRename.getAbsolutePath() + "/" + toRename.getName(),
									null,
									newName.getAbsolutePath() + "/" + newName.getName());
		}
		else {
			String parentPath = DirBrowserUtils.extractParentPath(toRename.getAbsolutePath());
			r = browser.renameDir(description.getUserName(), parentPath, toRename.getName(), newName.getName());
		}
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
		if (!toRename.isDir()) {
			usi.removeFileFromOpenedFiles(toRename);
			usi.addFileToOpenedFiles(newName);
		}
		res.setDescription(description);
		return res;
	}

	@Override
	public RequestProjectMoveItemResult moveItemOnProject(ProjectDescription description, FileItemInfo fromItem, FileItemInfo toItem) {
		RequestProjectMoveItemResult res = new RequestProjectMoveItemResult();
		res.setOperation("moving file/directory inside the project");
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
		boolean fileMoving = true;
		if (fromItem.isDir()) {
			fileMoving = false;
		}
		RequestResult r = browser.moveDirOrFile(description.getUserName(),
												(fileMoving) ? fromItem.getAbsolutePath() + "/" + fromItem.getName() : fromItem.getAbsolutePath(),
												toItem.getAbsolutePath(),
												fileMoving);
		if (r.getRetCode() != RequestResult.GENERAL_OK) {
			res.setRetCode(RequestProjectDeletionResult.GENERAL_FAIL);
			res.setResult(r.getResult());
		}
		r = updateProject(description);
		if (r.getRetCode() != RequestResult.GENERAL_OK) {
			res.setResult(r.getResult());
			res.setRetCode(r.getRetCode());
			return res;
		}
		usi.setProjectIdSelected(description);
		if (!fromItem.isDir()) {
			usi.removeFileFromOpenedFiles(fromItem);
			toItem.setAbsolutePath(toItem.getAbsolutePath() + "/" + fromItem.getName());
			toItem.setDir(false);
			toItem.setContent(fromItem.getContent());
			usi.addFileToOpenedFiles(toItem);
		}
		res.setDescription(description);
		return res;
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
				getProjectOperationalDir(description) + "/interpreter",
				getProjectOperationalDir(description) + "/addons",
				getProjectOperationalDir(description),
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
		browser.removeDir(description.getUserName(), null, description.getJavaSrcPath());
		if (description.getProjectPath() != null) {
			browser.removeDir(description.getUserName(), null, description.getProjectPath());
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
		description.wellForm();
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
															getProjectOperationalDir(description) + "/interpreter",
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
	
	private void correctFileItem(String projDir, FileItemInfo f) {
		if (f.getRelPath() == null && f.getAbsolutePath() != null) {
			String relPath = "";
			if (!projDir.equals(f.getAbsolutePath())) {
				relPath = f.getAbsolutePath().substring(projDir.length() + 1);
			}
			f.setRelPath(relPath);
		}
	}
	
	private RequestProjectImportResult importProjectImpl(String userName, RequestProjectImportResult res, byte[] archive, String toPath) throws Exception {
		List<FileItemInfo> vwmlProjFiles = new ArrayList<FileItemInfo>();
		ZipInputStream zis = null;
		try {
			boolean foundProjFile = false;
			DirBrowserImpl browser = (DirBrowserImpl)ServiceLocator.instance().locate(DirBrowserImpl.ID);
			if (browser == null) {
				res.setRetCode(RequestProjectImportResult.GENERAL_FAIL);
				res.setResult("Remote browser service wasn't found");
				return res;
			}
			zis = new ZipInputStream(new ByteArrayInputStream(archive));
			ZipEntry ze = zis.getNextEntry();
			ProjectDescription d = new ProjectDescription();
			d.setUserName(userName);
			while(ze != null && res.getRetCode() == RequestProjectImportResult.GENERAL_OK) {
				String fileName = Utils.extractJustFileName(ze.getName());
				String relPath = Utils.extractJustPath(ze.getName());
				String absPath = toPath + "/" + ze.getName();
				byte[] buffer = ServiceUtils.readFile(zis);
				//String content = out.toString();
				if (FileItemInfo.recognizeFileType(ze.getName()) == FilesTypesEnum.VWML && buffer != null) {
					String content = new String(buffer);
					FileItemInfo fi = new FileItemInfo(fileName, toPath + "/" + relPath, false);
					fi.setRelPath(relPath);
					fi.setContent(content);
					ImportProjectCompilationSink sink = new ImportProjectCompilationSink();
					VWML vwml = new VWML();
					vwml.setCompilationSink(sink);
					vwml.setChunkVwmlCode(content);
					vwml.handleArgs(s_vwmlCompilerDefaultSettings);
					if (sink.getProjectName() != null) { // main vwml project file detected
						d.setAuthor(sink.getAuthor());
						d.setDescr(sink.getDescription());
						FileItemInfo fiMainFile = new FileItemInfo(fi.getName(), fi.getAbsolutePath(), false);
						fiMainFile.setRelPath(relPath);
						d.setMainProjectFile(fiMainFile);
						d.setMainModuleName(sink.getModuleName());
						d.setPackageName(sink.getModulePackage());
						d.setJavaSrcPath(sink.getFileLocation());
						d.setProjectName(sink.getProjectName());
						d.setProjectPath(toPath);
						d.setId(new Random().nextInt());
						res.setDescription(d);
					}
					vwmlProjFiles.add(fi);
				}
				else {
					RequestResult r = null;
					if (buffer != null) {
						r = browser.createBinFile(userName, toPath + "/" + relPath, fileName, buffer);
					}
					else {
						r = browser.createAbsoluteDir(userName, absPath);
					}
					if (r.getRetCode() != RequestDirOperationResult.GENERAL_OK) {
						res.setResult(r.getResult());
						res.setRetCode(r.getRetCode());
					}
				}
				if (FileItemInfo.recognizeFileType(ze.getName()) == FilesTypesEnum.VWML_PROJ) {
					foundProjFile = true;
				}
				ze = zis.getNextEntry();
			}
			if (!foundProjFile && vwmlProjFiles.size() != 0) {
				generateProjectDescriptionFile(browser, d);
			}
			for(FileItemInfo fi : vwmlProjFiles) {
				fi.setProjectId(d.getId().longValue());
				RequestProjectAddFileResult addFileResult = addFileToProject(d, fi);
				if (addFileResult.getRetCode() == RequestProjectAddFileResult.GENERAL_FAIL) {
					res.setRetCode(addFileResult.getRetCode());
					res.setResult(addFileResult.getResult());
					break;
				}
			}
		}
		finally {
			if (zis != null) {
				zis.close();
			}
		}
		return res;
	}
}
