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
# 
#   SYNOPSIS
# 
#   DESCRIPTION
#
#   FILES
#
#   ENVIRONMENT
#
#   RETURN VALUES
#
#   CAUTIONS
#
#   EXAMPLES
#
#   SEE ALSO
#
#   BUGS     
#
#------------------------------------------------------------------------
#

#
# Function to print messages with a time stamp compatible with 
# eccsTestDriver and standard ccs log time stamps
#
set lastClock 0
set lastUsec  0
set stepNum   0
set saveTz ""
set dbPath ""
set dbPathControl ""
set dbPathData ""
set dbPathConfig ""
set simulationMode 0

proc printLog { str } {
    global lastClock lastUsec

    set clock [getclock]
    set time [fmtclock $clock "%Y-%m-%d  %H:%M:%S"]

    if { $lastClock == $clock } {
	incr lastUsec 1
    } else {
	set lastUsec 0
    }
    set lastClock $clock
    set nusec [format "%6.6d" $lastUsec]
    set str 
    puts "$time.$nusec wsf2ex1TestLog: $str"
}

proc printOkLog { str } {
    global lastClock lastUsec

    set clock [getclock]
    set time [fmtclock $clock "%Y-%m-%d  %H:%M:%S"]

    if { $lastClock == $clock } {
	incr lastUsec 1
    } else {
	set lastUsec 0
    }
    set lastClock $clock
    set nusec [format "%6.6d" $lastUsec]
    set str 
    puts "$time.$nusec wsf2ex1TestLog: $str --> test OK."
}

proc printErrLog { str } {
    global lastClock lastUsec

    set clock [getclock]
    set time [fmtclock $clock "%Y-%m-%d  %H:%M:%S"]

    if { $lastClock == $clock } {
	incr lastUsec 1
    } else {
	set lastUsec 0
    }
    set lastClock $clock
    set nusec [format "%6.6d" $lastUsec]
    set str 
    puts "$time.$nusec wsf2ex1TestLog: $str --> test FAIL."
}

proc printErrorStructure { errorStruct } {
    printLog " ---------------- Error Structure ---------------- "
    set str [format "Error Number   : %.8s\tSeverity : %s" [lindex $errorStruct 5] [lindex $errorStruct 6]]
    printLog $str
    set str [format "Module         : %.8s\tLocation : %s" [lindex $errorStruct 4] [string trim [lindex $errorStruct 3] :1234567890]]
    printLog $str
    set str [format "Error Text     : %s" [lindex $errorStruct 7]]
    printLog $str
}

proc testCmdNormalReply { destProcess cmd args } {
    global ws_ccsEnvName

    if {[catch "testSendRecvCmd $ws_ccsEnvName $destProcess $cmd $args" cmdData]} {
	printErrorStructure [seq_errGetFromStack 1]
	printLog "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	printLog "           UNEXPECTED FAILURE -> EXIT            "
	printLog "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
    } else {
	printLog "([lindex $cmdData 0]) Reply: [lindex $cmdData 1]"
	printLog "([lindex $cmdData 0]) Normal reply expected --> test OK."
    }
}

proc testCmdNormalReplyStripped { destProcess cmd args } {
    global ws_ccsEnvName

    if {[catch "testSendRecvCmd $ws_ccsEnvName $destProcess $cmd $args" cmdData]} {
	printErrorStructure [seq_errGetFromStack 1]
	printLog "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	printLog "           UNEXPECTED FAILURE -> EXIT            "
	printLog "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
    } else {
	printLog "([lindex $cmdData 0]) Reply: --- reply buffer intentionally stripped out ---"
	printLog "([lindex $cmdData 0]) Normal reply expected --> test OK."
    }
}

proc testCmdNormalReplySilent { destProcess cmd args } {
    global ws_ccsEnvName

    if {[catch "seq_msgSendCommand $ws_ccsEnvName $destProcess $cmd $args" cmdId]} {
        printLog "($cmdId) Error sending to $destProcess the command $cmd. Abort test!"
	exit 1
    } else {
        if {[catch "seq_msgRecvReply $cmdId errNr -last -60000" buf]} {
	    printLog "($cmdId) Error receiving the reply. Abort test!"
	    exit 1
	} else {
	    if {$errNr != 0} {
		printLog "($cmdId) Error reply."
		printErrorStructure [seq_errGetFromStack 1]
		printLog "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
		printLog "           UNEXPECTED FAILURE -> EXIT            "
		printLog "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
		exit 1
	    } 
	}
    }
}

proc testCmdErrorReply { destProcess cmd args } {
    global ws_ccsEnvName

    if {[catch "testSendRecvCmd $ws_ccsEnvName $destProcess $cmd $args" cmdData]} {
	printErrorStructure [seq_errGetFromStack 1]
	printLog "([lindex $cmdData 0]) Error reply expected --> test OK."
    } else {
	printLog "([lindex $cmdData 0]) Normal reply: [lindex $cmdData 1]"
	printLog "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	printLog "           UNEXPECTED SUCCESS -> EXIT            "
	printLog "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
    }
}

