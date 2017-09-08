'''
    context module part of scxml4py.
    
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
    $Id: context.py 1061 2015-07-13 15:03:59Z landolfa $
'''


class Context(object):
    def __init__(self):
        self.mName = ""
        self.mSessionId = ""
        self.mLastEvent = None
        self.mElements = dict()
        
    def __str__(self):
        return self.mName.__str__() + "_" + self.mSessionId.__str__()

    def getName(self):
        return self.mName
    
    def getSessionId(self):
        return self.mSessionId

    def getLastEvent(self):
        return self.mLastEvent;
    
    def getElement(self, theName):
        if theName in self.mElements.keys():
            return self.mElements[theName]
        else:
            return None
        
    def setName(self, theName):
        self.mName = theName
        
    def setSessionId(self, theId):
        self.mSessionId = theId
            
    def setLastEvent(self, theEvent):
        self.mLastEvent = theEvent
    
    def addElement(self, theName, theElement):
        if theElement != None and theName != None:
            self.mElements[theName] = theElement
    
    def delElement(self, theName):
        if theName in self.mElements.keys():
            del self.mElements[theName]
        