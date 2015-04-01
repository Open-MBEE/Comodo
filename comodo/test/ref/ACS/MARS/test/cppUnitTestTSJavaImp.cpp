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
#include "TSInterfaceC.h"

maci::SimpleClient client;

class TSJavaImpTest: public CppUnit::TestFixture 
{
	private:
	
		MARS::TSInterface *TSInterfaceComponent;
		
	public:
	
		void setUp() 
		{
			TSInterfaceComponent = client.getComponent<MARS::TSInterface>("testInstanceTSInterface", 0, true);
		}//

		void tearDown()
		{
			client.releaseComponent("testInstanceTSInterface");
		}//
		
		///////////////////////////////////////////////////////////
		// Test methods
		///////////////////////////////////////////////////////////
		
		void testRunManualRAB()
		{
			// TODO (generator autoimplementation)
		}//
		void testRunAutoRAB()
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
	
	// testRunManualRAB
	CppUnit::TestCaller<TSJavaImpTest> RunManualRABTest ("testInstanceTSJavaImp", &TSJavaImpTest::testRunManualRAB);
	
	// testRunAutoRAB
	CppUnit::TestCaller<TSJavaImpTest> RunAutoRABTest ("testInstanceTSJavaImp", &TSJavaImpTest::testRunAutoRAB);
	
	// testGetRobotsList
	CppUnit::TestCaller<TSJavaImpTest> GetRobotsListTest ("testInstanceTSJavaImp", &TSJavaImpTest::testGetRobotsList);
	
	// testGetRobotStatus
	CppUnit::TestCaller<TSJavaImpTest> GetRobotStatusTest ("testInstanceTSJavaImp", &TSJavaImpTest::testGetRobotStatus);
	
	
	client.logout();
}
