'''
    testEvent module part of scxml4py unit tests.
    
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
from scxml4py.event import Event


class CustomEvent1(Event):
    def __init__(self, theId):
        Event.__init__(self, theId)
        
class CustomEvent2(Event):
    def __init__(self):
        Event.__init__(self, "CustomEvent2")
        
class TestEvent(unittest.TestCase):

    def testEventId(self):
        e1 = CustomEvent1("CustomEvent1")
        assert(e1.getId()=="CustomEvent1")
        e1.setId("AnotherEventName")
        assert(e1.getId()=="AnotherEventName")
        e2 = CustomEvent2()
        assert(e2.getId()=="CustomEvent2")

    def testEventCompare(self):
        e11 = Event("e11")
        e111 = Event("e111")
        assert(e11 == e11)
        assert(e11 != e111)
        assert(e11 < e111)
        assert(e111 > e11)
        assert(None < e11)
        assert(e11 != None)

    def testEventClone(self):
        e = Event("myEvent")
        for i in range(1,6):
            clonedEvent = e.clone(i)
            assert(clonedEvent.getId() == "myEvent"+str(i))

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()

