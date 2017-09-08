'''
    testStateMachine module part of scxml4py unit tests.
    
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
from scxml4py.stateMachine import StateMachine
from scxml4py.state import StateCompound, StateAtomic, StateParallel

    
class TestStateMachine(unittest.TestCase):

    def testSMId(self):
        sm = StateMachine("MyStateMachine")
        assert(sm.getId()=="MyStateMachine")
        sm.setId("Pippo")
        assert(sm.getId()=="Pippo")

    def testSMSubstates(self):
        sm = StateMachine("MyStateMachine")
        s1 = StateCompound("S1")
        s11 = StateAtomic("S11")
        s12 = StateAtomic("S12")
        s1.addSubstate(s11)
        s1.addSubstate(s12)
        sm.addSubstate(s1)
        print(str(sm))

    def testSMInitialTrans(self):
        sm = StateMachine("MyStateMachine")
        s1 = StateCompound("S1")
        sm.addSubstate(s1)
        sm.setInitialState(s1)
        print(str(sm))
        
    def testSMClone1(self):
        sm = StateMachine("MyStateMachine")
        p = StateParallel("P")
        a = StateCompound("A")
        b = StateAtomic("B")
        c = StateAtomic("C")
        a.addSubstate(b)
        a.addSubstate(c)
        p.addSubstate(a)
        p.resolveAbsoluteId()
        a.resolveAbsoluteId()
        b.resolveAbsoluteId()
        c.resolveAbsoluteId()
        sm.addParallel(p)
        sm.updateStatesMap(p.getAbsoluteId(), p)
        sm.updateStatesMap(a.getAbsoluteId(), a)
        sm.updateStatesMap(b.getAbsoluteId(), b)
        sm.updateStatesMap(c.getAbsoluteId(), c)
        print(str(sm))
        print(sm.mStatesMap)
        sm.setId("ClonedStateMachine")
        sm.cloneParallel(2, "P", {}, {})
        assert(len(sm.mStatesMap) == 10)
        assert("P" in sm.mStatesMap.keys())
        assert("P.A" in sm.mStatesMap.keys())
        assert("P.A.B" in sm.mStatesMap.keys())
        assert("P.A.C" in sm.mStatesMap.keys())
        assert("P.A1" in sm.mStatesMap.keys())
        assert("P.A1.B1" in sm.mStatesMap.keys())
        assert("P.A1.C1" in sm.mStatesMap.keys())
        assert("P.A2" in sm.mStatesMap.keys())
        assert("P.A2.B2" in sm.mStatesMap.keys())
        assert("P.A2.C2" in sm.mStatesMap.keys())
        print(str(sm))
        print(sm.mStatesMap)

    def testSMClone2(self):
        sm = StateMachine("MyStateMachine2")
        a = StateCompound("A")
        p = StateParallel("P")
        b = StateAtomic("B")
        a.addSubstate(p)
        p.addSubstate(b)        
        p.resolveAbsoluteId()
        a.resolveAbsoluteId()
        b.resolveAbsoluteId()
        sm.addSubstate(a)
        sm.updateStatesMap(p.getAbsoluteId(), p)
        sm.updateStatesMap(a.getAbsoluteId(), a)
        sm.updateStatesMap(b.getAbsoluteId(), b)
        print(str(sm))
        print(sm.updateStatesMap)
        sm.setId("ClonedStateMachine2")
        sm.cloneParallel(3, "A.P", {}, {})
        assert(len(sm.mStatesMap) == 6)
        assert("A" in sm.mStatesMap.keys())
        assert("A.P" in sm.mStatesMap.keys())
        assert("A.P.B" in sm.mStatesMap.keys())
        assert("A.P.B1" in sm.mStatesMap.keys())
        assert("A.P.B2" in sm.mStatesMap.keys())
        assert("A.P.B3" in sm.mStatesMap.keys())
        print(str(sm))
        print(sm.mStatesMap)
        
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()

