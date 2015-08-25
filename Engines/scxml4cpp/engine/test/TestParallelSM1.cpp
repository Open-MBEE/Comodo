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
 * $Id: TestParallelSM1.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */


#include "TestParallelSM1.h"
 
CPPUNIT_TEST_SUITE_REGISTRATION (TestParallelSM1);
 
void TestParallelSM1::setUp() {
	mA0Entry = new ActionTest("s0Entry");
	mA0Exit = new ActionTest("s0Exit");
	mA1Entry = new ActionTest("s1Entry");
	mA1Exit = new ActionTest("s1Exit");
	mA2Entry = new ActionTest("s2Entry");
	mA2Exit = new ActionTest("s2Exit");
	mA3Entry = new ActionTest("s3Entry");
	mA3Exit = new ActionTest("s3Exit");
	mA4Entry = new ActionTest("s4Entry");
	mA4Exit = new ActionTest("s4Exit");

	mS0 = new StateParallel("s0");
	mS1 = new StateAtomic("s1");
	mS2 = new StateAtomic("s2");
	mS3 = new StateAtomic("s3");
	mS4 = new StateAtomic("s4");

	mS0->addSubstate(mS1);
	mS0->addSubstate(mS2);
	mS0->addSubstate(mS3);
	mS0->addSubstate(mS4);		
	mS0->setInitialState(mS1, NULL);
	mS0->setInitialState(mS2, NULL);
	mS0->setInitialState(mS3, NULL);
	mS0->setInitialState(mS4, NULL);
	
	mS0->addEntryAction(mA0Entry);
	mS0->addExitAction(mA0Exit);
	mS1->addEntryAction(mA1Entry);
	mS1->addExitAction(mA1Exit);
	mS2->addEntryAction(mA2Entry);
	mS2->addExitAction(mA2Exit);
	mS3->addEntryAction(mA3Entry);
	mS3->addExitAction(mA3Exit);
	mS4->addEntryAction(mA4Entry);
	mS4->addExitAction(mA4Exit);

	mSM = new StateMachine("Hello");
	mSM->addSubstate(mS0);
	mSM->setInitialState(mS0, NULL);
	mSM->setFinalState(mS0);
}

void TestParallelSM1::tearDown() {
	delete mSM;

	delete mA0Entry;
	delete mA0Exit;
	delete mA1Entry;
	delete mA1Exit;
	delete mA2Entry;
	delete mA2Exit;
	delete mA3Entry;
	delete mA3Exit;
	delete mA4Entry;
	delete mA4Exit;

	delete mS0;
	delete mS1;
	delete mS2;
	delete mS3;
	delete mS4;
}

void TestParallelSM1::testExecute(){

	Context* ctx = new Context();
	Executor* exec = new Executor(mSM, ctx);

	CPPUNIT_ASSERT(((ActionTest*)mA0Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA3Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA4Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA0Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA3Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA4Exit)->getExecuteCounter() == 0);

	set<State*> status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 0);

	exec->start();
	CPPUNIT_ASSERT(((ActionTest*)mA0Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA0Exit)->getExecuteCounter() == 0);		
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA3Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA4Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA0Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA3Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA4Exit)->getExecuteCounter() == 0);

	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 5);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS1) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS3) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS4) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS0) > 0);

	exec->stop();
	CPPUNIT_ASSERT(((ActionTest*)mA0Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA0Exit)->getExecuteCounter() == 1);		
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA3Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA4Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA0Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA3Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA4Exit)->getExecuteCounter() == 1);

	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 0);

}
