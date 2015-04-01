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
 * $Id: BaseStaticConfig.java 628 2012-08-07 14:43:18Z landolfa $
 * 
 */

package comodo.util;



/**
 * Static config variables for the generator
 * and methods to get general information
 */
public final class BaseStaticConfig 
{
	
	//////////////////////////////////////////////////////////////////////
	// Static vars
	//////////////////////////////////////////////////////////////////////

	/**
	 * Code Generation modes
	 */
	public final static  String GEN_MODE_ALL = "ALL"; // generate all files also what is responsibility of the developer (i.e. actions/activities/Makfile etc.)
	public final static  String GEN_MODE_NORMAL = "NORMAL"; // generate only (fully generated) files not to be modified by the developer
	public final static  String GEN_MODE_UPDATE = "UPDATE"; // generate (fully generated) files not to be modified by the developer and new actions/activities

	/**
	 * Supported Platforms
	 */
	public final static  String TARGET_PLATFORM_SCXML = "SCXML";   // SCXML only
	public final static  String TARGET_PLATFORM_JAVA  = "JAVA";    // Java with Apache Commons SCXML	
	public final static  String TARGET_PLATFORM_VLT   = "VLT";     // VLTSW
	public final static  String TARGET_PLATFORM_JPFSC = "JPFSC";   // Java PathFinder	 
	public final static  String TARGET_PLATFORM_ACS   = "ACS";     // Alma Common Software
	public final static  String TARGET_PLATFORM_RMQ   = "RMQ";     // RabbitMQ
	public final static  String TARGET_PLATFORM_RMQSA = "RMQSA";   // RabbitMQ for State Analysis
	
	/**
	 * MWE Workflow
	 */
	private final static String MWE_BASE_DIR = "comodo/mwe/";
	public final static  String MWE_SCXML    = MWE_BASE_DIR + "SCXMLWorkflow.mwe";
	public final static  String MWE_JAVA     = MWE_BASE_DIR + "JAVAWorkflow.mwe";		
	public final static  String MWE_VLT      = MWE_BASE_DIR + "VLTWorkflow.mwe";		
	public final static  String MWE_JPFSC    = MWE_BASE_DIR + "JPFSCWorkflow.mwe";	
	public final static  String MWE_RMQ      = MWE_BASE_DIR + "RMQWorkflow.mwe";
	public final static  String MWE_RMQSA    = MWE_BASE_DIR + "RMQSAWorkflow.mwe";
	public final static  String MWE_ACS      = MWE_BASE_DIR + "ACSWorkflow.mwe";
	
	/**
	 * Static var to represents the tmp OS dir.
	 */
	public final static String TEMP_OS_DIR = System.getProperty("java.io.tmpdir");
	
	/**
	 * Logging
	 */
	public final static String COMODO_LOGGER = "comodo"; 
	
	/**
	 * Wrap limit of words to generate the comments in the code generated
	 */
	public final static int WORD_WRAP_MAX = 10;
	
	
	
	public static final String getReleaseInfo() {
		String releaseInfo = "";
		
		String info = "$Id: BaseStaticConfig.java 628 2012-08-07 14:43:18Z landolfa $";
		String[] infoArray = info.split(" ");
		
		if (infoArray.length >= 4) {
			releaseInfo = infoArray[2] + " " + infoArray[3];
		}
		
		return releaseInfo;
	}
	
}









