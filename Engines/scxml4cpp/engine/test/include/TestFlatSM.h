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
 * $Id: TestFlatSM.h 1061 2015-07-13 15:03:59Z landolfa $
 */


#ifndef TestFlatSM_H
#define TestFlatSM_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <string>
#include <stdio.h>
#include <cppeng/Event.h>
#include <cppeng/StateMachine.h>
#include <cppeng/Action.h>
#include <cppeng/State.h>
#include <cppeng/StateAtomic.h>
#include <cppeng/Context.h>
#include <cppeng/Executor.h>
#include <cppunit/TestFixture.h>
#include <cppunit/extensions/HelperMacros.h>

#include "ActionTest.h"

using namespace std;

class TestFlatSM : public CppUnit::TestFixture
{
	CPPUNIT_TEST_SUITE(TestFlatSM);
	CPPUNIT_TEST(testExecuteWithTrueCond);
	CPPUNIT_TEST(testExecuteWithFalseCond1);
	CPPUNIT_TEST(testExecuteWithFalseCond2);
	CPPUNIT_TEST(testExecuteWithWrongEvent);
	CPPUNIT_TEST(testExecuteSelfTrans1);
	CPPUNIT_TEST(testExecuteSelfTrans2);
	CPPUNIT_TEST_SUITE_END();
public:	
	void setUp();
	void tearDown();
protected:
	void testExecuteWithTrueCond();
	void testExecuteWithFalseCond1();
	void testExecuteWithFalseCond2();
	void testExecuteWithWrongEvent();
	void testExecuteSelfTrans1();
	void testExecuteSelfTrans2();
private:
	Event* mE2;
	Event* mE22;
	Event* mE222;
	Event* mE3;

	StateMachine* mSM;

	Action* mA1Entry;
	Action* mA1Exit;
	Action* mA1Trans;
	Action* mA1Init;
	Action* mC1Trans;

	Action* mA2Entry;
	Action* mA2Exit;
	Action* mA2Trans;
	Action* mA22Trans;
	Action* mA222Trans;
	Action* mC2Trans;

	Action* mA3Entry;
	Action* mA3Exit;

	Action* mA4Entry;
	Action* mA4Exit;

	State* mS1;
	State* mS2;
	State* mS3;
	State* mS4;

	Helper* mHelper;
};

#endif
