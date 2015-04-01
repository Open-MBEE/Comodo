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
DatabaseImplTestPy Class.
Test Methods:
	-	testStoreProposal()
	-	testRemoveProposal()
	-	testGetProposals()
	-	testGetProposalStatus()
	-	testStoreImage()
	-	testClean()
	-	testSetProposalStatus()
	-	testGetProposalObservations()
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

class DatabaseImplTestPy(unittest.TestCase):

	################################################
	# lifecycle
	################################################
	
	def setUp(self):
		self.simpleClient = PySimpleClient()
		self.componentName = "testInstanceDatabaseImpl"
		self.simpleClient.getLogger().logInfo("pyUnitTestDatabaseImpl.DatabaseImplTestPy.setUp()...")
        self.simpleClient.getLogger().logInfo("Get component " + self.componentName)
        self.component = self.simpleClient.getComponent(self.componentName)
	#__pPp__	    	           
	
	def tearDown(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestDatabaseImpl.DatabaseImplTestPy.tearDown()...")
		self.simpleClient.getLogger().logInfo("Releasing component " + self.componentName)
		self.simpleClient.releaseComponent(self.componentName)
		self.simpleClient.disconnect()
	#__pPp__       
        
	################################################
	# test methods
	################################################
	
	def testStoreProposal(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestDatabaseImpl.DatabaseImplTestPy.testStoreProposal()...")
		response = None
		targets = Target()
		response = self.component.storeProposal(targets)
		# a return is expected, response should be not None
		assert response is not None
	#__pPp__
			    
	def testRemoveProposal(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestDatabaseImpl.DatabaseImplTestPy.testRemoveProposal()...")
		response = None
		pid = 0
		response = self.component.removeProposal(pid)
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testGetProposals(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestDatabaseImpl.DatabaseImplTestPy.testGetProposals()...")
		response = None
		response = self.component.getProposals()
		# a return is expected, response should be not None
		assert response is not None
	#__pPp__
			    
	def testGetProposalStatus(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestDatabaseImpl.DatabaseImplTestPy.testGetProposalStatus()...")
		response = None
		pid = 0
		response = self.component.getProposalStatus(pid)
		# a return is expected, response should be not None
		assert response is not None
	#__pPp__
			    
	def testStoreImage(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestDatabaseImpl.DatabaseImplTestPy.testStoreImage()...")
		response = None
		pid = 0
		image = ImageType()
		response = self.component.storeImage(pid, image)
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testClean(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestDatabaseImpl.DatabaseImplTestPy.testClean()...")
		response = None
		response = self.component.clean()
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testSetProposalStatus(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestDatabaseImpl.DatabaseImplTestPy.testSetProposalStatus()...")
		response = None
		pid = 0
		status = 0
		response = self.component.setProposalStatus(pid, status)
		# no return is expected, response should be None
		assert response is None
	#__pPp__
			    
	def testGetProposalObservations(self):
		self.simpleClient.getLogger().logInfo("pyUnitTestDatabaseImpl.DatabaseImplTestPy.testGetProposalObservations()...")
		response = None
		pid = 0
		response = self.component.getProposalObservations(pid)
		# a return is expected, response should be not None
		assert response is not None
	#__pPp__
			    

if __name__ == "__main__":
	unittest.main()
	print "End of DatabaseImplTestPy test __oOo__"
