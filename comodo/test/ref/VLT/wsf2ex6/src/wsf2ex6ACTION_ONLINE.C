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

#include "wsf2ex6.h"
wsf2ex6WSF_RCSID("@(#) $Id: wsf2ex6ACTION_ONLINE.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex6ACTION_ONLINE.h"
#include "wsf2ex6ACTION_MGR.h"
#include "wsf2ex6CONFIG.h"
#include "wsf2ex6DATA.h"
#include "wsf2ex6Actions.h"

#include "wsf2libCONTROL.h"
#include "wsf2libLOG.h"

/** 
 * Constructor
 * 
 * @param fsm pointer to the state machine.
 * @param dbRoot database root of the debug flag.
 */
wsf2ex6ACTION_ONLINE::wsf2ex6ACTION_ONLINE(wsf2ex6ACTION_MGR& actionMgr,
				       wsf2libCONTROL& control,
				       wsf2ex6CONFIG& config,
				       wsf2ex6DATA& data) :
    wsf2libACTION_ONLINE(wsf2ex6ACTION_STR_ONLINE, actionMgr, control),
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
wsf2ex6ACTION_ONLINE::~wsf2ex6ACTION_ONLINE()
{
    wsf2libLOG_TRACE();
}

	

// __oOo__

