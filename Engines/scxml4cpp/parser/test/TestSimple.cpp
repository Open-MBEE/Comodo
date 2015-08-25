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
 * $Id: TestSimple.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */

#include "TestSimple.h"
 
CPPUNIT_TEST_SUITE_REGISTRATION (TestSimple);

void TestSimple::setUp() {
}

void TestSimple::tearDown() {
}
		
void TestSimple::testLoadWithSAX(){	
/*	SCXMLReader* reader = new SAXSCXMLReader();
	StateMachine* sm = reader->read("src/org/eso/sdd/test/sm/parser/examples/Main.xml", NULL, NULL);
	CPPUNIT_ASSERT_EQUAL(sm->getId(), "DefaultSCXML");
	mHelper->printStateMachine(sm);*/	
}

void TestSimple::testLoadWithDOM(){
	SCXMLReader* reader = new DOMSCXMLReader();
	StateMachine* sm = reader->read("../config/scxmlsamples/Simple.xml", NULL, NULL);
	CPPUNIT_ASSERT( sm->getId().compare("DefaultSCXML") == 0 );
	mHelper->printStateMachine(sm);
}
