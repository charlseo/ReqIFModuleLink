package com.rcs.ap;

import java.util.ArrayList;

import org.w3c.dom.Document;

public class ReqIFModuleLink {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.print("Please specify the ReqIF file path: ");
		
		String filePath = System.console().readLine();
		
		Document doc = ReqIFParser.getInstance().readXML(filePath);
		
		ReqFactory reqFactory = new ReqFactory(doc);
		
		ArrayList<Requirement> reqs= reqFactory.getRequirements();
		
		for (int i = 0; i < reqs.size(); i++) {
			System.out.println(reqs.get(i).getReqID());
		}
		
		LinkFactory linkFactory = new LinkFactory(doc, reqs);
		
		ArrayList<ReqRelation> reqRelations = new ArrayList<ReqRelation>();
		
		reqRelations = linkFactory.getReqLinks();
		
		ReqIFModuleLinkBuilder linkBuilder = new ReqIFModuleLinkBuilder(reqs, reqRelations);
		
		linkBuilder.createXMLMaps();
	}

}
