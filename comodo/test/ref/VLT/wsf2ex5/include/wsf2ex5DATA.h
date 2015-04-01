#ifndef wsf2ex5DATA_H
#define wsf2ex5DATA_H
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


class wsf2ex5CONFIG;


class wsf2ex5DATA : public eccsERROR_CLASS
{
  public:
    wsf2ex5DATA(const dbSYMADDRESS& dbRoot, wsf2ex5CONFIG& config);
    ~wsf2ex5DATA();

    ccsCOMPL_STAT Init();

  private:
    wsf2ex5CONFIG& mConfig;

    wsf2ex5DATA(const wsf2ex5DATA&);
    wsf2ex5DATA& operator= (const wsf2ex5DATA&);
};

#endif // !wsf2ex5DATA_H

