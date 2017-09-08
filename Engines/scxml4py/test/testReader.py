'''
    testReader module part of scxml4py unit tests.
    
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

import unittest
import logging
import sys
import scxml4py.helper
from pathlib import Path
from scxml4py.reader import Reader
from scxml4py.action import Action
from scxml4py.activity import ThreadedActivity
from scxml4py.context import Context
from scxml4py.event import Event
from scxml4py.executor import Executor
from scxml4py.exceptions import ScxmlError, ScxmlSyntaxError


class GuardTrue(Action):
    def __init__(self, theId):
        Action.__init__(self, theId)
    def evaluate(self, theCtx):
        return True
    
class TestParser(unittest.TestCase):

    def setUp(self):        
        unittest.TestCase.setUp(self)
        logging.basicConfig(format='%(asctime)s - %(levelname)s - %(threadName)s - %(module)s - %(funcName)s - %(message)s', level=logging.INFO)
        # setup the path to the directory containing the SCXML models
        self.mModelsPath = Path(".")
        for x in self.mModelsPath.iterdir():
            #print(x)
            if x.is_dir() and x.__str__() == "models":
                self.mModelsPath = self.mModelsPath / "models"
                #print("found models dir")
                break
            elif x.is_dir() and x.__str__() == "test":
                self.mModelsPath = self.mModelsPath / "test" / "models"
                #print("found test dir")
                break
        #self.mCurrentPath.resolve()
        #print("===> " + self.mCurrentPath.__str__())  
    
    def testScxmlModel1(self):
        a1 = Action("logEntry")
        a2 = Action("logExit")
        a3 = Action("logTrans")    
        g1 = GuardTrue("logGuard")                
        actionList = [a1, a2, a3, g1]
        activityTest = ThreadedActivity("activityTest")
        activityList = [activityTest]
        reader = Reader()            
        filePath = self.mModelsPath / 'scxmlModel1.xml'
        filePath.resolve()        
        sm = reader.read(filePath.__str__(), actionList, activityList)
        print(scxml4py.helper.formatModel(sm))
        ctx = Context()
        ex = Executor(sm, ctx)
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "")
        ex.start()
        #print(scxml4py.helper.formatStatus(ex.getStatus()))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "NOTREADY STANDBY STATE")
        ex.processEvent(Event("INIT"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "READY STANDBY STATE")
        ex.processEvent(Event("ENABLE"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "IDLE OPERATIONAL STATE")
        ex.processEvent(Event("START"))
        #print(scxml4py.helper.formatStatus(ex.getStatus()))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "BUSY OPERATIONAL STATE")
        ex.processEvent(Event("STOP"))
        #print(scxml4py.helper.formatStatus(ex.getStatus()))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "IDLE OPERATIONAL STATE")
        ex.processEvent(Event("DISABLE"))
        #print(scxml4py.helper.formatStatus(ex.getStatus()))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "READY STANDBY STATE")
        ex.processEvent(Event("ENABLE"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "IDLE OPERATIONAL STATE")
        ex.processEvent(Event("RESET"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "NOTREADY STANDBY STATE")
        ex.stop()
    
    def testScxmlModelParallel(self):
        reader = Reader()
        filePath = self.mModelsPath / 'scxmlModelParallel.xml'
        filePath.resolve()
        sm = reader.read(filePath.__str__(), None, None)
        print(scxml4py.helper.formatModel(sm))
        ctx = Context()
        ex = Executor(sm, ctx)
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "")
        ex.start()
        #print(scxml4py.helper.formatStatus(ex.getStatus()))        
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "acquisition idle mode normal online")
        ex.processEvent(Event("startRec"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "acquisition mode normal online recording")      
        ex.processEvent(Event("startSim"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "acquisition mode online recording simulation")
        ex.processEvent(Event("stopRec"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "acquisition idle mode online simulation")
        ex.processEvent(Event("stopSim"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "acquisition idle mode normal online")
        ex.stop()
        
    def testScxmlModelHistory(self):
        reader = Reader()
        filePath = self.mModelsPath / 'scxmlModelHistory.xml'
        filePath.resolve()
        sm = reader.read(filePath.__str__(), None, None)
        print(scxml4py.helper.formatModel(sm))
        ctx = Context()
        ex = Executor(sm, ctx)
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "")
        ex.start()
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "IDLE")
        ex.processEvent(Event("Play"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "PLAYING SONG1")
        ex.processEvent(Event("Next"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "PLAYING SONG2")
        ex.processEvent(Event("Next"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "PLAYING SONG3")
        ex.processEvent(Event("Next"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "PLAYING SONG1")
        ex.processEvent(Event("Next"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "PLAYING SONG2")
        ex.processEvent(Event("Pause"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "PAUSE")
        ex.processEvent(Event("Play"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "PLAYING SONG2")
        ex.processEvent(Event("Stop"))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "IDLE")
        ex.stop()
    
    def testScxmlSyntaxErrors1(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml1 xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM">
        </scxml1>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "Root element of an SCXML document must be an <scxml> element.")    

    def testScxmlSyntaxErrors2(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM"
           initial="STATE">
        </scxml>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "Missing mandatory attribute 'version' for element <{http://www.w3.org/2005/07/scxml}scxml>.")    
    
    def testScxmlSyntaxErrors3(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM"
           version="0.0"
           initial="STATE">
        </scxml>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "The 'version' attribute of an <scxml> element must have value 1.0.")    

    def testScxmlSyntaxErrors4(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM"
           version="1.0"
           initial="STATE1">
          <state id="STATE">
          </state>
        </scxml>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "Initial state 'STATE1' is not part of the parsed states.")    

    def testScxmlSyntaxErrors5(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM"
           version="1.0"
           initial="STATE">
          <state id="STATE">
          </state>
          <parallel id="STATE">
          </parallel>
        </scxml>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "An SCXML element cannot have substates and parallel states at the same time.")    

    def testScxmlSyntaxErrors6(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM"
           version="1.0"
           initial="STATE">
          <state>
          </state>
        </scxml>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "Missing mandatory attribute 'id' for element <{http://www.w3.org/2005/07/scxml}state>.")    

    def testScxmlSyntaxErrors7(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM"
           version="1.0"
           initial="STATE">
          <parallel>
          </parallel>
        </scxml>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "Missing mandatory attribute 'id' for element <{http://www.w3.org/2005/07/scxml}parallel>.")    

    def testScxmlSyntaxErrors8(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM"
           version="1.0"
           initial="STATE">
          <state id="STATE">
            <initial>
              <transition event="e"/>
            </initial>
          </state>
        </scxml>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "Initial transition must specify target state.")    

    def testScxmlSyntaxErrors9(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM"
           version="1.0"
           initial="STATE">
          <state id="STATE">
            <initial>
              <transition target="XXX"/>
            </initial>
          </state>
        </scxml>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "Target state 'XXX' is not part of the parsed states.")    

    def testScxmlSyntaxErrors10(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM"
           version="1.0"
           initial="STATE">
          <state id="STATE">
              <transition target="XXX">
                  <customActionDomain:myAction name="myAction"/>
              </transition>
          </state>
          <state id="XXX">
          </state>
        </scxml>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "Custom action id 'myAction' not found.")    

    def testScxmlSyntaxErrors11(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM"
           version="1.0"
           initial="STATE">
          <state id="STATE">
              <transition target="XXX">
                  <customActionDomain:logEntry/>
              </transition>
          </state>
          <state id="XXX">
          </state>
        </scxml>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "Missing mandatory attribute 'name' for element <{http://my.custom-actions.domain/CUSTOM}logEntry>.")    

    def testScxmlSyntaxErrors12(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM"
           version="1.0"
           initial="STATE">
          <state id="STATE">
              <invoke/>  
          </state>
        </scxml>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "Missing mandatory attribute 'id' for element <{http://www.w3.org/2005/07/scxml}invoke>.")    

    def testScxmlSyntaxErrors13(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM"
           version="1.0"
           initial="STATE">
          <state id="STATE">
              <invoke id="myActivity"/>  
          </state>
        </scxml>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "Activity id 'myActivity' not found.")    

    def testScxmlSyntaxErrors14(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM"
           version="1.0"
           initial="STATE">
          <state id="STATE">
              <transition cond="myGuard"/>  
          </state>
        </scxml>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "Custom guard id 'myGuard' not found.")    

    def testScxmlSyntaxErrors15(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM"
           version="1.0"
           initial="STATE">
          <history/>
        </scxml>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "Missing mandatory attribute 'id' for element <{http://www.w3.org/2005/07/scxml}history>.")    

    def testScxmlSyntaxErrors16(self):
        model = """<?xml version="1.0" encoding="us-ascii"?>
        <scxml xmlns="http://www.w3.org/2005/07/scxml" xmlns:customActionDomain="http://my.custom-actions.domain/CUSTOM"
           version="1.0"
           initial="myHistoryState">
          <history id="myHistoryState">
            <transition/>
          </history>
        </scxml>
        """
        try:
            sm = Reader().readString("test", model, None, None)
        except ScxmlSyntaxError as e:
            assert(e.args[0] == "Transition from History state must have a target state.")    
    
    def testScxmlModelCloned(self):
        listAction = [Action("myEntryAction"), Action("myExitAction"), Action("myTransAction"), GuardTrue("myGuard")]
        listActivity = [ThreadedActivity("myRecordingActivity")]
        mapClonedAction = {}
        mapClonedAction["myEntryAction1"] = Action("myEntryAction1")
        mapClonedAction["myEntryAction2"] = Action("myEntryAction2")
        mapClonedAction["myEntryAction3"] = Action("myEntryAction3")
        mapClonedAction["myExitAction1"] = Action("myExitAction1")
        mapClonedAction["myExitAction2"] = Action("myExitAction2")
        mapClonedAction["myExitAction3"] = Action("myExitAction3")
        mapClonedAction["myTransAction1"] = Action("myTransAction1")
        mapClonedAction["myTransAction2"] = Action("myTransAction2")
        mapClonedAction["myTransAction3"] = Action("myTransAction3")
        mapClonedAction["myGuard1"] = GuardTrue("myGuard1")
        mapClonedAction["myGuard2"] = GuardTrue("myGuard2")
        mapClonedAction["myGuard3"] = GuardTrue("myGuard3")
        mapClonedActivity = {}
        mapClonedActivity["myRecordingActivity1"] = ThreadedActivity("myRecordingActivity1")
        mapClonedActivity["myRecordingActivity2"] = ThreadedActivity("myRecordingActivity2")
        mapClonedActivity["myRecordingActivity3"] = ThreadedActivity("myRecordingActivity3")
        reader = Reader()
        filePath = self.mModelsPath / 'scxmlModelCloned.xml'
        filePath.resolve()
        sm = reader.read(filePath.__str__(), listAction, listActivity)        
        print(scxml4py.helper.formatModel(sm))
        sm.cloneParallel(3, "online", mapClonedAction, mapClonedActivity)
        print(scxml4py.helper.formatModel(sm))
        ctx = Context()
        ex = Executor(sm, ctx)
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "")
        ex.start()
        #print(scxml4py.helper.formatStatus(ex.getStatus()))       
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "acquisition acquisition1 acquisition2 acquisition3 idle idle1 idle2 idle3 online")
        ex.processEvent(Event("startRec"))
        #print(scxml4py.helper.formatStatus(ex.getStatus()))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "acquisition acquisition1 acquisition2 acquisition3 idle1 idle2 idle3 online recording")
        ex.processEvent(Event("startRec1"))
        #print(scxml4py.helper.formatStatus(ex.getStatus()))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "acquisition acquisition1 acquisition2 acquisition3 idle2 idle3 online recording recording1")
        ex.processEvent(Event("stopRec"))
        #print(scxml4py.helper.formatStatus(ex.getStatus()))
        assert(scxml4py.helper.formatStatus(ex.getStatus()) == "acquisition acquisition1 acquisition2 acquisition3 idle idle2 idle3 online recording1")
        ex.stop()
        
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()

