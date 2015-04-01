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

#include "wsf2ex5.h"
wsf2ex5WSF_RCSID("@(#) $Id: wsf2ex5ACTION_SETTEMP.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex5ACTION_SETTEMP.h"
#include "wsf2ex5ACTION_MGR.h"
#include "wsf2ex5CONFIG.h"
#include "wsf2ex5DATA.h"
#include "wsf2ex5Actions.h"
#include "wsf2ex5Errors.h"

#include "wsf2libCONTROL.h"
#include "wsf2libLOG.h"
#include "wsf2libASSERT.h"


/** 
 * Constructor
 * 
 * @param fsm pointer to the state machine.
 * @param dbRoot database root of the debug flag.
 */
wsf2ex5ACTION_SETTEMP::wsf2ex5ACTION_SETTEMP(wsf2ex5ACTION_MGR& actionMgr,
				       wsf2libCONTROL& control,
				       wsf2ex5CONFIG& config,
				       wsf2ex5DATA& data) :
    wsf2libACTION(wsf2ex5ACTION_STR_SETTEMP),
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
wsf2ex5ACTION_SETTEMP::~wsf2ex5ACTION_SETTEMP()
{
    wsf2libLOG_TRACE();
}


/**
 * Init
 */
ccsCOMPL_STAT wsf2ex5ACTION_SETTEMP::Init()
{
    wsf2libLOG_TRACE();
    wsf2libLOG_ACTION(GetName());
    ErrReset();

    return SUCCESS;
}

/**
 * Execute
 */
ccsCOMPL_STAT wsf2ex5ACTION_SETTEMP::Execute()
{
    wsf2libLOG_TRACE();
    wsf2libLOG_ACTION(GetName());
    ErrReset();

    return SUCCESS;
}

/**
 * Complete. 
 */
ccsCOMPL_STAT wsf2ex5ACTION_SETTEMP::Complete()
{
    wsf2libLOG_TRACE();
    wsf2libLOG_ACTION(GetName());
    ErrReset();

    return SUCCESS;
}

/**
 * Abort. 
 */
ccsCOMPL_STAT wsf2ex5ACTION_SETTEMP::Abort()
{
    wsf2libLOG_TRACE();
    wsf2libLOG_ACTION(GetName());
    ErrReset();

    return SUCCESS;
}

/**
 * Reject 
 */
ccsCOMPL_STAT wsf2ex5ACTION_SETTEMP::Reject()
{
    wsf2libLOG_TRACE();
    wsf2libLOG_ACTION(GetName());
    ErrReset();

    return SUCCESS;
}

/**
 * Supersede 
 */
ccsCOMPL_STAT wsf2ex5ACTION_SETTEMP::Supersede()
{
    wsf2libLOG_TRACE();
    wsf2libLOG_ACTION(GetName());
    ErrReset();

    return SUCCESS;
}
	

// __oOo__

