'''
    reader module part of scxml4py.
    
    @authors: landolfa
    @date: 2017-01-02
    
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
import xml.etree.ElementTree as ET
import scxml4py.helper
from scxml4py.exceptions import ScxmlSyntaxError
from scxml4py.stateMachine import StateMachine
from scxml4py.event import Event
from scxml4py.state import HistoryType, StateHistory, StateAtomic, StateCompound, StateParallel


class stateInfo(object):
    def __init__(self, state, element):
        self.mState = state
        self.mElement = element
        
class Reader(object):
        
    def __init__(self):
        self.mNamespaces = {'scxml': 'http://www.w3.org/2005/07/scxml', 'customActionDomain': 'http://my.custom-actions.domain/CUSTOM'}
        self.mStateMachine = StateMachine("")
        self.mStatesInfo = {} # map the state 'absId' with the pair (scxml4py.State instances,  XML element)
        self.mActionList = list()
        self.mActivityList = list()
            
    def parseAttribute(self, element, attributeName, isMandatory):
        attributeValue = element.attrib.get(attributeName)            
        if attributeValue == None and isMandatory == True:
            raise ScxmlSyntaxError("Missing mandatory attribute '" + attributeName + "' for element <" + element.tag + ">.")       
        return attributeValue

    def parseTarget(self, element):
        theTarget = None
        theTargetId = self.parseAttribute(element, "target", False)
        if theTargetId != None:
            # @TODO should use AbsoluteId   
            if theTargetId not in self.mStatesInfo:
                raise ScxmlSyntaxError("Target state '" + theTargetId + "' is not part of the parsed states.")
            theTarget = self.mStatesInfo[theTargetId].mState
            if theTarget == None:
                raise ScxmlSyntaxError("Target state <" + theTargetId + "> of a transition not found.")       
        return theTarget
    
    def parseGuard(self, element):
        theGuard = None
        theGuardId = self.parseAttribute(element, "cond", False)
        if theGuardId != None:
            theGuard = scxml4py.helper.findActionInList(theGuardId, self.mActionList)
            if theGuard == None:
                raise ScxmlSyntaxError("Custom guard id '" + theGuardId + "' not found.")       
        return theGuard                    

    def parseActions(self, element):
        theActionList = list()
        for customAction in element:
            theActionId = self.parseAttribute(customAction, "name", True)
            # find action in actionList
            theAction = scxml4py.helper.findActionInList(theActionId, self.mActionList)
            if theAction == None:
                raise ScxmlSyntaxError("Custom action id '" + theActionId.__str__() + "' not found.")       
            theActionList.append(theAction)
        return theActionList                    

    def parseAction(self, element):
        theActionList = self.parseActions(element)
        if theActionList.__len__() > 0:
            return theActionList[0]
        return None
            
    def parseEvent(self, element):
        theEvent = None
        theEventId = self.parseAttribute(element, "event", False)
        if theEventId != None:
            theEvent = Event(theEventId)
        return theEvent

    def parseHistoryType(self, element):
        # by SCXML default the type is shallow
        historyType = HistoryType.SHALLOW
        theHistoryTypeId = self.parseAttribute(element, "type", False)
        if theHistoryTypeId != None:
            if theHistoryTypeId.lower() == "deep":
                historyType = HistoryType.DEEP
        return historyType

    def parseInvokes(self):
        for key in self.mStatesInfo:
            for invokeElement in self.mStatesInfo[key].mElement.findall('scxml:invoke', self.mNamespaces):
                theId = self.parseAttribute(invokeElement, "id", True)
                # find action in actionList
                theActivity = scxml4py.helper.findActivityInList(theId, self.mActivityList)
                if theActivity == None:
                    raise ScxmlSyntaxError("Activity id '" + theId + "' not found.")  
                self.mStatesInfo[key].mState.addActivity(theActivity)
                logging.getLogger("scxml4py").debug("Found activity " + theActivity.__str__() + " for state " + self.mStatesInfo[key].mState.__str__())            
    
    def parseTransitions(self):
        for key in self.mStatesInfo:
            # normal and internal transition
            for element in self.mStatesInfo[key].mElement.findall('scxml:transition', self.mNamespaces):
                theTarget = self.parseTarget(element)
                theEvent = self.parseEvent(element)
                theGuard = self.parseGuard(element)
                theAction = self.parseAction(element)
                
                # transition from history state must have destination state
                if type(self.mStatesInfo[key].mState) == StateHistory:
                    if theTarget == None:
                        raise ScxmlSyntaxError("Transition from History state must have a target state.")                    
                self.mStatesInfo[key].mState.addTransition(theTarget, theEvent, theAction, theGuard)
                logging.getLogger("scxml4py").debug("Found new transition to state: " + theTarget.__str__())            

            # initial transitions
            for element in self.mStatesInfo[key].mElement.findall('scxml:initial', self.mNamespaces):
                for child in element.findall('scxml:transition', self.mNamespaces):
                    theTarget = self.parseTarget(child)
                    if theTarget == None:
                        raise ScxmlSyntaxError("Initial transition must specify target state.")
                    theAction = self.parseAction(child) # action may or may not be specified
                    # According to SCXML guard and event are not supported for initial transition
                    self.mStatesInfo[key].mState.setInitialState(theTarget, theAction)
                    logging.getLogger("scxml4py").debug("Found initial transition to state: " + theTarget.__str__())            

    def parseStates(self, parentState, element):
        stateList = element.findall('scxml:state', self.mNamespaces)
        parallelList = element.findall('scxml:parallel', self.mNamespaces)
        finalList = element.findall('scxml:final', self.mNamespaces)
        historyList = element.findall('scxml:history', self.mNamespaces)
        
        if stateList.__len__() > 0 and parallelList.__len__() > 0:
            raise ScxmlSyntaxError("An SCXML element cannot have substates and parallel states at the same time.") 
 
        stateTag = "{"+self.mNamespaces['scxml']+"}state"
        parallelTag = "{"+self.mNamespaces['scxml']+"}parallel"
        finalTag = "{"+self.mNamespaces['scxml']+"}final"
        historyTag = "{"+self.mNamespaces['scxml']+"}history"
        
        theList = stateList + parallelList + finalList + historyList
        for item in theList:
            stateId = self.parseAttribute(item, "id", True)
            assert(stateId != None)
            newState = None
            if item.tag == stateTag:
                if item.findall('scxml:state', self.mNamespaces).__len__() == 0:
                    newState = StateAtomic(stateId)
                else:
                    newState = StateCompound(stateId)
            elif item.tag == parallelTag:
                newState = StateParallel(stateId)
            elif item.tag == historyTag:
                # @TODO historyState ID is optional, default value 'none'
                historyType = self.parseHistoryType(item)
                newState = StateHistory(stateId, historyType)
                if parentState != None:
                    parentState.setHistory(newState)
            elif item.tag == finalTag:
                newState = StateAtomic(stateId)
                newState.setIsFinal(True)
            else:
                assert(False)
            if parentState == None:
                self.mStateMachine.addSubstate(newState)
                newState.setAbsoluteId(stateId)
            else:
                parentState.addSubstate(newState)
                newState.setAbsoluteId(parentState.getAbsoluteId() + newState.getSeparator() + stateId)
            # @TODO should be AbsoluteId   
            #self.mStatesInfo[newState.getAbsoluteId()] = stateInfo(newState, item)
            self.mStatesInfo[newState.getId()] = stateInfo(newState, item)
            logging.getLogger("scxml4py").debug("Found new state: " + newState.__str__())            
            # onentry
            for onentryElement in item.findall('scxml:onentry', self.mNamespaces):
                entryActions = self.parseActions(onentryElement)
                newState.getEntryActions().setActions(entryActions)
                #newState.addEntryAction(theAction)
            # onexit
            for onexitElement in item.findall('scxml:onexit', self.mNamespaces):
                exitActions = self.parseActions(onexitElement)
                newState.getExitActions().setActions(exitActions)
                #newState.addExitAction(theAction)
            self.parseStates(newState, item)
         
    def parseScxmlInitialTransition(self, root):
        scxmlTag = "{"+self.mNamespaces['scxml']+"}scxml"
        if root.tag != scxmlTag:
            raise ScxmlSyntaxError("Root element of an SCXML document must be an <scxml> element.")       
        initialStateId = self.parseAttribute(root, "initial", False)
        if initialStateId != None:
            # @TODO should use the absolute id
            if initialStateId not in self.mStatesInfo:
                raise ScxmlSyntaxError("Initial state '" + initialStateId + "' is not part of the parsed states.")
            self.mStateMachine.setInitialState(self.mStatesInfo[initialStateId].mState, None)
            logging.getLogger("scxml4py").debug("Found initial transition to state: " + self.mStatesInfo[initialStateId].mState.__str__())
        else:
            # @TODO parse initial transition
            assert(False)
         

    def parseScxml(self, root):
        scxmlTag = "{"+self.mNamespaces['scxml']+"}scxml"
        if root.tag != scxmlTag:
            raise ScxmlSyntaxError("Root element of an SCXML document must be an <scxml> element.")       
                    
        scxmlElement = root
        
        # Parsing namespaces attributes (mandatory)
        #ns1 = scxmlElement.attrib.get('xmlns')
        #ns2 = scxmlElement.attrib.get('xmlns:customActionDomain')
        """
        if ns1 == None or ns2 == None:
            # throw exception missing mandatory NS attributes
            logging.getLogger("scxml4py").error("Missing mandatory NS attribute.")
            return        
        self.mNamespaces = {'scxml': ns1, 'customActionDomain': ns2}
        logging.getLogger("scxml4py").debug("Namaspaces: " + ns1 + ", " + ns2)
        """
        
        # Parsing version attribute (mandatory)
        ver = self.parseAttribute(scxmlElement, "version", True)
        if ver != "1.0":
            raise ScxmlSyntaxError("The 'version' attribute of an <scxml> element must have value 1.0.")
        logging.getLogger("scxml4py").debug("Version: " + ver)
        
        # Parsing name attribute (optional)
        name = self.parseAttribute(scxmlElement, "name", False)
        if name != None:
            self.mStateMachine.setId(name)
            logging.getLogger("scxml4py").debug("Name: " + name)
        else:
            logging.getLogger("scxml4py").debug("Optional name attribute not available.")

    def createStatesMap(self):
        for key in self.mStatesInfo:
            s = self.mStatesInfo[key].mState
            assert(s != None)
            self.mStateMachine.updateStatesMap(s.getAbsoluteId(), s)
            
    def parseModel(self, root): 
        #ET.dump(root)                        
        self.parseScxml(root)
        self.parseStates(None, root)
        self.parseScxmlInitialTransition(root)
        self.parseTransitions()
        self.parseInvokes()
        self.createStatesMap()
          
    def read(self, fileName, actionList, activityList):
        self.mActionList = actionList
        self.mActivityList = activityList
        tree = ET.parse(fileName)
        # by default set the StateMachine ID as the filename
        # to be overwritten by the name provided in the name attribute
        # of the scxml element
        self.mStateMachine.setId(fileName)
        self.parseModel(tree.getroot())
        return self.mStateMachine
    
    def readString(self, modelName, text, actionList, activityList):
        self.mActionList = actionList
        self.mActivityList = activityList
        if modelName != None:
            self.mStateMachine.setId(modelName)
        tree = ET.fromstring(text)
        self.parseModel(tree)
        return self.mStateMachine
    