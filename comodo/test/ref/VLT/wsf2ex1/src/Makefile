#*******************************************************************************
#    E.S.O. - VLT project
#  
#    "@(#) $Id$"
#
# who                when       what
# ----------------  ----------  ----------------------------------------------
# COMODO            -           Created.
# 

#******************************************************************************
# This Makefile follows VLT Standards (see Makefile(5) for more).
#******************************************************************************
# REMARKS
#    None
#------------------------------------------------------------------------------


#
# INCLUDE CODE GENERATED MAKEFILE
# -----------------
include wsf2ex1Makefile

#
# user definable C-compilation flags
USER_CFLAGS = -std=gnu++11 -DeccsLOG_LEVEL=3 -I$(INTROOT)/include/scxml4cpp 
	

#
# additional include and library search paths
#USER_INC =
#USER_LIB = 

#
# MODULE CODE DESCRIPTION:
# ------------------------
# As a general rule:  public files are "cleaned" and "installed"  
#                     local (_L) are not "installed".

#
# C programs (public and local)
# -----------------------------
EXECUTABLES     = wsf2ex1Control
EXECUTABLES_L   = 

#
# list of object files 
wsf2ex1_CONTROL_OBJECTS = 
wsf2ex1Control_OBJECTS  = wsf2ex1Control ${wsf2ex1_CONTROL_OBJECTS}

#
# linker flags and libraries to be linked to wsf2ex1Control program
wsf2ex1Control_LDFLAGS  =
wsf2ex1Control_LIBS     = wsf2ex1Lib wsf2lib evh eccs fnd misc C++ CCS scxml4cppeng scxml4cpppar xerces-c
	


#
# man-pages for wsf2ex1Control commands
wsf2ex1Control_MAN      = $(basename $(wildcard *.doc))

#
# special compilation flags for single c sources
#yyyyy_CFLAGS   = 

#
# Includes (.h) files (public only)
# ---------------------------------
INCLUDES        = wsf2ex1Defines.h

#
# Libraries (public and local)
# ----------------------------
LIBRARIES       = wsf2ex1Lib
LIBRARIES_L     =

#
# <brief description of lllll library>
wsf2ex1Lib_OBJECTS   = ${wsf2ex1CODE_GENERATED_OBJECTS}

		      
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
# UIF panels (public and local)
# ----------------------------
PANELS   =
PANELS_L = 

#
# On-Line Database Files
# ----------------------
DBL_CLASSES = wsf2ex1DB_MAIN    \
              wsf2ex1DB_CONFIG  \
              wsf2ex1DB_DATA

DBL_BCF =     
DBL_FLAGS =   $(I_PATH) -a 500
xxxx_DBL_FLAGS =

#
# man pages to be done
# --------------------
MANSECTIONS = 1
MAN1 = $(addsuffix .C, $(EXECUTABLES)) $(addsuffix .doc, $(wsf2ex1Control_MAN))
MAN4 = $(addsuffix .C, $(wsf2ex1_CONTROL_OBJECTS))

#
# local man pages
# ---------------
MANl = $(addprefix ../dbl/, $(addsuffix .class, $(DBL_CLASSES)))

#
# ASCII file to be converted into Framemaker-MIF
# --------------------
ASCII_TO_MIF = ../include/wsf2ex1Defines.h \
               ../include/wsf2ex1Errors.h  \
               ../ERRORS/wsf2ex1_ERRORS    \
               ../CDT/wsf2ex1Control.cdt

#
# other files to be installed
#----------------------------
INSTALL_FILES = ../config/wsf2ex1Events.txt ../config/wsf2ex1.xml

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
DEBUG=on
NOOPTIMIZE=on
#MAKE_PURIFY=on
MAKEDIR = $(VLTROOT)/include
include $(MAKEDIR)/vltMakefile

#
# TARGETS
# -------
all:	do_all
	$(AT) touch ../config/wsf2ex1Events.txt ../config/wsf2ex1.xml
	@echo " . . . 'all' done" 

clean : clean_all 
	$(RM) ../doc/*.pcode ../*/*~ ../*~ ../*/#* ../*/core
	@echo " . . . clean done"

man   : do_man
	@echo " . . . man page(s) done"

doxy  :
	$(AT) cp ../config/wsf2ex1Doxyfile.api $(INTROOT)/config
	$(AT) chmod 644 $(INTROOT)/config/wsf2ex1Doxyfile.*
	$(AT) if [ ! -d $(INTROOT)/man/man3/include ];\
	    then mkdir $(INTROOT)/man/man3/include; fi 
	$(AT) if [ ! -d $(INTROOT)/man/man3/dbl ];\
	    then mkdir $(INTROOT)/man/man3/dbl; fi 
	$(AT) if [ ! -d $(INTROOT)/man/man3/vw ];\
	    then mkdir $(INTROOT)/man/man3/vw; mkdir $(INTROOT)/man/man3/vw/include;\
		 mkdir $(INTROOT)/man/man3/vw/dbl; fi 
	@echo "Generating API documentation"
	cd $(INTROOT); doxygen config/wsf2ex1Doxyfile.api

install : install_all
	@echo " . . . installation done"

db : db_all
	@echo " . . . ../DB done"

#___oOo___

