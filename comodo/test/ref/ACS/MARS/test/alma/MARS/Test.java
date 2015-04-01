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

package alma.MARS;

import static org.junit.Assert.*;
import org.junit.Before;

import java.util.List;
import java.util.logging.Logger;

import alma.acs.logging.ClientLogManager;
import alma.acs.component.client.ComponentClientTestCase;

import alma.MARS.*;
import alma.MARS.OCJavaImp;

import alma.ACS.*;

public class OCJavaImpTest extends ComponentClientTestCase {
	private Logger logger;
	private String componentName = "testInstanceOCJavaImp";
	private OCJavaImp component;

	/////////////////////////////////////////////////////////////
	// Lifecycle
	/////////////////////////////////////////////////////////////

	public OCJavaImpTest(String name) throws Exception {
		super("OCJavaImp");
	}//

	protected void setUp() throws Exception {
		super.setUp();
		logger = ClientLogManager.getAcsLogManager().getLoggerForApplication(
				getClass().getSimpleName(), false);
		this.component = (OCJavaImp) this.getContainerServices().getComponent(
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
	public void testExecuteRAB() throws Exception {
		int type = 0;
		position pos = new position();
		this.component.executeRAB(type, pos);
	}//

	@org.junit.Test
	public void testGetReportsList() throws Exception {
		int response = this.component.getReportsList();
		assertNotNull(response);
	}//

	@org.junit.Test
	public void testGetReport() throws Exception {
		int iD = 0;
		report response = this.component.getReport(iD);
		assertNotNull(response);
	}//

	@org.junit.Test
	public void testGetSensorsList() throws Exception {
		int response = this.component.getSensorsList();
		assertNotNull(response);
	}//

	@org.junit.Test
	public void testGetSensorStatus() throws Exception {
		int id = 0;
		sensor_status response = this.component.getSensorStatus(id);
		assertNotNull(response);
	}//

	@org.junit.Test
	public void testGetRobotsList() throws Exception {
		int response = this.component.getRobotsList();
		assertNotNull(response);
	}//

	@org.junit.Test
	public void testGetRobotStatus() throws Exception {
		int id = 0;
		robot_status response = this.component.getRobotStatus(id);
		assertNotNull(response);
	}//

}
