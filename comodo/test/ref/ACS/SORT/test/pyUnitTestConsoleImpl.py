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
ConsoleImplTestPy Class.
Test Methods:
	-	testSetMode()
	-	testGetMode()
	-	testMoveTelescope()
	-	testGetTelescopePosition()
	-	testGetCameraImage()
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

class ConsoleImplTestPy(unittest.TestCase):

	################################################
	# lifecycle
	################################################
	
	def setUp(self):
		self.simpleClient = PySimpleClient()
		self.componentName = "testInstanceConsoleImpl"
		self.simpleClient.getLogger().logInfo("pyUnitTestConsoleImpl.ConsoleImplTestPy.setUp()...")
        self.simpleClient.getLogger().logInfo("Get component " + self.componentName)
        self.component = self.simpleClient.getComponent(self.componentName)
	#__pPp__	    	           
	
	def tearDown(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestConsoleImpl.ConsoleImplTestPy.tearDown()...")
		self.simpleClient.getLogger().logInfo("Releasing component " + self.componentName)
		self.simpleClient.releaseComponent(self.componentName)
		self.simpleClient.disconnect()
	#__pPp__       
        
	################################################
	# test methods
	################################################
	
	def testSetMode(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestConsoleImpl.ConsoleImplTestPy.testSetMode()...")
		response = None
		mode = False
		response = self.component.setMode(mode)
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testGetMode(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestConsoleImpl.ConsoleImplTestPy.testGetMode()...")
		response = None
		response = self.component.getMode()
		# a return is expected, response should be not None
		assert response is not None
	#__pPp__
			    
	def testMoveTelescope(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestConsoleImpl.ConsoleImplTestPy.testMoveTelescope()...")
		response = None
		coordinates = Position()
		response = self.component.moveTelescope(coordinates)
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testGetTelescopePosition(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestConsoleImpl.ConsoleImplTestPy.testGetTelescopePosition()...")
		response = None
		response = self.component.getTelescopePosition()
		# a return is expected, response should be not None
		assert response is not None
	#__pPp__
			    
	def testGetCameraImage(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestConsoleImpl.ConsoleImplTestPy.testGetCameraImage()...")
		response = None
		response = self.component.getCameraImage()
		# a return is expected, response should be not None
		assert response is not None
	#__pPp__
			    
	def testCameraOn(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestConsoleImpl.ConsoleImplTestPy.testCameraOn()...")
		response = None
		response = self.component.cameraOn()
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testCameraOff(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestConsoleImpl.ConsoleImplTestPy.testCameraOff()...")
		response = None
		response = self.component.cameraOff()
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testSetRGB(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestConsoleImpl.ConsoleImplTestPy.testSetRGB()...")
		response = None
		rgbConfig = RGB()
		response = self.component.setRGB(rgbConfig)
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testSetPixelBias(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestConsoleImpl.ConsoleImplTestPy.testSetPixelBias()...")
		response = None
		bias = 0
		response = self.component.setPixelBias(bias)
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testSetResetLevel(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestConsoleImpl.ConsoleImplTestPy.testSetResetLevel()...")
		response = None
		resetLevel = 0
		response = self.component.setResetLevel(resetLevel)
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    

if __name__ == "__main__":
	unittest.main()
	print "End of ConsoleImplTestPy test __oOo__"
