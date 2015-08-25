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
 * $Id: TestBasic.h 1061 2015-07-13 15:03:59Z landolfa $
 */


#ifndef TestBasic_H
#define TestBasic_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <string>
#include <stdio.h>
#include <cppeng/Helper.h>
#include <cppeng/State.h>
#include <cppeng/StateCompound.h>
#include <cppeng/StateAtomic.h>
#include <cppeng/StateComparator.h>
#include <cppeng/Event.h>
#include <cppeng/Transition.h>
#include <cppunit/TestFixture.h>
#include <cppunit/extensions/HelperMacros.h>

using namespace std;

class TestBasic : public CppUnit::TestFixture
{
	CPPUNIT_TEST_SUITE (TestBasic);
	CPPUNIT_TEST(testStateComparator);
	CPPUNIT_TEST(testIsDescendant);
	CPPUNIT_TEST(testGetAncestors);
	CPPUNIT_TEST(testGetProperAncestors);
	CPPUNIT_TEST(testFindLeastCommonAncestor);
	CPPUNIT_TEST(testEventMatch);
	CPPUNIT_TEST(testGetAtomicStates);
	CPPUNIT_TEST(testIsPreempted);
	CPPUNIT_TEST_SUITE_END();
public:	
	void setUp();
	void tearDown();
protected:
	void testStateComparator();
	void testIsDescendant();
	void testGetAncestors();
	void testGetProperAncestors();
	void testFindLeastCommonAncestor();
	void testEventMatch();
	void testGetAtomicStates();
	void testIsPreempted();
private:
	State* mS1;
	State* mS11;
	State* mS111;
	State* mS112;
	State* mS12;
	State* mS121;
	State* mS122;
	list<State*> mStatus;
	Helper* mHelper;
};

#endif
