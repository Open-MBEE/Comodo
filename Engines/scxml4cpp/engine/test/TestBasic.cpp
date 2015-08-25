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
 * $Id: TestBasic.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */


#include "TestBasic.h"
 
CPPUNIT_TEST_SUITE_REGISTRATION (TestBasic);
 
void TestBasic::setUp() {
	mS1 = new StateCompound("s1");
	mS11 = new StateCompound("s11");
	mS111 = new StateAtomic("s111");
	mS112 = new StateAtomic("s112");
	mS12 = new StateAtomic("s12");
	mS121 = new StateAtomic("s121");
	mS122 = new StateAtomic("s122");

	mS1->addSubstate(mS11);
	mS11->addSubstate(mS111);
	mS11->addSubstate(mS112);
	mS1->addSubstate(mS12);
	mS12->addSubstate(mS121);
	mS12->addSubstate(mS122);

	mS111->addTransition(mS121, NULL, NULL, NULL);
	mS121->addTransition(mS122, NULL, NULL, NULL);

	mStatus.push_back(mS112);
	mStatus.push_back(mS1);
	mStatus.push_back(mS12);
}

void TestBasic::tearDown() {
        delete mS1;
        delete mS11;
        delete mS111;
        delete mS112;
        delete mS12;
        delete mS121;
        delete mS122;
        delete mHelper;
}

void TestBasic::testStateComparator() {
	StateComparator sc;
	//sort(mStatus.begin(), mStatus.end(), sc);
	mStatus.sort(sc);
	list<State*>::iterator it = mStatus.begin();
        State* s = *it;
	CPPUNIT_ASSERT_EQUAL(s->getId(), mS1->getId());
	it++;
	s = *it;
	CPPUNIT_ASSERT_EQUAL(s->getId(), mS12->getId());
	it++;
	s = *it;
	CPPUNIT_ASSERT_EQUAL(s->getId(), mS112->getId());
}


void TestBasic::testIsDescendant() {
	CPPUNIT_ASSERT(mHelper->isDescendant(mS112, mS1));
	CPPUNIT_ASSERT(!mHelper->isDescendant(mS1, mS112));
	CPPUNIT_ASSERT(mHelper->isDescendant(mS112, NULL));
	CPPUNIT_ASSERT(!mHelper->isDescendant(NULL, mS112));
}

void TestBasic::testGetAncestors() {
        list<State*> states = mHelper->getAncestorsList(mS121);
	CPPUNIT_ASSERT(states.size() == 3);
	CPPUNIT_ASSERT(count(states.begin(),states.end(), mS1) > 0);
	CPPUNIT_ASSERT(count(states.begin(),states.end(), mS12) > 0);
	CPPUNIT_ASSERT(count(states.begin(),states.end(), mS121) > 0);

	set<State*> states1 = mHelper->getAncestors(mS122);
	CPPUNIT_ASSERT(states1.size() == 3);
	CPPUNIT_ASSERT(count(states1.begin(),states1.end(), mS1) > 0);
	CPPUNIT_ASSERT(count(states1.begin(),states1.end(), mS12) > 0);
	CPPUNIT_ASSERT(count(states1.begin(),states1.end(), mS122) > 0);

	set<State*> states2 = mHelper->getAncestors(mStatus);
	CPPUNIT_ASSERT(states2.size() == 4);
	CPPUNIT_ASSERT(count(states2.begin(),states2.end(), mS1) > 0);
	CPPUNIT_ASSERT(count(states2.begin(),states2.end(), mS11) > 0);
	CPPUNIT_ASSERT(count(states2.begin(),states2.end(), mS12) > 0);
	CPPUNIT_ASSERT(count(states2.begin(),states2.end(), mS112) > 0);
}

void TestBasic::testGetProperAncestors(){
	list<State*> states = mHelper->getProperAncestors(mS111, NULL);
	CPPUNIT_ASSERT(states.size() == 2);
	CPPUNIT_ASSERT(count(states.begin(),states.end(), mS1) > 0);
	CPPUNIT_ASSERT(count(states.begin(),states.end(), mS11) > 0);
	
	list<State*> states1 = mHelper->getProperAncestors(mS111, mS1);
	CPPUNIT_ASSERT(states1.size() == 1);
	CPPUNIT_ASSERT(count(states1.begin(),states1.end(), mS11) > 0);
}

void TestBasic::testFindLeastCommonAncestor(){
	State* lha = mHelper->findLeastCommonAncestor(mS111, mS111);
	CPPUNIT_ASSERT_EQUAL(lha->getId(), mS111->getId());
	lha = mHelper->findLeastCommonAncestor(mS111, mS112);
	CPPUNIT_ASSERT_EQUAL(lha->getId(), mS11->getId());
	lha = mHelper->findLeastCommonAncestor(mS111, NULL);
	CPPUNIT_ASSERT(lha == NULL);
}

void TestBasic::testEventMatch(){
	Event* e1 = new Event("pippo", Event::CHANGE_EVENT);
	Event* e2 = new Event("pippo", Event::CHANGE_EVENT);
	Event* e3 = new Event("pippo", Event::CALL_EVENT);
	Event* e4 = new Event("pippo", Event::CALL_EVENT);
	Event* e5 = NULL;
	Event* e6 = NULL;
	CPPUNIT_ASSERT(mHelper->eventMatch(e5, e6));
	CPPUNIT_ASSERT(mHelper->eventMatch(e1, e2));
	CPPUNIT_ASSERT(!mHelper->eventMatch(e2, e3));
	CPPUNIT_ASSERT(mHelper->eventMatch(e3, e4));
}

void TestBasic::testGetAtomicStates() {
	list<State*> atomicStates = mHelper->getAtomicStates(mStatus);
	list<State*>::iterator it;
	for( it = atomicStates.begin() ; it != atomicStates.end() ; it++ ){
		State* s = *it;
		CPPUNIT_ASSERT(s->isAtomic());
	}
}

void TestBasic::testIsPreempted() {
	CPPUNIT_ASSERT(mHelper->isPreempted(mS11, mS111->getTransitions()));
	CPPUNIT_ASSERT(!mHelper->isPreempted(mS12, mS121->getTransitions()));
}

