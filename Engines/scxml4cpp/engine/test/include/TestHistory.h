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
 * $Id: TestHistory.h 1061 2015-07-13 15:03:59Z landolfa $
 */


#ifndef TestHistory_H
#define TestHistory_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <string>
#include <stdio.h>
#include <cppeng/Context.h>
#include <cppeng/Event.h>
#include <cppeng/Executor.h>
#include <cppeng/State.h>
#include <cppeng/StateAtomic.h>
#include <cppeng/StateCompound.h>
#include <cppeng/StateMachine.h>
#include <cppeng/StateHistory.h>
#include <cppeng/Action.h>
#include <cppunit/TestFixture.h>
#include <cppunit/extensions/HelperMacros.h>

#include "ActionTest.h"

using namespace std;

class TestHistory : public CppUnit::TestFixture
{
	CPPUNIT_TEST_SUITE(TestHistory);
	CPPUNIT_TEST(testDeepHistory);
	CPPUNIT_TEST(testShallowHistory);
	CPPUNIT_TEST_SUITE_END();
public:	
	void setUp();
	void tearDown();
	void test(State*);

protected:
	void testDeepHistory();
	void testShallowHistory();

private:
	Event* mE1;
	Event* mE2;
	Event* mE3;
	Event* mE4;
	Event* mE31;
	Event* mE32;
	Event* mEh;

	StateMachine* mSM;

	Action* mA1Entry;
	Action* mA1Exit;
	Action* mA2Entry;
	Action* mA2Exit;
	Action* mA3Entry;
	Action* mA3Exit;
	Action* mA4Entry;
	Action* mA4Exit;
	Action* mA5Entry;
	Action* mA5Exit;
	Action* mA31Entry;
	Action* mA31Exit;
	Action* mA32Entry;
	Action* mA32Exit;
	Action* mA33Entry;
	Action* mA33Exit;
	Action* mAhTrans;

	State* mS1;
	State* mS2;
	State* mS3;
	State* mS31;
	State* mS32;
	State* mS33;
	State* mS4;
	State* mS5;
	State* mHistoryDeep;
	State* mHistoryShallow;
};

#endif
