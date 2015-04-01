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

#include "wsf2ex1.h"
wsf2ex1WSF_RCSID("@(#) $Id: wsf2ex1DATA.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex1DATA.h"
#include "wsf2ex1CONFIG.h"

#include "wsf2libLOG.h"


wsf2ex1DATA::wsf2ex1DATA(const dbSYMADDRESS& dbRoot, wsf2ex1CONFIG& config) :
    mConfig(config)
{
    wsf2libLOG_TRACE();
}

wsf2ex1DATA::~wsf2ex1DATA()
{
    wsf2libLOG_TRACE();
}

ccsCOMPL_STAT wsf2ex1DATA::Init()
{
    wsf2libLOG_TRACE();
    return SUCCESS;
}

// __oOo__

