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
 * $Id: TestFlatSM.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */


#include "TestFlatSM.h"
 
CPPUNIT_TEST_SUITE_REGISTRATION (TestFlatSM);

void TestFlatSM::setUp() {
	/*
	 * null -{a1Init}-> s1 -[c1Cond]{a1Trans}-> s2 -[c2Trans]e2{a2Trans}-> s3 --> s4 -> null
	 * s2 -e22{a22Trans}-> s2      self transition with event and entry/exit actions executions
	 * s2 -e222{a222Trans}-> s2      self transition with event without entry/exit actions executions
	 */
	mA1Entry = new ActionTest("s1Entry");
	mA1Exit = new ActionTest("s1Exit");
	mA1Trans = new ActionTest("s1Trans");
	mA1Init = new ActionTest("s1Init");
	mC1Trans = new ActionTest("c1Trans");

	mA2Entry = new ActionTest("s2Entry");
	mA2Exit = new ActionTest("s2Exit");
	mA2Trans = new ActionTest("s2Trans");
	mC2Trans = new ActionTest("c2Trans");
	mA22Trans = new ActionTest("s22SelfTrans");
	mA222Trans = new ActionTest("s222SelfTrans");

	mA3Entry = new ActionTest("s3Entry");
	mA3Exit = new ActionTest("s3Exit");

	mA4Entry = new ActionTest("s4Entry");
	mA4Exit = new ActionTest("s4Exit");

	mE2 = new Event("e2", Event::CHANGE_EVENT);
	mE22 = new Event("e22", Event::CHANGE_EVENT);
	mE222 = new Event("e222", Event::CHANGE_EVENT);
	mE3 = new Event("e3", Event::CHANGE_EVENT);

	mS1 = new StateAtomic("s1");
	mS2 = new StateAtomic("s2");
	mS3 = new StateAtomic("s3");
	mS4 = new StateAtomic("s4");

	mS1->addEntryAction(mA1Entry);
	mS1->addExitAction(mA1Exit);
	mS2->addEntryAction(mA2Entry);
	mS2->addExitAction(mA2Exit);
	mS3->addEntryAction(mA3Entry);
	mS3->addExitAction(mA3Exit);
	mS4->addEntryAction(mA4Entry);
	mS4->addExitAction(mA4Exit);

	//addTransition(list<State*> targets, Event *event, Action *condition, Action *action)
	//addTransition(State *target, Event *event, Action *condition, Action *action) 

	mS1->addTransition(mS2, NULL, mC1Trans, mA1Trans);
	mS2->addTransition(mS3, mE2, mC2Trans, mA2Trans);
	mS3->addTransition(mS4, NULL, NULL, NULL);
	mS2->addTransition(mS2, mE22, NULL, mA22Trans);	
	mS2->addTransition(NULL, mE222, NULL, mA222Trans);

	mSM = new StateMachine("Hello");
	mSM->addSubstate(mS1);
	mSM->addSubstate(mS2);
	mSM->addSubstate(mS3);
	mSM->addSubstate(mS4);
	mSM->setInitialState(mS1, mA1Init);
	mSM->setFinalState(mS4);

	mHelper->printStateMachine(mSM);
}

void TestFlatSM::tearDown() {
	delete mE2;
	delete mE22;
	delete mE222;
	delete mE3;

	delete mSM;

	delete mA1Entry;
	delete mA1Exit;
	delete mA1Trans;
	delete mA1Init;
	delete mC1Trans;

	delete mA2Entry;
	delete mA2Exit;
	delete mA2Trans;
	delete mA22Trans;
	delete mA222Trans;
	delete mC2Trans;

	delete mA3Entry;
	delete mA3Exit;

	delete mA4Entry;
	delete mA4Exit;

	delete mS1;
	delete mS2;
	delete mS3;
	delete mS4;
}
		
void TestFlatSM::testExecuteWithTrueCond(){
	Context* ctx = new Context();
	Executor* exec = new Executor(mSM, ctx);

	((ActionTest*)mC1Trans)->setEvaluateResult(true);
	((ActionTest*)mC2Trans)->setEvaluateResult(true);

	CPPUNIT_ASSERT(((ActionTest*)mA1Init)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mC1Trans)->getEvaluateCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Trans)->getExecuteCounter() == 0);
	
	exec->start();
	set<State*> status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Init)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mC1Trans)->getEvaluateCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Trans)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
	
	exec->processEvent(mE2);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS4) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mC2Trans)->getEvaluateCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Trans)->getExecuteCounter() == 1);		
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA3Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA3Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA4Entry)->getExecuteCounter() == 1);

	exec->stop();
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA4Exit)->getExecuteCounter() == 1);	
}

