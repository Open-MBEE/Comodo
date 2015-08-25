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
 * $Id: TestCompositeSM.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */


#include "TestCompositeSM.h"
 
CPPUNIT_TEST_SUITE_REGISTRATION (TestCompositeSM);
 
void TestCompositeSM::setUp() {
	// s1 -[c1]e1{a1}-> s2 -e2{a2}-> s3 -e3{a3}-> s4  
	// s31 -e31-> s32 -e32-> s33
	mA1Entry = new ActionTest("s1Entry");
	mA1Exit = new ActionTest("s1Exit");
	mA1Trans = new ActionTest("s1Trans");
	mA1Init = new ActionTest("a1Init");
	mC1Trans = new ActionTest("c1Trans");
	mA2Entry = new ActionTest("s2Entry");
	mA2Exit = new ActionTest("s2Exit");
	mA2Trans = new ActionTest("s2Trans");
	mA3Entry = new ActionTest("s3Entry");
	mA3Exit = new ActionTest("s3Exit");
	mA3Trans = new ActionTest("s3Trans");
	mA4Entry = new ActionTest("s4Entry");
	mA4Exit = new ActionTest("s4Exit");
	mA31Entry = new ActionTest("s31Entry");
	mA31Exit = new ActionTest("s31Exit");
	mA32Entry = new ActionTest("s32Entry");
	mA32Exit = new ActionTest("s32Exit");
	mA33Entry = new ActionTest("s33Entry");
	mA33Exit = new ActionTest("s33Exit");

	mE1 = new Event("e1", Event::CHANGE_EVENT);
	mE2 = new Event("e2", Event::CHANGE_EVENT);
	mE3 = new Event("e3", Event::CHANGE_EVENT);
	mE31 = new Event("e31", Event::CHANGE_EVENT);
	mE32 = new Event("e32", Event::CHANGE_EVENT);
	
	mS1 = new StateAtomic("s1");
	mS2 = new StateAtomic("s2");
	mS3 = new StateCompound("s3");
	mS31 = new StateAtomic("s31");
	mS32 = new StateAtomic("s32");
	mS33 = new StateAtomic("s33");

	mS3->addSubstate(mS31);
	mS3->addSubstate(mS32);
	mS3->addSubstate(mS33);
	mS3->setInitialState(mS31, NULL);
	mS4 = new StateAtomic("s4");

	mS1->addEntryAction(mA1Entry);
	mS1->addExitAction(mA1Exit);
	mS2->addEntryAction(mA2Entry);
	mS2->addExitAction(mA2Exit);
	mS3->addEntryAction(mA3Entry);
	mS3->addExitAction(mA3Exit);
	mS4->addEntryAction(mA4Entry);
	mS4->addExitAction(mA4Exit);
	mS31->addEntryAction(mA31Entry);
	mS31->addExitAction(mA31Exit);
	mS32->addEntryAction(mA32Entry);
	mS32->addExitAction(mA32Exit);
	mS33->addEntryAction(mA33Entry);
	mS33->addExitAction(mA33Exit);

	mS1->addTransition(mS2, mE1, mC1Trans, mA1Trans);
	mS2->addTransition(mS3, mE2, NULL, mA2Trans);
	mS3->addTransition(mS4, mE3, NULL, mA3Trans);
	mS31->addTransition(mS32, mE31, NULL, NULL);
	mS32->addTransition(mS33, mE32, NULL, NULL);

	mSM = new StateMachine("Hello");
	mSM->addSubstate(mS1);
	mSM->addSubstate(mS2);
	mSM->addSubstate(mS3);
	mSM->addSubstate(mS4);		
	mSM->setInitialState(mS1, mA1Init);
	mSM->setFinalState(mS4);

}

void TestCompositeSM::tearDown() {
	delete mE1;
	delete mE2;
	delete mE3;
	delete mE31;
	delete mE32;
	
	delete mSM;

	delete mA1Entry;
	delete mA1Exit;
	delete mA1Trans;
	delete mA1Init;
	delete mC1Trans;
	delete mA2Entry;
	delete mA2Exit;
	delete mA2Trans;
	delete mA3Entry;
	delete mA3Exit;
	delete mA3Trans;
	delete mA4Entry;
	delete mA4Exit;
	delete mA31Entry;
	delete mA31Exit;
	delete mA32Entry;
	delete mA32Exit;
	delete mA33Entry;
	delete mA33Exit;

	delete mS1;
	delete mS2;
	delete mS3;
	delete mS31;
	delete mS32;
	delete mS33;
	delete mS4;
}

void TestCompositeSM::testExecuteWithTrueCond(){
	Context* ctx = new Context();
	Executor* exec = new Executor(mSM, ctx);
	
	((ActionTest*)mC1Trans)->setEvaluateResult(true);

	// s1 -[c1]e1{a1}-> s2 -e2{a2}-> s3 -e3{a3}-> s4  
	// s31 -e31-> s32 -e32-> s33

	CPPUNIT_ASSERT(((ActionTest*)mA1Init)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 0);
	
	exec->start();		
	set<State*> status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS1) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Init)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 1);

	exec->processEvent(mE1);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mC1Trans)->getEvaluateCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Trans)->getExecuteCounter() == 1);		
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
	
	exec->processEvent(mE2);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 2);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS3) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS31) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Trans)->getExecuteCounter() == 1);		
	CPPUNIT_ASSERT(((ActionTest*)mA3Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA31Entry)->getExecuteCounter() == 1);
	
	exec->processEvent(mE31);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 2);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS3) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS32) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA31Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA32Entry)->getExecuteCounter() == 1);
	
	exec->processEvent(mE32);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 2);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS3) > 0);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS33) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA32Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA33Entry)->getExecuteCounter() == 1);
	
	exec->processEvent(mE3);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS4) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA33Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA3Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA3Trans)->getExecuteCounter() == 1);				
	CPPUNIT_ASSERT(((ActionTest*)mA4Entry)->getExecuteCounter() == 1);
	
	exec->stop();
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA4Exit)->getExecuteCounter() == 1);

}
