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
#include "OCInterfaceC.h"

maci::SimpleClient client;

class OCJavaImpTest: public CppUnit::TestFixture 
{
	private:
	
		MARS::OCInterface *OCInterfaceComponent;
		
	public:
	
		void setUp() 
		{
			OCInterfaceComponent = client.getComponent<MARS::OCInterface>("testInstanceOCInterface", 0, true);
		}//

		void tearDown()
		{
			client.releaseComponent("testInstanceOCInterface");
		}//
		
		///////////////////////////////////////////////////////////
		// Test methods
		///////////////////////////////////////////////////////////
		
		void testExecuteRAB()
		{
			// TODO (generator autoimplementation)
		}//
		void testGetReportsList()
		{
			// TODO (generator autoimplementation)
		}//
		void testGetReport()
		{
			// TODO (generator autoimplementation)
		}//
		void testGetSensorsList()
		{
			// TODO (generator autoimplementation)
		}//
		void testGetSensorStatus()
		{
			// TODO (generator autoimplementation)
		}//
		void testGetRobotsList()
		{
			// TODO (generator autoimplementation)
		}//
		void testGetRobotStatus()
		{
			// TODO (generator autoimplementation)
		}//
};

int main(int argc, char *argv[]) 
{
	client.init(argc, argv);
	
	client.login();
	
	// testExecuteRAB
	CppUnit::TestCaller<OCJavaImpTest> ExecuteRABTest ("testInstanceOCJavaImp", &OCJavaImpTest::testExecuteRAB);
	
	// testGetReportsList
	CppUnit::TestCaller<OCJavaImpTest> GetReportsListTest ("testInstanceOCJavaImp", &OCJavaImpTest::testGetReportsList);
	
	// testGetReport
	CppUnit::TestCaller<OCJavaImpTest> GetReportTest ("testInstanceOCJavaImp", &OCJavaImpTest::testGetReport);
	
	// testGetSensorsList
	CppUnit::TestCaller<OCJavaImpTest> GetSensorsListTest ("testInstanceOCJavaImp", &OCJavaImpTest::testGetSensorsList);
	
	// testGetSensorStatus
	CppUnit::TestCaller<OCJavaImpTest> GetSensorStatusTest ("testInstanceOCJavaImp", &OCJavaImpTest::testGetSensorStatus);
	
	// testGetRobotsList
	CppUnit::TestCaller<OCJavaImpTest> GetRobotsListTest ("testInstanceOCJavaImp", &OCJavaImpTest::testGetRobotsList);
	
	// testGetRobotStatus
	CppUnit::TestCaller<OCJavaImpTest> GetRobotStatusTest ("testInstanceOCJavaImp", &OCJavaImpTest::testGetRobotStatus);
	
	
	client.logout();
}
