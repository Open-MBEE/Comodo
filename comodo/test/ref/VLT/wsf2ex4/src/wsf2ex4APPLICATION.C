/*******************************************************************************
*    E.S.O. - VLT project
*
*    "@(#) $Id$"
*
* who                when       what
* ----------------  ----------  ----------------------------------------------
* COMODO            -           Created.
* 
*/

#define _POSIX_SOURCE 1
#include "vltPort.h"

#include "wsf2ex4.h"
wsf2ex4WSF_RCSID("@(#) $Id: wsf2ex4APPLICATION.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex4APPLICATION.h"
#include "wsf2ex4ACTION_MGR.h"
#include "wsf2ex4Defines.h"
#include "wsf2ex4Errors.h"

#include "wsf2libLOG.h"
#include "wsf2libASSERT.h"
#include "wsf2libHELPER.h"

#include "eccsTestTools.h"

#include "evhHANDLER.h"
#include "evhTASK.h"


wsf2ex4APPLICATION::wsf2ex4APPLICATION() :
    wsf2libAPPLICATION(),
    mActionMgr(NULL)
{
    wsf2libLOG_TRACE();
    
    /*
     * Set the module name used by the logging
     */
    wsf2libLOG_SETMODNAME(wsf2ex4MOD);
        
    /*
     * Check base class construction
     */
    if (wsf2libHELPER::ObjectOk(this, "APPLICATION") == ccsFALSE)
	{
	errAdd(wsf2ex4MOD, wsf2ex4ERR_CREATE, __FILE_LINE__, "wsf2libAPPLICATION");
	ObjStatus(FAILURE);	
	return;	
	}
}


wsf2ex4APPLICATION::~wsf2ex4APPLICATION()
{
    wsf2libLOG_TRACE();
    wsf2libASSERTPTR(mActionMgr);

    delete mActionMgr;
    mActionMgr = NULL;

    // Close everything and prepare to exit
    ccsExit();
    errCloseStack();
}


ccsCOMPL_STAT wsf2ex4APPLICATION::Init(int argCount, char *arg[])
{
    wsf2libLOG_TRACE();
    ErrReset();


    // Turn off logging of warning on EXIT command
    evhTASK::LogExitWarning(FALSE);

    // Parses the command line arguments
    // and extract the configuration parameters
    // for ENV NAME, DB POINT and PROC NAME from the
    // command line or from the enviroment variables
    if (EvaluateArgs(argCount, arg, wsf2ex4DB_ROOT_POINT) == FAILURE)
	{
	errAdd(wsf2ex4MOD, wsf2ex4ERR_INIT, __FILE_LINE__, "wrong argument(s)");
	return FAILURE;
	}

    // Initialize CCS and connect to database
    if(InitCCS() == FAILURE)
	{
	errAdd(wsf2ex4MOD, wsf2ex4ERR_INIT, __FILE_LINE__, "cannot init CCS");
	return FAILURE;      
	}

    // Logs current startup configuration
    eccsLOG_1(("%s - Application started (proc name: %s, DB root point: %s)", 
	       GetProcName(), GetProcName(), GetDbRoot()));
    logData(wsf2ex4MOD,"%s - Application started (proc name: %s, DB root point: %s)", 
	    GetProcName(), GetProcName(), GetDbRoot());

   /*
    * Create factory for ACTIONS, DATA, CONFIG and CONTROL
    */
    wsf2libASSERT(mActionMgr == NULL);
    mActionMgr = new wsf2ex4ACTION_MGR(GetDbRoot(), argCount, arg, GetEventMgr());
    if (mActionMgr == NULL)
	{
	errAdd(wsf2ex4MOD, wsf2ex4ERR_FATAL, __FILE_LINE__, "no memory for allocating wsf2ex4ACTION_MGR");
	return FAILURE;
	}
    if (wsf2libHELPER::ObjectOk(mActionMgr, "ACTION_MGR") == ccsFALSE)
	{
	errAdd(wsf2ex4MOD, wsf2ex4ERR_CREATE, __FILE_LINE__, "wsf2ex4ACTION_MGR");
	return FAILURE;	
	}

    /*
     * Set FSM context
     * TBD: rename method
     */
    SetControl(mActionMgr->GetControl());

    /*
     * Instantiate events and actions
     */
    if (CreateEvents(wsf2ex4EVENTS_FILENAME) == FAILURE)
	{
	errAdd(wsf2ex4MOD, wsf2ex4ERR_CREATE, __FILE_LINE__, "Events");
	return FAILURE;	
	}
    if (mActionMgr->CreateActions() == FAILURE)
	{
	errAdd(wsf2ex4MOD, wsf2ex4ERR_CREATE, __FILE_LINE__, "Actions");
	return FAILURE;	
	}
    if (mActionMgr->CreateActivities(GetProcName(), GetFsm(), GetThreadsHandler()) == FAILURE)
	{
	errAdd(wsf2ex4MOD, wsf2ex4ERR_CREATE, __FILE_LINE__, "Activities");
	return FAILURE;	
	}
    /*
     * Load SCXML Model
     */
    if (LoadModel(wsf2ex4MODEL_FILENAME, mActionMgr->SCXMLGetActions(), mActionMgr->SCXMLGetActivities()) == FAILURE)
	{
	errAdd(wsf2ex4MOD, wsf2ex4ERR_FATAL, __FILE_LINE__, "cannot load SCXML model");
	return FAILURE;
	}

    /*
     * Initialize all events and actions and data structures
     */
    if (mActionMgr->Init() == FAILURE)
	{
	errAdd(wsf2ex4MOD, wsf2ex4ERR_FATAL, __FILE_LINE__, "initializing actions and data structures");
	return FAILURE;
	}

    /*
     * Start the execution of SCXML model
     */
    if (StartModel() == FAILURE)
	{
	errAdd(wsf2ex4MOD, wsf2ex4ERR_FATAL, __FILE_LINE__, "cannot start SCXML model execution");
	return FAILURE;
	}

    return SUCCESS;
} 


wsf2ex4ACTION_MGR* wsf2ex4APPLICATION::GetActionMgr()
{
    wsf2libLOG_TRACE();
    return mActionMgr;
}


ccsCOMPL_STAT wsf2ex4APPLICATION::Run()
{
    wsf2libLOG_TRACE();
    ErrReset();

    ccsCOMPL_STAT stat = SUCCESS;

    eccsLOG_1(("wsf2ex4 - Entering the main loop."));
    while(IsRunning() == ccsTRUE)
	{
	// I can return on request, such as with EXIT command 
	// (stat == SUCCESS) or because of an error (stat == FAILURE)
	stat = evhHandler->MainLoop();

	// In case of error, I try to recover before exiting
	if (stat == FAILURE) 
	    {
	    eccsLOG_1(("A fatal error occurred, try to recover!"));
#if 0
	    wsf2libASSERTPTR(mEventMgr);
	    if (mEventMgr->Init() == FAILURE) stat = FAILURE;

	    wsf2libASSERTPTR(mActionMgr);
	    if (mActionMgr->Init() == FAILURE) stat = FAILURE;

	    wsf2libASSERTPTR(mFsm);
	    if (mFsm->Init() ==  FAILURE) stat = FAILURE;
#endif
	    errAdd(wsf2ex4MOD, wsf2ex4ERR_FATAL, __FILE_LINE__, GetProcName());
	    errCloseStack();

	    if (stat == FAILURE)
		{
		eccsLOG_1(("Cannot recover. Application aborted!"));
		errAdd(wsf2ex4MOD, wsf2ex4ERR_FATAL, __FILE_LINE__, GetProcName());
		errCloseStack();
		logData(wsf2ex4MOD, "%s - Application aborted!", GetProcName());
		Quit();
		}
	    }
	else
	    {
	    eccsLOG_1(("wsf2ex4 - Exiting from the main loop. Application quits."));
	    logData(wsf2ex4MOD, "%s - Application quits.", GetProcName());
	    Quit();
	    break;
	    }
	}

    return stat;
} 

// __oOo__

