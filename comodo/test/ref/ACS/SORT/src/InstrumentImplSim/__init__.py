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


import TYPES
import Acspy.Clients.SimpleClient
from Acspy.Common.Log import getLogger

def takeImage(params):
	logger = getLogger("InstrumentImpl Log")
	logger.logTrace( "takeImage executed...")
	
def cameraOn():
	logger = getLogger("InstrumentImpl Log")
	logger.logTrace( "cameraOn executed...")
	
def cameraOff():
	logger = getLogger("InstrumentImpl Log")
	logger.logTrace( "cameraOff executed...")
	
def setRGB(params):
	logger = getLogger("InstrumentImpl Log")
	logger.logTrace( "setRGB executed...")
	
def setPixelBias(params):
	logger = getLogger("InstrumentImpl Log")
	logger.logTrace( "setPixelBias executed...")
	
def setResetLevel(params):
	logger = getLogger("InstrumentImpl Log")
	logger.logTrace( "setResetLevel executed...")
	
# ___oOo___
