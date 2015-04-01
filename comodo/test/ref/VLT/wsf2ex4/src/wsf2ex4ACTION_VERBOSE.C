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
wsf2ex4WSF_RCSID("@(#) $Id: wsf2ex4ACTION_VERBOSE.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex4ACTION_VERBOSE.h"
#include "wsf2ex4ACTION_MGR.h"
#include "wsf2ex4CONFIG.h"
#include "wsf2ex4DATA.h"
#include "wsf2ex4Actions.h"

#include "wsf2libCONTROL.h"
#include "wsf2libLOG.h"

/** 
 * Constructor
 * 
 * @param fsm pointer to the state machine.
 * @param dbRoot database root of the debug flag.
 */
wsf2ex4ACTION_VERBOSE::wsf2ex4ACTION_VERBOSE(wsf2ex4ACTION_MGR& actionMgr,
				       wsf2libCONTROL& control,
				       wsf2ex4CONFIG& config,
				       wsf2ex4DATA& data) :
    wsf2libACTION_VERBOSE(wsf2ex4ACTION_STR_VERBOSE, actionMgr, control),
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
wsf2ex4ACTION_VERBOSE::~wsf2ex4ACTION_VERBOSE()
{
    wsf2libLOG_TRACE();
}

	

// __oOo__

