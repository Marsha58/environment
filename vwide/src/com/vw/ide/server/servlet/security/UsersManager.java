package com.vw.ide.server.servlet.security;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vw.ide.server.servlet.utils.XmlUtils;
import com.vw.ide.shared.servlet.security.UserInfo;


public class UsersManager {

	private static UsersManager instance = new UsersManager();
	
	private Map<String,UserInfo> usersList = null; 
	
	private static String s_defUsersXmlFileName = "users.xml";
	private static URL fullPath2Users;
	private DOMParser parser;
	
	
	
	private UsersManager() {
		usersList = new HashMap<String,UserInfo>();	
	}

	public static UsersManager getInstance() {
		return instance;
	}	
	
	
	
	public void openAndParseUsersXml(ServletContext context){
//  Such approach work only  in case when server side deployed not as zip-file
//		fullPath2Users = context.getRealPath("/WEB-INF/classes/" + s_defUsersXmlFileName);
		
		try {
			fullPath2Users = context.getResource("/WEB-INF/classes/" + s_defUsersXmlFileName);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
	
		try {
		    parser = new DOMParser();
		    parser.parse(fullPath2Users.toString());
		    Document doc = parser.getDocument();
		    NodeList root = doc.getChildNodes();
		    Node appl = XmlUtils.getNode("appl", root);
		    Node users = XmlUtils.getNode("users", appl.getChildNodes());
    		String userName;
    		String password;		    
		    for(int i = 0; i < users.getChildNodes().getLength(); i++) {
		    	Node user = users.getChildNodes().item(i);
	    		userName = "";
	    		password = "";		    
	    		userName  = XmlUtils.getNodeAttr("name", user);
	    		password  = XmlUtils.getNodeAttr("password", user);
		    	if(userName.length() != 0) {
		    		UserInfo newUser = new UserInfo(userName,password);
//	    			System.out.println("userName: " + userName);
//	    			System.out.println("password: " + password);
	    			
	    			Node roles = XmlUtils.getNode("roles", user.getChildNodes());
	    			String roleName = "";
	    			if(roles!=null) {
	    				for(int j = 0; j < roles.getChildNodes().getLength(); j++) {
	    					Node role = roles.getChildNodes().item(j);
	    					if(role !=null) {
	    						roleName = role.getTextContent();
	    						if(roleName.trim().length() != 0) {
	    							newUser.addRole(roleName);
//	    							System.out.println("roleName: " + roleName);
	    						}
	    					}
	    				}
	    			}
	    			usersList.put(userName, newUser);
		    	}
		    	
		    }
		}
		catch ( Exception e ) {
		    e.printStackTrace();
		}
		
	}	
	
	public Byte checkUserNameAndPassword(String userName, String password) {
		Byte res = -3;
		
		if(!usersList.containsKey(userName)) {
			res = -2; // user not found
		} else {
			UserInfo ui = usersList.get(userName);
			if (!ui.getPassword().equals(password)) {
				res = -1; // password not correct
			} else {
				res = 0;
			}
		}
		return res;
	}
	
}
