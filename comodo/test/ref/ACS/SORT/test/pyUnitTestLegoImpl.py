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
LegoImplTestPy Class.
Test Methods:
	-	testObserve()
	-	testMoveTo()
	-	testGetCurrentPosition()
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

class LegoImplTestPy(unittest.TestCase):

	################################################
	# lifecycle
	################################################
	
	def setUp(self):
		self.simpleClient = PySimpleClient()
		self.componentName = "testInstanceLegoImpl"
		self.simpleClient.getLogger().logInfo("pyUnitTestLegoImpl.LegoImplTestPy.setUp()...")
        self.simpleClient.getLogger().logInfo("Get component " + self.componentName)
        self.component = self.simpleClient.getComponent(self.componentName)
	#__pPp__	    	           
	
	def tearDown(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestLegoImpl.LegoImplTestPy.tearDown()...")
		self.simpleClient.getLogger().logInfo("Releasing component " + self.componentName)
		self.simpleClient.releaseComponent(self.componentName)
		self.simpleClient.disconnect()
	#__pPp__       
        
	################################################
	# test methods
	################################################
	
	def testObserve(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestLegoImpl.LegoImplTestPy.testObserve()...")
		response = None
		coordinates = Position()
		exposureTime = 0
		response = self.component.observe(coordinates, exposureTime)
		# a return is expected, response should be not None
		assert response is not None
	#__pPp__
			    
	def testMoveTo(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestLegoImpl.LegoImplTestPy.testMoveTo()...")
		response = None
		coordinates = Position()
		response = self.component.moveTo(coordinates)
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testGetCurrentPosition(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestLegoImpl.LegoImplTestPy.testGetCurrentPosition()...")
		response = None
		response = self.component.getCurrentPosition()
		# a return is expected, response should be not None
		assert response is not None
	#__pPp__
			    

if __name__ == "__main__":
	unittest.main()
	print "End of LegoImplTestPy test __oOo__"
