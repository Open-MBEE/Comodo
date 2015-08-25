/*
 *    scampl4cpp/parser
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
 * $Id: TestParallel.h 1061 2015-07-13 15:03:59Z landolfa $
 */

#ifndef TestParallel_H
#define TestParallel_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <stdio.h>
#include <boost/thread.hpp>
#include <cppunit/TestFixture.h>
#include <cppunit/extensions/HelperMacros.h>

#include <cppeng/Helper.h>
#include <cppeng/StateMachine.h>
#include <cppeng/Event.h>
#include <cppeng/Action.h>
#include <cppeng/Executor.h>
#include <cppeng/Context.h>
#include <cppeng/State.h>
#include <cpppar/SCXMLReader.h>
#include <cpppar/DOMSCXMLReader.h>

#include "ActionTest.h"

using namespace std;

class TestParallel : public CppUnit::TestFixture
{
	CPPUNIT_TEST_SUITE(TestParallel);
	CPPUNIT_TEST(testExecute1);
	CPPUNIT_TEST_SUITE_END();
public:	
	void setUp();
	void tearDown();
	list<Action*> mActions;
protected:
	void testExecute1();
private:
	Helper* mHelper;
	Event* mStartRec;
	Event* mStopRec;
	Event* mStartSim;
	Event* mStopSim;
	
	Action* mOnlineEntry;
	Action* mOnlineExit;
	Action* mAcquisitionEntry;
	Action* mAcquisitionExit;
	Action* mIdleEntry;
	Action* mIdleExit;
	Action* mRecordingEntry;
	Action* mRecordingExit;
	Action* mModeEntry;
	Action* mModeExit;
	Action* mNormalEntry;
	Action* mNormalExit;
	Action* mSimulationEntry;
	Action* mSimulationExit;
};

#endif
