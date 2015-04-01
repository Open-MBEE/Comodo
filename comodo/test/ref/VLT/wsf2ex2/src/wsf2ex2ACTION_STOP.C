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
wsf2ex2WSF_RCSID("@(#) $Id: wsf2ex2ACTION_STOP.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex2ACTION_STOP.h"
#include "wsf2ex2ACTION_MGR.h"
#include "wsf2ex2CONFIG.h"
#include "wsf2ex2DATA.h"
#include "wsf2ex2Actions.h"

#include "wsf2libCONTROL.h"
#include "wsf2libLOG.h"

/** 
 * Constructor
 * 
 * @param fsm pointer to the state machine.
 * @param dbRoot database root of the debug flag.
 */
wsf2ex2ACTION_STOP::wsf2ex2ACTION_STOP(wsf2ex2ACTION_MGR& actionMgr,
				       wsf2libCONTROL& control,
				       wsf2ex2CONFIG& config,
				       wsf2ex2DATA& data) :
    wsf2libACTION_STOP(wsf2ex2ACTION_STR_STOP, actionMgr, control),
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
wsf2ex2ACTION_STOP::~wsf2ex2ACTION_STOP()
{
    wsf2libLOG_TRACE();
}

	

// __oOo__

