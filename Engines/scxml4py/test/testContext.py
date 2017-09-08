'''
    testContext module part of scxml4py unit tests.
    
    @authors: landolfa
    @date: 2016-12-26
    
    @copyright: LGPL 2.1 
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
    02111-1307 USA.
'''

'''
    $Id: event.py 1061 2015-07-13 15:03:59Z landolfa $
'''

import unittest
from scxml4py.context import Context
from scxml4py.event import Event
        
class TestContext(unittest.TestCase):

    def testContextName(self):
        ctx = Context()
        ctx.setName("myContext")
        assert(ctx.getName() == "myContext")

    def testContextSessionId(self):
        ctx = Context()
        ctx.setSessionId("mySessionId")
        assert(ctx.getSessionId() == "mySessionId")

    def testContextLastEvent(self):
        e1 = Event("event1")
        ctx = Context()
        ctx.setLastEvent(e1);
        assert(ctx.getLastEvent() == e1)
        assert(ctx.getLastEvent().getId() == "event1")
        
    def testContextAddElement(self):
        ctx = Context()
        ctx.addElement("element1", "testElement1")
        assert ctx.getElement("element1") == "testElement1"
        ctx.addElement("element2", "testElement2")
        assert ctx.getElement("element2") == "testElement2"

    def testContextDelElement(self):
        ctx = Context()
        ctx.addElement("element1", "testElement1")
        ctx.addElement("element2", "testElement2")
        ctx.delElement("element1")
        assert ctx.getElement("element1") == None
        assert ctx.getElement("element2") == "testElement2"

    def testContextGetNonExistigElement(self):
        ctx = Context()
        element = ctx.getElement("element3")
        assert(element == None)
        
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()
