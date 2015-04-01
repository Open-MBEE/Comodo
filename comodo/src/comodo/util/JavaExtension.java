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
 * $Id: JavaExtension.java 612 2012-07-04 13:36:35Z landolfa $
 * 
 */

package comodo.util;

import java.io.File;
import java.util.Date;
import java.util.Vector;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;


/**
 * Java extension for xtend functions
 */
public class JavaExtension 
{

	/**
	 * The function return the full actual date
	 * @return String - the actual full date
	 */
	public final static String getFullActualDate()
	{
		return new Date().toString();
	}
	
	/**
	 * The function return the date formated for the comments
	 * @return String - the actual date for the comments
	 */
	public final static String getCommentDate()
	{
		Date currentDate = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String commentDate = f.format(currentDate);
		return commentDate;
	}
	
	/**
	 * Get a list of comment wrapped, 10 word per each line
	 * @return Vector<String> a list of comments wrapped
	 */
	public final static Vector<String> getCommentsWrapped(String comment)
	{
		String tmpLine="";
		int tmpCounter = 0;
		String[] wordArray = comment.split(" ");
		Vector<String> commentsWrapped = new Vector<String>();
		
		for( String word : wordArray)
		{
				tmpLine += " "; 
				tmpLine += word; 
				tmpCounter++;
				
			if(tmpCounter > BaseStaticConfig.WORD_WRAP_MAX)
			{
				commentsWrapped.add(tmpLine);
				tmpLine = "";
				tmpCounter = 0;
			}
		}
		
		return commentsWrapped;
	}
	
	/**
	 * Get the ID string for cvs, svn, etc.. general 
	 * control versions id propset
	 * @return  String
	 */
	public static String getPropSet(String prop)
	{
		//separate in order to not be replaced
		String ppSufix = "$";
		return ppSufix+prop+ppSufix;
	}
	
	/**
	 * Probably a very inefficient but easy way to remove duplicated
	 * names from a list.
	 */
	public final static List<String> removeDuplicates(List<String> list)
	{
		Set<String> set = new HashSet<String>(list);
		return new ArrayList<String>(set);
	}	

	public final static String getPrefix(String s)
	{
		return s.substring(0, s.indexOf('.'));
	}	

	public final static String combinePrefixPostfix(String s)
	{
		if (s.contains(".")) {
			String s1 = s.substring(0, s.indexOf('.'));
			String s2 = s.substring(s.indexOf('.')+1,s.length());
			return s1+s2;
		} else {
			return s;
		}
	}	

	public final static boolean fileExists(String filename)
	{
		File f = new File(filename);
		return f.exists();
	}	
	
}
