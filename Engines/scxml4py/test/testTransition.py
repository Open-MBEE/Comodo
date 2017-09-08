'''
    testTransition module part of scxml4py unit tests.
    
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
from scxml4py.transition import Transition
from scxml4py.context import Context
from scxml4py.event import Event
from scxml4py.action import Action
from scxml4py.state import StateAtomic

class CustomGuard1(Action):
    def __init__(self):
        Action.__init__(self, "CustomGuard1")
        
    def evaluate(self, theContext):
        return True    

class CustomGuard2(Action):
    def __init__(self):
        Action.__init__(self, "CustomGuard2")
        
    def evaluate(self, theContext):
        return False    


class TestTransition(unittest.TestCase):


    def testTransitionCreation(self):
        a = Action("myAction")
        e = Event("myEvent")
        g1 = CustomGuard1()
        g2 = CustomGuard2()        
        t = Transition()
        t.setEvent(e)
        t.addAction(a)
        t.addCondition(g1)
        t.addCondition(g2)
        print(t.__str__())
        assert(t.__str__() == "FromState <> ToState <>  Guards <CustomGuard1 CustomGuard2> Event <myEvent> Actions <myAction>")


    def testTransitionEnabled(self):
        c = Context()
        e = Event("myEvent")
        e1 = Event("myEvent1")
        g1 = CustomGuard1()
        g2 = CustomGuard2()
        
        # transition without event and without guards
        t1 = Transition()
        assert(t1.isEnabled(c, None) == True)
        assert(t1.isEnabled(c, e) == False)
        
        # transition with event and no guards
        t2 = Transition()
        t2.setEvent(e)
        assert(t2.isEnabled(c, None) == False)
        assert(t2.isEnabled(c, e) == True)
        assert(t2.isEnabled(c, e1) == False)
        
        # transition with event and True guards
        t3 = Transition()
        t3.setEvent(e)
        t3.addCondition(g1)
        assert(t2.isEnabled(c, None) == False)
        assert(t2.isEnabled(c, e) == True)
        assert(t2.isEnabled(c, e1) == False)

        # transition with event and False guards
        t3.addCondition(g2)
        assert(t3.isEnabled(c, None) == False)
        assert(t3.isEnabled(c, e) == False)
        assert(t3.isEnabled(c, e1) == False)
        
    def testCompare(self):
        e1 = Event("e1")
        e2 = Event("e2")
        a1 = Action("a1")
        a2 = Action("a2")
        g1 = Action("g1")
        g2 = Action("g2")
        s1 = StateAtomic("S1")
        s2 = StateAtomic("S2")
        t1 = Transition()
        t1.setEvent(e1)
        t1.addAction(a1)
        t1.addCondition(g1)
        t1.setSource(s1)
        t1.addTarget(s2)
        t2 = Transition()
        t2.setEvent(e2)
        t2.addAction(a2)
        t2.addCondition(g2)
        t2.setSource(s2)
        t2.addTarget(s1)
        assert(t1 != t2)
        assert(t1 == t1)
        
    def testClone(self):
        e = Event("e")
        a = Action("a")
        g = Action("g")
        s1 = StateAtomic("A")
        s2 = StateAtomic("B")
        t = Transition()
        t.setEvent(e)
        t.addAction(a)
        t.addCondition(g)
        t.setSource(s1)
        t.addTarget(s2)
        t1 = t.clone(1, s1, {})
        assert(t1.getEvent().getId() == "e1")
        assert(t1.getSource().getId() == "A")
        assert(t1.getTargets()[0].getId() == "B")
        assert(t1.getActions().getActions()[0].getId() == "a")
        assert(t1.getConditions().getActions()[0].getId() == "g")
        
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()
    