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
#include "Lamp1C.h"

maci::SimpleClient client;

class Lamp1ImplTest: public CppUnit::TestFixture 
{
	private:
	
		prototype::Lamp *LampComponent;
		prototype::Device *DeviceComponent;
		prototype::Lamp1 *Lamp1Component;
		
	public:
	
		void setUp() 
		{
			LampComponent = client.getComponent<prototype::Lamp>("testInstanceLamp", 0, true);
			DeviceComponent = client.getComponent<prototype::Device>("testInstanceDevice", 0, true);
			Lamp1Component = client.getComponent<prototype::Lamp1>("testInstanceLamp1", 0, true);
		}//

		void tearDown()
		{
			client.releaseComponent("testInstanceLamp");
			client.releaseComponent("testInstanceDevice");
			client.releaseComponent("testInstanceLamp1");
		}//
		
		///////////////////////////////////////////////////////////
		// Test methods
		///////////////////////////////////////////////////////////
		
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
	
	// testSwitchOn
	CppUnit::TestCaller<Lamp1ImplTest> SwitchOnTest ("testInstanceLamp1Impl", &Lamp1ImplTest::testSwitchOn);
	
	// testSwitchOff
	CppUnit::TestCaller<Lamp1ImplTest> SwitchOffTest ("testInstanceLamp1Impl", &Lamp1ImplTest::testSwitchOff);
	
	// testStandby
	CppUnit::TestCaller<Lamp1ImplTest> StandbyTest ("testInstanceLamp1Impl", &Lamp1ImplTest::testStandby);
	
	// testOnline
	CppUnit::TestCaller<Lamp1ImplTest> OnlineTest ("testInstanceLamp1Impl", &Lamp1ImplTest::testOnline);
	
	// testOff
	CppUnit::TestCaller<Lamp1ImplTest> OffTest ("testInstanceLamp1Impl", &Lamp1ImplTest::testOff);
	
	// testSetup
	CppUnit::TestCaller<Lamp1ImplTest> SetupTest ("testInstanceLamp1Impl", &Lamp1ImplTest::testSetup);
	
	
	client.logout();
}
