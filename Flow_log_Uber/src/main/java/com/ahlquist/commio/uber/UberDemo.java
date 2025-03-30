package com.ahlquist.commio.uber;

public class UberDemo {
	
	private static UberDemo instance = null;
	
	public static void main(String...strings) {
		new UberDemo();
	}
	
	public static UberDemo getInstance() {
		System.out.println("inside UberDemo getInstance()"); 
		if(instance==null) {
			instance = new UberDemo();
		}
		return instance;
	}
	
	public UberDemo() {
		super();
		System.out.println("inside UberDemo constructor");  
	}
	
	public UberDemo(String...strings) {
		super();
		System.out.println("inside UberDemo constructor with args"); 
		int i=0;
		for(String string: strings) {
			System.out.println(i + string);
			i++;
		}
	}

}
