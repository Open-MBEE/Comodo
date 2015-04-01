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
wsf2ex2WSF_RCSID("@(#) $Id: wsf2ex2DATA.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex2DATA.h"
#include "wsf2ex2CONFIG.h"

#include "wsf2libLOG.h"


wsf2ex2DATA::wsf2ex2DATA(const dbSYMADDRESS& dbRoot, wsf2ex2CONFIG& config) :
    mConfig(config)
{
    wsf2libLOG_TRACE();
}

wsf2ex2DATA::~wsf2ex2DATA()
{
    wsf2libLOG_TRACE();
}

ccsCOMPL_STAT wsf2ex2DATA::Init()
{
    wsf2libLOG_TRACE();
    return SUCCESS;
}

// __oOo__

