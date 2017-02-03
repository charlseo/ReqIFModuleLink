package com.ibm.rcs.ap.factory;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.rcs.ap.beans.Requirement;

public class ReqFactory {
	private Document doc;
	private ArrayList<Requirement> requirements = new ArrayList<Requirement>();
	
	public ReqFactory (Document doc){
		this.doc = doc;
		this.getReqRef();
	}
	
	private void getReqRef(){
		
		doc.getDocumentElement().normalize();
		String reqRef = null;
		String reqID = null;
		
		//System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
		NodeList nlist = doc.getElementsByTagName("SPEC-OBJECT");
		
		System.out.println("Reading requirement artifacts .........");
		
		for (int i = 0; i < nlist.getLength(); i++) {
			
			Node nNode = nlist.item(i);
			System.out.println("\nCurrent element: " + nNode.getNodeName());
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Requirement req = new Requirement();
				Element eElement = (Element) nNode;
				reqRef = eElement.getAttribute("IDENTIFIER");
				System.out.println("REF: " + reqRef);
				req.setRef(reqRef);
				
				reqID = eElement.getElementsByTagName("ATTRIBUTE-VALUE-INTEGER").item(0).getAttributes().getNamedItem("THE-VALUE").getTextContent();
				System.out.println("ReqID: " + reqID);
				req.setReqID(reqID);
				getReqCoreRef(doc,reqRef, req);
			}
		}
		
		
	}

	private void getReqCoreRef(Document doc, String reqRef, Requirement req){
		
		System.out.println("Collecting artifact IDs........");
		NodeList nlist = doc.getElementsByTagName("rm:SPEC-OBJECT-EXTENSION");
		
		System.out.println("----------------------");
		
		for (int i = 0; i < nlist.getLength(); i++) {
			
			Node nNode = nlist.item(i);
			String coreRef = null;
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String ReqIFUUID = eElement.getElementsByTagName("SPEC-OBJECT-REF").item(0).getTextContent();
				if (reqRef.equals(ReqIFUUID)) {
					System.out.println("\nCurrent element: " + nNode.getNodeName());
					try {
						coreRef = eElement.getElementsByTagName("rm:CORE-SPEC-OBJECT-REF").item(0).getTextContent();
					} catch (Exception e) {
						System.out.println(e.getStackTrace());
						coreRef = eElement.getElementsByTagName("rm:WRAPPED-RESOURCE-REF").item(0).getTextContent();
					}
					
					
					if (coreRef != null) {
						System.out.println("Core-Ref: " + coreRef);
						req.setCoreRef(coreRef);
						requirements.add(req);
					}
					
				}
				
			}
		}
	}
	
	
	public ArrayList<Requirement> getRequirements() {
		return requirements;
	}

	public void setRequirements(ArrayList<Requirement> requirements) {
		this.requirements = requirements;
	}

}
