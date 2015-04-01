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
#include "LampC.h"
#include "DeviceC.h"
#include "Lamp2C.h"

maci::SimpleClient client;

class Lamp2ImplTest: public CppUnit::TestFixture 
{
	private:
	
		prototype::Lamp *LampComponent;
		prototype::Device *DeviceComponent;
		prototype::Lamp2 *Lamp2Component;
		
	public:
	
		void setUp() 
		{
			LampComponent = client.getComponent<prototype::Lamp>("testInstanceLamp", 0, true);
			DeviceComponent = client.getComponent<prototype::Device>("testInstanceDevice", 0, true);
			Lamp2Component = client.getComponent<prototype::Lamp2>("testInstanceLamp2", 0, true);
		}//

		void tearDown()
		{
			client.releaseComponent("testInstanceLamp");
			client.releaseComponent("testInstanceDevice");
			client.releaseComponent("testInstanceLamp2");
		}//
		
		///////////////////////////////////////////////////////////
		// Test methods
		///////////////////////////////////////////////////////////
		
		void testWarmUp()
		{
			// TODO (generator autoimplementation)
		}//
		void testSwitchOn()
		{
			// TODO (generator autoimplementation)
		}//
		void testSwitchOff()
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
	
	// testWarmUp
	CppUnit::TestCaller<Lamp2ImplTest> WarmUpTest ("testInstanceLamp2Impl", &Lamp2ImplTest::testWarmUp);
	
	// testSwitchOn
	CppUnit::TestCaller<Lamp2ImplTest> SwitchOnTest ("testInstanceLamp2Impl", &Lamp2ImplTest::testSwitchOn);
	
	// testSwitchOff
	CppUnit::TestCaller<Lamp2ImplTest> SwitchOffTest ("testInstanceLamp2Impl", &Lamp2ImplTest::testSwitchOff);
	
	// testStandby
	CppUnit::TestCaller<Lamp2ImplTest> StandbyTest ("testInstanceLamp2Impl", &Lamp2ImplTest::testStandby);
	
	// testOnline
	CppUnit::TestCaller<Lamp2ImplTest> OnlineTest ("testInstanceLamp2Impl", &Lamp2ImplTest::testOnline);
	
	// testOff
	CppUnit::TestCaller<Lamp2ImplTest> OffTest ("testInstanceLamp2Impl", &Lamp2ImplTest::testOff);
	
	// testSetup
	CppUnit::TestCaller<Lamp2ImplTest> SetupTest ("testInstanceLamp2Impl", &Lamp2ImplTest::testSetup);
	
	
	client.logout();
}
