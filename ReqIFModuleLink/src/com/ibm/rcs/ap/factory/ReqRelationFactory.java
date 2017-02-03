package com.ibm.rcs.ap.factory;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.rcs.ap.beans.ReqRelation;
import com.ibm.rcs.ap.beans.Requirement;

public class ReqRelationFactory {

	Document doc;
	ArrayList<ReqRelation> reqLinks = new ArrayList<ReqRelation>();
	ArrayList<Requirement> requirements = new ArrayList<Requirement>();
	
	public ReqRelationFactory (Document doc, ArrayList<Requirement> requirements) {
		this.doc = doc;
		this.requirements = requirements;
		getReqRelations();
	}
	
	private void getReqRelations(){
		
		doc.getDocumentElement().normalize();
		//String reqRef = null;
		//String reqID = null;
		
		System.out.println("Checking requirement artifact linking.................");
		NodeList nlist = doc.getElementsByTagName("SPEC-RELATION");
		
		
		System.out.println("----------------------");

		
		for (int i = 0; i < nlist.getLength(); i++) {
			
			Node nNode = nlist.item(i);
			//System.out.println("\nCurrent element: " + nNode.getNodeName());
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element relation = (Element) nNode;
				String lastChange =  relation.getAttribute("LAST-CHANGE");
				String identifier =  relation.getAttribute("IDENTIFIER");
				
				NodeList source = relation.getElementsByTagName("SOURCE");
				NodeList target = relation.getElementsByTagName("TARGET");
				NodeList type = relation.getElementsByTagName("TYPE");
				
				Element sourceRef = (Element) source.item(0);
				Element targetRef = (Element) target.item(0);
				Element typeRef = (Element) type.item(0);
				
				String sourceRefCoreID = sourceRef.getElementsByTagName("SPEC-OBJECT-REF").item(0).getTextContent();
				String targetRefCoreID = targetRef.getElementsByTagName("SPEC-OBJECT-REF").item(0).getTextContent();
				String typeRefID = typeRef.getElementsByTagName("SPEC-RELATION-TYPE-REF").item(0).getTextContent();
				System.out.println("Source: " + sourceRefCoreID);
				System.out.println("Target: " + targetRefCoreID);
				ReqRelation reqRelation = new ReqRelation();
				reqRelation.setSourceCoreReqID(sourceRefCoreID);
				reqRelation.setTargetCoreReqID(targetRefCoreID);
				reqRelation.setLastChange(lastChange);
				reqRelation.setIdentifier(identifier);
				reqRelation.setType(typeRefID);
				//reqRelation.setSourceReqID(reqRef);
				reqLinks.add(reqRelation);
			}
		}
		
		addReqRefIntoReqRelation();
	}
	
	private void addReqRefIntoReqRelation(){
		
		for (int i = 0; i < requirements.size(); i++) {
			String reqCoreRef = requirements.get(i).getCoreRef();
			String reqRef = requirements.get(i).getRef();
			for (int j = 0; j < reqLinks.size(); j++) {
				String reqLinkSourceCoreID = reqLinks.get(j).getSourceCoreReqID();
				if (reqLinkSourceCoreID.equals(reqCoreRef)){
					reqLinks.get(j).setSourceReqID(reqRef);
				}
			}
		}
	}

	public ArrayList<ReqRelation> getReqLinks() {
		return reqLinks;
	}

	public void setReqLinks(ArrayList<ReqRelation> reqLinks) {
		this.reqLinks = reqLinks;
	}
}
