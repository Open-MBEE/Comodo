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
wsf2ex5WSF_RCSID("@(#) $Id: wsf2ex5CONFIG.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex5CONFIG.h"

#include "wsf2libLOG.h"


wsf2ex5CONFIG::wsf2ex5CONFIG(const dbSYMADDRESS& dbRoot, vltINT32 argCount, char* arg[]) 
{
    wsf2libLOG_TRACE();
}

wsf2ex5CONFIG::~wsf2ex5CONFIG()
{
    wsf2libLOG_TRACE();
}

ccsCOMPL_STAT wsf2ex5CONFIG::Init()
{
    wsf2libLOG_TRACE();
    return SUCCESS;
}

// __oOo__

