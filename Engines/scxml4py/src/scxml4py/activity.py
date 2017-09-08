'''
    activity module part of scxml4py.
    
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

import asyncio
import threading
import logging

logger = logging.getLogger("scxml4py")

class AbstractActivity(object):
    def __init__(self, theId, theEventQueue = None, theData = None):
        self.mId = theId
        self.mIsRunning = False
        self.mEventQueue = theEventQueue # queue to post internal events
        self.mData = theData # shared data with other actions and activities
        
    def __str__(self):
        return str(self.mId)

    def getId(self):
        return self.mId

    def setId(self, theId):
        self.mId = theId
    
    def sendInternalEvent(self, theEvent):
        if self.mEventQueue and theEvent:
            logger.debug("Activity <%s>: Triggering internal event <%s>"%(self, theEvent)) 
            self.mEventQueue.put(theEvent, True, 2)
    
    def isRunning(self):
        return self.mIsRunning
    
    def setRunning(self, running):
        self.mIsRunning = running

    def start(self):
        # this method has to be implemented in the specialized activity class
        pass
    
    def stop(self):
        # this method has to be implemented in the specialized activity class
        pass
        
    def run(self):
        # this method has to be implemented in the specialized activity class
        pass


class ThreadedActivity(AbstractActivity):
    """Threading based activity
    """
    def __init__(self, theId, theEventQueue = None, theData = None):
        super(ThreadedActivity, self).__init__(theId, theEventQueue, theData)
        self.mThread = None
        
    def start(self):
        if self.mThread and self.mThread.isAlive():
            logger.debug("Activity <%s> already started"%self)
            return
        
        logger.debug("Starting activity <%s>"%self)
        self.mThread = threading.Thread(target=self.run)
        self.setRunning(True)
        self.mThread.start()
    
    def stop(self):
        if self.mThread and self.isRunning():
            logger.debug("Stopping activity <%s>"%self)
            self.setRunning(False)
            self.mThread.join() # #TODO specify timeout?
        else:
            logger.debug("Activity <%s> already stopped"%self)
        
    def run(self):
        # this method has to be implemented in the specialized activity class
        pass

class CoroActivity(AbstractActivity):
    """Coroutine based activity.
    
    Concrete implementations should override and implement CoroActivity.run.

    @note CoroActivity requires asyncio for task scheduling. That means that the asyncio
    event loop needs to run for the activity to be run.
    """
    def __init__(self, evid, queue=None, data=None):
        super(CoroActivity, self).__init__(evid, queue, data)
        self.mTask = None

    def start(self):
        logger.debug("Starting activity <%s>"%self)
        self.mTask = asyncio.ensure_future(self.run())
        self.setRunning(True)

    def stop(self):
        if self.mTask and self.isRunning():
            logger.debug("Stopping activity <%s>"%self)
            self.mTask.cancel()
            self.mTask = None
            self.setRunning(False)
        else:
            logger.debug("Activity already stopped <%s>"%self)

    async def run(self):
        """ Implemented by concrete classes """
        pass
