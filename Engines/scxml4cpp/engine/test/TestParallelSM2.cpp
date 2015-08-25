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
 * $Id: TestParallelSM2.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */


#include "TestParallelSM2.h"
 
CPPUNIT_TEST_SUITE_REGISTRATION (TestParallelSM2);
 
void TestParallelSM2::setUp() {
	mA0Entry = new ActionTest("onlineEntry");
	mA0Exit = new ActionTest("onlineExit");
	mA1Entry = new ActionTest("acquistionEntry");
	mA1Exit = new ActionTest("acquisitionExit");
	mA2Entry = new ActionTest("simulationEntry");
	mA2Exit = new ActionTest("simulationExit");
	mA11Entry = new ActionTest("idleEntry");
	mA11Exit = new ActionTest("idleExit");
	mA12Entry = new ActionTest("recordingEntry");
	mA12Exit = new ActionTest("recordingExit");
	mA21Entry = new ActionTest("normalEntry");
	mA21Exit = new ActionTest("normalExit");
	mA22Entry = new ActionTest("simulatEntry");
	mA22Exit = new ActionTest("simulatExit");

	mStartRec = new Event("startRec", Event::CHANGE_EVENT);
	mStopRec = new Event("stopRec", Event::CHANGE_EVENT);

	mStartSim = new Event("startSim", Event::CHANGE_EVENT);
	mStopSim = new Event("stopSim", Event::CHANGE_EVENT);
		
	mS0 = new StateParallel("Online");
	mS1 = new StateCompound("Acquisition");
	mS2 = new StateCompound("Simulation");
	
	mS11 = new StateAtomic("Idle");
	mS12 = new StateAtomic("Recording");
	mS21 = new StateAtomic("Normal");
	mS22 = new StateAtomic("Simulat");

	mS0->addSubstate(mS1);
	mS0->addSubstate(mS2);
	
	mS1->addSubstate(mS11);
	mS1->addSubstate(mS12);

	mS2->addSubstate(mS21);
	mS2->addSubstate(mS22);
	
	mS0->setInitialState(mS1, NULL);		
	mS0->setInitialState(mS2, NULL);
	
	mS1->setInitialState(mS11, NULL);
	
	mS2->setInitialState(mS21, NULL);
	
	mS0->addEntryAction(mA0Entry);
	mS0->addExitAction(mA0Exit);
	mS1->addEntryAction(mA1Entry);
	mS1->addExitAction(mA1Exit);
	mS2->addEntryAction(mA2Entry);
	mS2->addExitAction(mA2Exit);
	
	mS11->addEntryAction(mA11Entry);
	mS11->addExitAction(mA11Exit);
	mS12->addEntryAction(mA12Entry);
	mS12->addExitAction(mA12Exit);
	mS21->addEntryAction(mA21Entry);
	mS21->addExitAction(mA21Exit);
	mS22->addEntryAction(mA22Entry);
	mS22->addExitAction(mA22Exit);

	mS11->addTransition(mS12, mStartRec, NULL, NULL);
	mS12->addTransition(mS11, mStopRec, NULL, NULL);
	mS21->addTransition(mS22, mStartSim, NULL, NULL);
	mS22->addTransition(mS21, mStopSim, NULL, NULL);
	
	mSM = new StateMachine("Hello");
	mSM->addSubstate(mS0);
	mSM->setInitialState(mS0, NULL);
}

void TestParallelSM2::tearDown() {
	delete mSM;

	delete mStartRec;
	delete mStopRec;
	delete mStartSim;
	delete mStopSim;
	
	delete mA0Entry;
	delete mA0Exit;
	delete mA1Entry;
	delete mA1Exit;
	delete mA2Entry;
	delete mA2Exit;
	delete mA11Entry;
	delete mA11Exit;
	delete mA12Entry;
	delete mA12Exit;
	delete mA21Entry;
	delete mA21Exit;
	delete mA22Entry;
	delete mA22Exit;

	delete mS0;
	delete mS1;
	delete mS2;
	delete mS11;
	delete mS12;
	delete mS21;
	delete mS22;
}

void TestParallelSM2::testExecute(){
	Context* ctx = new Context();
	Executor* exec = new Executor(mSM, ctx);
	
	set<State*> status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA0Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA0Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA11Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA11Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA12Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA12Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA21Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA21Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA22Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA22Exit)->getExecuteCounter() == 0);
	
	exec->start();

	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 5);		
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS0) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS1) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS11) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS21) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA0Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA0Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA11Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA11Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA12Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA12Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA21Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA21Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA22Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA22Exit)->getExecuteCounter() == 0);

	exec->processEvent(mStartSim);

	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 5);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS0) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS1) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS11) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS22) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA0Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA0Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA11Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA11Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA12Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA12Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA21Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA21Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA22Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA22Exit)->getExecuteCounter() == 0);

	exec->processEvent(mStartRec);

	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 5);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS0) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS1) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS12) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS22) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA0Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA0Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA11Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA11Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA12Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA12Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA21Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA21Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA22Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA22Exit)->getExecuteCounter() == 0);
	
	exec->processEvent(mStopRec);		

	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 5);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS0) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS1) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS11) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS22) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA0Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA0Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA11Entry)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mA11Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA12Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA12Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA21Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA21Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA22Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA22Exit)->getExecuteCounter() == 0);
	
	exec->processEvent(mStopSim);

	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 5);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS0) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS1) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS11) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS21) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA0Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA0Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA11Entry)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mA11Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA12Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA12Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA21Entry)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mA21Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA22Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA22Exit)->getExecuteCounter() == 1);
	
	exec->stop();

	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA0Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA0Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA11Entry)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mA11Exit)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mA12Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA12Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA21Entry)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mA21Exit)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mA22Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA22Exit)->getExecuteCounter() == 1);
}
