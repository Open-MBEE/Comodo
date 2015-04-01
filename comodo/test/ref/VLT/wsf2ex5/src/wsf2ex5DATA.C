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
wsf2ex5WSF_RCSID("@(#) $Id: wsf2ex5DATA.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex5DATA.h"
#include "wsf2ex5CONFIG.h"

#include "wsf2libLOG.h"


wsf2ex5DATA::wsf2ex5DATA(const dbSYMADDRESS& dbRoot, wsf2ex5CONFIG& config) :
    mConfig(config)
{
    wsf2libLOG_TRACE();
}

wsf2ex5DATA::~wsf2ex5DATA()
{
    wsf2libLOG_TRACE();
}

ccsCOMPL_STAT wsf2ex5DATA::Init()
{
    wsf2libLOG_TRACE();
    return SUCCESS;
}

// __oOo__

