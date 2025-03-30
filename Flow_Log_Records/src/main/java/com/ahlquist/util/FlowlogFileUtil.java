package com.ahlquist.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FlowlogFileUtil {
	
	BufferedWriter w = null;
	FileWriter fw = null;
	
	public FlowlogFileUtil(String filePath) throws IOException {
		this.fw = new FileWriter(filePath);
		this.w = new BufferedWriter(this.fw);
	}
	
	public void write(String lineToWrite) throws IOException {
		this.w.write(lineToWrite);
		this.w.newLine(); // Add a new line character
	}
	
	public void close() {
		if(this.w!=null) {
			try {
				this.w.close();
			} catch (IOException e) {
			}
		}
		if(this.fw!=null) {
			try {
				this.fw.close();
			} catch (IOException e) {
			}
		}
	}
}

