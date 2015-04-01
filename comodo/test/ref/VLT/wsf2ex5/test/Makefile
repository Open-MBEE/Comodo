#*******************************************************************************
#    E.S.O. - VLT project
#  
#    "@(#) $Id$"
#
# who                when       what
# ----------------  ----------  ----------------------------------------------
# COMODO            -           Created.
# 

#*******************************************************************************
# This Makefile follows VLT Standards (see Makefile(5) for more).
#*******************************************************************************
# REMARKS
#    None
#------------------------------------------------------------------------


#
# user definable C-compilation flags

USER_CFLAGS =  -DeccsLOG_LEVEL=3


#
# MODULE CODE DESCRIPTION:
# ------------------------
# As a general rule:  public file are "cleaned" and "installed"  
#                     local (_L) are not "installed".
TESTLIBS = evhEt misc evh eccs fnd C++

#
# MODULE CODE DESCRIPTION:
# ------------------------
# As a general rule:  public file are "cleaned" and "installed"  
#                     local (_L) are not "installed".

#
# C programs (public and local)
# -----------------------------
EXECUTABLES     =
EXECUTABLES_L   = 

#
# <brief description of xxxxx program>
xxxxx_OBJECTS = 
xxxxx_LIBS = 

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
# Source files to be linked here in order to be tested
# -----------------------------------------------------


#
# Scripts (public and local)
# ----------------------------
SCRIPTS         = TestDriver wsf2ex5TestSetEnv.sh wsf2ex5TestStart.sh
SCRIPTS_L       =

#
# TCL scripts (public and local)
# ------------------------------
GLOBAL_TCLSH = seqSh 

TCL_SCRIPTS     = wsf2ex5TestStdCommands 
TCL_SCRIPTS_L   =

#
# <brief description of tttttt tcl-script>
wsf2ex5TestStdCommands_OBJECTS  = wsf2ex5TestStdCommands
wsf2ex5TestStdCommands_TCLSH    = $(GLOBAL_TCLSH)
wsf2ex5TestStdCommands_LIBS     = wsf2ex5TestSupportLib


#
# TCL libraries (public and local)
# ------------------------------
TCL_LIBRARIES   = wsf2ex5TestSupportLib
TCL_LIBRARIES_L =

#
# <brief description of tttlll library>
wsf2ex5TestSupportLib_OBJECTS = wsf2ex5TestSupport

#
# UIF panels (public and local)
# ----------------------------
PANELS   = 
PANELS_L = 

#
# On-Line Database Files
# ----------------------
DBL_CLASSES = 
DBL_BCF = 
DBL_LOAD = wsf2ex5TestDb
DBL_FLAGS =  $(I_PATH) -a 1000 
xxxx_DBL_FLAGS =

#
# Dictionary and Alias Files
# --------------------------

#
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
# list of all possible C-sources (used to create automatic dependencies)
# ------------------------------
CSOURCENAMES = \
	$(foreach exe, $(EXECUTABLES) $(EXECUTABLES_L), $($(exe)_OBJECTS)) \
	$(foreach lib, $(LIBRARIES) $(LIBRARIES_L), $($(lib)_OBJECTS))

#
#>>>>> END OF standard rules

#
# CHOSE PLATFORM
# --------------
# Default is UNIX, for VxVorks application next line MUST BE UNCOMMENTED
#MAKE_VXWORKS = on   

#
# INCLUDE STANDARDS
# -----------------
MAKEDIR = $(VLTROOT)/include
include $(MAKEDIR)/vltMakefile

#
# Test files (produced during tests and to be deleted)
# ----------------------------------------------------

#
# TARGETS
# -------
all:	do_all 
	@echo " . . . 'all' done" 

clean : clean_all 
	$(AT) $(RM) *~ *.diff *.rep ../dbl/*~ core out/*.out out/*.diff sed.scan *.log .gdb* ENVIRONMENTS/wsTat/log/*.log wsf2ex1TestDb.dat
	@echo " . . . clean done"

man   : do_man 
	@echo " . . . man page(s) done"

install : install_all
	@echo " . . . installation done"

db : db_all
	@echo " . . . ../DB done"

#___oOo___

