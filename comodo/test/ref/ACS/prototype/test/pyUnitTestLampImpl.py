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
LampImplTestPy Class.
Test Methods:
	-	testSwitchOn()
	-	testSwitchOff()
	-	testStandby()
	-	testOnline()
	-	testOff()
	-	testSetup()
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

class LampImplTestPy(unittest.TestCase):

	################################################
	# lifecycle
	################################################
	
	def setUp(self):
		self.simpleClient = PySimpleClient()
		self.componentName = "testInstanceLampImpl"
		self.simpleClient.getLogger().logInfo("pyUnitTestLampImpl.LampImplTestPy.setUp()...")
        self.simpleClient.getLogger().logInfo("Get component " + self.componentName)
        self.component = self.simpleClient.getComponent(self.componentName)
	#__pPp__	    	           
	
	def tearDown(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestLampImpl.LampImplTestPy.tearDown()...")
		self.simpleClient.getLogger().logInfo("Releasing component " + self.componentName)
		self.simpleClient.releaseComponent(self.componentName)
		self.simpleClient.disconnect()
	#__pPp__       
        
	################################################
	# test methods
	################################################
	
	def testSwitchOn(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestLampImpl.LampImplTestPy.testSwitchOn()...")
		response = None
		response = self.component.switchOn()
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testSwitchOff(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestLampImpl.LampImplTestPy.testSwitchOff()...")
		response = None
		response = self.component.switchOff()
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testStandby(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestLampImpl.LampImplTestPy.testStandby()...")
		response = None
		response = self.component.standby()
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testOnline(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestLampImpl.LampImplTestPy.testOnline()...")
		response = None
		response = self.component.online()
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testOff(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestLampImpl.LampImplTestPy.testOff()...")
		response = None
		response = self.component.off()
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testSetup(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestLampImpl.LampImplTestPy.testSetup()...")
		response = None
		val = 'emptyString'
		timeout = 0
		response = self.component.setup(val, timeout)
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    

if __name__ == "__main__":
	unittest.main()
	print "End of LampImplTestPy test __oOo__"