proc testCommand { destProcess cmd args } {
    global ws_ccsEnvName

    if {[catch "testSendRecvCmd $ws_ccsEnvName $destProcess $cmd $args" cmdReply]} {
	set iLimit [seq_errGetStackSize]
	for {set i 1} {$i <= $iLimit} {incr i} {
	    printErrorStructure [seq_errGetFromStack $i]
	}
	printLog "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	printLog "           UNEXPECTED FAILURE -> EXIT            "
	printLog "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	exit 1
    }
    return $cmdReply
}

proc testSendRecvCmd { envName destProcess cmd args } {
  
    printLog "-------------"
    if {[catch "seq_msgSendCommand $envName $destProcess $cmd $args" cmdId]} {
        printLog "($cmdId) Error sending to $destProcess the command $cmd. Abort test!"
	exit 1
    } else {
        printLog "($cmdId) Command to $destProcess: $cmd $args"
        if {[catch "seq_msgRecvReply $cmdId errNr -last -60000" buf]} {
            printLog "($cmdId) Error receiving the reply. Abort test!"
	    exit 1
        } else {
	    if {$errNr == 0} {
		printLog "($cmdId) Normal reply."
		return -code 0 "$cmdId {$buf}"
	    } else {
		printLog "($cmdId) Error reply."
		return -code 1 "$cmdId {$buf}"
	    }
        }
    }
}

proc testSendCommand { destProcess cmd args } {
    global ws_ccsEnvName

    printLog "-------------"
    if {[catch "seq_msgSendCommand $ws_ccsEnvName $destProcess $cmd $args" cmdId]} {
	printLog "Error sending to $destProcess the command $cmd"
    } else {
        printLog "($cmdId) Command to $destProcess: $cmd $args"
    }    
    return $cmdId
}

proc testRecvReply { cmdId } {
    global seq_ccsEnvName
    printLog "-------------"
    if {[catch "seq_msgRecvReply $cmdId errNr -last -60000" buf]} {
        printLog "($cmdId) Error receiving the reply"
    } else {
        printLog "($cmdId) Reply: $buf"
    }
}

proc testRecvErrorReply { cmdId } {
    global seq_ccsEnvName
    printLog "-------------"
    if {[catch "seq_msgRecvReply $cmdId errNr -last -60000" buf]} {
        printLog "($cmdId) Error receiving the reply"
	exit 1
    } else {
	if {$errNr == 0} {
	    printLog "($cmdId) Normal reply: $buf"
	    printLog "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	    printLog "           UNEXPECTED SUCCESS -> EXIT            "
	    printLog "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	    exit 1
	} else {
	    printErrorStructure [seq_errGetFromStack 1]
	    printLog "($cmdId) Error reply expected --> test OK."
	}
    }
}

proc testRecvNormalReply { cmdId } {
    global seq_ccsEnvName
    printLog "-------------"
    if {[catch "seq_msgRecvReply $cmdId errNr -last -60000" buf]} {
        printLog "($cmdId) Error receiving the reply"
	exit 1
    } else {
	if {$errNr == 0} {
	    printLog "($cmdId) Reply: $buf"
	    printLog "($cmdId) Normal reply expected --> test OK."
	} else {
	    printErrorStructure [seq_errGetFromStack 1]
	    printLog "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	    printLog "           UNEXPECTED FAILURE -> EXIT            "
	    printLog "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	    exit 1
	}
    }
}

proc testDbRead { dbAddr } {
    global ws_ccsEnvName

    if {[catch "set value [seq_dbReadSymbolic @$ws_ccsEnvName:$dbAddr]"]} {
	printLog "Error when reading from DB: $dbAddr. Abort test!"
	exit 1    
    } else {
	printLog "DB Value for $dbAddr: $value"
    }
}

proc testDbWrite { dbAddr value } {
    global ws_ccsEnvName

    if {[catch "seq_dbWriteSymbolic @$ws_ccsEnvName:$dbAddr $value"]} {
	printLog "Error when writing into DB: $dbAddr. Abort test!"
	exit 1
    } else {
	printLog "Value $value written into: $dbAddr"
    }
}

proc testDbValue { dbAddr } {
    global ws_ccsEnvName

    set value [seq_dbReadSymbolic @$ws_ccsEnvName:$dbAddr]
    printLog "DB Value for $dbAddr: $value"
}

proc testDbVerify { dbAddr refValue } {
    global ws_ccsEnvName

    if {[catch "set value [seq_dbReadSymbolic @$ws_ccsEnvName:$dbAddr]"]} {
	printLog "Error when reading from DB: $dbAddr. Abort test!"
	exit 1    
    } 
    if { $value != $refValue } {
	printLog "DB Verify: Value for $dbAddr: $value - Expected $refValue FAIL"
    } else {
	printLog "DB Verify: Value for $dbAddr: $value OK"
    }
}

proc testEvtAttach {attr filter script } {
    global seq_ccsEnvName

    printLog "-------------"
    if { $script == "" } {
	set script testEvtAttachScript
	set scriptArg {%A}
    } else {
	set scriptArg {%A %D}
    }

    if {[catch "seq_evtAttach $attr $filter {$script $scriptArg}" eventId]} {
	printLog "Error when attaching event on $attr. Abort test!"
	exit 1
    }
    printLog "($eventId) Event attached on $attr: $filter $script"
    return $eventId
}

