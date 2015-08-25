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
 * $Id: TestParallel.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */


#include "TestParallel.h"
 
CPPUNIT_TEST_SUITE_REGISTRATION (TestParallel);

void TestParallel::setUp() {

	mStartRec = new Event("startRec", Event::CHANGE_EVENT);
	mStopRec = new Event("stopRec", Event::CHANGE_EVENT);

	mStartSim = new Event("startSim", Event::CHANGE_EVENT);
	mStopSim = new Event("stopSim", Event::CHANGE_EVENT);

	mOnlineEntry = new ActionTest("onlineEntry");
	mActions.push_back(mOnlineEntry);
	mOnlineExit = new ActionTest("onlineExit");
	mActions.push_back(mOnlineExit);
	mAcquisitionEntry = new ActionTest("acquisitionEntry");
	mActions.push_back(mAcquisitionEntry);
	mAcquisitionExit = new ActionTest("acquisitionExit");
	mActions.push_back(mAcquisitionExit);
	mRecordingEntry = new ActionTest("recordingEntry");
	mActions.push_back(mRecordingEntry);
	mRecordingExit = new ActionTest("recordingExit");
	mActions.push_back(mRecordingExit);
	mIdleEntry = new ActionTest("idleEntry");
	mActions.push_back(mIdleEntry);
	mIdleExit = new ActionTest("idleExit");
	mActions.push_back(mIdleExit);
	mModeEntry = new ActionTest("modeEntry");
	mActions.push_back(mModeEntry);
	mModeExit = new ActionTest("modeExit");
	mActions.push_back(mModeExit);
	mNormalEntry = new ActionTest("normalEntry");
	mActions.push_back(mNormalEntry);
	mNormalExit = new ActionTest("normalExit");
	mActions.push_back(mNormalExit);
	mSimulationEntry = new ActionTest("simulationEntry");
	mActions.push_back(mSimulationEntry);
	mSimulationExit = new ActionTest("simulationExit");
	mActions.push_back(mSimulationExit);
}

void TestParallel::tearDown() {
	delete mHelper;
	delete mStartRec;
	delete mStopRec;
	delete mStartSim;
	delete mStopSim;
	
	delete mOnlineEntry;
	delete mOnlineExit;
	delete mAcquisitionEntry;
	delete mAcquisitionExit;
	delete mIdleEntry;
	delete mIdleExit;
	delete mRecordingEntry;
	delete mRecordingExit;
	delete mModeEntry;
	delete mModeExit;
	delete mNormalEntry;
	delete mNormalExit;
	delete mSimulationEntry;
	delete mSimulationExit;
}
		
void TestParallel::testExecute1(){

	SCXMLReader* reader = new DOMSCXMLReader();
	StateMachine* sm = reader->read("../config/scxmlsamples/TestParallel.xml", &mActions, NULL);
	CPPUNIT_ASSERT( sm->getId().compare("TestParallel") == 0 );
	mHelper->printStateMachine(sm);

	Context* ctx = new Context();
	Executor* exec = new Executor(sm, ctx);
	set<State*> status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mOnlineEntry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mOnlineExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mAcquisitionEntry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mAcquisitionExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mModeEntry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mModeExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mIdleEntry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mIdleExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mRecordingEntry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mRecordingExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mNormalEntry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mNormalExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mSimulationEntry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mSimulationExit)->getExecuteCounter() == 0);

	exec->start();
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 5);
	CPPUNIT_ASSERT(((ActionTest*)mOnlineEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mOnlineExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mAcquisitionEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mAcquisitionExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mModeEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mModeExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mIdleEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mIdleExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mRecordingEntry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mRecordingExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mNormalEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mNormalExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mSimulationEntry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mSimulationExit)->getExecuteCounter() == 0);

	exec->processEvent(mStartSim);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 5);
	CPPUNIT_ASSERT(((ActionTest*)mOnlineEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mOnlineExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mAcquisitionEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mAcquisitionExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mModeEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mModeExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mIdleEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mIdleExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mRecordingEntry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mRecordingExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mNormalEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mNormalExit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mSimulationEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mSimulationExit)->getExecuteCounter() == 0);

	exec->processEvent(mStartRec);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 5);
	CPPUNIT_ASSERT(((ActionTest*)mOnlineEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mOnlineExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mAcquisitionEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mAcquisitionExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mModeEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mModeExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mIdleEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mIdleExit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mRecordingEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mRecordingExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mNormalEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mNormalExit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mSimulationEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mSimulationExit)->getExecuteCounter() == 0);

	exec->processEvent(mStopRec);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 5);
	CPPUNIT_ASSERT(((ActionTest*)mOnlineEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mOnlineExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mAcquisitionEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mAcquisitionExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mModeEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mModeExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mIdleEntry)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mIdleExit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mRecordingEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mRecordingExit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mNormalEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mNormalExit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mSimulationEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mSimulationExit)->getExecuteCounter() == 0);

	exec->processEvent(mStopSim);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 5);
	CPPUNIT_ASSERT(((ActionTest*)mOnlineEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mOnlineExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mAcquisitionEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mAcquisitionExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mModeEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mModeExit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mIdleEntry)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mIdleExit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mRecordingEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mRecordingExit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mNormalEntry)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mNormalExit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mSimulationEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mSimulationExit)->getExecuteCounter() == 1);

	exec->stop();
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mOnlineEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mOnlineExit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mAcquisitionEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mAcquisitionExit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mModeEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mModeExit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mIdleEntry)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mIdleExit)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mRecordingEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mRecordingExit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mNormalEntry)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mNormalExit)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mSimulationEntry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mSimulationExit)->getExecuteCounter() == 1);
}

