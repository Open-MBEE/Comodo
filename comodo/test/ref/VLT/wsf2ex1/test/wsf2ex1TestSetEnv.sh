#! /bin/sh -f
#*******************************************************************************
#    E.S.O. - VLT project
#  
#    "@(#) $Id$"
#
# who                when       what
# ----------------  ----------  ----------------------------------------------
# COMODO            -           Created.
# 

#************************************************************************
#   NAME
#   wsf2ex1SetEnv - Set environment variables for wsf2ex1 module
#
#   SYNOPSIS
#   wsf2ex1SetEnv [INS_ROOT]
#
#   DESCRIPTION
#   This script set environment variables for TCS tracking WS module
#   using the given parameter as INS_ROOT path. If no parameter is given
#   uses $PWD/INS_ROOT.
#
#   FILES
#
#   ENVIRONMENT
#   The following variables are set:
#
#   INS_ROOT       $PWD/INS_ROOT of command line argument
#   INS_USER       SYSTEM
#   INS_SETUPPATH  $INS_ROOT/SYSTEM/COMMON/SETUPFILES/TARG
#   CCSSTATE       SIMULATION
#   TCS_ENVNAME    $RTAPENV
#   TCS_DBPOINT    ":Appl_data:TCS"
#
#   RETURN VALUES
#
#   CAUTIONS
#   In order to be effective, the script must be executed with the source 
#   command: source wsf2ex1SetEnv
#
#   EXAMPLES
#
#   SEE ALSO
#
#   BUGS     
#
#------------------------------------------------------------------------
#

# signal trap (if any)

echo "Setting environment variables for wsf2ex1 module"
echo "To be effective, run the script as: source wsf2ex1SetEnv [INS_ROOT]"
if [ $# -lt 1 ]; then
  INS_ROOT=$PWD/INS_ROOT
else
  INS_ROOT=$1
fi

# echo "INS root directory is: " $INS_ROOT
INS_USER=SYSTEM
INS_SETUPPATH=$INS_ROOT/SYSTEM/COMMON/SETUPFILES/TARG
CCSSTATE=SIMULATION

export INS_USER INS_SETUPPATH CCSSTATE INS_ROOT


#
# ___oOo___

