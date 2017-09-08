'''
    testExecutor module part of scxml4py unit tests.
    
    @authors: landolfa
    @date: 2016-12-30
    
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
    $Id: testExecutor.py 1061 2015-07-13 15:03:59Z landolfa $
'''

from queue import Queue

import unittest
import logging
import time
import scxml4py.helper
from scxml4py.context import Context
from scxml4py.event import Event
from scxml4py.action import Action
from scxml4py.state import StateAtomic
from scxml4py.state import StateCompound
from scxml4py.stateMachine import StateMachine
from scxml4py.executor import Executor

class CounterAction(Action):
    def __init__(self, theId):
        Action.__init__(self, theId)
        self.mCounter = 0
        self.mLastExecutionTime = 0
    def execute(self, theCtx):
        self.mCounter += 1
        self.mLastExecutionTime = time.clock()
        #print("executing action: " + self.getId())
    def getCounter(self):
        return self.mCounter
    def getLastExecutionTime(self):
        return self.mLastExecutionTime
    def resetCounter(self):
        self.mCounter = 0
    def resetLastExecutionTime(self):
        self.mLastExecutionTime = 0
        
class TriggerEventAction(Action):
    def __init__(self, theId, theEventQueue, theEvent):
        Action.__init__(self, theId, theEventQueue)
        self.mEvent = theEvent
    def execute(self, theCtx):
        self.sendInternalEvent(self.mEvent) 
        
