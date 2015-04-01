#*******************************************************************************
# ALMA - Atacama Large Millimiter Array
# (c) UNSPECIFIED - FILL IN, 2009 
# 
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
# 
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
# 
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
#
#  "@(#) $Id$"
#
# who                when       what
# ----------------  ----------  ----------------------------------------------
# COMODO                        Created.
# 


#/usr/bin/env python
'''
DESCRIPTION
SubsystemImplTestPy Class.
Test Methods:
	-	testSendCommand()
'''

import unittest
from Acspy.Clients.SimpleClient import PySimpleClient

from prototypeCommon *
from Device import *
from Lamp import *
from Motor import *
from Lamp2 import *
from Filter import *
from CCDImag import *
from Lamp1 import *
from CCDSpectro import *
from ICS import *
from DCS import *
from Subsystem import *
from OS import *

class SubsystemImplTestPy(unittest.TestCase):

	################################################
	# lifecycle
	################################################
	
	def setUp(self):
		self.simpleClient = PySimpleClient()
		self.componentName = "testInstanceSubsystemImpl"
		self.simpleClient.getLogger().logInfo("pyUnitTestSubsystemImpl.SubsystemImplTestPy.setUp()...")
        self.simpleClient.getLogger().logInfo("Get component " + self.componentName)
        self.component = self.simpleClient.getComponent(self.componentName)
	#__pPp__	    	           
	
	def tearDown(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestSubsystemImpl.SubsystemImplTestPy.tearDown()...")
		self.simpleClient.getLogger().logInfo("Releasing component " + self.componentName)
		self.simpleClient.releaseComponent(self.componentName)
		self.simpleClient.disconnect()
	#__pPp__       
        
	################################################
	# test methods
	################################################
	
	def testSendCommand(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestSubsystemImpl.SubsystemImplTestPy.testSendCommand()...")
		response = None
		commandName = 'emptyString'
		parameterList = 'emptyString'
		cb = CBstring()
		desc = CBDescIn()
		response = self.component.sendCommand(commandName, parameterList, cb, desc)
		# a return is expected, response should be not None
		assert response is not None
	#__pPp__
			    

if __name__ == "__main__":
	unittest.main()
	print "End of SubsystemImplTestPy test __oOo__"
