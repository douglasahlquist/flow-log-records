package com.ahlquist.app;

import java.io.*;
import java.util.*;


/**
 *  String LOOKUP_FILE = "lookup.csv"; // CSV file: dstport,protocol,tag
    String FLOW_LOG_FILE = "flowlog.txt"; // Flow log file
 */
public class Application {
	

    private static Map<String, String> tagLookup = new HashMap<>();

    public static void main(String... args) {
    	
    	new Application(args[0], args[1]);
    }
    
    public Application(String lookupFile, String flowLogFile) {
    	 try {
             loadLookupTable(lookupFile);
             processFlowLogs(flowLogFile);
         } catch (IOException e) {
             System.err.println("Error processing files: " + e.getMessage());
         }
    }

    private static void loadLookupTable(String lookupFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(lookupFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) continue;
                String key = parts[0].trim() + ":" + parts[1].trim(); // dstport:protocol
                String tag = parts[2].trim();
                tagLookup.put(key, tag);
            }
        }
    }

    private static void processFlowLogs(String flowLogFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(flowLogFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length < 13) continue; // Ensure we have enough columns
                
                String dstPort = parts[5];  // Destination port
                String protocol = parts[7]; // Protocol number (e.g., TCP=6, UDP=17)
                String key = dstPort + ":" + protocol;

                String tag = tagLookup.getOrDefault(key, "UNKNOWN");
                System.out.println(line + " TAG=" + tag);
            }
        }
    }
}