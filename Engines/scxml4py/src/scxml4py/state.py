'''
    state module part of scxml4py.
    
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
from enum import Enum
from scxml4py.executableContent import ExecutableContent
from scxml4py.transition import Transition

class StateType(Enum):
    ATOMIC = 0
    COMPOUND = 1
    PARALLEL = 2
    HISTORY = 3

class State(object):
    def __init__(self, theId, theType):
        self.mId = theId
        self.mAbsId = None
        self.mType = theType # @TODO could be removed
        self.mParent = None
        self.mSubstates = list()
        self.mHistory = None
        self.mTransitions = list()
        self.mOnEntry = ExecutableContent()
        self.mOnExit = ExecutableContent()
        self.mActivities = list()
        self.mInitialTrans = list()
        self.mIsInitial = False
        self.mIsFinal = False
        
    def __str__(self):
        return self.getAbsoluteId().__str__()
        """
        tmp = self.mId.__str__() + "\n"
        tmp += " Type:       " + self.mType.__str__() + "\n"
        tmp += " Parent:     " 
        if self.mParent != None:
            tmp += self.mParent.getId()
        tmp += "\n"
        tmp += " OnEntry:    " + self.mOnEntry.__str__() + "\n"
        tmp += " OnExit:     " + self.mOnExit.__str__() + "\n"
        tmp += " Activities: "
        isFirst = True
        for a in  self.mActivities:
            if isFirst == True:
                isFirst = False
            else:
                tmp += " "
            tmp += a.__str__()
        tmp += "\n"    
        tmp += " IsInitial:  " + self.mIsInitial.__str__() + "\n"
        tmp += " IsFinal:    " + self.mIsFinal.__str__() + "\n"        
        return tmp 
        """
        
    def __lt__(self, other):
        if other == None:
            return False
        return self.__str__() < other.__str__()
    
    def __eq__(self, other):
        if other == None:
            return False
        return self.__str__() == other.__str__()
    
    def __hash__(self, *args, **kwargs):
        # needed to avoid the 'TypeError: unhashable type' when defining __eq__ 
        return object.__hash__(self, *args, **kwargs)
    
    def getId(self):
        return self.mId

    def getAbsoluteId(self):
        if self.mAbsId == None:
            self.resolveAbsoluteId()
        return self.mAbsId
    
    def getType(self):
        return self.mType
    
    def getParent(self):
        return self.mParent
    
    def getHistory(self):
        return self.mHistory
    
    def getEntryActions(self):
        return self.mOnEntry
    
    def getExitActions(self):
        return self.mOnExit
    
    def getInitialTrans(self):
        return self.mInitialTrans
    
    def getInitialState(self):
        if self.mInitialTrans != None:
            for t in self.mInitialTrans:
                targets = t.getTargets()
                if targets.__len__() > 0:
                    return targets[0]
        return None

    def getSubstates(self):
        return self.mSubstates
    
    def getTransitions(self):
        return self.mTransitions
    
    def getActivities(self):
        return self.mActivities
    
    def getProperAncestors(self, upperBoundState):
        ancestors = list()
        # proper ancestor does not include given state,
        # therefore starts from the parent of the given state
        s = self.getParent()
        while s != None:
            # @TODO is == operator enough?
            if upperBoundState != None and upperBoundState == s:
                break
            # @TODO is 'in' operator enough or shall we use helper.isStateInList?
            elif s in ancestors:
                break
            else:
                ancestors.append(s)
            s = s.getParent()
        return ancestors

    @staticmethod
    def getSeparator():
        return "."
    
    def setId(self, theId):
        self.mId = theId
    
    def setAbsoluteId(self, theAbsId):
        self.mAbsId = theAbsId
        
    def setParent(self, theParent):
        self.mParent = theParent
        
    def setIsInitial(self, isInitial):
        self.mIsInitial = isInitial
        
    def setIsFinal(self, isFinal):
        self.mIsFinal = isFinal
    
    def setHistory(self, history):
        self.mHistory = history
         
    def setInitialState(self, initialState, theAction): 
        t = Transition()
        t.setSource(None)
        t.addTarget(initialState)
        t.addAction(theAction)
        self.mInitialTrans.append(t)
        initialState.setIsInitial(True)
            
    def setFinalState(self, finalState):
        finalState.setIsFinal(True)
        
    def setSubstates(self, substates):
        self.mSubstates = substates
        
    def setTransitions(self, transitions):
        self.mTransitions = transitions
        
    def setActivities(self, activities):
        self.mActivities = activities

    def isInitial(self):
        return self.mIsInitial
    
    def isFinal(self):
        return self.mIsFinal
    
    def isCompound(self):
        return self.mType == StateType.COMPOUND
    
    def isParallel(self):
        return self.mType == StateType.PARALLEL
    
    def isAtomic(self):
        return self.mType == StateType.ATOMIC
    
    def isHistory(self):
        return self.mType == StateType.HISTORY

    def isDescendantFrom(self, parent):
        if parent == None:
            return True
        p = self.getParent()
        while p != None:
            # @TODO: is == operator sufficient?
            if p == parent:
                return True
            p = p.getParent()
        return False

    def countParents(self):
        numParents = 0
        s1 = self.getParent();
        while s1 != None:
            numParents += 1
            s1 = s1.getParent()
        return numParents
    
    def resolveAbsoluteId(self):
        self.mAbsId = self.mId        
        p = self.getParent()
        while p != None:
            self.mAbsId = p.getId() + self.getSeparator() + self.mAbsId
            p = p.getParent()
    
    """
    @TODO obsolete since replaced by the map in the StateMachine class
    def findState(self, absId):
        # 
        if self.getAbsoluteId() == absId:
            return self
        for s in self.mSubstates:
            if s != None:
                if s.findState(absId) != None:
                    return s
        return None
    """
    
    def addEntryAction(self, a):
        if a != None:
            self.mOnEntry.addAction(a)
        
    def addExitAction(self, a):
        if a != None:
            self.mOnExit.addAction(a)

    def addActivity(self, activity):
        self.mActivities.append(activity)
    
    def startActivities(self):
        for a in self.mActivities:
            a.start()
            logging.getLogger('scxml4py').info("Activity: " + a.getId() + " started.")
                   
    def cancelActivities(self):
        for a in self.mActivities:
            a.stop()
            logging.getLogger('scxml4py').info("Activity: " + a.getId() + " stopped.")
            
    def addSubstate(self, s):
        if s != None:
            s.setParent(self);
            self.mSubstates.append(s)
        
    def addTransition(self, target, event, action, condition = None):
        t = Transition()
        t.setSource(self)
        #t.setTargets(targets)
        t.addTarget(target)
        t.setEvent(event)
        t.addAction(action)
        t.addCondition(condition)
        self.mTransitions.append(t)
    
    def copy(self, regionNo, clonedParentState, clonedState, statesMap, actionMap, activityMap):
        if clonedParentState != None:
            clonedState.mParent = clonedParentState
        clonedState.resolveAbsoluteId()
        if clonedState.getAbsoluteId() not in statesMap.keys():
            statesMap[clonedState.getAbsoluteId()] = clonedState
        else:
            # @TODO throw exception?
            assert(False)
        for a in self.mOnEntry.getActions():
            newId = a.getId() + str(regionNo)
            if newId in actionMap.keys():
                clonedState.addEntryAction(actionMap[newId])
            else:
                clonedState.addEntryAction(a)
        for a in self.mOnExit.getActions():
            newId = a.getId() + str(regionNo)
            if newId in actionMap.keys():
                clonedState.addExitAction(actionMap[newId])
            else:
                clonedState.addExitAction(a)
        for a in self.mActivities:
            newId = a.getId() + str(regionNo)
            if newId in activityMap.keys():
                clonedState.addActivity(activityMap[newId])
            else:
                clonedState.addActivity(a)
        clonedState.mIsInitial = self.mIsInitial
        clonedState.mIsFinal = self.mIsFinal
        for s in self.mSubstates:
            clonedState.addSubstate(s.clone(regionNo, clonedState, statesMap, actionMap, activityMap))
        # cloning transition without updating the target states
        for t in self.mTransitions:
            clonedState.mTransitions.append(t.clone(regionNo, clonedState, actionMap))
        for t in self.mInitialTrans:
            clonedState.mInitialTrans.append(t.clone(regionNo, None, actionMap))
    
                        
class StateAtomic(State):
    def __init__(self, theId):
        State.__init__(self, theId, StateType.ATOMIC)

    def clone(self, regionNo, clonedParentState, statesMap, actionMap, activityMap):
        clonedState = StateAtomic(self.mId + str(regionNo))
        State.copy(self, regionNo, clonedParentState, clonedState, statesMap, actionMap, activityMap)
        return clonedState
    
class StateCompound(State):
    def __init__(self, theId):
        State.__init__(self, theId, StateType.COMPOUND)

    def __str__(self):
        tmp = State.__str__(self)
        tmp += "\n"
        for s in self.mSubstates:
            tmp += " " + s.__str__() + "\n"
        return tmp

    def clone(self, regionNo, clonedParentState, statesMap, actionMap, activityMap):
        clonedState = StateCompound(self.mId + str(regionNo))
        State.copy(self, regionNo, clonedParentState, clonedState, statesMap, actionMap, activityMap)
        return clonedState
    
class StateParallel(State):
    def __init__(self, theId):
        State.__init__(self, theId, StateType.PARALLEL)

    def __str__(self):
        tmp = State.__str__(self)
        tmp += "\n"
        for s in self.mSubstates:
            tmp += " " + s.__str__() + "\n"
        return tmp

    def clone(self, regionNo, clonedParentState, statesMap, actionMap, activityMap):
        clonedState = StateParallel(self.mId + str(regionNo))
        State.copy(self, regionNo, clonedParentState, clonedState, statesMap, actionMap, activityMap)
        return clonedState


class HistoryType(Enum):
    SHALLOW = 0 
    DEEP = 1

class StateHistory(State):
    def __init__(self, theId, theHistoryType):
        State.__init__(self, theId, StateType.HISTORY)
        self.mHistoryType = theHistoryType
        self.mHistoryValues = list()
    
    def getHistoryType(self):
        return self.mHistoryType
    
    def getHistoryValues(self):
        return self.mHistoryValues

    def setHistoryType(self, theHistoryType):
        self.mHistoryType = theHistoryType

    def clearHistoryValues(self):
        self.mHistoryValues.clear()
        
    def popHistoryValue(self):
        if self.mHistoryValues.__len__() == 0:
            return None
        else:
            return self.mHistoryValues.pop()

    def pushHistoryValue(self, s):
        if s != None:
            # @TODO check for duplicates?
            logging.getLogger('scxml4py').info("HistoryState: adding " + s.getId() + " to history stack.")
            self.mHistoryValues.append(s)

    def setHistoryValues(self, historyValues):
        self.mHistoryValues = historyValues

    def clone(self, regionNo, clonedParentState, statesMap, actionMap, activityMap):
        clonedState = StateHistory(self.mId + str(regionNo))
        clonedState.mHistoryType = self.mHistoryType
        # don't need to clone runtime information
        #for v in self.mHistoryValues:
        #    clonedState.mHistoryValues.append(v)
        State.copy(self, regionNo, clonedParentState, clonedState, statesMap, actionMap, activityMap)
        return clonedState
