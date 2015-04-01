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
wsf2ex4WSF_RCSID("@(#) $Id: wsf2ex4CONFIG.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex4CONFIG.h"

#include "wsf2libLOG.h"


wsf2ex4CONFIG::wsf2ex4CONFIG(const dbSYMADDRESS& dbRoot, vltINT32 argCount, char* arg[]) 
{
    wsf2libLOG_TRACE();
}

wsf2ex4CONFIG::~wsf2ex4CONFIG()
{
    wsf2libLOG_TRACE();
}

ccsCOMPL_STAT wsf2ex4CONFIG::Init()
{
    wsf2libLOG_TRACE();
    return SUCCESS;
}

// __oOo__

