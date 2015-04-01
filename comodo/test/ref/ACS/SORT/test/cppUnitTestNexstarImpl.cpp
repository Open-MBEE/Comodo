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
#include "DevCCDC.h"

maci::SimpleClient client;

class NexstarImplTest: public CppUnit::TestFixture 
{
	private:
	
		SORT::DevCCD *DevCCDComponent;
		
	public:
	
		void setUp() 
		{
			DevCCDComponent = client.getComponent<SORT::DevCCD>("testInstanceDevCCD", 0, true);
		}//

		void tearDown()
		{
			client.releaseComponent("testInstanceDevCCD");
		}//
		
		///////////////////////////////////////////////////////////
		// Test methods
		///////////////////////////////////////////////////////////
		
		void testImage()
		{
			// TODO (generator autoimplementation)
		}//
		void testLock()
		{
			// TODO (generator autoimplementation)
		}//
		void testUnlock()
		{
			// TODO (generator autoimplementation)
		}//
		void testOn()
		{
			// TODO (generator autoimplementation)
		}//
		void testOff()
		{
			// TODO (generator autoimplementation)
		}//
};

int main(int argc, char *argv[]) 
{
	client.init(argc, argv);
	
	client.login();
	
	// testImage
	CppUnit::TestCaller<NexstarImplTest> ImageTest ("testInstanceNexstarImpl", &NexstarImplTest::testImage);
	
	// testLock
	CppUnit::TestCaller<NexstarImplTest> LockTest ("testInstanceNexstarImpl", &NexstarImplTest::testLock);
	
	// testUnlock
	CppUnit::TestCaller<NexstarImplTest> UnlockTest ("testInstanceNexstarImpl", &NexstarImplTest::testUnlock);
	
	// testOn
	CppUnit::TestCaller<NexstarImplTest> OnTest ("testInstanceNexstarImpl", &NexstarImplTest::testOn);
	
	// testOff
	CppUnit::TestCaller<NexstarImplTest> OffTest ("testInstanceNexstarImpl", &NexstarImplTest::testOff);
	
	
	client.logout();
}
