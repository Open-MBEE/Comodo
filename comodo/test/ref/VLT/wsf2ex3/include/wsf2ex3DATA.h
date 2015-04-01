#ifndef wsf2ex3DATA_H
#define wsf2ex3DATA_H
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


class wsf2ex3CONFIG;


class wsf2ex3DATA : public eccsERROR_CLASS
{
  public:
    wsf2ex3DATA(const dbSYMADDRESS& dbRoot, wsf2ex3CONFIG& config);
    ~wsf2ex3DATA();

    ccsCOMPL_STAT Init();

  private:
    wsf2ex3CONFIG& mConfig;

    wsf2ex3DATA(const wsf2ex3DATA&);
    wsf2ex3DATA& operator= (const wsf2ex3DATA&);
};

#endif // !wsf2ex3DATA_H

