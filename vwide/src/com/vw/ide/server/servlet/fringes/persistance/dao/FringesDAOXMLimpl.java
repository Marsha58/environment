package com.vw.ide.server.servlet.fringes.persistance.dao;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vw.ide.server.servlet.utils.XmlUtils;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;


public class FringesDAOXMLimpl implements FringesDAO{

	private ServletContext context;
	private String fringestoreFileName = "fringestore.xml";
	private URL fullPath2FringeStore = null;
	private DOMParser parser;
	
	@Override
	public void setContext(ServletContext context) {
		this.context = context;
	}
	
	
	@Override
	public Fringe create(Integer id, String name, String path, String description, Integer categoryId) throws FringeDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Fringe delete(Integer id) throws FringeDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Fringe update(Integer id, Fringe fringe) throws FringeDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Fringe findById(Integer Id) throws FringeDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Fringe[] findByName(String name) throws FringeDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Fringe> getAll() throws FringeDAOException {
		List<Fringe> fringes = new ArrayList<>();
		String id;
    	String name;
    	String path;
    	Boolean loaded;
    	String categoryId;
    	String description;
		try {
			
			fullPath2FringeStore = context.getResource("/WEB-INF/classes/" + fringestoreFileName);
		    parser = new DOMParser();
		    parser.parse(fullPath2FringeStore.toString());
		    Document doc = parser.getDocument();
		    NodeList root = doc.getChildNodes();
		    Node storeNode = XmlUtils.getNode("store", root);
		    Node fringesNode = XmlUtils.getNode("fringes", storeNode.getChildNodes());
    		
		    for(int i = 0; i < fringesNode.getChildNodes().getLength(); i++) {
		    	Node curFringeNode = fringesNode.getChildNodes().item(i);
		    	
		    	id = XmlUtils.getNodeAttr("id", curFringeNode);
		    	name = XmlUtils.getNodeAttr("name", curFringeNode);
		    	path = XmlUtils.getNodeAttr("path", curFringeNode);
		    	if (XmlUtils.getNodeAttr("loaded", curFringeNode).equalsIgnoreCase("true")) {
		    		loaded = true;
		    	} else {
		    		loaded = false;
		    	}
		    	categoryId = XmlUtils.getNodeAttr("category_id", curFringeNode);
		    	description = XmlUtils.getNodeAttr("description", curFringeNode);

		    	if (checkParams(id,name,categoryId)) {
		    		fringes.add(new Fringe(Integer.parseInt(id), name, path, loaded, Integer.parseInt(categoryId), description));
		    	}
		    }	
		}
		catch ( Exception e ) {
		    e.printStackTrace();
		}
		
		return fringes;
	}

	private boolean checkParams(String id,String name, String categoryId) {
		boolean res =  (id.length() > 0)&&(name.length() > 0)&&(categoryId.length() > 0);
		if (res) {
			try { 
				Integer.parseInt(id);
				Integer.parseInt(categoryId);
				res = true;
			} catch (Exception e) {
				res =  false;
			} 
		}
		return res;
	}
	


}
