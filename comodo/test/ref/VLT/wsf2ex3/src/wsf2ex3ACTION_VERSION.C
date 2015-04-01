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

#include "wsf2ex3.h"
wsf2ex3WSF_RCSID("@(#) $Id: wsf2ex3ACTION_VERSION.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex3ACTION_VERSION.h"
#include "wsf2ex3ACTION_MGR.h"
#include "wsf2ex3CONFIG.h"
#include "wsf2ex3DATA.h"
#include "wsf2ex3Actions.h"
#include "wsf2ex3Errors.h"

#include "wsf2libCONTROL.h"
#include "wsf2libLOG.h"
#include "wsf2libASSERT.h"   
#include "wsf2libCMDH_IN.h"

/** 
 * Constructor
 * 
 * @param fsm pointer to the state machine.
 * @param dbRoot database root of the debug flag.
 */
wsf2ex3ACTION_VERSION::wsf2ex3ACTION_VERSION(wsf2ex3ACTION_MGR& actionMgr,
				       wsf2libCONTROL& control,
				       wsf2ex3CONFIG& config,
				       wsf2ex3DATA& data) :
    wsf2libACTION_VERSION(wsf2ex3ACTION_STR_VERSION, actionMgr, control),
    mActionMgr(actionMgr), 
    mControl(control), 
    mConfig(config), 
    mData(data)
{
    wsf2libLOG_TRACE();
}


/** 
 * Destructor
 */
wsf2ex3ACTION_VERSION::~wsf2ex3ACTION_VERSION()
{
    wsf2libLOG_TRACE();
}

/**
 * Execute
 */
ccsCOMPL_STAT wsf2ex3ACTION_VERSION::Execute()
{
    wsf2libLOG_TRACE();
    wsf2libLOG_ACTION(GetName());    
    wsf2libASSERTPTR(mCmdVersion);    
    ErrReset();

    // get version and build the reply buffer
    char buf[512];
    snprintf(buf, sizeof(buf), "%s %s", wsf2ex3MOD, rcsId);

    if (mCmdVersion->SendNormalReply(buf) == FAILURE)
	{
	ErrAdd(wsf2ex3MOD, wsf2ex3ERR_ACTION, __FILE_LINE__, GetName(), __FUNCTION__,
	       mActionMgr.GetStateDesc());
	return FAILURE;
	}

    return SUCCESS;
}
	

// __oOo__

