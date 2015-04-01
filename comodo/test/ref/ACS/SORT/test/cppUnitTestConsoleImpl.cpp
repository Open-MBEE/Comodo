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


#include <cppunit/TestFixture.h>
#include <cppunit/TestCaller.h>
#include <maciSimpleClient.h>

#include "SORTCommonC.h"
#include "CommonOperationsC.h"
#include "ConsoleC.h"

maci::SimpleClient client;

class ConsoleImplTest: public CppUnit::TestFixture 
{
	private:
	
		SORT::CommonOperations *CommonOperationsComponent;
		SORT::Console *ConsoleComponent;
		
	public:
	
		void setUp() 
		{
			CommonOperationsComponent = client.getComponent<SORT::CommonOperations>("testInstanceCommonOperations", 0, true);
			ConsoleComponent = client.getComponent<SORT::Console>("testInstanceConsole", 0, true);
		}//

		void tearDown()
		{
			client.releaseComponent("testInstanceCommonOperations");
			client.releaseComponent("testInstanceConsole");
		}//
		
		///////////////////////////////////////////////////////////
		// Test methods
		///////////////////////////////////////////////////////////
		
		void testSetMode()
		{
			// TODO (generator autoimplementation)
		}//
		void testGetMode()
		{
			// TODO (generator autoimplementation)
		}//
		void testMoveTelescope()
		{
			// TODO (generator autoimplementation)
		}//
		void testGetTelescopePosition()
		{
			// TODO (generator autoimplementation)
		}//
		void testGetCameraImage()
		{
			// TODO (generator autoimplementation)
		}//
		void testCameraOn()
		{
			// TODO (generator autoimplementation)
		}//
		void testCameraOff()
		{
			// TODO (generator autoimplementation)
		}//
		void testSetRGB()
		{
			// TODO (generator autoimplementation)
		}//
		void testSetPixelBias()
		{
			// TODO (generator autoimplementation)
		}//
		void testSetResetLevel()
		{
			// TODO (generator autoimplementation)
		}//
};

int main(int argc, char *argv[]) 
{
	client.init(argc, argv);
	
	client.login();
	
	// testSetMode
	CppUnit::TestCaller<ConsoleImplTest> SetModeTest ("testInstanceConsoleImpl", &ConsoleImplTest::testSetMode);
	
	// testGetMode
	CppUnit::TestCaller<ConsoleImplTest> GetModeTest ("testInstanceConsoleImpl", &ConsoleImplTest::testGetMode);
	
	// testMoveTelescope
	CppUnit::TestCaller<ConsoleImplTest> MoveTelescopeTest ("testInstanceConsoleImpl", &ConsoleImplTest::testMoveTelescope);
	
	// testGetTelescopePosition
	CppUnit::TestCaller<ConsoleImplTest> GetTelescopePositionTest ("testInstanceConsoleImpl", &ConsoleImplTest::testGetTelescopePosition);
	
	// testGetCameraImage
	CppUnit::TestCaller<ConsoleImplTest> GetCameraImageTest ("testInstanceConsoleImpl", &ConsoleImplTest::testGetCameraImage);
	
	// testCameraOn
	CppUnit::TestCaller<ConsoleImplTest> CameraOnTest ("testInstanceConsoleImpl", &ConsoleImplTest::testCameraOn);
	
	// testCameraOff
	CppUnit::TestCaller<ConsoleImplTest> CameraOffTest ("testInstanceConsoleImpl", &ConsoleImplTest::testCameraOff);
	
	// testSetRGB
	CppUnit::TestCaller<ConsoleImplTest> SetRGBTest ("testInstanceConsoleImpl", &ConsoleImplTest::testSetRGB);
	
	// testSetPixelBias
	CppUnit::TestCaller<ConsoleImplTest> SetPixelBiasTest ("testInstanceConsoleImpl", &ConsoleImplTest::testSetPixelBias);
	
	// testSetResetLevel
	CppUnit::TestCaller<ConsoleImplTest> SetResetLevelTest ("testInstanceConsoleImpl", &ConsoleImplTest::testSetResetLevel);
	
	
	client.logout();
}
