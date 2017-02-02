package com.rcs.ap;

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

public class ReqIFModuleLink {

	private static String tempFolder = System.getProperty("java.io.tmpdir");
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
//		boolean isZipFile = false;
//		String filePath = null;
//		while (!isZipFile) {
//			System.out.print("-----Please specify the ReqIF file (*.reqifz) path: ");
//			filePath = System.console().readLine();
//			File file = new File(filePath);
//	        
//	        try {
//				RandomAccessFile raf = new RandomAccessFile(file, "r");
//				System.out.println("test");
//				long n = raf.readInt();
//				if (n == 0x504B0304){
//					isZipFile = true;
//				} else {
//					System.out.println("It seems the file is not an archive formate. Please check and enter a correct file path: ");
//				}
//				
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		String workingFolder = filePath.
//		
		//String filePath = "/Users/charlieseo/Documents/IBM/PMR/AU/RDNG/Downer/Sydney_demo/Requirements.reqif";
		//String reqIFFile = unZip(filePath);
		//System.out.println(reqIFFile);
		
		System.out.print("-----Please specify the ReqIF file path: ");
		String filePath = System.console().readLine();
		Document doc = ReqIFParser.getInstance().readXML(filePath);
		
		ReqFactory reqFactory = new ReqFactory(doc);
		
		ArrayList<Requirement> reqs= reqFactory.getRequirements();
		
		for (int i = 0; i < reqs.size(); i++) {
			System.out.println(reqs.get(i).getReqID());
		}
		
		ReqRelationFactory linkFactory = new ReqRelationFactory(doc, reqs);
		
		ArrayList<ReqRelation> reqRelations = new ArrayList<ReqRelation>();
		
		reqRelations = linkFactory.getReqLinks();
		
		ReqIFModuleLinkBuilder linkBuilder = new ReqIFModuleLinkBuilder(reqs, reqRelations);
		
		linkBuilder.createXMLMaps();
	}
	

}
