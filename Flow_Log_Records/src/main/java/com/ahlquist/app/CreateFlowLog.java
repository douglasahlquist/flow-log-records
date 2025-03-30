package com.ahlquist.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ahlquist.util.FlowlogFileUtil;


/**
 * Creates the Flow Log Data.  In the format:
 * 2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 6 25 20000 16201407611620140821 ACCEPT OK 
 * 2 123456789012 eni-4d3c2b1a 192.168.1.100 203.0.113.101 23 49154 6 15 12000 16201407611620140821 REJECT OK 
 * 2 123456789012 eni-5e6f7g8h 192.168.1.101 198.51.100.3 25 49155 6 10 8000 1620140761 1620140821 ACCEPT OK 
 */
public class CreateFlowLog {
	
	//FOR THE PURPOSES OF THIS EXERCISE ONLY THE PORT WILL BE CHANGED IN THIS STRING
	public static String FLOW_LOG_DATA_LINE = 
			"2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 %PORT% 49153 6 25 20000 16201407611620140821 ACCEPT OK";
	
	public static void main(String...strings) {
		
		try {
			//Create a List<ProtocolKey> from the file
			List<Integer> pkList = createPKPortList(strings[0]);
			createfowLogData(pkList,  strings[1], Integer.parseInt(strings[2]));
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * Utility function that creates the flow log data, altering just the port
	 * @param pkList
	 * @param flowLogOutput
	 * @param totalFlowLinesCount
	 * @throws IOException
	 */
	public static void createfowLogData(List<Integer> pkList, String flowLogOutput, int totalFlowLinesCount) throws IOException{
		
		int pkSize = pkList.size();
		List<String> list = new ArrayList<>();
		FlowlogFileUtil ffu = new FlowlogFileUtil(flowLogOutput);
		for (int i=0 ; i<totalFlowLinesCount ; i++) {
			int index = (int) (pkSize * Math.random());
			String randomNumber = Integer.toString(pkList.get(index));
			String outLine = FLOW_LOG_DATA_LINE.replaceAll("%PORT%", randomNumber);
			list.add(outLine);
		}
		
		Collections.sort(list);  // Sort cars

	    for (String str : list) {
	      System.out.println("OutLine: " + str);
	    //for now no checking is done if there is an error writing the correct number of lines
			ffu.write(str + "\n");
	    }

		ffu.close();
	}
	
	/**
	 * Utility function that creates the Data for the Flow Log Parser. Eventually this should be stripped from any artifact deployed
	 * 
	 * @param protocolKeyFilepath - The file that contains the list of all listed protocols,  
	 * 	Note: simply generating a random port may not yield the best desired results.
	 * @param totalLines - an Integer of the number of records to generate
	 * @return List<Integer> - because this only needs to create a list of integers from the PK file
	 */
	public static List<Integer> createPKPortList(String protocolKeyFilepath) throws IOException {
		List<Integer> pkPortList = new ArrayList<>();
		
	    try (BufferedReader reader = new BufferedReader(new FileReader(protocolKeyFilepath))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	        	String[] pieces = line.split(",");
	        	try {
	        		int n = Integer.parseInt(pieces[0]);
	        		pkPortList.add(n);	        		
	        	}catch(Exception e) {
	        		e.printStackTrace();
	        	}
	        }
	    } 
		return pkPortList;
	}
}
