package com.ahlquist.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import com.ahlquist.util.ProtocolKey;
import com.ahlquist.util.ProtocolKeyMap;

public class GenerateTagMap {
	
	private ProtocolKeyMap protocolMap = null;
	
	//private 
	
	public static void main(String...strings) {
		
		//fail fast if mis-configured
		try {	
			String protocolKeyInputFile = strings[0];
			String flowLogDataInputFile = strings[1]; 
			String outputFile = strings[2];
			GenerateTagMap gtm = new GenerateTagMap(protocolKeyInputFile, flowLogDataInputFile, outputFile);
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public GenerateTagMap(String protocolKeyInputFile, String flowLogDataInputFile, String outputCountFile) throws FileNotFoundException, IOException{
		
		// Load ProtocolMap;
		protocolMap = loadProtocolMap(protocolKeyInputFile);
		System.out.println(protocolMap.toString());
		
		// Process Flow Log Records
		
		
		
	}
	
	public ProtocolKeyMap loadProtocolMap(String protocolKeyInputFile) throws FileNotFoundException, IOException{
		ProtocolKeyMap pkm = new ProtocolKeyMap(protocolKeyInputFile);
		pkm.readProtocolMapFile();
		return pkm;
	}


}
