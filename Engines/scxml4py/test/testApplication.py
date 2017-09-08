'''
    testApplication module part of scxml4py unit tests.
    
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
import threading
import time
from pathlib import Path
from queue import Queue

import scxml4py.helper
from scxml4py.listeners import StatusListener
from scxml4py.reader import Reader
from scxml4py.action import Action
from scxml4py.activity import ThreadedActivity
from scxml4py.context import Context
from scxml4py.event import Event
from scxml4py.executor import Executor
from scxml4py.exceptions import ScxmlError, ScxmlSyntaxError


class ActionStatus(Action, StatusListener):
    # implemented by the developer, stub can be generated
    def __init__(self, theData):
        Action.__init__(self, "ActionStatus", None, theData)
        self.mStatus = None
    
    def notify(self, status):
        self.mStatus = status
    
    def execute(self, theCtx):
        logging.getLogger("scxml4py").info("Status: <" + scxml4py.helper.formatStatus(self.mStatus) + ">")

class ActionSetup(Action):
    # implemented by the developer, stub can be generated
    def __init__(self, theEventQueue, theData):
        Action.__init__(self, "ActionSetup", theEventQueue, theData)

    def execute(self, theCtx):
        logging.getLogger("scxml4py").info("Configuring ...")
        self.sendInternalEvent(Event("DONE"))
        
class ActionExit(Action):
    # implemented by the developer, stub can be generated
    def __init__(self, theData):
        Action.__init__(self, "ActionExit", None, theData)

    def execute(self, theCtx):
        logging.getLogger("scxml4py").info("Bye bye ...")


class ActivityBusy(ThreadedActivity):
    def __init__(self, theEventQueue, theData):
        ThreadedActivity.__init__(self, "ActivityBusy", theEventQueue, theData)

    def run(self):
        counter = 0
        while self.isRunning() == True:
            logging.getLogger("scxml4py").info("Activity <" + self.getId() + "> is running...")
            time.sleep(1)
            counter += 1
            if counter == 5:
                self.sendInternalEvent(Event("STOP"))
                self.setRunning(False)
                break
            
class Data(object):
    # implemented by the developer
    # data shared between actions and activities
    def __init__(self):
        self.mSharedInfo = None
    
    def getSharedInfo(self):
        # requires mutex
        return sharedInfo
    
    def setSharedInfo(self, sharedInfo):
        # requires mutex
        self.mSharedInfo = sharedInfo


class ActionMgr(object):
    # generated class    
    def __init__(self):
        self.mActionList = list()
        self.mActivityList = list()

    def getAction(self, theActionId):
        for a in self.mActionList:
            if a.getId() == theActionId:
                return a
        return None

    def getActions(self):
        return self.mActionList
    
    def getActivity(self, theActivityId):
        for a in self.mActivityList:
            if a.getId() == theActivityId:
                return a
        return None
    
    def getActivities(self):
        return self.mActivityList
    
    def createActions(self, theEventQueue, theData):
        self.mActionList = [ActionStatus(theData), ActionSetup(theEventQueue, theData), ActionExit(theData)]
    
    def createActivities(self, theEventQueue, theData):
        self.mActivityList = [ActivityBusy(theEventQueue, theData)]
    
        
class Application(threading.Thread):   
    def __init__(self, theModelPath, theEventQueue):
        threading.Thread.__init__(self)
        self.mRunning = False
        self.mModelPath = theModelPath
        self.mEventQueue = theEventQueue
        self.mData = Data()
        
        self.mActionMgr = ActionMgr()
        self.mActionMgr.createActions(self.mEventQueue, self.mData)
        self.mActionMgr.createActivities(self.mEventQueue, self.mData)
        
        self.mContext = Context()
        logging.getLogger("scxml4py").info("Loading SCXML model: <" + theModelPath.__str__() + ">") 
        self.mModel = Reader().read(theModelPath.__str__(), self.mActionMgr.getActions(), self.mActionMgr.getActivities())
        logging.getLogger("scxml4py").debug("Loaded SCXML model: " + scxml4py.helper.formatModel(self.mModel))
        self.mExecutor = Executor(self.mModel, self.mContext)
        self.mExecutor.addStatusListener(self.mActionMgr.getAction("ActionStatus"))
        logging.getLogger("scxml4py").info("Status: <" + scxml4py.helper.formatStatus(self.mExecutor.getStatus()) + ">")
        
    def run(self):
        logging.getLogger("scxml4py").info("Starting execution of <" + self.mModel.getId() + ">")
        self.mRunning = True
        self.mExecutor.start()
        logging.getLogger("scxml4py").info("Status: <" + scxml4py.helper.formatStatus(self.mExecutor.getStatus()) + ">")
        while self.mRunning == True:
            theEvent = self.mEventQueue.get(True, None)
            #Queue().get(block, timeout)
            # loop on the event queue send the event to the SM engine
            logging.getLogger("scxml4py").debug("Application received event = <" + theEvent.__str__() + ">")
            self.mExecutor.processEvent(theEvent)
            if theEvent.getId() == "EXIT":
                logging.getLogger("scxml4py").debug("Application exiting...")
                self.mRunning = False
        logging.getLogger("scxml4py").info("Stopping execution of <" + self.mModel.getId() + ">")
        self.mExecutor.stop()
        logging.getLogger("scxml4py").info("Status: <" + scxml4py.helper.formatStatus(self.mExecutor.getStatus()) + ">")
            
            
            
class TestApplication(unittest.TestCase):

    def setUp(self):        
        unittest.TestCase.setUp(self)
        logging.basicConfig(format='%(asctime)s - %(levelname)s - %(threadName)s - %(module)s - %(funcName)s - %(message)s', level=logging.DEBUG)
        # setup the path to the directory containing the SCXML models
        self.mModelsPath = Path(".")
        for x in self.mModelsPath.iterdir():
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
          
    def testApplication(self):
        myEventQueue = Queue()
        filePath = self.mModelsPath / 'scxmlApplication.xml'
        filePath.resolve()
        myApp = Application(filePath, myEventQueue)
        myApp.start()
        myEventQueue.put(Event("STATUS"), True, 2)
        myEventQueue.put(Event("START"), True, 2)
        time.sleep(2)
        myEventQueue.put(Event("STATUS"), True, 2)
        myEventQueue.put(Event("STOP"), True, 2)
        myEventQueue.put(Event("STATUS"), True, 2)
        myEventQueue.put(Event("START"), True, 2)
        time.sleep(10)
        myEventQueue.put(Event("STATUS"), True, 2)
        myEventQueue.put(Event("SETUP"), True, 2)
        time.sleep(1)
        myEventQueue.put(Event("STATUS"), True, 2)        
        myEventQueue.put(Event("EXIT"), True, 2)
        myApp.join()
        
        

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()

