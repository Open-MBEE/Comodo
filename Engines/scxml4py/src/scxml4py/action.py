'''
    action module part of scxml4py
    
    @authors: landolfa
    @date: Jan 9, 2017
    
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
from functools import total_ordering

@total_ordering
class Action(object):
    def __init__(self, theId, theEventQueue = None, theData = None):
        self.mId = theId
        self.mEventQueue = theEventQueue # queue to post internal events
        self.mData = theData # shared data with other actions and activities
        
    def __str__(self):
        return self.mId.__str__()

    def __lt__(self, other):
        if other == None:
            return False        
        return self.mId.__str__() < other.mId.__str__()

    def __eq__(self, other):
        if other == None:
            return False
        return self.mId.__str__() == other.mId.__str__()
    
    def getId(self):
        return self.mId
            
    def getData(self):
        return self.mData
    
    def setId(self, theId):
        self.mId = theId

    def setData(self, theData):
        self.mData = theData

    def sendInternalEvent(self, theEvent):
        if self.mEventQueue != None and theEvent != None:
            logging.getLogger("scxml4py").debug("Triggering internal event <" + theEvent.__str__() + ">") 
            self.mEventQueue.put(theEvent, True, 2)
        
    def execute(self, theCtx):
        pass
    
    def evaluate(self, theCtx):
        return False
    