package com.vw.ide.server.servlet.fringes.persistance.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.vw.ide.server.servlet.utils.XmlUtils;
import com.vw.ide.shared.servlet.fringes.model.Category;
import com.vw.ide.shared.servlet.fringes.model.Fringe;




public class CategoriesDAOXMLimpl implements ItemDAO<Category>{

	private ServletContext context;

	
	
	@Override
	public void setContext(ServletContext context) {
		this.context = context;
	}

	
	
	@Override
	public void add(Category category)  {
		try {
			XMLConnection.getInstance().openStore(context);
			Document doc = XMLConnection.getInstance().getDocument();
		    Node categoriesNode = XMLConnection.getInstance().getNodeByItemType(FringeStoreItemTypes.CATEGORIES);

		    Boolean isCategoryExists = false;
			for (int i = 0; i < categoriesNode.getChildNodes().getLength(); i++) {
				Node curFringeNode = categoriesNode.getChildNodes().item(i);
				if (XmlUtils.getNodeAttr("id", curFringeNode).equalsIgnoreCase(category.getId().toString())) {
					isCategoryExists = true;
					break;
				}
			}
			if (!isCategoryExists) {
				Element newCategoryElement = doc.createElement("category");
				newCategoryElement.setAttribute("id", category.getId().toString());
				newCategoryElement.setAttribute("name", category.getName());
				newCategoryElement.setAttribute("icon", category.getIcon());
				newCategoryElement.setAttribute("description", category.getDescription());
				categoriesNode.appendChild(newCategoryElement);		
				try {
					XMLConnection.getInstance().saveStore(doc);
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update(Category category)  {
		try {
			XMLConnection.getInstance().openStore(context);
			Document doc = XMLConnection.getInstance().getDocument();
		    Node categoriesNode = XMLConnection.getInstance().getNodeByItemType(FringeStoreItemTypes.CATEGORIES);
			for (int i = 0; i < categoriesNode.getChildNodes().getLength(); i++) {
				if (XmlUtils.getNodeAttr("id", categoriesNode.getChildNodes().item(i)).equalsIgnoreCase(category.getId().toString())) {
					XmlUtils.setNodeAttr("name", categoriesNode.getChildNodes().item(i), category.getName());
					XmlUtils.setNodeAttr("icon", categoriesNode.getChildNodes().item(i), category.getIcon());
					XmlUtils.setNodeAttr("description", categoriesNode.getChildNodes().item(i), category.getDescription());
					try {
						XMLConnection.getInstance().saveStore(doc);
					} catch (TransformerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Integer id)  {
		try {
			XMLConnection.getInstance().openStore(context);
			Document doc = XMLConnection.getInstance().getDocument();
		    Node categoriesNode = XMLConnection.getInstance().getNodeByItemType(FringeStoreItemTypes.CATEGORIES);

			for (int i = 0; i < categoriesNode.getChildNodes().getLength(); i++) {
				if (XmlUtils.getNodeAttr("id", categoriesNode.getChildNodes().item(i)).equalsIgnoreCase(id.toString())) {
					categoriesNode.removeChild(categoriesNode.getChildNodes().item(i));
					try {
						XMLConnection.getInstance().saveStore(doc);
					} catch (TransformerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
					break;
				}
			}
	
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public Category findById(Integer id)  {
		Category category = new Category();
		try {
			XMLConnection.getInstance().openStore(context);
		    Node categoriesNode = XMLConnection.getInstance().getNodeByItemType(FringeStoreItemTypes.FRINGES);

			for (int i = 0; i < categoriesNode.getChildNodes().getLength(); i++) {
				if (XmlUtils.getNodeAttr("id", categoriesNode.getChildNodes().item(i)).equalsIgnoreCase(id.toString())) {
					category = convertNodeToCategory(categoriesNode.getChildNodes().item(i)); 
					break;
				}
			}
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return category;
	}

	@Override
	public Category[] findByName(String name)  {
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
			XMLConnection xmlConnection = XMLConnection.getInstance();
			xmlConnection.openStore(context);
		    Node categoriesNode = xmlConnection.getNodeByItemType(FringeStoreItemTypes.CATEGORIES);
			if(categoriesNode != null) {
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
	
	
	private Category convertNodeToCategory(Node node) {
		Category category = new Category();
		if (XmlUtils.getNodeAttr("id", node) != null) {
			category.setId(Integer.parseInt(XmlUtils.getNodeAttr("id", node)));
		}
		if (XmlUtils.getNodeAttr("name", node)!= null) {
			category.setName(XmlUtils.getNodeAttr("name", node));
		}
		if (XmlUtils.getNodeAttr("icon", node)!= null) {
			category.setIcon(XmlUtils.getNodeAttr("icon", node));
		}
		if (XmlUtils.getNodeAttr("description", node)!= null) {
			category.setDescription(XmlUtils.getNodeAttr("description", node));
		}
		return category;
	}



	@Override
	public Map<Integer, Category> getAllMap() {
		Map<Integer, Category> map = new HashMap<>();
		for (Category category : getAll()) {
			map.put(category.getId(), category);
		}
		return map;

	}



	@Override
	public String getHash() {
		String res = "";
		try {
			res = XMLConnection.getInstance().calcFileHash(context);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}










	
}
