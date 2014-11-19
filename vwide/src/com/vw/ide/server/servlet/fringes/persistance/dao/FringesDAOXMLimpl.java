package com.vw.ide.server.servlet.fringes.persistance.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.vw.ide.server.servlet.utils.XmlUtils;
import com.vw.ide.shared.servlet.fringes.model.Fringe;

public class FringesDAOXMLimpl implements ItemDAO<Fringe> {

	private ServletContext context;

	

	@Override
	public void setContext(ServletContext context) {
		this.context = context;
	}

	@Override
	public void add(Fringe fringe) {
		try {
			XMLConnection.getInstance().openStore(context);
			Document doc = XMLConnection.getInstance().getDocument();
		    Node fringesNode = XMLConnection.getInstance().getNodeByItemType(FringeStoreItemTypes.FRINGES);

		    Boolean isFringeExists = false;
			for (int i = 0; i < fringesNode.getChildNodes().getLength(); i++) {
				Node curFringeNode = fringesNode.getChildNodes().item(i);
				if (XmlUtils.getNodeAttr("id", curFringeNode).equalsIgnoreCase(fringe.getId().toString())) {
					isFringeExists = true;
					break;
				}
			}
			if (!isFringeExists) {
				Element newFringeElement = doc.createElement("fringe");
				newFringeElement.setAttribute("id", fringe.getId().toString());
				newFringeElement.setAttribute("name", fringe.getName());
				newFringeElement.setAttribute("path", fringe.getPath());
				newFringeElement.setAttribute("filename", fringe.getFilename());
				newFringeElement.setAttribute("classname", fringe.getClassname());
				newFringeElement.setAttribute("loaded", fringe.getLoaded().toString());
				newFringeElement.setAttribute("category_id", fringe.getCategoryId().toString());
				newFringeElement.setAttribute("description", fringe.getDescription());
				fringesNode.appendChild(newFringeElement);		
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
	public void update(Fringe fringe) {
		try {
			XMLConnection.getInstance().openStore(context);
			Document doc = XMLConnection.getInstance().getDocument();
		    Node fringesNode = XMLConnection.getInstance().getNodeByItemType(FringeStoreItemTypes.FRINGES);

			for (int i = 0; i < fringesNode.getChildNodes().getLength(); i++) {
				if (XmlUtils.getNodeAttr("id", fringesNode.getChildNodes().item(i)).equalsIgnoreCase(fringe.getId().toString())) {
					XmlUtils.setNodeAttr("name", fringesNode.getChildNodes().item(i), fringe.getName());
					XmlUtils.setNodeAttr("path", fringesNode.getChildNodes().item(i), fringe.getPath());
					XmlUtils.setNodeAttr("filename", fringesNode.getChildNodes().item(i), fringe.getFilename());
					XmlUtils.setNodeAttr("classname", fringesNode.getChildNodes().item(i), fringe.getClassname());
					XmlUtils.setNodeAttr("loaded", fringesNode.getChildNodes().item(i), fringe.getLoaded().toString());
					XmlUtils.setNodeAttr("category_id", fringesNode.getChildNodes().item(i), fringe.getCategoryId().toString());
					XmlUtils.setNodeAttr("description", fringesNode.getChildNodes().item(i), fringe.getDescription());
					
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
		    Node fringesNode = XMLConnection.getInstance().getNodeByItemType(FringeStoreItemTypes.FRINGES);

			for (int i = 0; i < fringesNode.getChildNodes().getLength(); i++) {
				if (XmlUtils.getNodeAttr("id", fringesNode.getChildNodes().item(i)).equalsIgnoreCase(id.toString())) {
					fringesNode.removeChild(fringesNode.getChildNodes().item(i));
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
	public Fringe findById(Integer fringeId)  {
		Fringe fringe = new Fringe();
		try {
			XMLConnection.getInstance().openStore(context);
		    Node fringesNode = XMLConnection.getInstance().getNodeByItemType(FringeStoreItemTypes.FRINGES);

			for (int i = 0; i < fringesNode.getChildNodes().getLength(); i++) {
				if (XmlUtils.getNodeAttr("id", fringesNode.getChildNodes().item(i)).equalsIgnoreCase(fringeId.toString())) {
					fringe = convertNodeToFringe(fringesNode.getChildNodes().item(i)); 
					break;
				}
			}
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fringe;
	}

	private Fringe convertNodeToFringe(Node node) {
		Fringe fringe = new Fringe();
		if (XmlUtils.getNodeAttr("id", node) != null) {
			fringe.setId(Integer.parseInt(XmlUtils.getNodeAttr("id", node)));
		}
		if (XmlUtils.getNodeAttr("name", node)!= null) {
			fringe.setName(XmlUtils.getNodeAttr("name", node));
		}
		
		if (XmlUtils.getNodeAttr("path", node)!= null) {
			fringe.setPath(XmlUtils.getNodeAttr("path", node));
		}
		if (XmlUtils.getNodeAttr("filename", node)!= null) {
			fringe.setFilename(XmlUtils.getNodeAttr("filename", node));
		}
		if (XmlUtils.getNodeAttr("classname", node)!= null) {
			fringe.setFilename(XmlUtils.getNodeAttr("classname", node));
		}		
		if (XmlUtils.getNodeAttr("loaded", node)!= null) {
			if (XmlUtils.getNodeAttr("loaded", node).equalsIgnoreCase("true")) {
				fringe.setLoaded(true);
			} else {
				fringe.setLoaded(false);
			}
		}
		if (XmlUtils.getNodeAttr("category_id", node)!= null) {
			fringe.setCategoryId(Integer.parseInt(XmlUtils.getNodeAttr("category_id", node)));
		}
		if (XmlUtils.getNodeAttr("description", node)!= null) {
			fringe.setDescription(XmlUtils.getNodeAttr("description", node));
		}
		return fringe;
	}
	
	
	@Override
	public Fringe[] findByName(String name)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Fringe> getAll()  {
		List<Fringe> fringes = new ArrayList<>();
		String id;
		String name;
		String path;
		String filename;
		String classname;
		Boolean loaded;
		String categoryId;
		String description;
		try {
			XMLConnection xmlConnection = XMLConnection.getInstance();
			xmlConnection.openStore(context);
			Node fringesNode = xmlConnection.getNodeByItemType(FringeStoreItemTypes.FRINGES);
			if(fringesNode != null) {
				for (int i = 0; i < fringesNode.getChildNodes().getLength(); i++) {
					Node curFringeNode = fringesNode.getChildNodes().item(i);
					
					id = XmlUtils.getNodeAttr("id", curFringeNode);
					name = XmlUtils.getNodeAttr("name", curFringeNode);
					path = XmlUtils.getNodeAttr("path", curFringeNode);
					filename = XmlUtils.getNodeAttr("filename", curFringeNode);
					classname = XmlUtils.getNodeAttr("classname", curFringeNode);
					if (XmlUtils.getNodeAttr("loaded", curFringeNode).equalsIgnoreCase("true")) {
						loaded = true;
					} else {
						loaded = false;
					}
					categoryId = XmlUtils.getNodeAttr("category_id", curFringeNode);
					description = XmlUtils.getNodeAttr("description", curFringeNode);
					
					if (checkParams(id, name, categoryId)) {
						Fringe fringe = new Fringe(Integer.parseInt(id), name, path, filename, classname, loaded, Integer.parseInt(categoryId), description);
						fringes.add(fringe);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fringes;
	}

	private boolean checkParams(String id, String name, String categoryId) {
		boolean res = (id.length() > 0) && (name.length() > 0) && (categoryId.length() > 0);
		if (res) {
			try {
				Integer.parseInt(id);
				Integer.parseInt(categoryId);
				res = true;
			} catch (Exception e) {
				res = false;
			}
		}
		return res;
	}

	@Override
	public Map<Integer, Fringe> getAllMap() {
		Map<Integer, Fringe> map = new HashMap<>();
		for (Fringe fringe : getAll()) {
			map.put(fringe.getId(), fringe);
		}
		return map;
	}

}