proc testEvtAttachScript { attr } {
    global seq_ccsEnvName

    printLog "-------------"
    printLog "(event) Event on $attr handled"
}

proc testStepHeader {str} {
    global stepNum

    if {$stepNum > 1} {
	printLog ""
    }
    printLog "#####################################################################"
    printLog ""
    printLog "STEP $stepNum: $str"
    printLog ""
    incr stepNum
}

proc testPing { envName procName wait } {

    # Wait that the process under test is ready and reactive
    printLog "Waiting for the $procName process to be ready"
    #puts "Env.  $envName Proc. $procName"

    set count 0
    while {[catch "seq_msgSendCommand $envName $procName PING" cmdId]} {
	sleep 1
	incr count 1
	if { $count > $wait } {
	    printLog "After $count sec. $procName is not ready yet. Abort test!"
	    exit 1
	}
    }
    seq_msgRecvReply $cmdId errNr -60000
}

proc testRestoreSimulation { procName } {

    global simulationMode

    if {$simulationMode == 1} {
	if {[catch {testCmdNormalReplySilent $procName SIMULAT ""}]} {
	    printLog "Cannot set $procName in simulation."
	    exit 1
	}
	#puts "Simulation ON"
    } else {
	if {[catch {testCmdNormalReplySilent $procName STOPSIM ""}]} {
	    printLog "Cannot unset $procName from simulation."
	    exit 1
	}
	#puts "Simulation OFF"
    }
    #puts "Simulation flag restored"
}


#
# Set TimeZone to UTC time     
#
proc testSetTimeZoneToUTC { } {

    global saveTz

    set localTz "UTC"
    catch { set saveTz $env(TZ) }
    set env(TZ) $localTz
}

#
# Restore orignal TimeZone     
#
proc testRestoreLocalTime { } {

    global saveTz
    catch { set env(TZ) $saveTz }
}

#
# Make a backup of the Database
#
proc testBackupDatabase { fileName } {
    global ws_ccsEnvName

    exec dbBackup -nb "@$ws_ccsEnvName:Appl_data" -o $fileName
}

#
# Restore Database from backup
#
proc testRestoreDatabase { fileName } {
    exec dbRestore -f $fileName
}

proc testProlog { testNum testTitle procName } {
    global stepNum
    global env
    global seq_ccsEnvName
    global ws_ccsEnvName
    global simulationMode
    global argv 
    global dbPath 
    global dbPathControl
    global dbPathData 
    global dbPathConfig 


    printLog "#####################################################################"
    printLog ""
    printLog "TEST $testNum: $testTitle"
    printLog ""
    
    testStepHeader "Inializing the test environment"

    #
    # Initialyze CCS 
    #

    seq_ccsInit seq_$env(USER)
    seq_ccsAsyncInput open


    #
    # Parse command line parameters, if any
    # - WS environment
    # - DB path 
    # - simulation mode flag 0 = simulation off, 1 = simulation on
    #

    set wsEnv ""
    set dbPath "Appl_data:wsf2ex1"
    set simulationMode 1
    if { $argv != "" } {
	set wsEnv [lindex $argv 0]
	set dbPath [lindex $argv 1]
	set simulationMode [lindex $argv 2]
    }
    set dbPathControl "${dbPath}:control"
    set dbPathData    "${dbPath}:data"
    set dbPathConfig  "${dbPath}:config"

    #
    # Set WS environment: 
    # WS env. can be given as parameter when running the test
    # or it is set by TAT via wsTat UNIX variable
    #
    
    if {[string compare $wsEnv ""] == 0} {
	if {[catch {set ws_ccsEnvName  $env(wsTat)}]} {
	    printLog "Environment variable wsTat is undefined. Exiting..."
	    exit
	}
    } else {
	set ws_ccsEnvName $wsEnv
    }

    #
    # Debug info
    #
    #puts "WS environment:  $ws_ccsEnvName"
    #puts "Process name:    $procName"
    #puts "Simulation mode: $simulationMode"

    #
    # Waits a second before starting to give time the
    # processes to initialize.
    #

    sleep 1

    #
    # If defined, ping the process under test
    #

    if {[string length $procName] > 0} {
	if {[catch {testPing $ws_ccsEnvName $procName 10}]} {
	    printLog "Process $procName not started. Exiting..."
	    exit
	}
    }

}

proc testEpilog { procName } {
    
    global ws_ccsEnvName

    printLog ""
    printLog "#####################################################################"
    printLog ""
    printLog "Test completed !!!"
    printLog ""
    printLog "#####################################################################"

    #
    # If defined, send EXIT to the process under test
    #

    if {[string length $procName] > 0} {
	testCmdNormalReply $procName EXIT ""
    }

    #
    # Waits a couple of seconds before exiting, so that the exit command can be
    # processed
    #
    sleep 2

    exit 0
}

#
# ___oOo___