void TestFlatSM::testExecuteWithFalseCond1() {  
	Context* ctx = new Context();
	Executor* exec = new Executor(mSM, ctx);
	
	((ActionTest*)mC1Trans)->setEvaluateResult(false);
	((ActionTest*)mC2Trans)->setEvaluateResult(false);

	CPPUNIT_ASSERT(((ActionTest*)mA1Init)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mC1Trans)->getEvaluateCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Trans)->getExecuteCounter() == 0);
	
	exec->start();		
	set<State*> status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS1) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Init)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mC1Trans)->getEvaluateCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Trans)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 0);
			
	exec->processEvent(mE2);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS1) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mC2Trans)->getEvaluateCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Trans)->getExecuteCounter() == 0);		
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA3Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA3Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA4Entry)->getExecuteCounter() == 0);

	exec->stop();
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA4Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 1);
}

void TestFlatSM::testExecuteWithFalseCond2() {  
	Context* ctx = new Context();
	Executor* exec = new Executor(mSM, ctx);
	
	((ActionTest*)mC1Trans)->setEvaluateResult(true);
	((ActionTest*)mC2Trans)->setEvaluateResult(false);

	CPPUNIT_ASSERT(((ActionTest*)mA1Init)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mC1Trans)->getEvaluateCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Trans)->getExecuteCounter() == 0);
	
	exec->start();		
	set<State*> status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Init)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mC1Trans)->getEvaluateCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Trans)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
			
	exec->processEvent(mE2);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1); 
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mC2Trans)->getEvaluateCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Trans)->getExecuteCounter() == 0);		
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA3Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA3Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA4Entry)->getExecuteCounter() == 0);

	exec->stop();
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 0); 
	CPPUNIT_ASSERT(((ActionTest*)mA4Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 1);
}

void TestFlatSM::testExecuteWithWrongEvent() {  
	Context* ctx = new Context();
	Executor* exec = new Executor(mSM, ctx);
	
	((ActionTest*)mC1Trans)->setEvaluateResult(true);
	((ActionTest*)mC2Trans)->setEvaluateResult(true);

	CPPUNIT_ASSERT(((ActionTest*)mA1Init)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mC1Trans)->getEvaluateCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Trans)->getExecuteCounter() == 0);
	
	exec->start();		
	set<State*> status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA1Init)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mC1Trans)->getEvaluateCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Trans)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA1Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
			
	exec->processEvent(mE3);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1); 
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mC2Trans)->getEvaluateCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Trans)->getExecuteCounter() == 0);		
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA3Entry)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA3Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA4Entry)->getExecuteCounter() == 0);

	exec->stop();
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 0); 
	CPPUNIT_ASSERT(((ActionTest*)mA4Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 1);
}

void TestFlatSM::testExecuteSelfTrans1() {  
	Context* ctx = new Context();
	Executor* exec = new Executor(mSM, ctx);
	
	((ActionTest*)mC1Trans)->setEvaluateResult(true);
	((ActionTest*)mC2Trans)->setEvaluateResult(true);
	
	exec->start();		
	set<State*> status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA22Trans)->getExecuteCounter() == 0);
		
	exec->processEvent(mE22);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA22Trans)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA222Trans)->getExecuteCounter() == 0);

	exec->processEvent(mE2);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS4) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mA22Trans)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA222Trans)->getExecuteCounter() == 0);

	exec->stop();
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA4Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 2);
	CPPUNIT_ASSERT(((ActionTest*)mA22Trans)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA222Trans)->getExecuteCounter() == 0);
}

void TestFlatSM::testExecuteSelfTrans2 () {  
	Context* ctx = new Context();
	Executor* exec = new Executor(mSM, ctx);
	
	((ActionTest*)mC1Trans)->setEvaluateResult(true);
	((ActionTest*)mC2Trans)->setEvaluateResult(true);
	
	exec->start();		
	set<State*> status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA22Trans)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA222Trans)->getExecuteCounter() == 0);

	exec->processEvent(mE222);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS2) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA22Trans)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA222Trans)->getExecuteCounter() == 1);
	
	exec->processEvent(mE2);
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 1);
	CPPUNIT_ASSERT(count(status.begin(),status.end(), mS4) > 0);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA22Trans)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA222Trans)->getExecuteCounter() == 1);

	exec->stop();
	status = exec->getStatus();
	CPPUNIT_ASSERT(status.size() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA4Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Entry)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA2Exit)->getExecuteCounter() == 1);
	CPPUNIT_ASSERT(((ActionTest*)mA22Trans)->getExecuteCounter() == 0);
	CPPUNIT_ASSERT(((ActionTest*)mA222Trans)->getExecuteCounter() == 1);
}
