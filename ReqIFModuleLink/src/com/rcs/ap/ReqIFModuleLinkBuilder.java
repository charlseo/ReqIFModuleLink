package com.rcs.ap;

import java.util.ArrayList;

public class ReqIFModuleLinkBuilder {
	
	private ArrayList<Requirement> reqs = new ArrayList<Requirement>();
	private ArrayList<ReqRelation> relations = new ArrayList<ReqRelation>();
	String specRelationXML;
	private String SPECRELATIONS = "<SPEC-RELATION LAST-CHANGE=\"";
	
	public ReqIFModuleLinkBuilder(ArrayList<Requirement> reqs, ArrayList<ReqRelation> relations){
		this.reqs = reqs;
		this.relations = relations;
	}
	
	private void getReqLinkMappings() {
		
		for (int i=0; i < relations.size(); i++) {
			
			for (int j=0; j < reqs.size(); j++){
				String reqRef = reqs.get(j).getRef();
				String reqCoreRef = reqs.get(j).getCoreRef();
				if (relations.get(i).getTargetCoreReqID().equals(reqCoreRef)){
					relations.get(i).setTargetReqID(reqRef);
				}
			}
				
			
		}
	}
	
	public String createXMLMaps(){
		getReqLinkMappings();
		StringBuilder sbOutput  = new StringBuilder();
		
		for (int i=0; i < relations.size(); i++) {
			System.out.println(relations.get(i).getSourceReqID());
			System.out.println(relations.get(i).getTargetReqID());
			
			sbOutput.append("<SPEC-RELATION LAST-CHANGE=\"" + relations.get(i).getLastChange() + "\" IDENTIFIER=\"" +
					relations.get(i).getIdentifier() + "\">\n");
			sbOutput.append("<SOURCE>\n" + "<SPEC-OBJECT-REF>" + relations.get(i).getSourceReqID() + "</SPEC-OBJECT-REF>\n" +
					"</SOURCE>\n");
			sbOutput.append("<TARGET>\n" + "<SPEC-OBJECT-REF>" + relations.get(i).getTargetReqID() + "</SPEC-OBJECT-REF>\n" +
					"</TARGET>\n");
			sbOutput.append("<TYPE>\n" + "<SPEC-RELATION-TYPE-REF>" + relations.get(i).getType() + "</SPEC-RELATION-TYPE-REF>\n" +
					"</TYPE>\n");
			sbOutput.append("</SPEC-RELATION>\n");
		}
		
		String finalString = sbOutput.toString();
		
		System.out.println(finalString);
		return specRelationXML;
	}
}
