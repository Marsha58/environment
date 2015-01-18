package com.vw.ide.server.servlet.processor.command.sandr;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.vw.ide.server.servlet.ServiceUtils;
import com.vw.ide.server.servlet.locator.ServiceLocator;
import com.vw.ide.server.servlet.remotebrowser.DirBrowserImpl;
import com.vw.ide.shared.servlet.processor.CommandProcessorResult;
import com.vw.ide.shared.servlet.processor.dto.sandr.SearchAndReplaceBundle;
import com.vw.ide.shared.servlet.processor.dto.sandr.SearchAndReplaceResult;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;
import com.vw.ide.shared.servlet.tracer.TracerSearchAndReplaceMessage;

public class SearchAndReplaceCommandHandler {
	
	private Logger logger = Logger.getLogger(SearchAndReplaceCommandHandler.class);
	
	private SearchAndReplaceBundle bundle;
	
	private SearchAndReplaceCommandHandler(SearchAndReplaceBundle bundle) {
		this.bundle = bundle;
	}
	
	public static SearchAndReplaceCommandHandler instance(SearchAndReplaceBundle bundle) {
		return new SearchAndReplaceCommandHandler(bundle);
	}
	
	public CommandProcessorResult handle() throws Exception {
		CommandProcessorResult r = new CommandProcessorResult();
		r.setOperation("search_and_replace");
		r.setRetCode(CommandProcessorResult.GENERAL_OK);
		ProjectDescription project = bundle.getProject();
		if (project == null) {
			throw new Exception("invalid request; the project field must not be null");
		}
		if (bundle.getPhase() == SearchAndReplaceBundle.PHASE_SEARCH) {
			handleSearch(r, project.getUserName(), project.getProjectFiles());
		}
		else
		if (bundle.getPhase() == SearchAndReplaceBundle.PHASE_REPLACE) {
			handleReplace(r, project.getUserName(), bundle.getReplacedItems());
		}		
		return r;
	}
	
	protected void handleSearch(CommandProcessorResult r, String userName, List<FileItemInfo> files) throws Exception {
		boolean asRegex = false;
		Pattern spattern = null;
		if (files == null) {
			r.setResult("no files were specified");
			r.setRetCode(CommandProcessorResult.GENERAL_FAIL);
			return;
		}
		try {
			spattern = Pattern.compile(bundle.getSearch());
			asRegex = true;
		}
		catch(Exception e) {
		}
		for(FileItemInfo fi : files) {
			String filePath = fi.getAbsolutePath() + "/" + fi.getName();
			if (logger.isDebugEnabled()) {
				logger.debug("scanning in '" + filePath + "'");
			}
			FileInputStream fis = new FileInputStream(new File(filePath));
			Scanner scanner = new Scanner(fis);
			int lineNumber = 0;
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (asRegex) {
					Matcher m = spattern.matcher(line);
					while (m.find()) {
						int pos = m.start();
						if (logger.isDebugEnabled()) {
							logger.debug("The '" + bundle.getSearch() + "' found in line '" + lineNumber + "'; position '" + pos + "'");
						}
						postSearchResult(userName,
								Integer.valueOf(pos),
								Integer.valueOf(lineNumber + 1),
								bundle.getSearch(),
								bundle.getReplace(),
								bundle.getProject().getProjectName(),
								fi);
					}
				}
				else {
					int pos = line.indexOf(bundle.getSearch());
					if (pos != -1) {
						if (logger.isDebugEnabled()) {
							logger.debug("The '" + bundle.getSearch() + "' found in line '" + lineNumber + "'; position '" + pos + "'");
						}
						postSearchResult(userName,
										Integer.valueOf(pos),
										Integer.valueOf(lineNumber + 1),
										bundle.getSearch(),
										bundle.getReplace(),
										bundle.getProject().getProjectName(),
										fi);
					}
				}
				lineNumber++;
			}
			scanner.close();
		}
	}

	protected void handleReplace(CommandProcessorResult r, String userName, List<FileItemInfo> files) throws Exception {
		if (files == null) {
			r.setResult("no files were specified");
			r.setRetCode(CommandProcessorResult.GENERAL_FAIL);
			return;
		}
		boolean asRegex = false;
		try {
			Pattern.compile(bundle.getSearch());
			asRegex = true;
		}
		catch(Exception e) {
		}
		for(FileItemInfo fi : files) {
			String newContent = new String();
			String filePath = fi.getAbsolutePath() + "/" + fi.getName();
			if (logger.isDebugEnabled()) {
				logger.debug("scanning in '" + filePath + "'");
			}
			FileInputStream fis = new FileInputStream(new File(filePath));
			Scanner scanner = new Scanner(fis);
			int lineNumber = 0;
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (asRegex) {
					line = line.replaceAll(bundle.getSearch(), bundle.getReplace());
				}
				else {
					int pos = 0;
					String p = line;
					do {
						pos = p.indexOf(bundle.getSearch());
						if (pos != -1) {
							if (logger.isDebugEnabled()) {
								logger.debug("The '" + bundle.getSearch() + "' found in line '" + lineNumber + "'; position '" + pos + "'");
							}
							line = line.replace(bundle.getSearch(), bundle.getReplace());
							p = line.substring(pos + bundle.getReplace().length());
						}
					} while(pos != -1);
				}
				newContent += line;
				newContent += "\r\n";
				lineNumber++;
			}
			scanner.close();
			fi.setContent(newContent);
			postReplaceResult(r, userName, fi);
		}
	}

	private void postSearchResult(String userName, int pos, int lineNumber, String search, String replace, String projectName, FileItemInfo fi) {
		SearchAndReplaceResult sr = new SearchAndReplaceResult(userName,
				Integer.valueOf(pos),
				Integer.valueOf(lineNumber + 1),
				bundle.getSearch(),
				bundle.getReplace(),
				bundle.getProject().getProjectName(),
				fi);
		ServiceUtils.pushDataToTracer(userName, new TracerSearchAndReplaceMessage(sr));
	}
	
	private CommandProcessorResult postReplaceResult(CommandProcessorResult r, String userName, FileItemInfo fileToSave) {
		DirBrowserImpl browser = (DirBrowserImpl)ServiceLocator.instance().locate(DirBrowserImpl.ID);
		if (browser == null) {
			r.setRetCode(CommandProcessorResult.GENERAL_FAIL);
			r.setResult("Remote browser service wasn't found");
			return r;
		}
		SearchAndReplaceResult sr = new SearchAndReplaceResult(userName,
				-1,
				-1,
				bundle.getSearch(),
				bundle.getReplace(),
				bundle.getProject().getProjectName(),
				fileToSave);
		sr.setFileAsReplaced(true);
		ServiceUtils.pushDataToTracer(userName, new TracerSearchAndReplaceMessage(sr));
		return r;
	}
}
