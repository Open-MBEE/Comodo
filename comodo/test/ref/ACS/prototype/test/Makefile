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
EXECUTABLES     += cppUnitTestDeviceImpl
EXECUTABLES     += cppUnitTestDCSImpl
EXECUTABLES     += cppUnitTestLamp2Impl
EXECUTABLES     += cppUnitTestCCDImagImpl
EXECUTABLES     += cppUnitTestSubsystemImpl
EXECUTABLES     += cppUnitTestLamp1Impl
EXECUTABLES     += cppUnitTestCCDSpectroImpl
EXECUTABLES     += cppUnitTestMotorImpl
EXECUTABLES     += cppUnitTestICSImpl
EXECUTABLES     += cppUnitTestOSImpl
EXECUTABLES     += cppUnitTestLampImpl
EXECUTABLES     += cppUnitTestFilterImpl
EXECUTABLES_L   = 


#
# DeviceImpl cpp unit test
# -----------------------------
cppUnitTestDeviceImpl_OBJECTS   = cppUnitTestDeviceImpl
cppUnitTestDeviceImpl_LDFLAGS   =
cppUnitTestDeviceImpl_LIBS      += cppunit
cppUnitTestDeviceImpl_LIBS      += maciClient
cppUnitTestDeviceImpl_LIBS      += prototypeCommonStubs
cppUnitTestDeviceImpl_LIBS      += DeviceStubs
cppUnitTestDeviceImpl_LIBS      += LampStubs
cppUnitTestDeviceImpl_LIBS      += MotorStubs
cppUnitTestDeviceImpl_LIBS      += Lamp2Stubs
cppUnitTestDeviceImpl_LIBS      += FilterStubs
cppUnitTestDeviceImpl_LIBS      += CCDImagStubs
cppUnitTestDeviceImpl_LIBS      += Lamp1Stubs
cppUnitTestDeviceImpl_LIBS      += CCDSpectroStubs
cppUnitTestDeviceImpl_LIBS      += ICSStubs
cppUnitTestDeviceImpl_LIBS      += DCSStubs
cppUnitTestDeviceImpl_LIBS      += SubsystemStubs
cppUnitTestDeviceImpl_LIBS      += OSStubs

#
# DCSImpl cpp unit test
# -----------------------------
cppUnitTestDCSImpl_OBJECTS   = cppUnitTestDCSImpl
cppUnitTestDCSImpl_LDFLAGS   =
cppUnitTestDCSImpl_LIBS      += cppunit
cppUnitTestDCSImpl_LIBS      += maciClient
cppUnitTestDCSImpl_LIBS      += prototypeCommonStubs
cppUnitTestDCSImpl_LIBS      += DeviceStubs
cppUnitTestDCSImpl_LIBS      += LampStubs
cppUnitTestDCSImpl_LIBS      += MotorStubs
cppUnitTestDCSImpl_LIBS      += Lamp2Stubs
cppUnitTestDCSImpl_LIBS      += FilterStubs
cppUnitTestDCSImpl_LIBS      += CCDImagStubs
cppUnitTestDCSImpl_LIBS      += Lamp1Stubs
cppUnitTestDCSImpl_LIBS      += CCDSpectroStubs
cppUnitTestDCSImpl_LIBS      += ICSStubs
cppUnitTestDCSImpl_LIBS      += DCSStubs
cppUnitTestDCSImpl_LIBS      += SubsystemStubs
cppUnitTestDCSImpl_LIBS      += OSStubs

#
# Lamp2Impl cpp unit test
# -----------------------------
cppUnitTestLamp2Impl_OBJECTS   = cppUnitTestLamp2Impl
cppUnitTestLamp2Impl_LDFLAGS   =
cppUnitTestLamp2Impl_LIBS      += cppunit
cppUnitTestLamp2Impl_LIBS      += maciClient
cppUnitTestLamp2Impl_LIBS      += prototypeCommonStubs
cppUnitTestLamp2Impl_LIBS      += DeviceStubs
cppUnitTestLamp2Impl_LIBS      += LampStubs
cppUnitTestLamp2Impl_LIBS      += MotorStubs
cppUnitTestLamp2Impl_LIBS      += Lamp2Stubs
cppUnitTestLamp2Impl_LIBS      += FilterStubs
cppUnitTestLamp2Impl_LIBS      += CCDImagStubs
cppUnitTestLamp2Impl_LIBS      += Lamp1Stubs
cppUnitTestLamp2Impl_LIBS      += CCDSpectroStubs
cppUnitTestLamp2Impl_LIBS      += ICSStubs
cppUnitTestLamp2Impl_LIBS      += DCSStubs
cppUnitTestLamp2Impl_LIBS      += SubsystemStubs
cppUnitTestLamp2Impl_LIBS      += OSStubs

