'''
    helper module part of scxml4py.
    
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

def getAtomicStates(states):
    atomicStates = list()
    for s in states:
        if s.isAtomic() == True:
            atomicStates.append(s)
    return atomicStates

# includes the starting state itself
def getAncestorsList(states):
    ancestors = list()
    for s in states:
        s1 = s
        while s1 != None:
            if s1 in ancestors:
                break
            ancestors.append(s1)
            s1 = s1.getParent()
    return ancestors

# includes the starting state itself
def getAncestorsSet(states):
    ancestors = set()
    for s in states:
        s1 = s
        while s1 != None:
            ancestors.add(s1)
            s1 = s1.getParent()
    return ancestors


def findLeastCommonAncestor(s1, s2):
    if s1 == None or s2 == None:
        return None
    if s1.getId() == s2.getId():
        return s1 # self-transition
    elif s1.isDescendantFrom(s2) == True: 
        return s2 # s2 is ancestor of s1
    elif s2.isDescendantFrom(s1) == True:
        return s1 # s1 is ancestor of s2

    # build list of ancestors of s1
    parents = list()
    tmp = s1
    while tmp != None:
        parents.append(tmp)
        tmp = tmp.getParent()

    # get the ancestors of s2 until one is found which is common with s1
    tmp = s2
    while tmp != None:
        if tmp in parents:
            return tmp
        tmp = tmp.getParent()

    return None

def findActionInList(theActionId, theList):
    if theList == None:
        return None
    for a in theList:
        if a.getId() == theActionId:
            return a
    return None

def findActivityInList(theActivityId, theList):
    if theList == None:
        return None
    for a in theList:
        if a.getId() == theActivityId:
            return a
    return None

def isPreempted(s, transitions):
    """
     * Return true if a transition 't' in 'transitions' exits an ancestor of state 's'.
     *
     * In this case, taking 't' will pull the state machine out of 's' and we say that
     * it preempts the selection of a transition from 's'. Such preemption will occur
     * only if 's' is a descendant of a parallel region and 't' exits that region.
     * If we did not do this preemption check, we could end up in an illegal configuration,
     * namely one in which there were multiple active states that were not all descendants
     * of a common parallel ancestor.
     """
    preempted = False
    for t in transitions:
        source = t.getSource()
        targets = t.getTargets()
        if targets.__len__() > 0:
            LCA = findLeastCommonAncestor(source, targets[0])
            if s.isDescendantFrom(LCA):
                preempted = True
                break
    return preempted


def compareStates(s1, s2):
    """
    returns true: first argument goes before the second argument in the specific strict weak ordering it defines,
    returns false: otherwise.
    """    
    if s1 != s2:
        p1 = s1.countParents()
        p2 = s2.countParents()
        if p1 < p2:
            return -1
        elif p1 > p2:
            return 1
        else:
            return 0
    return 0

def isStateInList(theState, theList):
    for s in theList:
        # if myState.getId() == s.getId():
        if theState.getAbsoluteId() == s.getAbsoluteId():
            return True
    return False
    
def isTransitionInList(theTrans, theList):
    for t in theList:
        if t == theTrans:
            return True
    return False
        
def getClonedStateAbsId(regionNo, rootState, state, absId):
    
    if state == None:
        return absId
    if rootState.getAbsoluteId() == state.getAbsoluteId():
        regionNo = None
    if absId == None:
        absId = state.getId() + str(regionNo)
    elif regionNo == None:
        absId = state.getId() + rootState.getSeparator() + absId
    else:
        absId = state.getId() + str(regionNo) + rootState.getSeparator() + absId
    return getClonedStateAbsId(regionNo, rootState, state.getParent(), absId)

def updateClonedTransitionTargets(regionNo, rootState, clonedState, statesMap):
    if clonedState == None:
        return
    allTrans = clonedState.getTransitions() + clonedState.getInitialTrans()
    for t in allTrans:
        targets = t.getTargets()
        n = len(targets)
        for i in range(0, n, 1):
            if targets[i] != None:
                assert(targets[i].getAbsoluteId() != None)
                newAbsId = getClonedStateAbsId(regionNo, rootState, targets[i], None)
                if newAbsId in statesMap.keys():
                    assert(statesMap[newAbsId] != None)
                    targets[i] = statesMap[newAbsId]                        
    for s in clonedState.getSubstates():
        updateClonedTransitionTargets(regionNo, rootState, s, statesMap)
        
def formatStatus(status, toBeSorted = True):
    tmp = ""
    if status != None:
        firstItem = True
        for s in status:
            if firstItem == True:
                firstItem = False
            else:
                tmp += " "
            tmp += s.getId()
        if toBeSorted == True and tmp.__len__() > 0:
            tmpList = tmp.split(' ')
            tmpList.sort()
            tmp = " ".join(tmpList)
    return tmp

def formatTransitions(transitions):
    tmp = ""
    if transitions != None:
        for t in transitions:
            tmp += t.__str__()
            tmp += "\n"
    return tmp

def formatState(s, prefix):
    if s == None:
        return ""
    tmp = "\n" + prefix + "<" + s.getId() + ">\n"
    tmp += prefix + "AbsId: <" + s.getAbsoluteId().__str__() + ">\n"    
    tmp += prefix + "Type: <" + s.getType().__str__() + ">\n"
    tmp += prefix + "Parent: <" 
    if s.getParent() != None:
        tmp += s.getParent().getId()
    tmp += ">\n"
    tmp += prefix + "OnEntry: <" + s.getEntryActions().__str__() + ">\n"
    tmp += prefix + "OnExit: <" + s.getExitActions().__str__() + ">\n"
    tmp += prefix + "Activities: <"
    isFirst = True
    for a in s.getActivities():
        if isFirst == True:
            isFirst = False
        else:
            tmp += " "
        tmp += a.__str__()
    tmp += ">\n"    
    tmp += prefix + "IsInitial: <" + s.isInitial().__str__() + ">\n"
    tmp += prefix + "IsFinal: <" + s.isFinal().__str__() + ">\n"        
    tmp += prefix + "Initial Transitions:\n" 
    for t in s.getInitialTrans():
        tmp += prefix + " " + t.__str__() + "\n"
    tmp += prefix + "Transitions:\n" 
    for t in s.getTransitions():
        tmp += prefix + " " + t.__str__() + "\n"
    tmp += prefix + "Sub-states:\n" 
    for s1 in s.getSubstates():
        tmp += formatState(s1, prefix + "  ")
    return tmp 

def formatModel(stateMachine):
    tmp = ""
    if stateMachine != None:
        tmp += "StateMachine <" + stateMachine.getId() + ">\n"
        tmp += "\n Initial Transitions: \n" 
        for t in stateMachine.getInitialTrans():
            tmp += "  " + t.__str__() + "\n"
        tmp += "\n Sub-states: \n" 
        for s in stateMachine.getSubstates():
            tmp += formatState(s, "  ")
        tmp += "\n Parallel-states: \n"         
        for p in stateMachine.getParallel():
            tmp += formatState(p, "  ")    
    return tmp
