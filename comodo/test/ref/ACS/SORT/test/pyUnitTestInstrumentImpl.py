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
InstrumentImplTestPy Class.
Test Methods:
	-	testTakeImage()
	-	testCameraOn()
	-	testCameraOff()
	-	testSetRGB()
	-	testSetPixelBias()
	-	testSetResetLevel()
'''

import unittest
from Acspy.Clients.SimpleClient import PySimpleClient

from SORTCommon *
from Database import *
from Telescope import *
from DevCCD import *
from Instrument import *
from CommonOperations import *
from Console import *

class InstrumentImplTestPy(unittest.TestCase):

	################################################
	# lifecycle
	################################################
	
	def setUp(self):
		self.simpleClient = PySimpleClient()
		self.componentName = "testInstanceInstrumentImpl"
		self.simpleClient.getLogger().logInfo("pyUnitTestInstrumentImpl.InstrumentImplTestPy.setUp()...")
        self.simpleClient.getLogger().logInfo("Get component " + self.componentName)
        self.component = self.simpleClient.getComponent(self.componentName)
	#__pPp__	    	           
	
	def tearDown(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestInstrumentImpl.InstrumentImplTestPy.tearDown()...")
		self.simpleClient.getLogger().logInfo("Releasing component " + self.componentName)
		self.simpleClient.releaseComponent(self.componentName)
		self.simpleClient.disconnect()
	#__pPp__       
        
	################################################
	# test methods
	################################################
	
	def testTakeImage(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestInstrumentImpl.InstrumentImplTestPy.testTakeImage()...")
		response = None
		exposureTime = 0
		response = self.component.takeImage(exposureTime)
		# a return is expected, response should be not None
		assert response is not None
	#__pPp__
			    
	def testCameraOn(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestInstrumentImpl.InstrumentImplTestPy.testCameraOn()...")
		response = None
		response = self.component.cameraOn()
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testCameraOff(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestInstrumentImpl.InstrumentImplTestPy.testCameraOff()...")
		response = None
		response = self.component.cameraOff()
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testSetRGB(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestInstrumentImpl.InstrumentImplTestPy.testSetRGB()...")
		response = None
		rgbConfig = RGB()
		response = self.component.setRGB(rgbConfig)
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testSetPixelBias(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestInstrumentImpl.InstrumentImplTestPy.testSetPixelBias()...")
		response = None
		bias = 0
		response = self.component.setPixelBias(bias)
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testSetResetLevel(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestInstrumentImpl.InstrumentImplTestPy.testSetResetLevel()...")
		response = None
		resetLevel = 0
		response = self.component.setResetLevel(resetLevel)
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    

if __name__ == "__main__":
	unittest.main()
	print "End of InstrumentImplTestPy test __oOo__"
