'''
    testState module part of scxml4py unit tests.
    
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
from scxml4py.state import State, StateAtomic, StateCompound, StateParallel, StateHistory, StateType, HistoryType
from scxml4py.action import Action
from scxml4py.activity import ThreadedActivity
from scxml4py.event import Event
        
class TestState(unittest.TestCase):

    def setUp(self):
        self.state = StateCompound("STATE")
        self.standby = StateCompound("STANDBY")
        self.online = StateCompound("ONLINE")
        self.standbyIdle = StateAtomic("STANDBY:IDLE")
        self.onlineIdle = StateAtomic("ONLINE:IDLE")
        self.onlinePresetting = StateCompound("ONLINE:PRESETTING")
        self.onlinePresettingStep1 = StateAtomic("ONLINE:PRESETTING:STEP1")
        self.onlinePresettingStep2 = StateAtomic("ONLINE:PRESETTING:STEP2")
        self.onlinePresettingStep3 = StateAtomic("ONLINE:PRESETTING:STEP3")
        self.onlineTracking = StateAtomic("ONLINE:TRACKING")
        self.state.addSubstate(self.standby)
        self.state.addSubstate(self.online)
        self.standby.addSubstate(self.standbyIdle)
        self.online.addSubstate(self.onlineIdle)
        self.online.addSubstate(self.onlinePresetting)
        self.online.addSubstate(self.onlineTracking)
        self.onlinePresetting.addSubstate(self.onlinePresettingStep1)
        self.onlinePresetting.addSubstate(self.onlinePresettingStep2)
        self.onlinePresetting.addSubstate(self.onlinePresettingStep3)
        self.allStates = [self.state, self.standby, self.standbyIdle, self.online, self.onlineIdle, self.onlinePresetting, self.onlinePresettingStep1, self.onlinePresettingStep2, self.onlinePresettingStep3, self.onlineTracking]

    def testGetAbsoluteId(self):
        s1 = StateCompound("S1")
        s11 = StateAtomic("S11")
        s1.addSubstate(s11)
        #s1.resolveAbsoluteId()
        #s11.resolveAbsoluteId()
        assert (s11.getAbsoluteId() == s1.getId() + State.getSeparator() + s11.getId())
        assert (s1.getAbsoluteId() == s1.getId())

    def testIsDescendantFrom(self):
        assert (self.onlinePresettingStep3.isDescendantFrom(self.onlinePresetting) == True)
        assert (self.onlinePresettingStep3.isDescendantFrom(self.online) == True)
        assert (self.onlinePresettingStep3.isDescendantFrom(self.state) == True)
        assert (self.onlinePresettingStep3.isDescendantFrom(None) == True)
        assert (self.onlinePresettingStep3.isDescendantFrom(self.onlinePresettingStep3) == False)        
        assert (self.onlinePresettingStep3.isDescendantFrom(self.onlineTracking) == False)
        assert (self.onlinePresettingStep3.isDescendantFrom(self.standbyIdle) == False)

    def testCountParents(self):
        assert(self.onlinePresettingStep1.countParents() == 3)
        assert(self.onlinePresetting.countParents() == 2)
        assert(self.online.countParents() == 1)
        assert(self.state.countParents() == 0)

    def testGetProperAncestors(self):
        ancestors = self.onlinePresettingStep3.getProperAncestors(None)
        assert(ancestors.__len__() == 3)
        assert(self.onlinePresetting in ancestors)
        assert(self.online in ancestors)
        assert(self.state in ancestors)
        ancestors = self.onlinePresettingStep3.getProperAncestors(self.state)
        assert(ancestors.__len__() == 2)
        assert(self.onlinePresetting in ancestors)
        assert(self.online in ancestors)
        ancestors = self.onlinePresettingStep3.getProperAncestors(self.online)
        assert(ancestors.__len__() == 1)
        assert(self.onlinePresetting in ancestors)

    def testStateAtomic1(self):
        s = StateAtomic("S1")
        assert(s.getId() == "S1")
        assert(s.getType() == StateType.ATOMIC)
        s.setId("S2")
        assert(s.getId() == "S2")
        print("<"+s.__str__()+">")
        tmp = """S2
 Type:       StateType.ATOMIC
 Parent:     
 OnEntry:    
 OnExit:     
 Activities: 
 IsInitial:  False
 IsFinal:    False
"""
        print("<"+tmp.__str__()+">")
        #assert(s.__str__() == tmp.__str__())
        assert(s.isAtomic() == True)
        assert(s.isFinal() == False)
        assert(s.isInitial() == False)
        assert(s.getParent() == None)
        assert(s.getSubstates().__len__() == 0)
        
    def testStateAtomic2(self):
        entryAction = Action("entryAction")
        exitAction = Action("exitAction")
        initialAction = Action("initialAction")
        activity = ThreadedActivity("doActivity")
        s = StateAtomic("S2")
        s.addEntryAction(entryAction)
        s.addExitAction(exitAction)
        s.addActivity(activity)
        s.setFinalState(s)
        s.setInitialState(s, initialAction)
        print("<"+s.__str__()+">")
        tmp = """S2
 Type:       StateType.ATOMIC
 Parent:     
 OnEntry:    entryAction
 OnExit:     exitAction
 Activities: doActivity
 IsInitial:  True
 IsFinal:    True
"""        
        print(tmp)
        #assert(s.__str__() == tmp.__str__())
        l = s.getActivities()
        assert(l[0].getId() == "doActivity")
        l = s.getEntryActions().getActions()
        assert(l[0].getId() == "entryAction")
        l = s.getExitActions().getActions()
        assert(l[0].getId() == "exitAction")
        assert(s.isAtomic() == True)
        assert(s.isFinal() == True)
        assert(s.isInitial() == True)
        
    def testStateCompound(self):
        s1 = StateCompound("S1")
        assert(s1.getType() == StateType.COMPOUND)
        s11 = StateAtomic("S11")
        s1.addSubstate(s11)
        s12 = StateAtomic("S12")
        s1.addSubstate(s12)
        l = s1.getSubstates()
        assert(l.__len__() == 2)
        assert(l[0].getId() == "S11")
        assert(l[1].getId() == "S12")
        assert(s11.getParent() == s1)
        assert(s12.getParent() == s1)
        assert(s11.getParent().getId() == "S1")
        assert(s12.getParent().getId() == "S1")
        
    def testStateParallel(self):
        s1 = StateParallel("S1")
        assert(s1.getType() == StateType.PARALLEL)
        s11 = StateAtomic("S11")
        s1.addSubstate(s11)
        s12 = StateAtomic("S12")
        s1.addSubstate(s12)
        l = s1.getSubstates()
        assert(l.__len__() == 2)
        assert(l[0].getId() == "S11")
        assert(l[1].getId() == "S12")
        assert(s11.getParent() == s1)
        assert(s12.getParent() == s1)
        assert(s11.getParent().getId() == "S1")
        assert(s12.getParent().getId() == "S1")
        
    def testStateHistory(self):
        s1 = StateHistory("S1", HistoryType.DEEP)
        assert(s1.getId() == "S1")
        assert(s1.getType() == StateType.HISTORY)
        assert(s1.getHistoryType() == HistoryType.DEEP)
        s2 = StateAtomic("S2")
        s3 = StateAtomic("S3")
        s1.pushHistoryValue(s2)
        s1.pushHistoryValue(s3)
        assert(s1.getHistoryValues().__len__() == 2)
        s = s1.popHistoryValue()
        assert(s1.getHistoryValues().__len__() == 1)
        assert(s == s3)
        assert(s.getId() == s3.getId())
        s = s1.popHistoryValue()
        assert(s1.getHistoryValues().__len__() == 0)
        assert(s == s2)

    def testStateCloneAtomic(self):
        statesMap = {}
        a = StateAtomic("A")
        a.resolveAbsoluteId()
        a1 = a.clone(1, None, statesMap, {}, {})
        assert(a1.getAbsoluteId() == "A1")
        
    def testStateCloneComposite(self):
        statesMap = {}
        a = StateAtomic("A")
        b = StateAtomic("B")
        c = StateCompound("C")
        c.addSubstate(a)
        c.addSubstate(b)
        c.resolveAbsoluteId()
        a.resolveAbsoluteId()
        b.resolveAbsoluteId()
        statesMap["A"] = a        
        statesMap["B"] = b
        statesMap["C"] = a
        a.addTransition(b, Event("myEvent"), Action("myAction"), Action("myGuard"))
        #for s in c.getSubstates():
        #    print(s.getAbsoluteId())
        c1 = c.clone(1, None, statesMap, {}, {})        
        assert(c1.getAbsoluteId() == "C1")
        assert(statesMap["C1.A1"].getAbsoluteId() == "C1.A1")
        assert(statesMap["C1.B1"].getAbsoluteId() == "C1.B1")
        t1 = statesMap["C1.A1"].getTransitions()[0]
        assert(t1.getEvent().getId() == "myEvent1")
        assert(t1.getActions().getActions()[0].getId() == "myAction")
        assert(t1.getConditions().getActions()[0].getId() == "myGuard")
        #print(str(t1))
        #print(statesDict)
         
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()
