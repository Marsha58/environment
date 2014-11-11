package com.vw.ide.server.servlet.fringes.persistance.dao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.vw.ide.client.utils.Utils;
import com.vw.ide.server.servlet.utils.XmlUtils;

public class XMLConnection {

	private static XMLConnection instance = new XMLConnection();
	private DOMParser parser = null;
	public DOMParser getParser() {
		return parser;
	}

	public void setParser(DOMParser parser) {
		this.parser = parser;
	}

	private String fringeStoreDir = null;
	private String fringeStoreFileName = null;
	private String fringeStoreFullFileName = null;	
	

	private XMLConnection() {
	}
	
	public static XMLConnection getInstance() {
		return instance;
	}
	
	public void loadFringeStoreProperties(ServletContext context) {

		Properties prop = new Properties();
		try {
			InputStream isPropertiesFile = context.getResourceAsStream("/WEB-INF/classes/config.properties");
			if (isPropertiesFile != null) {
				prop.load(isPropertiesFile);
				if (prop.getProperty("fringes_base_xml_dir") != null) {
					fringeStoreDir = prop.getProperty("fringes_base_xml_dir");
				} else {
					fringeStoreDir = "/var/fringes";
				}
				if (prop.getProperty("fringestore_xml_file_name") != null) {
					fringeStoreFileName = prop.getProperty("fringestore_xml_file_name");
				} else {
					fringeStoreFileName = "fringstore.xml";
				}
				fringeStoreFullFileName = fringeStoreDir + Utils.FILE_SEPARATOR +  fringeStoreFileName;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}		

	
	public void openStore(ServletContext context) throws SAXException, IOException {
		if(fringeStoreDir == null || fringeStoreFileName == null) {
			loadFringeStoreProperties(context);
		}
		parser  = new DOMParser();
		parser.parse(fringeStoreFullFileName);
	}	
	

	public void saveStore(Document doc) throws IOException, TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(fringeStoreFullFileName));
		transformer.transform(source, result);
	}
	
	public Document getDocument() {
		return parser.getDocument();
	}	
	
	public Node getNodeByItemType(FringeStoreItemTypes item_type) {
		NodeList root = parser.getDocument().getChildNodes();
		if (root != null) {
			Node storeNode = XmlUtils.getNode("store", root);
			if (storeNode != null) {
				Node itemNode = XmlUtils.getNode(item_type.getName(), storeNode.getChildNodes());
				return itemNode;
			}
			
		}
		return null;
	}	
		
	
}
