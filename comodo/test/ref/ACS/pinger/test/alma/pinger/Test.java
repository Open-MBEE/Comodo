/*******************************************************************************
 *    ALMA - Atacama Large Millimiter Array
 *
 *    (c) European Southern Observatory, 2002
 *    Copyright by ESO (in the framework of the ALMA collaboration)
 *    and Cosylab 2002, All rights reserved
 *  
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *
 * "@(#) $Id$" 
 *
 * who                when       what
 * ----------------  ----------  ----------------------------------------------
 * COMODO                        Created.
 * 
 */

package alma.pinger;

import static org.junit.Assert.*;
import org.junit.Before;

import java.util.List;
import java.util.logging.Logger;

import alma.acs.logging.ClientLogManager;
import alma.acs.component.client.ComponentClientTestCase;

import alma.pinger.*;
import alma.pinger.PingerImpl;

import alma.ACS.*;
import alma.ACS.jbaci.*;
import alma.ACS.impl.*;

public class PingerImplTest extends ComponentClientTestCase {
	private Logger logger;
	private String componentName = "testInstancePingerImpl";
	private PingerImpl component;

	/////////////////////////////////////////////////////////////
	// Lifecycle
	/////////////////////////////////////////////////////////////

	public PingerImplTest(String name) throws Exception {
		super("PingerImpl");
	}//

	protected void setUp() throws Exception {
		super.setUp();
		logger = ClientLogManager.getAcsLogManager().getLoggerForApplication(
				getClass().getSimpleName(), false);
		this.component = (PingerImpl) this.getContainerServices().getComponent(
				this.componentName);
	}//

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.getContainerServices().releaseComponent(this.componentName);
	}//

	/////////////////////////////////////////////////////////////
	// Test Methods
	/////////////////////////////////////////////////////////////

	@org.junit.Test
	public void testSpawnChildren() throws Exception {
		int howMany = 0;
		String container = new String();
		String baseName = new String();
		this.component.spawnChildren(howMany, container, baseName);
	}//

	@org.junit.Test
	public void testLogInfo() throws Exception {
		this.component.logInfo();
	}//

	@org.junit.Test
	public void testPing() throws Exception {
		boolean fast = false;
		boolean recursive = false;
		int id = 0;
		boolean response = this.component.ping(fast, recursive, id);
		assertNotNull(response);
	}//

}
