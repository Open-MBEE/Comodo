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
SchedulerImplTestPy Class.
Test Methods:
	-	testStart()
	-	testStop()
	-	testProposalUnderExecution()
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

class SchedulerImplTestPy(unittest.TestCase):

	################################################
	# lifecycle
	################################################
	
	def setUp(self):
		self.simpleClient = PySimpleClient()
		self.componentName = "testInstanceSchedulerImpl"
		self.simpleClient.getLogger().logInfo("pyUnitTestSchedulerImpl.SchedulerImplTestPy.setUp()...")
        self.simpleClient.getLogger().logInfo("Get component " + self.componentName)
        self.component = self.simpleClient.getComponent(self.componentName)
	#__pPp__	    	           
	
	def tearDown(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestSchedulerImpl.SchedulerImplTestPy.tearDown()...")
		self.simpleClient.getLogger().logInfo("Releasing component " + self.componentName)
		self.simpleClient.releaseComponent(self.componentName)
		self.simpleClient.disconnect()
	#__pPp__       
        
	################################################
	# test methods
	################################################
	
	def testStart(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestSchedulerImpl.SchedulerImplTestPy.testStart()...")
		response = None
		response = self.component.start()
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testStop(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestSchedulerImpl.SchedulerImplTestPy.testStop()...")
		response = None
		response = self.component.stop()
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testProposalUnderExecution(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestSchedulerImpl.SchedulerImplTestPy.testProposalUnderExecution()...")
		response = None
		response = self.component.proposalUnderExecution()
		# a return is expected, response should be not None
		assert response is not None
	#__pPp__
			    

if __name__ == "__main__":
	unittest.main()
	print "End of SchedulerImplTestPy test __oOo__"
