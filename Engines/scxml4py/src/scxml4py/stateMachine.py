'''
    stateMachine module part of scxml4py.
    
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

import logging
import scxml4py.helper
from scxml4py.transition import Transition
from scxml4py.state import StateParallel



class StateMachine(object):
    
    def __init__(self, theId):
        self.mId = theId
        self.mInitialTrans = list()
        self.mSubstates = list()
        self.mParallel = list()
        self.mStatesMap = {}
        
    def __str__(self):
        tmp = self.mId.__str__() + "\n"
        tmp += " Initial Transitions: \n" 
        for t in self.mInitialTrans:
            tmp += " " + t.__str__() + "\n"
        tmp += " Substates: \n\n"    
        for s in self.mSubstates:
            tmp += " " + s.__str__() + "\n"
        tmp += " Parallel states: \n\n"    
        for s in self.mParallel:
            tmp += " " + s.__str__() + "\n"
        return tmp
        
    def getId(self):
        return self.mId
    
    def getInitialTrans(self):
        return self.mInitialTrans
    
    def getSubstates(self):
        return self.mSubstates
    
    def getParallel(self):
        return self.mParallel

    def setId(self, theId):
        self.mId = theId
        
    def setInitialState(self, initialState, a = None):
        t = Transition()
        t.setSource(None)
        t.addTarget(initialState)
        #t.setEvent(None)
        t.addAction(a)
        self.mInitialTrans.append(t)
        initialState.setIsInitial(True)

    def setFinalState(self, finalState):
        finalState.setIsFinal(True)

    def setSubstates(self, substates):
        self.mSubstates = substates
        
    def setParallel(self, parallel):
        self.mParallel = parallel

    def addSubstate(self, s):
        self.mSubstates.append(s)
    
    def addParallel(self, s):
        self.mParallel.append(s)

    def updateStatesMap(self, absId, s):
        if s != None and absId != None and absId not in self.mStatesMap.keys():
            self.mStatesMap[absId] = s
    
    def findStateInMap(self, absId):
        s = None
        if absId != None and absId in self.mStatesMap.keys():
            s = self.mStatesMap[absId]
        return s
    
    def cloneParallel(self, numberOfClones, rootStateAbsId, actionMap, activityMap):
        # duplicate n times what is inside the parallel state defined by sourceAbsId
        if numberOfClones < 1:
            return
        # find cloning root
        rootState = self.findStateInMap(rootStateAbsId)
        if rootState == None or type(rootState) != StateParallel:
            # @TODO throw exception
            logging.getLogger("scxml4py").error("Error: " + rootStateAbsId + " does not point to a state or the state is not parallel.")
            return
        # clone states
        newSubstates = list()
        for regionNo in range(1, numberOfClones+1):
            # clone all substates
            for s in rootState.getSubstates():
                newState = s.clone(regionNo, rootState, self.mStatesMap, actionMap, activityMap)
                newSubstates.append(newState)
        # update transition's target states
        regionNo = 1
        for s in newSubstates:
            scxml4py.helper.updateClonedTransitionTargets(regionNo, rootState, s, self.mStatesMap)
            regionNo += 1
        # update initial transitions for the source state
        newInitialTrans = list()
        for regionNo in range(1, numberOfClones+1):
            for t in rootState.getInitialTrans():
                if t != None:
                    t1 = t.clone(regionNo, None, actionMap)
                    newInitialTrans.append(t1)
                    targets = t1.getTargets()
                    n = len(targets)
                    for i in range(0, n, 1):
                        if targets[i] != None:
                            assert(targets[i].getAbsoluteId() != None)
                            newAbsId = scxml4py.helper.getClonedStateAbsId(regionNo, rootState, targets[i], None)
                            if newAbsId in self.mStatesMap.keys():
                                assert(self.mStatesMap[newAbsId] != None)
                                targets[i] = self.mStatesMap[newAbsId]        
                            else:
                                assert(False)
        for t in newInitialTrans:
            rootState.mInitialTrans.append(t)   
        # add cloned states to the state machines
        for newState in newSubstates:
            rootState.addSubstate(newState)
            
            
                