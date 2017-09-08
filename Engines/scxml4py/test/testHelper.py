'''
    testHelper module part of scxml4py unit tests.
    
    @authors: landolfa
    @date: 2016-12-27
    
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
import scxml4py.helper
from scxml4py.state import StateAtomic, StateCompound

class TestHelper(unittest.TestCase):

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
        
    def testGetAtomicStates(self):
        res = scxml4py.helper.getAtomicStates(self.allStates)
        assert(res.__len__() == 6)
        assert(self.onlinePresettingStep1 in res)
        assert(self.onlinePresettingStep2 in res)
        assert(self.onlinePresettingStep3 in res)
        assert(self.onlineTracking in res)
        assert(self.onlineIdle in res)
        assert(self.standbyIdle in res)
        
    def testGetAncestorsList(self):
        ancestors = scxml4py.helper.getAncestorsList([self.onlinePresettingStep3])
        assert(ancestors.__len__() == 4)
        assert(self.onlinePresettingStep3 in ancestors)
        assert(self.onlinePresetting in ancestors)
        assert(self.online in ancestors)
        assert(self.state in ancestors)
        ancestors = scxml4py.helper.getAncestorsList([None])
        assert(ancestors.__len__() == 0)
        ancestors = scxml4py.helper.getAncestorsList([self.state])
        assert(ancestors.__len__() == 1)
        assert(self.state in ancestors)
        ancestors = scxml4py.helper.getAncestorsList([self.onlinePresettingStep3, self.onlinePresettingStep2])
        assert(ancestors.__len__() == 5)
        assert(self.onlinePresettingStep3 in ancestors)
        assert(self.onlinePresettingStep2 in ancestors)
        assert(self.onlinePresetting in ancestors)
        assert(self.online in ancestors)
        assert(self.state in ancestors)
        ancestors = scxml4py.helper.getAncestorsList([self.onlineIdle, self.standbyIdle])
        assert(ancestors.__len__() == 5)
        assert(self.onlineIdle in ancestors)
        assert(self.online in ancestors)
        assert(self.standbyIdle in ancestors)
        assert(self.standby in ancestors)
        assert(self.state in ancestors)
    
    def testGetAncestorsSet(self):
        myList = list()
        myList.append(self.onlinePresettingStep3)
        ancestors = scxml4py.helper.getAncestorsSet(myList)
        assert(ancestors.__len__() == 4)
        assert(self.onlinePresettingStep3 in ancestors)
        assert(self.onlinePresetting in ancestors)
        assert(self.online in ancestors)
        assert(self.state in ancestors)
        ancestors = scxml4py.helper.getAncestorsSet([None])
        assert(ancestors.__len__() == 0)
        ancestors = scxml4py.helper.getAncestorsSet([self.state])
        assert(ancestors.__len__() == 1)
        assert(self.state in ancestors)
        ancestors = scxml4py.helper.getAncestorsSet([self.onlinePresettingStep3, self.onlinePresettingStep2])
        assert(ancestors.__len__() == 5)
        assert(self.onlinePresettingStep3 in ancestors)
        assert(self.onlinePresettingStep2 in ancestors)
        assert(self.onlinePresetting in ancestors)
        assert(self.online in ancestors)
        assert(self.state in ancestors)
        ancestors = scxml4py.helper.getAncestorsSet([self.onlineIdle, self.standbyIdle])
        assert(ancestors.__len__() == 5)
        assert(self.onlineIdle in ancestors)
        assert(self.online in ancestors)
        assert(self.standbyIdle in ancestors)
        assert(self.standby in ancestors)
        assert(self.state in ancestors)

    def testFindLeastCommonAncestor(self):
        s = scxml4py.helper.findLeastCommonAncestor(self.onlinePresettingStep1, self.onlinePresettingStep3)
        assert(s == self.onlinePresetting)
        s = scxml4py.helper.findLeastCommonAncestor(self.onlinePresettingStep1, self.onlineIdle)
        assert(s == self.online)
        s = scxml4py.helper.findLeastCommonAncestor(self.standbyIdle, self.onlineIdle)
        assert(s == self.state)
        s = scxml4py.helper.findLeastCommonAncestor(self.state, self.onlineIdle)
        assert(s == self.state)
        s = scxml4py.helper.findLeastCommonAncestor(None, self.onlineIdle)
        assert(s == None)
        s = scxml4py.helper.findLeastCommonAncestor(self.onlineIdle, None)
        assert(s == None)
        s1 = StateAtomic("Test")
        s = scxml4py.helper.findLeastCommonAncestor(self.onlineIdle, s1)
        assert(s == None)
    
    def testCompareStates(self):
        s1 = StateCompound("S1")
        s11 = StateAtomic("S11")
        s1.addSubstate(s11)
        s2 = StateCompound("S2")
        s22 = StateAtomic("S22")
        s2.addSubstate(s22)
        assert (scxml4py.helper.compareStates(s1, s2) == 0)
        assert (scxml4py.helper.compareStates(s11, s22) == 0)
        assert (scxml4py.helper.compareStates(s1, s22) == -1)
        assert (scxml4py.helper.compareStates(s11, s2) == 1)
    
    def testIsStateInList(self):
        s1 = StateCompound("S1")
        s11 = StateAtomic("S11")
        s2 = StateAtomic("S2")
        s1.addSubstate(s11)
        l = [s1, s11]
        assert(scxml4py.helper.isStateInList(s1, l) == True)
        assert(scxml4py.helper.isStateInList(s11, l) == True)
        assert(scxml4py.helper.isStateInList(s2, l) == False)
    
    def testIsPreempted(self):
        pass
    
    
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()