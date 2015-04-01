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
wsf2ex3WSF_RCSID("@(#) $Id: wsf2ex3CONFIG.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex3CONFIG.h"

#include "wsf2libLOG.h"


wsf2ex3CONFIG::wsf2ex3CONFIG(const dbSYMADDRESS& dbRoot, vltINT32 argCount, char* arg[]) 
{
    wsf2libLOG_TRACE();
}

wsf2ex3CONFIG::~wsf2ex3CONFIG()
{
    wsf2libLOG_TRACE();
}

ccsCOMPL_STAT wsf2ex3CONFIG::Init()
{
    wsf2libLOG_TRACE();
    return SUCCESS;
}

// __oOo__

