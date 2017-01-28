package com.rcs.ap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class ReqIFParser {
	
	private static ReqIFParser reqIFParser;
	private static Object classLock = ReqIFParser.class;
	
	private ReqIFParser(){
		
	}
	
	public static ReqIFParser getInstance() {
		
		if (reqIFParser == null) {
			synchronized(ReqIFParser.class){
				if (reqIFParser == null) {
					System.out.println("First time instantiating getInstace()");
					reqIFParser = new ReqIFParser();
					
				}
				
			}
		}
		
		return reqIFParser;
	}

	public void readXML(){
		try{
			File xmlFile = new File("/Users/charlieseo/Documents/IBM/PMR/AU/RDNG/Downer/Test02/Requirements.reqif");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbFactory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			
			// optional but recommended, http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			
			doc.getDocumentElement().normalize();
		
			
			this.getReqRef(doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void getReqRef(Document doc){
		
		doc.getDocumentElement().normalize();
		String reqRef = null;
		
		System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
		NodeList nlist = doc.getElementsByTagName("SPEC-OBJECT");
		
		System.out.println("----------------------");
		
		for (int i = 0; i < nlist.getLength(); i++) {
			
			Node nNode = nlist.item(i);
			System.out.println("\nCurrent element: " + nNode.getNodeName());
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				reqRef = eElement.getAttribute("IDENTIFIER");
				System.out.println("REF: " + reqRef);
				
				System.out.println("ReqID: " + eElement.getElementsByTagName("ATTRIBUTE-VALUE-INTEGER").item(0).getAttributes().getNamedItem("THE-VALUE").getTextContent());
				
				getReqCoreRef(doc,reqRef);
			}
		}
		
		
	}
	
	private void getReqCoreRef(Document doc, String reqRef){
		
		doc.getDocumentElement().normalize();
		
		System.out.println("Object mapping: " + doc.getDocumentElement().getNodeName());
		NodeList nlist = doc.getElementsByTagName("rm:SPEC-OBJECT-EXTENSION");
		
		System.out.println("----------------------");
		
		for (int i = 0; i < nlist.getLength(); i++) {
			
			Node nNode = nlist.item(i);
			
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String ReqIFUUID = eElement.getElementsByTagName("SPEC-OBJECT-REF").item(0).getTextContent();
				if (reqRef.equals(ReqIFUUID)) {
					System.out.println("\nCurrent element: " + nNode.getNodeName());
					System.out.println("Core-Ref: " + eElement.getElementsByTagName("rm:CORE-SPEC-OBJECT-REF").item(0).getTextContent());
				}
				
			}
		}
	}
}
