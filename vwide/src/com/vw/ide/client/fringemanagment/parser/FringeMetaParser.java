package com.vw.ide.client.fringemanagment.parser;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

public class FringeMetaParser {

	private static Logger logger = Logger.getLogger("");

	private String fullText = "";
	private String sectionLanguage = "";
	private int initialSectionLanguageLength = -1;


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


	public FringeMetaParser() {

	}

	public FringeMetaParser(String fullText) {
		this.fullText = fullText;
	}

	public Boolean catchLanguageSection() {
		Boolean res = false;
		String pattern = "(language)[\\s]*=[a-zA-Z_0-9\\s]*(\\{){1}[a-zA-Z_0-9\\s\".:=\\-{()}\\\\\\/]*(})";
		RegExp regExp = RegExp.compile(pattern);
		MatchResult matcher = regExp.exec(fullText);
		boolean matchFound = matcher != null;
		if (matchFound) {
			sectionLanguage = matcher.getGroup(0);
			res = true;
		}
		logger.log(Level.INFO, "sectionLanguage : " + sectionLanguage);
		initialSectionLanguageLength = sectionLanguage.length();		
		return res;
	};



	private Boolean catchBeyondSection() {
		Boolean res = false;
		String pattern = "beyond[\\s]*{[a-zA-Z_0-9\\s()\\-\".\\/\\/]*}";
		RegExp regExp = RegExp.compile(pattern);
		MatchResult matcher = regExp.exec(sectionLanguage);
		res =  matcher != null;
		logger.log(Level.INFO, "catchBeyondSection() : " + res);
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
		logger.log(Level.INFO, "insertText : " + res);

		return res;
	};

	public void makeBeyondSection() {
		int beyondSectionInsertPlace = getBeyondSectionInsertPlace();
		String beyondSection = "beyond {\n\t}";
		if (beyondSectionInsertPlace != -1) {
			sectionLanguage = insertText(sectionLanguage, beyondSectionInsertPlace, beyondSection);
			logger.log(Level.INFO, "sectionLanguage : " + sectionLanguage);

		} else {
			logger.log(Level.SEVERE, "beyondSectionInsertPlace == -1");
		}
	};
	
	public int getBeyondSectionInsertPlace() {
		int res = -1;
		int parenthesisCount = 0;		
		int beforeThisParenthesis = 1;
		if (!catchBeyondSection()) {
			for (int i = sectionLanguage.length() - 1; i > 0; i--) {
				if (sectionLanguage.charAt(i) == '}') {
					parenthesisCount++;
					if(parenthesisCount == beforeThisParenthesis) {
						res = i - 1;
						break;
					}
				}
			}
		}
		return res;
	}		

	public Boolean catchFringesSection() {
		Boolean res = false;
		String pattern = "([\\s]*fringe[a-zA-Z_0-9\\s]*[(][a-zA-Z_0-9\\s\".\\/\\/]*[)][\\s]*)+";
		RegExp regExp = RegExp.compile(pattern);
		MatchResult matcher = regExp.exec(sectionLanguage);
		res =  matcher != null;
		logger.log(Level.INFO, "catchFringesSection() : " + res);
		
		return res;
	};



	private String makeFringeText(Fringe fringe) {
		String res = "\t\t"  + fringe.getName() + " ias \"" + fringe.getClassname() + "\"";
		return res;
	}

	private String replaceSectionLanguageInText() {
		String blockBegin = fullText.substring(0,fullText.indexOf("language"));
		String blockEnd = fullText.substring(fullText.indexOf("language") + initialSectionLanguageLength);
		fullText = blockBegin + sectionLanguage + blockEnd;
		return fullText;
	}

	
	public Boolean catchFringeGroupSection(String groupName) {
		Boolean res = false;
		String pattern = "((fringe){1}[\\s]*"+groupName+"[\\s]*(ias){1}[\\s]*[(][a-zA-Z_0-9\\s\".\\/\\/]*[)])+";
		RegExp regExp = RegExp.compile(pattern);
		MatchResult matcher = regExp.exec(sectionLanguage);
		res = matcher != null;
		logger.log(Level.INFO, "catchFringeGroupSection() : " + res);
		
		return res;
	};	
	
	public void insertFringe(Fringe fringe) {
		catchLanguageSection();
		
		if (!catchBeyondSection()) {
			makeBeyondSection();
		}
		
		if(!catchFringeGroupSection(fringe.getCategory().getName())){
			makeFringeGroupSection(fringe.getCategory().getName());
		}
		
		int fringeSectionInsertPlace = getFringeSectionInsertPlace(fringe.getCategory().getName());
		if (fringeSectionInsertPlace != -1) {		
			sectionLanguage = insertText(sectionLanguage, fringeSectionInsertPlace, makeFringeText(fringe));
			logger.log(Level.INFO, "sectionLanguage : " + sectionLanguage);
		} else {
			logger.log(Level.SEVERE, "fringeSectionInsertPlace == -1");
		}
		replaceSectionLanguageInText();

	}
	
	public void makeFringeGroupSection(String fringeGroupName) {
		int fringeGroupSectionInsertPlace = getFringeGroupSectionInsertPlace(fringeGroupName,2);
		String fringeSection = "\tfringe "+ fringeGroupName+" ias (\n\t\t\t)";
		if(fringeGroupSectionInsertPlace != -1) {
			sectionLanguage = insertText(sectionLanguage, fringeGroupSectionInsertPlace, fringeSection);
		}else {
			logger.log(Level.SEVERE, "fringeGroupSectionInsertPlace == -1");
		}
	};	
	
	

	
	public int getFringeGroupSectionInsertPlace(String fringeGroupName,int beforeThisParenthesis) {
		int res = -1;
		int parenthesisCount = 0;
		if (!catchFringeGroupSection(fringeGroupName)) {
			for (int i = sectionLanguage.length() - 1; i > 0; i--) {
				if (sectionLanguage.charAt(i) == '}') {
					parenthesisCount++;
					if(parenthesisCount == beforeThisParenthesis) {
						res = i - 1;
						break;
					}
					
				}
			}
		} else {
			
		}
		return res;
	}	
	
	public int getFringeSectionInsertPlace(String groupName) {
		int res = -1;
		String pattern = "((fringe){1}[\\s]*"+groupName+"[\\s]*(ias){1}[\\s]*[(])+";
		RegExp regExp = RegExp.compile(pattern);
		MatchResult matcher = regExp.exec(sectionLanguage);
		boolean matchFound = matcher != null;
		if (matchFound) {
			String matchedString =	matcher.getGroup(0);
			res = sectionLanguage.indexOf(matchedString) + matchedString.length() + 1;
		}
		return res;
	}	

}
