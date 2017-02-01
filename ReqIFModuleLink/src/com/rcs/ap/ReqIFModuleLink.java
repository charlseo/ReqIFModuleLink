package com.rcs.ap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.print("Please specify the ReqIF file path: ");
		
		String filePath = System.console().readLine();
		//String filePath = "/Users/charlieseo/Documents/IBM/PMR/AU/RDNG/Downer/DOWNERDNG01.reqifz";
		String reqIFFile = unZip(filePath);
		
		Document doc = ReqIFParser.getInstance().readXML(reqIFFile);
		
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
	
	private static String unZip(String filePath) {
		
		String reqIFFileName = null;
		byte[] buffer = new byte[1024];
		
		try {
			File folder = new File(tempFolder);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			
			ZipInputStream zipInput = new ZipInputStream(new FileInputStream(filePath));
			
			ZipEntry zipEntry = zipInput.getNextEntry();
			
			while (zipEntry != null) {
				String fileName = zipEntry.getName();
				File newFile = new File(tempFolder + File.separator + fileName);
				
				
				if (fileName.contains(".reqif")) {
					reqIFFileName = copy(newFile);
					System.out.println(reqIFFileName);
				}
								
				System.out.println("File unzip: " + newFile.getAbsolutePath());
				
				new File(newFile.getParent()).mkdirs();
				
				FileOutputStream fileOutput = new FileOutputStream(newFile);
				
				int len;
				
				while ((len =  zipInput.read(buffer))>0){
					fileOutput.write(buffer, 0, len);
				}
				
				fileOutput.close();
				zipEntry = zipInput.getNextEntry();
			}
			
			zipInput.closeEntry();
			zipInput.close();
			
		} catch (IOException e) {
			
		}
		System.out.println(reqIFFileName);
		
		return reqIFFileName;
	}
	
	private static String copy(File src) throws IOException {
		String fileName = "Requirements.reqif";
		File dst = new File(fileName);
		
		Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
		
		fileName = dst.getAbsolutePath();
		
	    return fileName;
	}

}
