package com.vw.ide.server.servlet.fringes.persistance.dao;

import java.net.MalformedURLException;
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




public class CategoriesDAOXMLimpl implements CategoriesDAO{

	private ServletContext context;
	private String fringestoreFileName = "fringestore.xml";
	private URL fullPath2FringeStore = null;
	private DOMParser parser;
	

	@Override
	public void setContext(ServletContext context) {
		this.context = context;
	}

	
	
	@Override
	public Category create(Integer id, String name, String icon, String description) throws FringeDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category delete(Integer id) throws FringeDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category update(Integer id, Category Category) throws FringeDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category findById(Integer Id) throws FringeDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category[] findByName(String name) throws FringeDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public List<Category> getAll(){
		List<Category> categories = new ArrayList<>();
		String id;
    	String name;
    	String icon;
    	String description;		
		try {
			
			fullPath2FringeStore = context.getResource("/WEB-INF/classes/" + fringestoreFileName);
		    parser = new DOMParser();
		    parser.parse(fullPath2FringeStore.toString());
		    Document doc = parser.getDocument();
		    NodeList root = doc.getChildNodes();
		    Node storeNode = XmlUtils.getNode("store", root);
		    Node categoriesNode = XmlUtils.getNode("categories", storeNode.getChildNodes());
    		
		    for(int i = 0; i < categoriesNode.getChildNodes().getLength(); i++) {
		    	Node categoryNode = categoriesNode.getChildNodes().item(i);
		    	
		    	id = XmlUtils.getNodeAttr("id", categoryNode);
		    	name = XmlUtils.getNodeAttr("name", categoryNode);
		    	icon = XmlUtils.getNodeAttr("icon", categoryNode);
		    	description = XmlUtils.getNodeAttr("description", categoryNode);

		    	if (checkParams(id,name)) {
		    		categories.add(new Category(Integer.parseInt(id), name, icon, description));
		    	}
		    }	
		}
		catch ( Exception e ) {
		    e.printStackTrace();
		}
		
		return categories;
	}		

	private boolean checkParams(String id,String name) {
		boolean res =  (id.length() > 0)&&(name.length() > 0);
		if (res) {
			try { 
				Integer.parseInt(id);
				res = true;
			} catch (Exception e2) {
				res =  false;
			} 
		}
		return res;
	}
	
}
