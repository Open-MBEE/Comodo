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
PingerImplTestPy Class.
Test Methods:
	-	testSpawnChildren()
	-	testLogInfo()
	-	testPing()
'''

import unittest
from Acspy.Clients.SimpleClient import PySimpleClient

from pingerCommon *
from PingerIF import *

class PingerImplTestPy(unittest.TestCase):

	################################################
	# lifecycle
	################################################
	
	def setUp(self):
		self.simpleClient = PySimpleClient()
		self.componentName = "testInstancePingerImpl"
		self.simpleClient.getLogger().logInfo("pyUnitTestPingerImpl.PingerImplTestPy.setUp()...")
        self.simpleClient.getLogger().logInfo("Get component " + self.componentName)
        self.component = self.simpleClient.getComponent(self.componentName)
	#__pPp__	    	           
	
	def tearDown(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestPingerImpl.PingerImplTestPy.tearDown()...")
		self.simpleClient.getLogger().logInfo("Releasing component " + self.componentName)
		self.simpleClient.releaseComponent(self.componentName)
		self.simpleClient.disconnect()
	#__pPp__       
        
	################################################
	# test methods
	################################################
	
	def testSpawnChildren(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestPingerImpl.PingerImplTestPy.testSpawnChildren()...")
		response = None
		howMany = 0
		container = 'emptyString'
		baseName = 'emptyString'
		response = self.component.spawnChildren(howMany, container, baseName)
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testLogInfo(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestPingerImpl.PingerImplTestPy.testLogInfo()...")
		response = None
		response = self.component.logInfo()
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testPing(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestPingerImpl.PingerImplTestPy.testPing()...")
		response = None
		fast = False
		recursive = False
		id = 0
		response = self.component.ping(fast, recursive, id)
		# a return is expected, response should be not None
		assert response is not None
	#__pPp__
			    

if __name__ == "__main__":
	unittest.main()
	print "End of PingerImplTestPy test __oOo__"
