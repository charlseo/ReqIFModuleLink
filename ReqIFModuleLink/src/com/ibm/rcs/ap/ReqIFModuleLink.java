package com.ibm.rcs.ap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.w3c.dom.Document;

import com.ibm.rcs.ap.beans.ReqRelation;
import com.ibm.rcs.ap.beans.Requirement;
import com.ibm.rcs.ap.util.AddXMLNode;
import com.ibm.rcs.ap.util.ConsoleHelper;
import com.ibm.rcs.ap.util.Test;
import com.ibm.rcs.ap.util.Unzip;
import com.ibm.rcs.ap.util.ZipHandler;

public class ReqIFModuleLink {

	//private static String tempFolder = System.getProperty("java.io.tmpdir");
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		
		ConsoleHelper console = new ConsoleHelper();		
		String filePath = console.getReqIfzFile();		
		String reqifzFileName = console.getFileName();
		
//		Test test = new Test();
//		
//		String filePath = test.getReqIFzFile();
//		
//		String reqIFFile = test.getReqIFFile();
		
		
		
		ZipHandler zipHandler = new ZipHandler(filePath);
		
		zipHandler.unzipit();
		
		String reqIFFile = zipHandler.getReqifFilePath();
				
		Document doc = ReqIFParser.getInstance().readXML(reqIFFile);
		
		ReqFactory reqFactory = new ReqFactory(doc);
		
		ArrayList<Requirement> reqs= reqFactory.getRequirements();
		
		for (int i = 0; i < reqs.size(); i++) {
			System.out.println(reqs.get(i).getReqID());
		}
		
		ReqRelationFactory linkFactory = new ReqRelationFactory(doc, reqs);
		
		ArrayList<ReqRelation> reqRelations = new ArrayList<ReqRelation>();
		
		reqRelations = linkFactory.getReqLinks();
		
		ReqIFModuleLinkBuilder linkBuilder = new ReqIFModuleLinkBuilder(reqs, reqRelations, reqIFFile);
		
		linkBuilder.createXMLMaps();
		AddXMLNode addXmlNode = linkBuilder.getAddXmlNode();
		ReqModuleFactory reqModFactory = new ReqModuleFactory(doc, addXmlNode);
		addXmlNode.addNameSpace();
		reqModFactory.updateDOORSReqIFDefinition();
		//zipHandler.zipit();
		
		
		
		
	}
	

}
