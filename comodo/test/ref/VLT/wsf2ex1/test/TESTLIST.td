#*******************************************************************************
#    E.S.O. - VLT project
#  
#    "@(#) $Id$"
#
# who                when       what
# ----------------  ----------  ----------------------------------------------
# COMODO            -           Created.
# 

###################################################################
#
#  EVH TESTLIST
#
#  "@(#) $Id: TESTLIST.td,v 0.18 2006/09/13 10:06:32 vltsccm Exp $"
#
#  Following is a list of test programs that are called 
#  sequencially.
#  When two programs are specified the first one is treated
#  as a server and started in the background.
#
#  Following actions are performed:
#
#  - Program is started with output redirected into file.
#  - If two programs are specified the output is merged.
#  - The timestamp is striped off the output
#  - The output is compared with the reference output <program>.REF
#  - If difference are found the are listed in <program>.diff
# 
#
###################################################################
#
# Test the main engine for evh
#

1 wsf2ex1TestStdCommands     wsf2ex1TestStart.sh "@SLEEP 10" wsf2ex1TestStdCommands 

#___o0o___

