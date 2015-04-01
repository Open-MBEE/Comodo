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
#include "DatabaseC.h"

maci::SimpleClient client;

class DatabaseImplTest: public CppUnit::TestFixture 
{
	private:
	
		SORT::Database *DatabaseComponent;
		
	public:
	
		void setUp() 
		{
			DatabaseComponent = client.getComponent<SORT::Database>("testInstanceDatabase", 0, true);
		}//

		void tearDown()
		{
			client.releaseComponent("testInstanceDatabase");
		}//
		
		///////////////////////////////////////////////////////////
		// Test methods
		///////////////////////////////////////////////////////////
		
		void testStoreProposal()
		{
			// TODO (generator autoimplementation)
		}//
		void testRemoveProposal()
		{
			// TODO (generator autoimplementation)
		}//
		void testGetProposals()
		{
			// TODO (generator autoimplementation)
		}//
		void testGetProposalStatus()
		{
			// TODO (generator autoimplementation)
		}//
		void testStoreImage()
		{
			// TODO (generator autoimplementation)
		}//
		void testClean()
		{
			// TODO (generator autoimplementation)
		}//
		void testSetProposalStatus()
		{
			// TODO (generator autoimplementation)
		}//
		void testGetProposalObservations()
		{
			// TODO (generator autoimplementation)
		}//
};

int main(int argc, char *argv[]) 
{
	client.init(argc, argv);
	
	client.login();
	
	// testStoreProposal
	CppUnit::TestCaller<DatabaseImplTest> StoreProposalTest ("testInstanceDatabaseImpl", &DatabaseImplTest::testStoreProposal);
	
	// testRemoveProposal
	CppUnit::TestCaller<DatabaseImplTest> RemoveProposalTest ("testInstanceDatabaseImpl", &DatabaseImplTest::testRemoveProposal);
	
	// testGetProposals
	CppUnit::TestCaller<DatabaseImplTest> GetProposalsTest ("testInstanceDatabaseImpl", &DatabaseImplTest::testGetProposals);
	
	// testGetProposalStatus
	CppUnit::TestCaller<DatabaseImplTest> GetProposalStatusTest ("testInstanceDatabaseImpl", &DatabaseImplTest::testGetProposalStatus);
	
	// testStoreImage
	CppUnit::TestCaller<DatabaseImplTest> StoreImageTest ("testInstanceDatabaseImpl", &DatabaseImplTest::testStoreImage);
	
	// testClean
	CppUnit::TestCaller<DatabaseImplTest> CleanTest ("testInstanceDatabaseImpl", &DatabaseImplTest::testClean);
	
	// testSetProposalStatus
	CppUnit::TestCaller<DatabaseImplTest> SetProposalStatusTest ("testInstanceDatabaseImpl", &DatabaseImplTest::testSetProposalStatus);
	
	// testGetProposalObservations
	CppUnit::TestCaller<DatabaseImplTest> GetProposalObservationsTest ("testInstanceDatabaseImpl", &DatabaseImplTest::testGetProposalObservations);
	
	
	client.logout();
}