#
# CCDImagImpl cpp unit test
# -----------------------------
cppUnitTestCCDImagImpl_OBJECTS   = cppUnitTestCCDImagImpl
cppUnitTestCCDImagImpl_LDFLAGS   =
cppUnitTestCCDImagImpl_LIBS      += cppunit
cppUnitTestCCDImagImpl_LIBS      += maciClient
cppUnitTestCCDImagImpl_LIBS      += prototypeCommonStubs
cppUnitTestCCDImagImpl_LIBS      += DeviceStubs
cppUnitTestCCDImagImpl_LIBS      += LampStubs
cppUnitTestCCDImagImpl_LIBS      += MotorStubs
cppUnitTestCCDImagImpl_LIBS      += Lamp2Stubs
cppUnitTestCCDImagImpl_LIBS      += FilterStubs
cppUnitTestCCDImagImpl_LIBS      += CCDImagStubs
cppUnitTestCCDImagImpl_LIBS      += Lamp1Stubs
cppUnitTestCCDImagImpl_LIBS      += CCDSpectroStubs
cppUnitTestCCDImagImpl_LIBS      += ICSStubs
cppUnitTestCCDImagImpl_LIBS      += DCSStubs
cppUnitTestCCDImagImpl_LIBS      += SubsystemStubs
cppUnitTestCCDImagImpl_LIBS      += OSStubs

#
# SubsystemImpl cpp unit test
# -----------------------------
cppUnitTestSubsystemImpl_OBJECTS   = cppUnitTestSubsystemImpl
cppUnitTestSubsystemImpl_LDFLAGS   =
cppUnitTestSubsystemImpl_LIBS      += cppunit
cppUnitTestSubsystemImpl_LIBS      += maciClient
cppUnitTestSubsystemImpl_LIBS      += prototypeCommonStubs
cppUnitTestSubsystemImpl_LIBS      += DeviceStubs
cppUnitTestSubsystemImpl_LIBS      += LampStubs
cppUnitTestSubsystemImpl_LIBS      += MotorStubs
cppUnitTestSubsystemImpl_LIBS      += Lamp2Stubs
cppUnitTestSubsystemImpl_LIBS      += FilterStubs
cppUnitTestSubsystemImpl_LIBS      += CCDImagStubs
cppUnitTestSubsystemImpl_LIBS      += Lamp1Stubs
cppUnitTestSubsystemImpl_LIBS      += CCDSpectroStubs
cppUnitTestSubsystemImpl_LIBS      += ICSStubs
cppUnitTestSubsystemImpl_LIBS      += DCSStubs
cppUnitTestSubsystemImpl_LIBS      += SubsystemStubs
cppUnitTestSubsystemImpl_LIBS      += OSStubs

#
# Lamp1Impl cpp unit test
# -----------------------------
cppUnitTestLamp1Impl_OBJECTS   = cppUnitTestLamp1Impl
cppUnitTestLamp1Impl_LDFLAGS   =
cppUnitTestLamp1Impl_LIBS      += cppunit
cppUnitTestLamp1Impl_LIBS      += maciClient
cppUnitTestLamp1Impl_LIBS      += prototypeCommonStubs
cppUnitTestLamp1Impl_LIBS      += DeviceStubs
cppUnitTestLamp1Impl_LIBS      += LampStubs
cppUnitTestLamp1Impl_LIBS      += MotorStubs
cppUnitTestLamp1Impl_LIBS      += Lamp2Stubs
cppUnitTestLamp1Impl_LIBS      += FilterStubs
cppUnitTestLamp1Impl_LIBS      += CCDImagStubs
cppUnitTestLamp1Impl_LIBS      += Lamp1Stubs
cppUnitTestLamp1Impl_LIBS      += CCDSpectroStubs
cppUnitTestLamp1Impl_LIBS      += ICSStubs
cppUnitTestLamp1Impl_LIBS      += DCSStubs
cppUnitTestLamp1Impl_LIBS      += SubsystemStubs
cppUnitTestLamp1Impl_LIBS      += OSStubs

