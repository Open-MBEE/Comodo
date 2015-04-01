#ifndef wsf2ex2DATA_H
#define wsf2ex2DATA_H
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


class wsf2ex2CONFIG;


class wsf2ex2DATA : public eccsERROR_CLASS
{
  public:
    wsf2ex2DATA(const dbSYMADDRESS& dbRoot, wsf2ex2CONFIG& config);
    ~wsf2ex2DATA();

    ccsCOMPL_STAT Init();

  private:
    wsf2ex2CONFIG& mConfig;

    wsf2ex2DATA(const wsf2ex2DATA&);
    wsf2ex2DATA& operator= (const wsf2ex2DATA&);
};

#endif // !wsf2ex2DATA_H

