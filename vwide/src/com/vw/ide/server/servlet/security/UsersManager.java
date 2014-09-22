package com.vw.ide.server.servlet.security;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vw.ide.shared.servlet.security.UserInfo;


public class UsersManager {

	private static UsersManager instance = new UsersManager();
	
	private Map<String,UserInfo> usersList = null; 
	
	private static String s_defUsersXmlFileName = "users.xml";
	private static String fullPath2Users;
	private DOMParser parser;
	
	
	
	private UsersManager() {
		usersList = new HashMap<String,UserInfo>();	
	}

	public static UsersManager getInstance() {
		return instance;
	}	
	
	
	protected Node getNode(String tagName, NodeList nodes) {
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            return node;
	        }
	    }
	 
	    return null;
	}
	 
	protected String getNodeValue( Node node ) {
	    NodeList childNodes = node.getChildNodes();
	    for (int x = 0; x < childNodes.getLength(); x++ ) {
	        Node data = childNodes.item(x);
	        if ( data.getNodeType() == Node.TEXT_NODE )
	            return data.getNodeValue();
	    }
	    return "";
	}
	 
	protected String getNodeValue(String tagName, NodeList nodes ) {
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            NodeList childNodes = node.getChildNodes();
	            for (int y = 0; y < childNodes.getLength(); y++ ) {
	                Node data = childNodes.item(y);
	                if ( data.getNodeType() == Node.TEXT_NODE )
	                    return data.getNodeValue();
	            }
	        }
	    }
	    return "";
	}
	 
	protected String getNodeAttr(String attrName, Node node ) {
	    NamedNodeMap attrs = node.getAttributes();
	    if (attrs != null) {
	    	for (int y = 0; y < attrs.getLength(); y++ ) {
	    		Node attr = attrs.item(y);
	    		if (attr.getNodeName().equalsIgnoreCase(attrName)) {
	    			return attr.getNodeValue();
	    		}
	    	}
	    }
	    return "";
	}
	
	protected String getNodeAttr(String tagName, String attrName, NodeList nodes ) {
	    for ( int x = 0; x < nodes.getLength(); x++ ) {
	        Node node = nodes.item(x);
	        if (node.getNodeName().equalsIgnoreCase(tagName)) {
	            NodeList childNodes = node.getChildNodes();
	            for (int y = 0; y < childNodes.getLength(); y++ ) {
	                Node data = childNodes.item(y);
	                if ( data.getNodeType() == Node.ATTRIBUTE_NODE ) {
	                    if ( data.getNodeName().equalsIgnoreCase(attrName) )
	                        return data.getNodeValue();
	                }
	            }
	        }
	    }
	 
	    return "";
	}
	
	
	public void openAndParseUsersXml(ServletContext context){
		fullPath2Users = context.getRealPath("/WEB-INF/classes/" + s_defUsersXmlFileName);
	
		try {
		    parser = new DOMParser();
		    parser.parse(fullPath2Users);
		    Document doc = parser.getDocument();
		    NodeList root = doc.getChildNodes();
		    Node appl = getNode("appl", root);
		    Node users = getNode("users", appl.getChildNodes());
    		String userName;
    		String password;		    
		    for(int i = 0; i < users.getChildNodes().getLength(); i++) {
		    	Node user = users.getChildNodes().item(i);
	    		userName = "";
	    		password = "";		    
	    		userName  = getNodeAttr("name", user);
	    		password  = getNodeAttr("password", user);
		    	if(userName.length() != 0) {
		    		UserInfo newUser = new UserInfo(userName,password);
//	    			System.out.println("userName: " + userName);
//	    			System.out.println("password: " + password);
	    			
	    			Node roles = getNode("roles", user.getChildNodes());
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
