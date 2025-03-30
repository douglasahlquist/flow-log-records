package com.ahlquist.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Now this class could have extend Map to eliminate the get function.  
 * but this hides many of Map's functions.
 */
public class ProtocolKeyMap {

	String filePath = null;
	Map<ProtocolKey, String> map = null;

	public ProtocolKeyMap(String filePath) throws FileNotFoundException, IOException {
		this.map = new HashMap<>();
		this.filePath = filePath;
		readProtocolMapFile();
	}

	public void readProtocolMapFile() throws FileNotFoundException, IOException {
		File file = new File(filePath);
		try (FileReader fr = new FileReader(file); 
			BufferedReader br = new BufferedReader(fr)) {
			String line;
			while ((line = br.readLine()) != null) {
				try {
					line = line.trim();
					processLine(line);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}	
	}

	public void processLine(String line) {
		String[] pieces = line.split(",");
		ProtocolKey pk = new ProtocolKey(Integer.parseInt(pieces[0]), pieces[1]);
		map.put(pk, pieces[2]);
	}

	/**
	 * Accessor function to get the value out of the map
	 * @param port
	 * @param baseProtocol
	 * @return
	 */
	public String getValue(Integer port, String baseProtocol) {
		ProtocolKey pk = new ProtocolKey(port, baseProtocol);
		return getValue(pk);
	}

	public String getValue(ProtocolKey pk) {
		return map.get(pk);
	}
	
	/**
	 * this toString function is created for debugging
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		List<String> list = new ArrayList<>();
		
		Set<ProtocolKey> set = map.keySet();
		for(ProtocolKey pk: set) {
			list.add(pk.getPort() + "," + pk.getProtocol() + "," + map.get(pk));
		}
		Collections.sort(list);  

		for (String str : list) {
		    sb.append(str).append("\n");
		}
		return sb.toString();
	}
}
