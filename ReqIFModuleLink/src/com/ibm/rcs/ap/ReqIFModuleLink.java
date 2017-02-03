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
import com.ibm.rcs.ap.factory.ReqFactory;
import com.ibm.rcs.ap.factory.ReqModuleFactory;
import com.ibm.rcs.ap.factory.ReqRelationFactory;
import com.ibm.rcs.ap.util.AddXMLNode;
import com.ibm.rcs.ap.util.ConsoleHelper;
import com.ibm.rcs.ap.util.Test;
import com.ibm.rcs.ap.util.Unzip;
import com.ibm.rcs.ap.util.ZipHandler;

public class ReqIFModuleLink {

	//private static String tempFolder = System.getProperty("java.io.tmpdir");
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		// Provide console interface to get ReqIFz as an input.
		ConsoleHelper console = new ConsoleHelper();		
		String filePath = console.getReqIfzFile();		
		//String reqifzFileName = console.getFileName();

		
		// Eclipse test class
//		Test test = new Test();
//		String filePath = test.getReqIFzFile();	
//		String reqIFFile = test.getReqIFFile();
		
		// Handling uncompression and compression by ZipHandler
		
		ZipHandler zipHandler = new ZipHandler(filePath);		
		zipHandler.unzipit();
		
		// Return reqif file
		String reqIFFile = zipHandler.getReqifFilePath();
				
		Document doc = ReqIFParser.getInstance().readXML(reqIFFile);
		
		// Collect requirement artifacts from reqif xmml
		ReqFactory reqFactory = new ReqFactory(doc);
		
		ArrayList<Requirement> reqs= reqFactory.getRequirements();
		
		// Collect link mapping information from reqif xml
		ReqRelationFactory linkFactory = new ReqRelationFactory(doc, reqs);		
		ArrayList<ReqRelation> reqRelations = new ArrayList<ReqRelation>();
		reqRelations = linkFactory.getReqLinks();
		
		// Building link mapping for Artifact Module linking 
		// Core artifact Ref mapping is used to create artifact module mapping with module artifact refs
		ReqIFModuleLinkBuilder linkBuilder = new ReqIFModuleLinkBuilder(reqs, reqRelations, reqIFFile);
		
		// Create XML link mapping and append into the existing reqif file. 
		linkBuilder.createXMLMaps();
		AddXMLNode addXmlNode = linkBuilder.getAddXmlNode();
		
		// Collect module ref ID 
		ReqModuleFactory reqModFactory = new ReqModuleFactory(doc, addXmlNode);
		
		// Adding xmlns:doors namespace for DOORS REQIF-DEFINITION
		addXmlNode.addNameSpace();
		
		// Appending DOORS REQIF-DEFINITION with MODULE LOCK
		reqModFactory.updateDOORSReqIFDefinition();
		
		// Compress all relevant files into reqifz as a final outcome. 
		zipHandler.zipit();
		
		
		
		
	}
	

}
