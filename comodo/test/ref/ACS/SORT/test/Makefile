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

#*******************************************************************************
# This Makefile follows VLT Standards (see Makefile(5) for more).
#*******************************************************************************
# REMARKS
#    None
#------------------------------------------------------------------------

#
# user definable C-compilation flags
#USER_CFLAGS = 

#
# additional include and library search paths
#USER_INC = 
USER_LIB =  -lACE \
            -lTAO \
            -lTAO_DsLogAdmin \
            -lTAO_CosNaming \
            -lTAO_IORTable \
            -lTAO_PortableServer \
            -lTAO_Svc_Utils \
            -lTAO_CosTrading \
            -lTAO_CosNotification \
            -lTAO_DynamicAny \
            -lTAO_IFR_Client \
            -lTAO_CosProperty \
            -lacsutil \
            -lcdb \
            -llogging \
            -lacscomponent \
            -lbaci \
            -lmaci \
            -lmaciClient \
            -lacserr \
            -lm \
            -lloki \
            -lACSErrTypeCommon
            
#
# MODULE CODE DESCRIPTION:
# ------------------------
# As a general rule:  public file are "cleaned" and "installed"  
#                     local (_L) are not "installed".

#
# C programs (public and local)
# -----------------------------
EXECUTABLES     =
EXECUTABLES     += cppUnitTestLegoImpl
EXECUTABLES     += cppUnitTestInstrumentImpl
EXECUTABLES     += cppUnitTestSchedulerImpl
EXECUTABLES     += cppUnitTestDatabaseImpl
EXECUTABLES     += cppUnitTestConsoleImpl
EXECUTABLES     += cppUnitTestNexstarImpl
EXECUTABLES_L   = 


#
# LegoImpl cpp unit test
# -----------------------------
cppUnitTestLegoImpl_OBJECTS   = cppUnitTestLegoImpl
cppUnitTestLegoImpl_LDFLAGS   =
cppUnitTestLegoImpl_LIBS      += cppunit
cppUnitTestLegoImpl_LIBS      += maciClient
cppUnitTestLegoImpl_LIBS      += SORTCommonStubs
cppUnitTestLegoImpl_LIBS      += DatabaseStubs
cppUnitTestLegoImpl_LIBS      += TelescopeStubs
cppUnitTestLegoImpl_LIBS      += DevCCDStubs
cppUnitTestLegoImpl_LIBS      += InstrumentStubs
cppUnitTestLegoImpl_LIBS      += CommonOperationsStubs
cppUnitTestLegoImpl_LIBS      += ConsoleStubs

#
# InstrumentImpl cpp unit test
# -----------------------------
cppUnitTestInstrumentImpl_OBJECTS   = cppUnitTestInstrumentImpl
cppUnitTestInstrumentImpl_LDFLAGS   =
cppUnitTestInstrumentImpl_LIBS      += cppunit
cppUnitTestInstrumentImpl_LIBS      += maciClient
cppUnitTestInstrumentImpl_LIBS      += SORTCommonStubs
cppUnitTestInstrumentImpl_LIBS      += DatabaseStubs
cppUnitTestInstrumentImpl_LIBS      += TelescopeStubs
cppUnitTestInstrumentImpl_LIBS      += DevCCDStubs
cppUnitTestInstrumentImpl_LIBS      += InstrumentStubs
cppUnitTestInstrumentImpl_LIBS      += CommonOperationsStubs
cppUnitTestInstrumentImpl_LIBS      += ConsoleStubs

#
# SchedulerImpl cpp unit test
# -----------------------------
cppUnitTestSchedulerImpl_OBJECTS   = cppUnitTestSchedulerImpl
cppUnitTestSchedulerImpl_LDFLAGS   =
cppUnitTestSchedulerImpl_LIBS      += cppunit
cppUnitTestSchedulerImpl_LIBS      += maciClient
cppUnitTestSchedulerImpl_LIBS      += SORTCommonStubs
cppUnitTestSchedulerImpl_LIBS      += DatabaseStubs
cppUnitTestSchedulerImpl_LIBS      += TelescopeStubs
cppUnitTestSchedulerImpl_LIBS      += DevCCDStubs
cppUnitTestSchedulerImpl_LIBS      += InstrumentStubs
cppUnitTestSchedulerImpl_LIBS      += CommonOperationsStubs
cppUnitTestSchedulerImpl_LIBS      += ConsoleStubs

#
# DatabaseImpl cpp unit test
# -----------------------------
cppUnitTestDatabaseImpl_OBJECTS   = cppUnitTestDatabaseImpl
cppUnitTestDatabaseImpl_LDFLAGS   =
cppUnitTestDatabaseImpl_LIBS      += cppunit
cppUnitTestDatabaseImpl_LIBS      += maciClient
cppUnitTestDatabaseImpl_LIBS      += SORTCommonStubs
cppUnitTestDatabaseImpl_LIBS      += DatabaseStubs
cppUnitTestDatabaseImpl_LIBS      += TelescopeStubs
cppUnitTestDatabaseImpl_LIBS      += DevCCDStubs
cppUnitTestDatabaseImpl_LIBS      += InstrumentStubs
cppUnitTestDatabaseImpl_LIBS      += CommonOperationsStubs
cppUnitTestDatabaseImpl_LIBS      += ConsoleStubs

