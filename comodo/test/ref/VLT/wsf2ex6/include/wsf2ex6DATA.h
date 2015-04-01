#ifndef wsf2ex6DATA_H
#define wsf2ex6DATA_H
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

/****************************************************************************
 * Shared runtime data.
 *---------------------------------------------------------------------------
 */

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#ifndef eccs_H
#include "eccs.h"
#endif

#ifndef eccsDB_ATTR_H
#include "eccsDB_ATTR.h"
#endif


class wsf2ex6CONFIG;


class wsf2ex6DATA : public eccsERROR_CLASS
{
  public:
    wsf2ex6DATA(const dbSYMADDRESS& dbRoot, wsf2ex6CONFIG& config);
    ~wsf2ex6DATA();

    ccsCOMPL_STAT Init();

  private:
    wsf2ex6CONFIG& mConfig;

    wsf2ex6DATA(const wsf2ex6DATA&);
    wsf2ex6DATA& operator= (const wsf2ex6DATA&);
};

#endif // !wsf2ex6DATA_H

