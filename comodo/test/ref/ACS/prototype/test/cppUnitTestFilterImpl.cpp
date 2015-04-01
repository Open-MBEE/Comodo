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

#include "prototypeCommonC.h"
#include "MotorC.h"
#include "DeviceC.h"
#include "FilterC.h"

maci::SimpleClient client;

class FilterImplTest: public CppUnit::TestFixture 
{
	private:
	
		prototype::Motor *MotorComponent;
		prototype::Device *DeviceComponent;
		prototype::Filter *FilterComponent;
		
	public:
	
		void setUp() 
		{
			MotorComponent = client.getComponent<prototype::Motor>("testInstanceMotor", 0, true);
			DeviceComponent = client.getComponent<prototype::Device>("testInstanceDevice", 0, true);
			FilterComponent = client.getComponent<prototype::Filter>("testInstanceFilter", 0, true);
		}//

		void tearDown()
		{
			client.releaseComponent("testInstanceMotor");
			client.releaseComponent("testInstanceDevice");
			client.releaseComponent("testInstanceFilter");
		}//
		
		///////////////////////////////////////////////////////////
		// Test methods
		///////////////////////////////////////////////////////////
		
		void testMove()
		{
			// TODO (generator autoimplementation)
		}//
		void testStandby()
		{
			// TODO (generator autoimplementation)
		}//
		void testOnline()
		{
			// TODO (generator autoimplementation)
		}//
		void testOff()
		{
			// TODO (generator autoimplementation)
		}//
		void testSetup()
		{
			// TODO (generator autoimplementation)
		}//
};

int main(int argc, char *argv[]) 
{
	client.init(argc, argv);
	
	client.login();
	
	// testMove
	CppUnit::TestCaller<FilterImplTest> MoveTest ("testInstanceFilterImpl", &FilterImplTest::testMove);
	
	// testStandby
	CppUnit::TestCaller<FilterImplTest> StandbyTest ("testInstanceFilterImpl", &FilterImplTest::testStandby);
	
	// testOnline
	CppUnit::TestCaller<FilterImplTest> OnlineTest ("testInstanceFilterImpl", &FilterImplTest::testOnline);
	
	// testOff
	CppUnit::TestCaller<FilterImplTest> OffTest ("testInstanceFilterImpl", &FilterImplTest::testOff);
	
	// testSetup
	CppUnit::TestCaller<FilterImplTest> SetupTest ("testInstanceFilterImpl", &FilterImplTest::testSetup);
	
	
	client.logout();
}
