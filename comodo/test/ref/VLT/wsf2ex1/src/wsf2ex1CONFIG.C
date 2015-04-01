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
wsf2ex1WSF_RCSID("@(#) $Id: wsf2ex1CONFIG.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex1CONFIG.h"

#include "wsf2libLOG.h"


wsf2ex1CONFIG::wsf2ex1CONFIG(const dbSYMADDRESS& dbRoot, vltINT32 argCount, char* arg[]) 
{
    wsf2libLOG_TRACE();
}

wsf2ex1CONFIG::~wsf2ex1CONFIG()
{
    wsf2libLOG_TRACE();
}

ccsCOMPL_STAT wsf2ex1CONFIG::Init()
{
    wsf2libLOG_TRACE();
    return SUCCESS;
}

// __oOo__

