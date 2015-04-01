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
#include "SchedulerC.h"

maci::SimpleClient client;

class SchedulerImplTest: public CppUnit::TestFixture 
{
	private:
	
		SORT::Scheduler *SchedulerComponent;
		
	public:
	
		void setUp() 
		{
			SchedulerComponent = client.getComponent<SORT::Scheduler>("testInstanceScheduler", 0, true);
		}//

		void tearDown()
		{
			client.releaseComponent("testInstanceScheduler");
		}//
		
		///////////////////////////////////////////////////////////
		// Test methods
		///////////////////////////////////////////////////////////
		
		void testStart()
		{
			// TODO (generator autoimplementation)
		}//
		void testStop()
		{
			// TODO (generator autoimplementation)
		}//
		void testProposalUnderExecution()
		{
			// TODO (generator autoimplementation)
		}//
};

int main(int argc, char *argv[]) 
{
	client.init(argc, argv);
	
	client.login();
	
	// testStart
	CppUnit::TestCaller<SchedulerImplTest> StartTest ("testInstanceSchedulerImpl", &SchedulerImplTest::testStart);
	
	// testStop
	CppUnit::TestCaller<SchedulerImplTest> StopTest ("testInstanceSchedulerImpl", &SchedulerImplTest::testStop);
	
	// testProposalUnderExecution
	CppUnit::TestCaller<SchedulerImplTest> ProposalUnderExecutionTest ("testInstanceSchedulerImpl", &SchedulerImplTest::testProposalUnderExecution);
	
	
	client.logout();
}
