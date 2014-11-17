package com.vw.ide.client.fringemanagment.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.apache.commons.lang3.ArrayUtils;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

public class FringeMetaParser {

	private static Logger logger = Logger.getLogger("");

	private String fullText = "";
	private String sectionLanguage = "";
	private int initialSectionLanguageLength = -1;
	private String sectionBeyond = "";
	private List<String> sectionFringes = new ArrayList<>();

	public String getFullText() {
		return fullText;
	}

	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

	public String getSectionLanguage() {
		return sectionLanguage;
	}

	public void setSectionLanguage(String sectionLanguage) {
		this.sectionLanguage = sectionLanguage;
	}

	public String getSectionBeyond() {
		return sectionBeyond;
	}

	public void setSectionBeyond(String sectionBeyond) {
		this.sectionBeyond = sectionBeyond;
	}

	public List<String> getSectionFringes() {
		return sectionFringes;
	}

	public void setSectionFringes(List<String> sectionFringes) {
		this.sectionFringes = sectionFringes;
	}

	public FringeMetaParser() {

	}

	public FringeMetaParser(String fullText) {
		this.fullText = fullText;
	}

	public Boolean catchLanguageSection(String out_pattern) {
		Boolean res = false;
		String pattern = "";
		if (out_pattern != null) {
			pattern = out_pattern;
		} else {
			pattern = "language[\\s]*=[\\s]*[\\s\\S]*{[a-zA-Z_0-9\\s()\".\\/\\/]*}[\\s]*}";
		}	
		RegExp regExp = RegExp.compile(pattern);
		MatchResult matcher = regExp.exec(fullText);
		boolean matchFound = matcher != null;
		if (matchFound) {
			sectionLanguage = matcher.getGroup(0);
			logger.log(Level.INFO, "sectionLanguage. matcher.getGroup(" + 0 + ")" + matcher.getGroup(0));
			res = true;
		}
		initialSectionLanguageLength = sectionLanguage.length();		
		return res;
	};

	public int getBeyondSectionInsertPlace() {
		int res = -1;
		if (!catchBeyondSection()) {
			for (int i = sectionLanguage.length() - 1; i > 0; i--) {
				if (sectionLanguage.charAt(i) == '}') {
					res = i - 1;
					break;
				}
			}
		}
		return res;
	}

	private Boolean catchBeyondSection() {
		Boolean res = false;
		String pattern = "beyond[\\s]*{[a-zA-Z_0-9\\s()\".\\/\\/]*}";
		RegExp regExp = RegExp.compile(pattern);
		MatchResult matcher = regExp.exec(sectionLanguage);
		boolean matchFound = matcher != null;
		if (matchFound) {
			sectionBeyond = matcher.getGroup(0);
			logger.log(Level.INFO, "sectionBeyond. matcher.getGroup(" + 0 + ")" + matcher.getGroup(0));
			res = true;
		}
		return res;
	};

	private String insertText(String text, int position, String insertingBlock) {
		String res = "";
		if (position < text.length()) {
			String textBegin = text.substring(0, position - 1);
			String textEnd = text.substring(position);
			res = textBegin + "\n\t\t" + insertingBlock + "\n" + textEnd;
		}
		if (res.length() == 0) {
			res = text;
		}
		return res;
	};

	public void makeBeyondSection() {
		int beyondSectionInsertPlace = getBeyondSectionInsertPlace();
		String beyondSection = "beyond {\n\t}";
		sectionLanguage = insertText(sectionLanguage, beyondSectionInsertPlace, beyondSection);
	};

	public Boolean catchFringesSection() {
		Boolean res = false;
		// String pattern =
		// "fringe[\\s]*[a-zA-Z_0-9]*[\\s]*([a-zA-Z_0-9\\s(\".\\/\\/]*) ";
		String pattern = "([\\s]*fringe[a-zA-Z_0-9\\s]*[(][a-zA-Z_0-9\\s\".\\/\\/]*[)][\\s]*)+";
		RegExp regExp = RegExp.compile(pattern);
		MatchResult matcher = regExp.exec(sectionBeyond);

		boolean matchFound = matcher != null;
		if (matchFound) {
			for (int i = 0; i < matcher.getGroupCount(); i++) {
				sectionFringes.add(matcher.getGroup(i));
				if (i == 0) {
					String[] sec = matcher.getGroup(i).split("[)]");

					for (int j = 0; j < sec.length; j++) {
						logger.log(Level.INFO, "sec" + j + ": " + sec[j]);
					}

				}
				logger.log(Level.INFO, "matcher.getGroup(" + i + ")" + matcher.getGroup(i));
			}
			res = true;
		}
		return res;
	};

	public int getFringeSectionInsertPlace() {
		int res = -1;
		int parenthesisCounter = 0;
		for (int i = sectionLanguage.length() - 1; i > 0; i--) {
			if (sectionLanguage.charAt(i) == '}') {
				if (parenthesisCounter == 0) {
					parenthesisCounter++;
				} else {
					res = i - 1;
					break;
				}
			}
		}

		return res;
	}

	private String makeFringeText(Fringe fringe) {
		String res = fringe.getClassname();
		return res;
	}

	private String replaceSectionLanguageInText() {
		String blockBegin = fullText.substring(0,fullText.indexOf("language"));
		String blockEnd = fullText.substring(fullText.indexOf("language") + initialSectionLanguageLength);
		fullText = blockBegin + sectionLanguage + blockEnd;
		return fullText;
	}

	public void insertFringe(Fringe fringe) {
		catchLanguageSection(null);
		
		if (!catchBeyondSection()) {
			catchLanguageSection("language[\\s]*=[a-zA-Z_0-9\\s]*\\{[a-zA-Z_0-9\\s\".:=\\\\\\/]*\\}");
			makeBeyondSection();
		}
		int fringeSectionInsertPlace = getFringeSectionInsertPlace();
		sectionLanguage = insertText(sectionLanguage, fringeSectionInsertPlace, makeFringeText(fringe));
		replaceSectionLanguageInText();

	}

}
