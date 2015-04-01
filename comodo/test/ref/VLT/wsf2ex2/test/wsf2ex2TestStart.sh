#! /bin/sh
#*******************************************************************************
#    E.S.O. - VLT project
#  
#    "@(#) $Id$"
#
# who                when       what
# ----------------  ----------  ----------------------------------------------
# COMODO            -           Created.
# 

# Processes are re-created for each test (i.e. they are not in the CcsEnvTable).
# At the end of the test the processes are deleted by wsf2ex2TestStop.sh script.
# Make sure log output in on CONSOLE to allow TAT test checking behaviour.
dbWrite ":Appl_data:wsf2ex2:control.verbose" "CONSOLE"
wsf2ex2Control -r :Appl_data:wsf2ex2 &

# valgrind --tool=memcheck --leak-check=yes -v --trace-children=yes --show-reachable=yes --num-callers=50 --log-file=./wsf2ex2_valgrind.log wsf2ex2Control -r :Appl_data:wsf2ex2 &

# ___oOo___

