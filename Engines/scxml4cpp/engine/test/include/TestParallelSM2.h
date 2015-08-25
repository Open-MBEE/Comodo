/*
 *    scampl4cpp/engine
 *
 *    Copyright by European Southern Observatory, 2012
 *    All rights reserved
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
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
 *    02111-1307 USA.
 */

/*
 * $Id: TestParallelSM2.h 1061 2015-07-13 15:03:59Z landolfa $
 */


#ifndef TestParallelSM2_H
#define TestParallelSM2_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <string>
#include <stdio.h>
#include <cppeng/Context.h>
#include <cppeng/Executor.h>
#include <cppeng/State.h>
#include <cppeng/StateAtomic.h>
#include <cppeng/StateParallel.h>
#include <cppeng/StateCompound.h>
#include <cppeng/StateMachine.h>
#include <cppeng/Action.h>
#include <cppunit/TestFixture.h>
#include <cppunit/extensions/HelperMacros.h>

#include "ActionTest.h"

using namespace std;

class TestParallelSM2 : public CppUnit::TestFixture
{
	CPPUNIT_TEST_SUITE(TestParallelSM2);
	CPPUNIT_TEST(testExecute);
	CPPUNIT_TEST_SUITE_END();
public:	
	void setUp();
	void tearDown();

protected:
	void testExecute();

private:
	StateMachine* mSM;

	Event* mStartRec;
	Event* mStopRec;
	Event* mStartSim;
	Event* mStopSim;
	
	Action* mA0Entry;
	Action* mA0Exit;
	Action* mA1Entry;
	Action* mA1Exit;
	Action* mA2Entry;
	Action* mA2Exit;
	Action* mA11Entry;
	Action* mA11Exit;
	Action* mA12Entry;
	Action* mA12Exit;
	Action* mA21Entry;
	Action* mA21Exit;
	Action* mA22Entry;
	Action* mA22Exit;

	State* mS0;
	State* mS1;
	State* mS2;
	State* mS11;
	State* mS12;
	State* mS21;
	State* mS22;
	
};

#endif
