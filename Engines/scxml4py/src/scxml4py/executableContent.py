'''
    executableContent module part of scxml4py.
    
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
    $Id: executableContent.py 1061 2015-07-13 15:03:59Z landolfa $
'''

import logging
from functools import total_ordering

@total_ordering
class ExecutableContent(object):
    
    def __init__(self):
        self.mActions = list()

    def __str__(self):
        tmp = ""
        isFirst = True
        for a in self.mActions:
            if isFirst == True:
                isFirst = False
            else:
                tmp += " "
            tmp += a.__str__()
        return tmp

    def __lt__(self, other):
        if other == None:
            return False
        return self.__str__() < other.__str__()    

    def __eq__(self, other):
        if other == None:
            return False
        return self.__str__() == other.__str__()    
    
    def getActions(self):
        return self.mActions
    
    def setActions(self, actions):
        self.mActions = actions
        
    def addAction(self, theAction):
        if theAction != None:
            self.mActions.append(theAction)

    def execute(self, theContext):
        for a in self.mActions:
            logging.getLogger('scxml4py').debug("Executing action <" + a.getId() + ">")    
            a.execute(theContext)
            
    def evaluate(self, theContext):
        res = True
        for a in self.mActions:
            if a.evaluate(theContext) == False:
                res = False
                logging.getLogger('scxml4py').debug("Evaluating guard <" + a.getId() + "> == false")    
                break
            else:
                logging.getLogger('scxml4py').debug("Evaluating guard <" + a.getId() + "> == true")           
        return res 
            
    