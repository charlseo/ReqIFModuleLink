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

	
}
