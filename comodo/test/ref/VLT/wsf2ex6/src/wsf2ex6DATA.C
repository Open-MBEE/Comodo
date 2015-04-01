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
wsf2ex6WSF_RCSID("@(#) $Id: wsf2ex6DATA.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex6DATA.h"
#include "wsf2ex6CONFIG.h"

#include "wsf2libLOG.h"


wsf2ex6DATA::wsf2ex6DATA(const dbSYMADDRESS& dbRoot, wsf2ex6CONFIG& config) :
    mConfig(config)
{
    wsf2libLOG_TRACE();
}

wsf2ex6DATA::~wsf2ex6DATA()
{
    wsf2libLOG_TRACE();
}

ccsCOMPL_STAT wsf2ex6DATA::Init()
{
    wsf2libLOG_TRACE();
    return SUCCESS;
}

// __oOo__

