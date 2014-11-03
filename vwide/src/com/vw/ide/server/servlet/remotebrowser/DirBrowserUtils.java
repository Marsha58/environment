package com.vw.ide.server.servlet.remotebrowser;

public class DirBrowserUtils {
	
	public static String extractParentPath(String input) {
		String output = "";
		String delims = "[\\\\/]+";
		if (input != null) {
			String[] arrPath = input.split(delims);
			for(int i = 0; i < arrPath.length - 1; i++) {
				output += arrPath[i];
				if (i + 1 < arrPath.length - 1) {
					output += "/";
				}
			}
		}
		return output;
	}

}