#
# CCDSpectroImpl cpp unit test
# -----------------------------
cppUnitTestCCDSpectroImpl_OBJECTS   = cppUnitTestCCDSpectroImpl
cppUnitTestCCDSpectroImpl_LDFLAGS   =
cppUnitTestCCDSpectroImpl_LIBS      += cppunit
cppUnitTestCCDSpectroImpl_LIBS      += maciClient
cppUnitTestCCDSpectroImpl_LIBS      += prototypeCommonStubs
cppUnitTestCCDSpectroImpl_LIBS      += DeviceStubs
cppUnitTestCCDSpectroImpl_LIBS      += LampStubs
cppUnitTestCCDSpectroImpl_LIBS      += MotorStubs
cppUnitTestCCDSpectroImpl_LIBS      += Lamp2Stubs
cppUnitTestCCDSpectroImpl_LIBS      += FilterStubs
cppUnitTestCCDSpectroImpl_LIBS      += CCDImagStubs
cppUnitTestCCDSpectroImpl_LIBS      += Lamp1Stubs
cppUnitTestCCDSpectroImpl_LIBS      += CCDSpectroStubs
cppUnitTestCCDSpectroImpl_LIBS      += ICSStubs
cppUnitTestCCDSpectroImpl_LIBS      += DCSStubs
cppUnitTestCCDSpectroImpl_LIBS      += SubsystemStubs
cppUnitTestCCDSpectroImpl_LIBS      += OSStubs

#
# MotorImpl cpp unit test
# -----------------------------
cppUnitTestMotorImpl_OBJECTS   = cppUnitTestMotorImpl
cppUnitTestMotorImpl_LDFLAGS   =
cppUnitTestMotorImpl_LIBS      += cppunit
cppUnitTestMotorImpl_LIBS      += maciClient
cppUnitTestMotorImpl_LIBS      += prototypeCommonStubs
cppUnitTestMotorImpl_LIBS      += DeviceStubs
cppUnitTestMotorImpl_LIBS      += LampStubs
cppUnitTestMotorImpl_LIBS      += MotorStubs
cppUnitTestMotorImpl_LIBS      += Lamp2Stubs
cppUnitTestMotorImpl_LIBS      += FilterStubs
cppUnitTestMotorImpl_LIBS      += CCDImagStubs
cppUnitTestMotorImpl_LIBS      += Lamp1Stubs
cppUnitTestMotorImpl_LIBS      += CCDSpectroStubs
cppUnitTestMotorImpl_LIBS      += ICSStubs
cppUnitTestMotorImpl_LIBS      += DCSStubs
cppUnitTestMotorImpl_LIBS      += SubsystemStubs
cppUnitTestMotorImpl_LIBS      += OSStubs

#
# ICSImpl cpp unit test
# -----------------------------
cppUnitTestICSImpl_OBJECTS   = cppUnitTestICSImpl
cppUnitTestICSImpl_LDFLAGS   =
cppUnitTestICSImpl_LIBS      += cppunit
cppUnitTestICSImpl_LIBS      += maciClient
cppUnitTestICSImpl_LIBS      += prototypeCommonStubs
cppUnitTestICSImpl_LIBS      += DeviceStubs
cppUnitTestICSImpl_LIBS      += LampStubs
cppUnitTestICSImpl_LIBS      += MotorStubs
cppUnitTestICSImpl_LIBS      += Lamp2Stubs
cppUnitTestICSImpl_LIBS      += FilterStubs
cppUnitTestICSImpl_LIBS      += CCDImagStubs
cppUnitTestICSImpl_LIBS      += Lamp1Stubs
cppUnitTestICSImpl_LIBS      += CCDSpectroStubs
cppUnitTestICSImpl_LIBS      += ICSStubs
cppUnitTestICSImpl_LIBS      += DCSStubs
cppUnitTestICSImpl_LIBS      += SubsystemStubs
cppUnitTestICSImpl_LIBS      += OSStubs

