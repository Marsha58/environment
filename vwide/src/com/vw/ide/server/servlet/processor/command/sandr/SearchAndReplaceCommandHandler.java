package com.vw.ide.shared.servlet.processor.command.sandr;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.vw.ide.server.servlet.ServiceUtils;
import com.vw.ide.shared.servlet.processor.CommandProcessorResult;
import com.vw.ide.shared.servlet.processor.dto.sandr.SearchAndReplaceBundle;
import com.vw.ide.shared.servlet.projectmanager.ProjectDescription;
import com.vw.ide.shared.servlet.remotebrowser.FileItemInfo;

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
		List<FileItemInfo> files = project.getProjectFiles();
		if (files == null) {
			throw new Exception("invalid project description; files must be set to non-null value");
		}
		if (bundle.getReplace() == null || bundle.getReplace().length() == 0) {
			handleSearch(r, project.getUserName(), files);
		}
		return r;
	}
	
	protected void handleSearch(CommandProcessorResult r, String userName, List<FileItemInfo> files) throws Exception {
		boolean asRegex = false;
		Pattern spattern = null;
		try {
			spattern = Pattern.compile(bundle.getReplace());
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

	private void postSearchResult(String userName, int pos, int lineNumber, String search, String replace, String projectName, FileItemInfo fi) {
		SearchAndReplaceResult sr = new SearchAndReplaceResult(userName,
				Integer.valueOf(pos),
				Integer.valueOf(lineNumber + 1),
				bundle.getSearch(),
				bundle.getReplace(),
				bundle.getProject().getProjectName(),
				fi);
		ServiceUtils.pushDataToTracer(userName, new SearchAndReplaceMessage(sr));
	}
}
