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
EXECUTABLES     += cppUnitTestTSJavaImp
EXECUTABLES     += cppUnitTestTRDJavaImp
EXECUTABLES     += cppUnitTestSCJavaImp
EXECUTABLES     += cppUnitTestFDJavaImp
EXECUTABLES     += cppUnitTestOCJavaImp
EXECUTABLES_L   = 


#
# TSJavaImp cpp unit test
# -----------------------------
cppUnitTestTSJavaImp_OBJECTS   = cppUnitTestTSJavaImp
cppUnitTestTSJavaImp_LDFLAGS   =
cppUnitTestTSJavaImp_LIBS      += cppunit
cppUnitTestTSJavaImp_LIBS      += maciClient
cppUnitTestTSJavaImp_LIBS      += MARSCommonStubs
cppUnitTestTSJavaImp_LIBS      += FDInterfaceStubs
cppUnitTestTSJavaImp_LIBS      += OCInterfaceStubs
cppUnitTestTSJavaImp_LIBS      += SCInterfaceStubs
cppUnitTestTSJavaImp_LIBS      += TRDInterfaceStubs
cppUnitTestTSJavaImp_LIBS      += TSInterfaceStubs

#
# TRDJavaImp cpp unit test
# -----------------------------
cppUnitTestTRDJavaImp_OBJECTS   = cppUnitTestTRDJavaImp
cppUnitTestTRDJavaImp_LDFLAGS   =
cppUnitTestTRDJavaImp_LIBS      += cppunit
cppUnitTestTRDJavaImp_LIBS      += maciClient
cppUnitTestTRDJavaImp_LIBS      += MARSCommonStubs
cppUnitTestTRDJavaImp_LIBS      += FDInterfaceStubs
cppUnitTestTRDJavaImp_LIBS      += OCInterfaceStubs
cppUnitTestTRDJavaImp_LIBS      += SCInterfaceStubs
cppUnitTestTRDJavaImp_LIBS      += TRDInterfaceStubs
cppUnitTestTRDJavaImp_LIBS      += TSInterfaceStubs

#
# SCJavaImp cpp unit test
# -----------------------------
cppUnitTestSCJavaImp_OBJECTS   = cppUnitTestSCJavaImp
cppUnitTestSCJavaImp_LDFLAGS   =
cppUnitTestSCJavaImp_LIBS      += cppunit
cppUnitTestSCJavaImp_LIBS      += maciClient
cppUnitTestSCJavaImp_LIBS      += MARSCommonStubs
cppUnitTestSCJavaImp_LIBS      += FDInterfaceStubs
cppUnitTestSCJavaImp_LIBS      += OCInterfaceStubs
cppUnitTestSCJavaImp_LIBS      += SCInterfaceStubs
cppUnitTestSCJavaImp_LIBS      += TRDInterfaceStubs
cppUnitTestSCJavaImp_LIBS      += TSInterfaceStubs

#
# FDJavaImp cpp unit test
# -----------------------------
cppUnitTestFDJavaImp_OBJECTS   = cppUnitTestFDJavaImp
cppUnitTestFDJavaImp_LDFLAGS   =
cppUnitTestFDJavaImp_LIBS      += cppunit
cppUnitTestFDJavaImp_LIBS      += maciClient
cppUnitTestFDJavaImp_LIBS      += MARSCommonStubs
cppUnitTestFDJavaImp_LIBS      += FDInterfaceStubs
cppUnitTestFDJavaImp_LIBS      += OCInterfaceStubs
cppUnitTestFDJavaImp_LIBS      += SCInterfaceStubs
cppUnitTestFDJavaImp_LIBS      += TRDInterfaceStubs
cppUnitTestFDJavaImp_LIBS      += TSInterfaceStubs

#
# OCJavaImp cpp unit test
# -----------------------------
cppUnitTestOCJavaImp_OBJECTS   = cppUnitTestOCJavaImp
cppUnitTestOCJavaImp_LDFLAGS   =
cppUnitTestOCJavaImp_LIBS      += cppunit
cppUnitTestOCJavaImp_LIBS      += maciClient
cppUnitTestOCJavaImp_LIBS      += MARSCommonStubs
cppUnitTestOCJavaImp_LIBS      += FDInterfaceStubs
cppUnitTestOCJavaImp_LIBS      += OCInterfaceStubs
cppUnitTestOCJavaImp_LIBS      += SCInterfaceStubs
cppUnitTestOCJavaImp_LIBS      += TRDInterfaceStubs
cppUnitTestOCJavaImp_LIBS      += TSInterfaceStubs


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
PY_SCRIPTS         += pyUnitTestTSJavaImp
PY_SCRIPTS         += pyUnitTestTRDJavaImp
PY_SCRIPTS         += pyUnitTestSCJavaImp
PY_SCRIPTS         += pyUnitTestFDJavaImp
PY_SCRIPTS         += pyUnitTestOCJavaImp
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
JARFILES= MARSTest
MARSTest_DIRS= alma/MARS
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