#
# ConsoleImpl cpp unit test
# -----------------------------
cppUnitTestConsoleImpl_OBJECTS   = cppUnitTestConsoleImpl
cppUnitTestConsoleImpl_LDFLAGS   =
cppUnitTestConsoleImpl_LIBS      += cppunit
cppUnitTestConsoleImpl_LIBS      += maciClient
cppUnitTestConsoleImpl_LIBS      += SORTCommonStubs
cppUnitTestConsoleImpl_LIBS      += DatabaseStubs
cppUnitTestConsoleImpl_LIBS      += TelescopeStubs
cppUnitTestConsoleImpl_LIBS      += DevCCDStubs
cppUnitTestConsoleImpl_LIBS      += InstrumentStubs
cppUnitTestConsoleImpl_LIBS      += CommonOperationsStubs
cppUnitTestConsoleImpl_LIBS      += ConsoleStubs

#
# NexstarImpl cpp unit test
# -----------------------------
cppUnitTestNexstarImpl_OBJECTS   = cppUnitTestNexstarImpl
cppUnitTestNexstarImpl_LDFLAGS   =
cppUnitTestNexstarImpl_LIBS      += cppunit
cppUnitTestNexstarImpl_LIBS      += maciClient
cppUnitTestNexstarImpl_LIBS      += SORTCommonStubs
cppUnitTestNexstarImpl_LIBS      += DatabaseStubs
cppUnitTestNexstarImpl_LIBS      += TelescopeStubs
cppUnitTestNexstarImpl_LIBS      += DevCCDStubs
cppUnitTestNexstarImpl_LIBS      += InstrumentStubs
cppUnitTestNexstarImpl_LIBS      += CommonOperationsStubs
cppUnitTestNexstarImpl_LIBS      += ConsoleStubs


#
# special compilation flags for single c sources
#yyyyy_CFLAGS   = 

#
# Includes (.h) files (public only)
# ---------------------------------
INCLUDES        =

#
# Libraries (public and local)
# ----------------------------
LIBRARIES       =
LIBRARIES_L     =

#
# <brief description of lllll library>
lllll_OBJECTS   =

#
# Scripts (public and local)
# ----------------------------
SCRIPTS         =
SCRIPTS_L       =

#
# TCL scripts (public and local)
# ------------------------------
TCL_SCRIPTS     =
TCL_SCRIPTS_L   =

#
# Python stuff (public and local)
# ----------------------------
PY_SCRIPTS         =
PY_SCRIPTS         += pyUnitTestLegoImpl
PY_SCRIPTS         += pyUnitTestInstrumentImpl
PY_SCRIPTS         += pyUnitTestSchedulerImpl
PY_SCRIPTS         += pyUnitTestDatabaseImpl
PY_SCRIPTS         += pyUnitTestConsoleImpl
PY_SCRIPTS         += pyUnitTestNexstarImpl
PY_SCRIPTS_L       =

PY_MODULES         =
PY_MODULES_L       =

PY_PACKAGES        =
PY_PACKAGES_L      =
pppppp_MODULES	   =

#
# <brief description of tttttt tcl-script>
tttttt_OBJECTS  =
tttttt_TCLSH    = 
tttttt_LIBS     = 

#
# TCL libraries (public and local)
# ------------------------------
TCL_LIBRARIES   =
TCL_LIBRARIES_L =

#
# <brief description of tttlll library>
tttlll_OBJECTS  = 

#
# Configuration Database Files
# ----------------------------
CDB_SCHEMAS = 

# 
# IDL Files and flags
# ----------------------------
IDL_FILES = 
TAO_IDLFLAGS =
USER_IDL =

#
# Jarfiles and their directories
#
JARFILES= SORTTest
SORTTest_DIRS= alma/SORT
jjj_EXTRAS= 

#
# java sources in Jarfile on/off
DEBUG=on

#
# ACS XmlIdl generation on/off
#
XML_IDL= 

#
# Java Component Helper Classes generation on/off
#
COMPONENT_HELPERS=

#
# Java Entity Classes generation on/off
#
XSDBIND=

#
# Schema Config files for the above
#
XSDBIND_INCLUDE=

# man pages to be done
# --------------------
MANSECTIONS =
MAN1 =
MAN3 =
MAN5 =
MAN7 =
MAN8 =

#
# local man pages
# ---------------
MANl =

#
# ASCII file to be converted into Framemaker-MIF
# --------------------
ASCII_TO_MIF = 

#
# other files to be installed
#----------------------------
INSTALL_FILES =

#
# list of all possible C-sources (used to create automatic dependencies)
# ------------------------------
CSOURCENAMES = \
	$(foreach exe, $(EXECUTABLES) $(EXECUTABLES_L), $($(exe)_OBJECTS)) \
	$(foreach rtos, $(RTAI_MODULES) , $($(rtos)_OBJECTS)) \
	$(foreach lib, $(LIBRARIES) $(LIBRARIES_L), $($(lib)_OBJECTS))

#
#>>>>> END OF standard rules

#
# INCLUDE STANDARDS
# -----------------

MAKEDIRTMP := $(shell searchFile include/acsMakefile)
ifneq ($(MAKEDIRTMP),\#error\#)
   MAKEDIR := $(MAKEDIRTMP)/include
   include $(MAKEDIR)/acsMakefile
endif

#
# TARGETS
# -------
all:	do_all
	@echo " . . . 'all' done" 

clean : clean_all 
	@echo " . . . clean done"

clean_dist : clean_all clean_dist_all 
	@echo " . . . clean_dist done"

man   : do_man 
	@echo " . . . man page(s) done"

install : install_all
	@echo " . . . installation done"

#___oOo___
