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
 * $Id: NodeElement.java 480 2011-10-10 08:41:55Z landolfa $
 * 
 */

package comodo.util;

/**
 * Class that represents an ENode for the
 * topologicalSort 
 */
public class NodeElement 
{
	public String job;
	public String [] others;
	
	/**
	 * Constructor
	 * @param job
	 * @param other
	 */
	public NodeElement(String job, String [] other) 
	{
		this.job = job;
		this.others = other;
	}
}
