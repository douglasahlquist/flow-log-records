package com.ahlquist.commio.uber;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class UberApplication {

	static String className = null;
	static List<Exception> exceptions = new ArrayList<>();

	/**
	 * Creates a system process based on the command line arguments passed into the
	 * main method <code>
	 * @param args
	 * ==> args[0] - The string representation of the package and class name (required)
	 * ==> args[1] - The static method to be called (required, no now, late you will be able to call the constructor)
	 * ==> args[2] - an optional parameter flag '-a'
	 * ==> args[3] - The optional array of String arguments bound in double quotes with no spaces
	 * </code>
	 */
	public static void main(String[] args) {
		System.out.println("inside UberApplication.main...");
		for(int i=0 ; i< args.length; i++) {
			System.out.println("i:" + i + " " + args[i] + "\n");
		}
		
		if (args.length < 1) {
			System.err.println("Please provide the class name to run.");
			return;
		}
		try {
			new UberApplication(args);
		} catch (Exception e) {
			e.printStackTrace();
			exceptions.add(e);
		}
	}

	public UberApplication(String[] args) throws ClassNotFoundException, NoSuchMethodException, Exception {
		className = args[0]; // The first argument is the class name
		String[] mainArgs = null;
		if (args.length > 2) {
			if ("-a".equals(args[2])) {
				mainArgs = new String[args.length - 3];
			}
		} else {
			mainArgs = new String[args.length - 2]; // Remaining arguments to pass to the main method
		}
		System.arraycopy(args, 2, mainArgs, 0, mainArgs.length); // Copy arguments to the mainArgs array

		try {
			// Load the class dynamically
			Class<?> clazz = Class.forName(className);

			// Get the main method of the class
			Method mainMethod = null;
			if(args.length>2 || (args.length>=2 && "main".equalsIgnoreCase(args[1]))) {
				mainMethod = clazz.getDeclaredMethod(args[1], String[].class);
			}else {
				mainMethod = clazz.getDeclaredMethod(args[1]);
			}

			// Check if the main method is static
			if (!java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers())) {
				throw new IllegalArgumentException("The '" + args[1] + "' method must be static.");
			}

			// Call the main method of the loaded class
			if(args.length>2 || (args.length>=2 && "main".equalsIgnoreCase(args[1]))) {
				mainMethod.invoke(null, (Object) mainArgs); // The cast is necessary to pass the String array correctly
			}else {
				mainMethod.invoke(null);
			}
			
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found: " + className);
			exceptions.add(e);
			throw e;
		} catch (NoSuchMethodException e) {
			System.err.println("The class does not have a method named: '" + args[1] + "'.");
			exceptions.add(e);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			exceptions.add(e);
			throw e;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("UberApplication completed executing [");

		if(!exceptions.isEmpty()) {
		for(Exception e: exceptions) {
			System.err.println(convert(e));
		}
		}else {
			System.out.println("No exception found while starting the execution of " + args[0]);
		}
		
		if (args.length > 1) {
			sb.append(args[0]);
			sb.append("].");
			sb.append("[" + args[1] + "]");
		} else {
			sb.append("]");
		}
		System.out.println(sb.toString());
	}

	public List<Exception> getExceptions() {
		return exceptions;
	}
	
	/**
	 * Converts an exception to a String
	 * @param e - the exception having been thrown
	 * @return
	 */
	public static String convert(final /*@NonNull*/ Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString(); 
	}

	/**
	 * Converts an exception and wraps it in a keyed JSONObject
	 * @param key
	 * @param e
	 * @return
	 */
	public static JSONObject exceptionToString(final String key, final Exception e) {

		JSONObject json = new JSONObject();
		json.put(key, convert(e));
		return json; 
	}

}
