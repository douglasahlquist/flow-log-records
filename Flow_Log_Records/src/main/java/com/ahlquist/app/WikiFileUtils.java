package com.ahlquist.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.ahlquist.util.ProtocolKey;

/**
 * This is a utility function used solely to create the input file for the flow
 * log record parser.
 */
public class WikiFileUtils {

	/**
	 * Main driver
	 * 
	 * @param strings - takes one argument with two strings input and output
	 */
	public static void main(String... strings) {
		try {
			processWikiFileCopy(strings[0], strings[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void processWikiFileCopy(@NonNull final String input, @NonNull final String output) {

		try (FileWriter writer = new FileWriter(output, true)) {
			Map<ProtocolKey, String> map = readFileLines(input);

			Set<ProtocolKey> set = map.keySet();
			System.out.println("map size: " + map.size());
			List<String> list = new ArrayList<>();
			for (ProtocolKey pKey : set) {

				String protocolName = map.get(pKey);
				String key = pKey.getPort() + "," + pKey.getProtocol();
				String value = key + "," + protocolName ;
				list.add(value);
			}
			Collections.sort(list);  
			
			for(String str : list) {
				System.out.println("outline: " + str);
				writer.write(str + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<ProtocolKey, String> readFileLines(@NonNull final String filePath) throws IOException {

		Map<ProtocolKey, String> map = new HashMap<>();
		File file = new File(filePath);
		try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr);) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				//if(!line.startsWith("#")) { //ignore comments
					map = processLine(map, line);
				//}
			}
		}
		return map;
	}

	/**
	 * Process a single line for the Protocol listing which will eventually by used,
	 * in parsing the Flow Log Records list @see
	 * https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers
	 * 
	 * @param map  - A map containing the previous produced list of entries
	 * @param line - the yet to be parse line from the Wikipedia protocol list.
	 * @return Map<ProtocolKey, String> - the current version of the map
	 */
	private static int previousPort = 0;
	public static Map<ProtocolKey, String> processLine(@NonNull Map<ProtocolKey, String> map, @NonNull String line) {
		
		System.out.println("~=".repeat(20));
		System.out.println(line);
		System.out.println("--".repeat(20));
		String[] pieces = line.split("\t");

		if(pieces[0].trim().length()==0) {
			//return map;
		}
		try {
			int port = -1;
			
			try {
				port = Integer.parseInt(pieces[0]);
				previousPort = port;
			}catch(Exception e) {
				line = previousPort + "\t" + line;
				pieces = line.split("\t");
				port = Integer.parseInt(pieces[0]);
			}
			String tcp = pieces[1];
			if(tcp!=null) {
				tcp = tcp.trim();
			}
			String udp = pieces[2];
			if(udp!=null) {
				udp = udp.trim();
			}
			String name = null;
			try {
				name = getNameFromPiece(pieces[5]);
			}catch(Exception e) {
				name = getNameFromPiece(pieces[pieces.length-1]);
			}
			if(name!=null) {
				name = name.trim();
				System.out.println("name: " + name);
			}
			
			//further cleanup
			if(name!=null) {
				name = name.replaceAll("[^a-zA-Z0-9\\s]", "");
				if(tcp!=null &&  
						(
						"Yes".equalsIgnoreCase(tcp) ||
						"Assigned".equalsIgnoreCase(tcp) ||
						"Unofficial".equalsIgnoreCase(tcp)) && 
						!"No".equalsIgnoreCase(tcp)
						) {
					map = associate(map, port, "tcp", name);
				}
				if(udp!=null && 
						(
						!"No".equalsIgnoreCase(udp) && (
						"Yes".equalsIgnoreCase(udp) ||
						"Unofficial".equalsIgnoreCase(tcp)))
						) {
					map = associate(map, port, "udp", name);
				}
			}
			
			
			
		} catch (Exception e) {
			System.err.println("Line: " + line);
			e.printStackTrace();
		}
		
		return map;
	}

	/**
	 * Utility method that create a map record
	 * 
	 * @param map          - the map that will have the entry added to
	 * @param port         - the integer port
	 * @param baseProtocol - either 'tcp' or 'udp'
	 * @param protocolName - the name extracted from a table version of the wiki
	 *                     page:
	 *                     https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers
	 * 
	 *                     Note: in some cases the mappings may not be correctly
	 *                     assigned a name, specifically port 3000, where there are
	 *                     numerous possible mapping
	 * @return the map plus a new key value pair
	 */
	public static Map<ProtocolKey, String> associate(@NonNull Map<ProtocolKey, String> map, final int port,
			@NonNull final String baseProtocol, @NonNull final String protocolName) {

		ProtocolKey pk = new ProtocolKey(port, baseProtocol);
		map.put(pk, protocolName);

		return map;
	}

	// Used below
	static int undefined5 = 0; // used if not pieces[5] is defined

	/**
	 * Utility wrapper function used to get the protocol name from the input data
	 * file
	 * 
	 * @param pieces
	 * @return
	 */
	public static String getNameFromArray(@NonNull final String[] pieces) {
		
		String description = null;
		if (pieces.length > 5) {
			description = "Undefined_" + undefined5++;
			System.out.println("description: " + description);
			return description;
		}
		System.out.println("description: " + pieces[5]);
		return getNameFromPiece(pieces[5]);
	}

	/**
	 * Note: That this implementation based of the Wikipedia Document should be
	 * revised in the future to more easily define the tag names 1. Look for the
	 * first name in the 'pieces[5] bounded by '(' and ')', else 2. use the first
	 * works in the piece
	 * 
	 * @param piece
	 * @return String - name tag name
	 */
	public static String getNameFromPiece(@NonNull String piece) {
		String name = null;

		// remove all contents included within '[???]'
		piece = piece.replaceAll("\\[.*?\\]", "");
		// now find if there is a ',' in the remaining piece...
		int index = 0;

		if ((name = getValueBetweenParentheses("\\((.*?)\\)", piece)) != null) {
			System.out.println("(...) <" + name + ">");
			return name;
		} else if ((index = piece.indexOf(",")) > -1){
			String temp = piece.substring(0, index);
			//temp = temp.replace(">", "");
			System.out.println(", <" + temp + ">");
			return temp;
		}else {
			// Else return the first word
			// this could be refined or the input source refined
			if ((index = piece.indexOf(" ")) > -1) {
				String temp = piece.substring(0, index);
				System.out.println("first: <" + temp + ">");
				return temp;
			}
		}
		return name;
	}

	/**
	 * Utility function
	 * 
	 * @param matcherStr
	 * @param input
	 * @return String - the name value bounded by opposing parens
	 */
	public static String getValueBetweenParentheses(@NonNull final String matcherStr, @NonNull final String input) {
		Pattern pattern = Pattern.compile(matcherStr);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return null;
		}
	}
}