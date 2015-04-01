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

#include "wsf2ex2.h"
wsf2ex2WSF_RCSID("@(#) $Id: wsf2ex2ACTION_SETUP.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex2ACTION_SETUP.h"
#include "wsf2ex2ACTION_MGR.h"
#include "wsf2ex2CONFIG.h"
#include "wsf2ex2DATA.h"
#include "wsf2ex2Actions.h"
#include "wsf2ex2Errors.h"

#include "wsf2libCONTROL.h"
#include "wsf2libLOG.h"
#include "wsf2libASSERT.h"


/** 
 * Constructor
 * 
 * @param fsm pointer to the state machine.
 * @param dbRoot database root of the debug flag.
 */
wsf2ex2ACTION_SETUP::wsf2ex2ACTION_SETUP(wsf2ex2ACTION_MGR& actionMgr,
				       wsf2libCONTROL& control,
				       wsf2ex2CONFIG& config,
				       wsf2ex2DATA& data) :
    wsf2libACTION(wsf2ex2ACTION_STR_SETUP),
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
wsf2ex2ACTION_SETUP::~wsf2ex2ACTION_SETUP()
{
    wsf2libLOG_TRACE();
}


/**
 * Init
 */
ccsCOMPL_STAT wsf2ex2ACTION_SETUP::Init()
{
    wsf2libLOG_TRACE();
    wsf2libLOG_ACTION(GetName());
    ErrReset();

    return SUCCESS;
}

/**
 * Execute
 */
ccsCOMPL_STAT wsf2ex2ACTION_SETUP::Execute()
{
    wsf2libLOG_TRACE();
    wsf2libLOG_ACTION(GetName());
    ErrReset();

    return SUCCESS;
}

/**
 * Complete. 
 */
ccsCOMPL_STAT wsf2ex2ACTION_SETUP::Complete()
{
    wsf2libLOG_TRACE();
    wsf2libLOG_ACTION(GetName());
    ErrReset();

    return SUCCESS;
}

/**
 * Abort. 
 */
ccsCOMPL_STAT wsf2ex2ACTION_SETUP::Abort()
{
    wsf2libLOG_TRACE();
    wsf2libLOG_ACTION(GetName());
    ErrReset();

    return SUCCESS;
}

/**
 * Reject 
 */
ccsCOMPL_STAT wsf2ex2ACTION_SETUP::Reject()
{
    wsf2libLOG_TRACE();
    wsf2libLOG_ACTION(GetName());
    ErrReset();

    return SUCCESS;
}

/**
 * Supersede 
 */
ccsCOMPL_STAT wsf2ex2ACTION_SETUP::Supersede()
{
    wsf2libLOG_TRACE();
    wsf2libLOG_ACTION(GetName());
    ErrReset();

    return SUCCESS;
}
	

// __oOo__