class TestExecutor(unittest.TestCase):

    def setUp(self):        
        unittest.TestCase.setUp(self)
        logging.basicConfig(format='%(asctime)s - %(levelname)s - %(threadName)s - %(module)s - %(funcName)s - %(message)s', level=logging.DEBUG)

    def testExecutorSM1(self):
        """
        Verify transition on on 2 states SM.
        o --> S1(entry: a1, exit: a2) -e/a-> S2(entry: a3, exit: a4)
        """
        S1 = StateAtomic("S1")
        S1.setIsInitial(True)
        S2 = StateAtomic("S2")
        S2.setIsFinal(True)
        e = Event("myEvent")
        a = CounterAction("actionOnTransition")
        a1 = CounterAction("actionEnterS1")
        a2 = CounterAction("actionExitS1")
        a3 = CounterAction("actionEnterS2")
        a4 = CounterAction("actionExitS2")
        S1.addEntryAction(a1)
        S1.addExitAction(a2)     
        S2.addEntryAction(a3)
        S2.addExitAction(a4)
        S1.addTransition(S2, e, a, None)
        sm = StateMachine("myStateMachine1")
        sm.setInitialState(S1, None)
        ctx = Context()
        executor = Executor(sm, ctx)
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 0)
        assert(a.getCounter() == 0)
        assert(a1.getCounter() == 0)
        assert(a2.getCounter() == 0)
        assert(a3.getCounter() == 0)
        assert(a4.getCounter() == 0)
        executor.start()
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 1)
        assert(S1 in executor.getStatus())
        assert(a.getCounter() == 0)
        assert(a1.getCounter() == 1)
        assert(a2.getCounter() == 0)
        assert(a3.getCounter() == 0)
        assert(a4.getCounter() == 0)
        e1 = Event("myEvent")
        executor.processEvent(e1)
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 1)
        assert(S2 in executor.getStatus())
        assert(ctx.getLastEvent().getId() == e1.getId())
        assert(a.getCounter() == 1)
        assert(a1.getCounter() == 1)
        assert(a2.getCounter() == 1)
        assert(a3.getCounter() == 1)
        assert(a4.getCounter() == 0)
        executor.stop()
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 0)
        assert(a.getCounter() == 1)
        assert(a1.getCounter() == 1)
        assert(a2.getCounter() == 1)
        assert(a3.getCounter() == 1)
        assert(a4.getCounter() == 1)

    def testExecutorSM2(self):
        """
        Verify entry/exit action execution with composite states + self transition.
        S1
          S11
            S111
          S12
            S121
        """
        s1 = StateCompound("S1")
        s11 = StateCompound("S11")
        s12 = StateCompound("S12")
        s111 = StateAtomic("S111")
        s121 = StateAtomic("S121")
        
        s1.addSubstate(s11)
        s1.addSubstate(s12)
        s11.addSubstate(s111)
        s12.addSubstate(s121)

        sm = StateMachine("myStateMachine2")
        sm.addSubstate(s1)
        
        sm.setInitialState(s1, None)
        s1.setInitialState(s11, None)
        s11.setInitialState(s111, None)
        s12.setInitialState(s121, None)

        entryS1 = CounterAction("entryS1")
        exitS1 = CounterAction("exitS1")
        s1.addEntryAction(entryS1)
        s1.addExitAction(exitS1)
        entryS11 = CounterAction("entryS11")
        exitS11 = CounterAction("exitS11")
        s11.addEntryAction(entryS11)
        s11.addExitAction(exitS11)
        entryS12 = CounterAction("entryS12")
        exitS12 = CounterAction("exitS12")
        s12.addEntryAction(entryS12)
        s12.addExitAction(exitS12)
        entryS111 = CounterAction("entryS111")
        exitS111 = CounterAction("exitS111")
        s111.addEntryAction(entryS111)
        s111.addExitAction(exitS111)
        entryS121 = CounterAction("entryS121")
        exitS121 = CounterAction("exitS121")
        s121.addEntryAction(entryS121)
        s121.addExitAction(exitS121)

        s1.addTransition(s1, Event("e1"), None, None)        
        s11.addTransition(s12, Event("e2"), None, None)        
        s111.addTransition(s121, Event("e3"), None, None)        

        ctx = Context()
        executor = Executor(sm, ctx)

        executor.start()
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 3)
        assert(s1 in executor.getStatus())
        assert(s11 in executor.getStatus())
        assert(s111 in executor.getStatus())
        assert(entryS1.getCounter() - exitS11.getCounter() == 1)
        assert(entryS11.getCounter() - exitS11.getCounter() == 1)
        assert(entryS111.getCounter() - exitS111.getCounter() == 1)
        assert(entryS12.getCounter() - exitS12.getCounter() == 0)
        assert(entryS121.getCounter() - exitS121.getCounter() == 0)
        # verify order
        assert(entryS11.getLastExecutionTime() >= entryS1.getLastExecutionTime())
        assert(entryS111.getLastExecutionTime() >= entryS11.getLastExecutionTime())
        
        executor.processEvent(Event("e3"))
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 3)
        assert(s1 in executor.getStatus())
        assert(s12 in executor.getStatus())
        assert(s121 in executor.getStatus())
        assert(entryS1.getCounter() - exitS1.getCounter() == 1)
        assert(entryS11.getCounter() - exitS11.getCounter() == 0)
        assert(entryS111.getCounter() - exitS111.getCounter() == 0)
        assert(entryS12.getCounter() - exitS12.getCounter() == 1)
        assert(entryS121.getCounter() - exitS121.getCounter() == 1)
        # verify order
        assert(exitS111.getLastExecutionTime() <= exitS11.getLastExecutionTime())
        assert(entryS121.getLastExecutionTime() >= entryS12.getLastExecutionTime())

        executor.stop()
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 0)
        assert(entryS1.getCounter() - exitS1.getCounter() == 0)
        assert(entryS11.getCounter() - exitS11.getCounter() == 0)
        assert(entryS111.getCounter() - exitS111.getCounter() == 0)
        assert(entryS12.getCounter() - exitS12.getCounter() == 0)
        assert(entryS121.getCounter() - exitS121.getCounter() == 0)
        # verify order
        assert(exitS121.getLastExecutionTime() <= exitS12.getLastExecutionTime())
        assert(exitS12.getLastExecutionTime() <= exitS1.getLastExecutionTime())
        
        
    def testExecutorSM3(self):
        """
        Verify transitions on composite states + event-less transition.
        STATE 
          ONLINE
            IDLE
            PRESETTIG
              STEP1
              STEP2
            TRACKING
          STANDBY
            IDLE
        """
        state = StateCompound("STATE")
        stateStandby = StateCompound("STATE:STANDBY")
        stateStandbyIdle = StateAtomic("STATE:STANDBY:IDLE")
        stateOnline = StateCompound("STATE:ONLINE")
        stateOnlineIdle = StateAtomic("STATE:ONLINE:IDLE")
        stateOnlinePresetting = StateCompound("STATE:ONLINE:PRESETTING")
        stateOnlinePresettingStep1 = StateAtomic("STATE:ONLINE:PRESETTING:STEP1")
        stateOnlinePresettingStep2 = StateAtomic("STATE:ONLINE:PRESETTING:STEP2")
        stateOnlineTracking = StateAtomic("STATE:ONLINE:TRACKING")
        
        state.addSubstate(stateStandby)
        state.addSubstate(stateOnline)
        stateStandby.addSubstate(stateStandbyIdle)
        stateOnline.addSubstate(stateOnlineIdle)
        stateOnline.addSubstate(stateOnlineTracking)
        stateOnline.addSubstate(stateOnlinePresetting)
        stateOnlinePresetting.addSubstate(stateOnlinePresettingStep1)
        stateOnlinePresetting.addSubstate(stateOnlinePresettingStep2)

        sm = StateMachine("myStateMachine3")
        sm.addSubstate(state)
        
        sm.setInitialState(state, None)
        state.setInitialState(stateStandby, None)
        stateStandby.setInitialState(stateStandbyIdle, None)
        stateOnline.setInitialState(stateOnlineIdle, None)
        stateOnlinePresetting.setInitialState(stateOnlinePresettingStep1, None)

        resetEvent = Event("resetEvent")                
        initEvent = Event("initEvent")
        presetEvent = Event("presetEvent")
        stopEvent = Event("stopEvent")
        stepEvent = Event("stepEvent")

        state.addTransition(stateStandbyIdle, resetEvent, None, None)        
        stateStandbyIdle.addTransition(stateOnlineIdle, initEvent, None, None)
        stateOnlineIdle.addTransition(stateOnlinePresetting, presetEvent, None, None)
        stateOnlinePresettingStep1.addTransition(stateOnlinePresettingStep2, stepEvent, None, None)
        # event-less transition from STEP2 to TRACKING
        stateOnlinePresettingStep2.addTransition(stateOnlineTracking, None, None, None)
        stateOnline.addTransition(stateOnlineIdle, stopEvent, None, None)

        ctx = Context()
        executor = Executor(sm, ctx)
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 0)

        executor.start()
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 3)
        assert(state in executor.getStatus())
        assert(stateStandby in executor.getStatus())
        assert(stateStandbyIdle in executor.getStatus())
        
        executor.processEvent(Event("initEvent"))
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 3)
        assert(state in executor.getStatus())
        assert(stateOnline in executor.getStatus())
        assert(stateOnlineIdle in executor.getStatus())

        executor.processEvent(Event("presetEvent"))
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 4)
        assert(state in executor.getStatus())
        assert(stateOnline in executor.getStatus())
        assert(stateOnlinePresetting in executor.getStatus())
        assert(stateOnlinePresettingStep1 in executor.getStatus())

        executor.processEvent(Event("stepEvent"))
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 3)
        assert(state in executor.getStatus())
        assert(stateOnline in executor.getStatus())
        assert(stateOnlineTracking in executor.getStatus())
        
        executor.processEvent(Event("stopEvent"))
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 3)
        assert(state in executor.getStatus())
        assert(stateOnline in executor.getStatus())
        assert(stateOnlineIdle in executor.getStatus())

        executor.processEvent(Event("resetEvent"))
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 3)
        assert(state in executor.getStatus())
        assert(stateStandby in executor.getStatus())
        assert(stateStandbyIdle in executor.getStatus())

        executor.stop()
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 0)
        print(scxml4py.helper.formatModel(sm))
       
    def testExecutorSM4(self):
        internalEventQueue = Queue()
        externalEventQueue = Queue()
        a1 = TriggerEventAction("a1", internalEventQueue, Event("e2"))
        s1 = StateAtomic("S1")
        s2 = StateAtomic("S2")
        s2.addEntryAction(a1)
        sm = StateMachine("StateMachine4")
        sm.addSubstate(s1)
        sm.addSubstate(s2)
        s1.addTransition(s2, Event("e1"), None, None)
        s2.addTransition(s1, Event("e2"), None, None)       
        sm.setInitialState(s1, None)
        print(scxml4py.helper.formatModel(sm))
        ctx = Context()
        executor = Executor(sm, ctx, externalEventQueue, internalEventQueue)
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 0)
        executor.start()
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 1)
        assert(s1 in executor.getStatus())
        executor.processEvent(Event("e1"))
        print(scxml4py.helper.formatStatus(executor.getStatus()))
        assert(executor.getStatus().__len__() == 1)
        assert(s1 in executor.getStatus())
        
if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()