#
# OSImpl cpp unit test
# -----------------------------
cppUnitTestOSImpl_OBJECTS   = cppUnitTestOSImpl
cppUnitTestOSImpl_LDFLAGS   =
cppUnitTestOSImpl_LIBS      += cppunit
cppUnitTestOSImpl_LIBS      += maciClient
cppUnitTestOSImpl_LIBS      += prototypeCommonStubs
cppUnitTestOSImpl_LIBS      += DeviceStubs
cppUnitTestOSImpl_LIBS      += LampStubs
cppUnitTestOSImpl_LIBS      += MotorStubs
cppUnitTestOSImpl_LIBS      += Lamp2Stubs
cppUnitTestOSImpl_LIBS      += FilterStubs
cppUnitTestOSImpl_LIBS      += CCDImagStubs
cppUnitTestOSImpl_LIBS      += Lamp1Stubs
cppUnitTestOSImpl_LIBS      += CCDSpectroStubs
cppUnitTestOSImpl_LIBS      += ICSStubs
cppUnitTestOSImpl_LIBS      += DCSStubs
cppUnitTestOSImpl_LIBS      += SubsystemStubs
cppUnitTestOSImpl_LIBS      += OSStubs

#
# LampImpl cpp unit test
# -----------------------------
cppUnitTestLampImpl_OBJECTS   = cppUnitTestLampImpl
cppUnitTestLampImpl_LDFLAGS   =
cppUnitTestLampImpl_LIBS      += cppunit
cppUnitTestLampImpl_LIBS      += maciClient
cppUnitTestLampImpl_LIBS      += prototypeCommonStubs
cppUnitTestLampImpl_LIBS      += DeviceStubs
cppUnitTestLampImpl_LIBS      += LampStubs
cppUnitTestLampImpl_LIBS      += MotorStubs
cppUnitTestLampImpl_LIBS      += Lamp2Stubs
cppUnitTestLampImpl_LIBS      += FilterStubs
cppUnitTestLampImpl_LIBS      += CCDImagStubs
cppUnitTestLampImpl_LIBS      += Lamp1Stubs
cppUnitTestLampImpl_LIBS      += CCDSpectroStubs
cppUnitTestLampImpl_LIBS      += ICSStubs
cppUnitTestLampImpl_LIBS      += DCSStubs
cppUnitTestLampImpl_LIBS      += SubsystemStubs
cppUnitTestLampImpl_LIBS      += OSStubs

#
# FilterImpl cpp unit test
# -----------------------------
cppUnitTestFilterImpl_OBJECTS   = cppUnitTestFilterImpl
cppUnitTestFilterImpl_LDFLAGS   =
cppUnitTestFilterImpl_LIBS      += cppunit
cppUnitTestFilterImpl_LIBS      += maciClient
cppUnitTestFilterImpl_LIBS      += prototypeCommonStubs
cppUnitTestFilterImpl_LIBS      += DeviceStubs
cppUnitTestFilterImpl_LIBS      += LampStubs
cppUnitTestFilterImpl_LIBS      += MotorStubs
cppUnitTestFilterImpl_LIBS      += Lamp2Stubs
cppUnitTestFilterImpl_LIBS      += FilterStubs
cppUnitTestFilterImpl_LIBS      += CCDImagStubs
cppUnitTestFilterImpl_LIBS      += Lamp1Stubs
cppUnitTestFilterImpl_LIBS      += CCDSpectroStubs
cppUnitTestFilterImpl_LIBS      += ICSStubs
cppUnitTestFilterImpl_LIBS      += DCSStubs
cppUnitTestFilterImpl_LIBS      += SubsystemStubs
cppUnitTestFilterImpl_LIBS      += OSStubs


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
PY_SCRIPTS         += pyUnitTestDeviceImpl
PY_SCRIPTS         += pyUnitTestDCSImpl
PY_SCRIPTS         += pyUnitTestLamp2Impl
PY_SCRIPTS         += pyUnitTestCCDImagImpl
PY_SCRIPTS         += pyUnitTestSubsystemImpl
PY_SCRIPTS         += pyUnitTestLamp1Impl
PY_SCRIPTS         += pyUnitTestCCDSpectroImpl
PY_SCRIPTS         += pyUnitTestMotorImpl
PY_SCRIPTS         += pyUnitTestICSImpl
PY_SCRIPTS         += pyUnitTestOSImpl
PY_SCRIPTS         += pyUnitTestLampImpl
PY_SCRIPTS         += pyUnitTestFilterImpl
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
JARFILES= prototypeTest
prototypeTest_DIRS= alma/prototype
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
