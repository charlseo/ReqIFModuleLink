package com.ibm.rcs.ap.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AddXMLNode {
	
	private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder documentBuilder;
	private String reqIfFilePath;
	private String linkMaps;
	
	public AddXMLNode(String reqIfFilePath) throws Exception{
		this.reqIfFilePath = reqIfFilePath;
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		
	}
	
	public void addNameSpace() throws Exception, IOException {
		
		File xmlFile = new File(reqIfFilePath);
		BufferedReader br = new BufferedReader(new FileReader(xmlFile));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while((line = br.readLine())!= null)
		{
			if(line.indexOf("xmlns:doors") != -1){
				
			} else {
				if(line.indexOf("<REQ-IF ") != -1)
			    {
			        line = line.replaceAll("<REQ-IF ","<REQ-IF xmlns:doors=\"http://www.ibm.com/rdm/doors/REQIF/xmlns/1.0\" ");
			    }  
			}
		    sb.append(line);                
		}
		br.close();

		BufferedWriter bw = new BufferedWriter(new FileWriter(xmlFile));
		bw.write(sb.toString());
		bw.close();
  	    
  	    System.out.println("--- NameSpace added");
	}
	
	public void addLinkMaps(String linkMaps) throws Exception, IOException{
		
		this.linkMaps = linkMaps;
		Document doc = documentBuilder.parse(reqIfFilePath);
        
        doc.getDocumentElement().normalize();
		Node specRelationsNode = doc.getElementsByTagName("SPEC-RELATIONS").item(0);
		
		appendXmlFragment(documentBuilder, specRelationsNode, linkMaps);
	}
	
	public void addReqIfDef(String reqIfDefinition) throws Exception, IOException{
		
		Document doc = documentBuilder.parse(reqIfFilePath);
        
        doc.getDocumentElement().normalize();
		Node specRelationsNode = doc.getElementsByTagName("REQ-IF-TOOL-EXTENSION").item(0);
		
		appendXmlFragment(documentBuilder, specRelationsNode, reqIfDefinition);
	}
	
	public void addModDef(String modDefinition) throws Exception, IOException{
		
		Document doc = documentBuilder.parse(reqIfFilePath);
        
        doc.getDocumentElement().normalize();
		Node specRelationsNode = doc.getElementsByTagName("doors:DOORS-RIF-DEFINITION").item(0);
		
		appendXmlFragment(documentBuilder, specRelationsNode, modDefinition);
	}

	private void appendXmlFragment(
  	      DocumentBuilder docBuilder, Node parent,
  	      String fragment) throws IOException, SAXException, Exception {
  	    
		Document doc = parent.getOwnerDocument();
  	    Node fragmentNode = docBuilder.parse(
  	        new InputSource(new StringReader(fragment)))
  	        .getDocumentElement();
  	    
  	    fragmentNode = doc.importNode(fragmentNode, true);
  	    parent.appendChild(fragmentNode);
  	    DOMSource source = new DOMSource(doc);
  	    
  	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
  	    Transformer transformer = transformerFactory.newTransformer();
  	    StreamResult result = new StreamResult(reqIfFilePath);
  	    transformer.transform(source, result);
  	    //System.out.println("XML updated in " + reqIfFilePath);
  }
	
	private Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
}
