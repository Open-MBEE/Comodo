/** 
 * COMODO - Multiplatform Component Code Generator 
 *    (c) European Southern Observatory, 2011 
 *    Copyright by ESO 
 *    All rights reserved 
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * $Id: COMODO.java 628 2012-08-07 14:43:18Z landolfa $
 * 
 */

package comodo.gen;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;


import org.eclipse.emf.mwe.core.WorkflowRunner;

import comodo.util.BaseStaticConfig;



/**
* Component Code Generator Main class
* @name COMODO.java
* @version $Id: COMODO.java 628 2012-08-07 14:43:18Z landolfa $
*/
public class COMODO 
{

	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException
	{	
		Logger logger = Logger.getLogger(BaseStaticConfig.COMODO_LOGGER);
		logger.getParent().setLevel(Level.ALL);
		
		// Print some info
		printVersion();
		
		// Setup Command Line Options
		Options opt = new Options();

		opt.addOption("h", "help", false, "Print help for this application");
		opt.addOption("d", "debug", false, "Debug information");	
		opt.addOption("t", "platform", true, "Specify the target software platform [SCXML|JAVA|VLT|JPFSC]");		
		opt.addOption("m", "model", true, "Model file path");
		opt.addOption("p", "profile", true, "Path to comodoProfile");
		opt.addOption("o", "output", true, "Output folder path");
		opt.addOption("g", "mode", true, "Generation mode [all|normal|update]");
		opt.addOption("c", "config", true, "Configuration parameters for the platform");
		
		// -e option is added manually since requires multiple number of values
		// -e for some platform may not be required
		OptionBuilder.withArgName("e");
		OptionBuilder.withLongOpt("modules");
		OptionBuilder.hasArgs();
		OptionBuilder.withDescription("Specify the module(s) to generate");
		opt.addOption(OptionBuilder.create("e"));
		
		// parse the options
		CommandLine cl = null;
		BasicParser parser = new BasicParser();
		try 
		{
			cl = parser.parse(opt, args);
		} 
		catch (ParseException e) 
		{
			logger.log(Level.SEVERE, "Parsing command line failed: " + e.getMessage());
			printHelp(opt);
			return;
		}

		// Print the help if has been chosen, or there's no options or args
		if (cl.hasOption('h') || cl.getOptions().length == 0)
		{
			printHelp(opt);
			return;
		}
		
		// Enable logging of debug information by setting proper logging levels
		if (cl.hasOption('d'))
		{
			// This does not work: you get less info!!! ToBeFixed
			//BaseStaticConfig.enableDebugLogging();
			logger.log(Level.INFO, "Debug logging enabled");
		}
		
		String modelPath = "";
		if (cl.hasOption('m') == false) {
			logger.log(Level.SEVERE, "Model is missing.");
			printHelp(opt);	
			return;
		} else {
			try {
				File modelFile = new File(cl.getOptionValue("m"));
				modelPath = modelFile.toURI().toURL().toString();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Model " + cl.getOptionValue("m") + "does not exist (" + e.getMessage() + ").");
				return;
			}
		}
		
		String profilePath = "";
		if (cl.hasOption('p') == false) {
			logger.log(Level.SEVERE, "Profile is missing.");
			printHelp(opt);	
			return;
		} else {
			try {
				File profileFile = new File(cl.getOptionValue("p"));
				profilePath = profileFile.toURI().toURL().toString();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Profile " + cl.getOptionValue("p") + "does not exist (" + e.getMessage() + ").");
				return;
			}
		}

		String outputPath = cl.getOptionValue("o");
		if (cl.hasOption('o') == false) {
			logger.log(Level.SEVERE, "Output directory is missing.");
			printHelp(opt);	
			return;
		} 

		if (cl.hasOption('e') == false) {
			logger.log(Level.SEVERE, "Module(s) name missing.");
			printHelp(opt);	
			return;
		} 
		String[] moduleNames = cl.getOptionValues("e");

		String generationMode = BaseStaticConfig.GEN_MODE_NORMAL;
		if (cl.hasOption('g') == true) {
			if (cl.getOptionValue("g").equalsIgnoreCase(BaseStaticConfig.GEN_MODE_ALL)) {
				generationMode = BaseStaticConfig.GEN_MODE_ALL;
			} else if (cl.getOptionValue("g").equalsIgnoreCase(BaseStaticConfig.GEN_MODE_NORMAL)) {
				generationMode = BaseStaticConfig.GEN_MODE_NORMAL;
			} else if (cl.getOptionValue("g").equalsIgnoreCase(BaseStaticConfig.GEN_MODE_UPDATE)) {
				generationMode = BaseStaticConfig.GEN_MODE_UPDATE;				
			} else {
				logger.log(Level.SEVERE, "Code generation mode not supported.");
				printHelp(opt);
				return;
			}
		} 
		
		
		// select the platform and start the related MWE workflow
		
		String mweFileName = "";	
		if (cl.hasOption("t")) {
			if (cl.getOptionValue("t").equalsIgnoreCase(BaseStaticConfig.TARGET_PLATFORM_SCXML)) {
				mweFileName = BaseStaticConfig.MWE_SCXML;
			} else if (cl.getOptionValue("t").equalsIgnoreCase(BaseStaticConfig.TARGET_PLATFORM_JAVA)) {
				mweFileName = BaseStaticConfig.MWE_JAVA;								
			} else if (cl.getOptionValue("t").equalsIgnoreCase(BaseStaticConfig.TARGET_PLATFORM_VLT)) {
				mweFileName = BaseStaticConfig.MWE_VLT;						
			} else if (cl.getOptionValue("t").equalsIgnoreCase(BaseStaticConfig.TARGET_PLATFORM_ACS)) {
				mweFileName = BaseStaticConfig.MWE_ACS;
			} else if (cl.getOptionValue("t").equalsIgnoreCase(BaseStaticConfig.TARGET_PLATFORM_RMQ)) {
				mweFileName = BaseStaticConfig.MWE_RMQ;		
			} else if (cl.getOptionValue("t").equalsIgnoreCase(BaseStaticConfig.TARGET_PLATFORM_RMQSA)) {
				mweFileName = BaseStaticConfig.MWE_RMQSA;						
			} else if (cl.getOptionValue("t").equalsIgnoreCase(BaseStaticConfig.TARGET_PLATFORM_JPFSC)) {
				mweFileName = BaseStaticConfig.MWE_JPFSC;				
			} else {
				logger.log(Level.SEVERE, "Target platform not supported.");
				printHelp(opt);
				return;
			}
		} else {
			logger.log(Level.SEVERE, "Target platform not specified.");
			printHelp(opt);
			return;
		}

		// Get, if available, the configurations parameters for the platform. 
		String platformConfig = "";
		if (cl.hasOption('c') == true) {
			platformConfig = cl.getOptionValue("c");
		}

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		System.out.println("ClassLoader: " + classLoader.getClass().toString());
		URL u = classLoader.getResource(mweFileName);
		if (u == null) {
			logger.severe("MWE " + mweFileName + " not found!");
		}
		
		/*
		String current = new java.io.File( "." ).getCanonicalPath();
        System.out.println("Current dir:"+current);
		try {
			File mweFile = new File(mweFileName);
			if (mweFile.exists() == false) {
				logger.log(Level.SEVERE, "File does not exists: " + mweFileName);
				return;				
			}
			if (mweFile.canRead() == false) {
				logger.log(Level.SEVERE, "Cannot read workflow file: " + mweFileName);
				return;
			}
			String mweFileURL = mweFile.toURI().toURL().toString();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Workflow file: " + mweFileName + " not found! (" + e.getMessage() + ").");
			return;
		}
		*/

		logger.log(Level.INFO, "Model: " + modelPath);
		logger.log(Level.INFO, "Profile: " + profilePath);
		logger.log(Level.INFO, "Output directory: " + outputPath);
		logger.log(Level.INFO, "Target platform: " + cl.getOptionValue("t"));
		logger.log(Level.INFO, "Platform config.: " + platformConfig);			
		logger.log(Level.INFO, "Mode: " + generationMode);	
		logger.log(Level.INFO, "Executing MWE Workflow: " + mweFileName);
		
		for (int i = 0; i < moduleNames.length; i++) {

			String moduleName = moduleNames[i];
			logger.log(Level.INFO, "Module: " + moduleNames[i] + " (" + (i+1) + " / " + moduleNames.length + ")");

			// Assign command line parameters to the workflow's properties 
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("modelFileURI", modelPath);
			properties.put("profileFileURI", profilePath);
			properties.put("moduleName", moduleName);
			properties.put("ouputFolderURI", outputPath);
			properties.put("generationMode", generationMode);
			properties.put("platformConfig", platformConfig);
			Map<String, String> slotContents = new HashMap<String, String>();

			WorkflowRunner wrunner = new WorkflowRunner();	

			try {
				wrunner.run(mweFileName, null, properties, slotContents);
			}
			/*
			catch (ConfigurationException e) {
				logger.log(Level.SEVERE, "Error while executing " + mweFileName + ": " + e.getMessage());
				return;				
			}
			*/
			catch(Exception e) {
				logger.log(Level.SEVERE, "Error while executing " + mweFileName + ": " + e.getMessage());
				return;
			}
		}
        logger.log(Level.INFO, "Done");
	}
	
	/**
	 * Print help
	 */
	public static void printHelp(Options opt)
	{
		HelpFormatter f = new HelpFormatter();
		f.printHelp("\njava -jar comodo.jar {options}\n\n", opt);
	}
	
	/**
	 * Print info about the generator.
	 */
	public static void printVersion() 
	{
		//Logger.getLogger(BaseStaticConfig.COMODO_LOGGER).log(Level.INFO, "COMODO Code Generator - Release " + BaseStaticConfig.getReleaseInfo() + " - Build " + BaseStaticConfig.getBuildNumber());
		Logger.getLogger(BaseStaticConfig.COMODO_LOGGER).log(Level.INFO, "COMODO Code Generator - Release " + BaseStaticConfig.getReleaseInfo());
	
	}	

}

