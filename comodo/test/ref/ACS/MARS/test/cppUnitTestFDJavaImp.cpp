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

#include "MARSCommonC.h"
#include "FDInterfaceC.h"

maci::SimpleClient client;

class FDJavaImpTest: public CppUnit::TestFixture 
{
	private:
	
		MARS::FDInterface *FDInterfaceComponent;
		
	public:
	
		void setUp() 
		{
			FDInterfaceComponent = client.getComponent<MARS::FDInterface>("testInstanceFDInterface", 0, true);
		}//

		void tearDown()
		{
			client.releaseComponent("testInstanceFDInterface");
		}//
		
		///////////////////////////////////////////////////////////
		// Test methods
		///////////////////////////////////////////////////////////
		
		void testGetSensorsList()
		{
			// TODO (generator autoimplementation)
		}//
		void testSensorStatus()
		{
			// TODO (generator autoimplementation)
		}//
		void testResetSensor()
		{
			// TODO (generator autoimplementation)
		}//
};

int main(int argc, char *argv[]) 
{
	client.init(argc, argv);
	
	client.login();
	
	// testGetSensorsList
	CppUnit::TestCaller<FDJavaImpTest> GetSensorsListTest ("testInstanceFDJavaImp", &FDJavaImpTest::testGetSensorsList);
	
	// testSensorStatus
	CppUnit::TestCaller<FDJavaImpTest> SensorStatusTest ("testInstanceFDJavaImp", &FDJavaImpTest::testSensorStatus);
	
	// testResetSensor
	CppUnit::TestCaller<FDJavaImpTest> ResetSensorTest ("testInstanceFDJavaImp", &FDJavaImpTest::testResetSensor);
	
	
	client.logout();
}
